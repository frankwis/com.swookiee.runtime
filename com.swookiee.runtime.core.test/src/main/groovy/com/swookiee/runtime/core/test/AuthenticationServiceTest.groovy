package com.swookiee.runtime.core.test

import static org.junit.Assert.*

import java.security.MessageDigest

import org.junit.Test
import org.osgi.framework.BundleContext
import org.osgi.service.useradmin.Role
import org.osgi.service.useradmin.User
import org.osgi.service.useradmin.UserAdmin

import com.github.groovyosgi.testing.OSGiTest
import com.swookiee.core.auth.AuthenticationService
import com.swookiee.core.internal.Activator

class AuthenticationServiceTest extends OSGiTest{

    @Test
    public void test() {
        UserAdmin useradmin = getService(UserAdmin)

        User user = (User) useradmin.createRole("admin", Role.USER)

        MessageDigest digest = MessageDigest.getInstance("MD5")
        def foo = digest.digest("test".getBytes("UTF-8"))

        user.credentials.put("password", foo)

        AuthenticationService authenticationService = getService(AuthenticationService)

        assertTrue authenticationService.validateUserCredentials("admin", "test")
        assertFalse authenticationService.validateUserCredentials("admin1", "test")
        assertFalse authenticationService.validateUserCredentials("admin", "test1")
    }

    @Override
    protected BundleContext getBundleContext() {
        return Activator.context
    }
}