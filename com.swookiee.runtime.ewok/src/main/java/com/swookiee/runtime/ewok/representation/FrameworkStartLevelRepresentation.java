package com.swookiee.runtime.ewok.representation;

public class FrameworkStartLevelRepresentation {

    private int startLevel;
    private int initialStartLevel;

    public FrameworkStartLevelRepresentation() {
    }

    public FrameworkStartLevelRepresentation(final int startLevel, final int initialStartLevel) {
        this.startLevel = startLevel;
        this.initialStartLevel = initialStartLevel;
    }

    public int getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(final int startLevel) {
        this.startLevel = startLevel;
    }

    public int getInitialStartLevel() {
        return initialStartLevel;
    }
}
