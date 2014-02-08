package com.swookiee.core;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

/**
 * When active (system property 'productionLogging' is non-null), this adds an appender to the Logback root logger. This
 * appender will append any log messages to a log file in JSON format.
 */
public class JsonFileLoggingDecorator {

    private static final Logger logger = LoggerFactory.getLogger(JsonFileLoggingDecorator.class);
    private DeploymentLogging deploymentLogging;

    /**
     * Holds configuration parameters for deployment logging.
     */
    public static class DeploymentLogging {
        /** If this is non-null, the deployment logging is active. */
        private final static String ACTIVATION = "productionLogging";
        /** If set, this is the directory logs will be generated in. */
        private final static String LOGGING_DIRECTORY = "loggingDirectory";

        private String loggingDirectory;
        private final boolean active;
        private final String filenamePattern;
        private final int maxHistory = 30;
        private final String file;

        public DeploymentLogging(final BundleContext context) {
            loggingDirectory = context.getProperty(LOGGING_DIRECTORY);
            if (loggingDirectory == null || loggingDirectory.length() == 0) {
                loggingDirectory = ".";
            }
            filenamePattern = loggingDirectory + "/logFile.%d{yyyy-MM-dd}.log";
            file = loggingDirectory + "/osgi.log";

            active = context.getProperty(ACTIVATION) != null;
        }

        public String getLoggingDirectory() {
            return loggingDirectory;
        }

        public boolean isActive() {
            return active;
        }

        public String getFilenamePattern() {
            return filenamePattern;
        }

        public int getMaxHistory() {
            return maxHistory;
        }

        public String getFile() {
            return file;
        }

    }

    public void install(final BundleContext context) {
        deploymentLogging = new DeploymentLogging(context);
        if (deploymentLogging.isActive()) {
            addJsonAppenderToRootLogger();
            logger.info("deployment logging activated");
        } else {
            logger.info("NOT setting up deployment logging. Set property '" + DeploymentLogging.ACTIVATION
                    + "' to activate.");
        }
    }

    private void addJsonAppenderToRootLogger() {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("root");
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setFile(deploymentLogging.getFile());

        TimeBasedRollingPolicy<Object> policy = new TimeBasedRollingPolicy<>();
        policy.setContext(loggerContext);
        policy.setParent(appender);
        policy.setFileNamePattern(deploymentLogging.getFilenamePattern());
        policy.setMaxHistory(deploymentLogging.getMaxHistory());
        policy.start();

        appender.setRollingPolicy(policy);

        final LayoutWrappingEncoder<ILoggingEvent> wrappingEncoder = new LayoutWrappingEncoder<>();
        JsonLayout jsonLayout = new JsonLayout();
        wrappingEncoder.setLayout(jsonLayout);

        JacksonJsonFormatter formatter = new JacksonJsonFormatter();
        formatter.setPrettyPrint(false);
        jsonLayout.setJsonFormatter(formatter);
        jsonLayout.setAppendLineSeparator(true);

        appender.setContext(loggerContext);
        appender.setEncoder(wrappingEncoder);
        appender.start();

        rootLogger.addAppender(appender);
    }
}