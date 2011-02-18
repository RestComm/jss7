package org.mobicents.protocols.ss7.m3ua.impl;

public enum AsState {
    DOWN("DOWN"), INACTIVE("INACTIVE"), ACTIVE("ACTIVE"), PENDING("PENDING");

    private String name;

    private AsState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static AsState getState(String name) {
        if (name.compareTo(DOWN.getName()) == 0) {
            return DOWN;
        } else if (name.compareTo(INACTIVE.getName()) == 0) {
            return INACTIVE;
        } else if (name.compareTo(ACTIVE.getName()) == 0) {
            return ACTIVE;
        } else if (name.compareTo(PENDING.getName()) == 0) {
            return PENDING;
        }

        return null;
    }

}
