package org.mobicents.protocols.ss7.m3ua.impl;

/**
 * 
 * @author amit bhayani
 * 
 */
public enum AspState {
    DOWN("DOWN"), DOWN_SENT("DOWN_SENT"), UP_SENT("UP_SENT"), INACTIVE_SENT("INACTIVE_SENT"), INACTIVE("INACTIVE"), ACTIVE_SENT(
            "ACTIVE_SENT"), ACTIVE("ACTIVE");

    private String name;

    private AspState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static AspState getState(String name) {
        if (name.compareTo(DOWN.getName()) == 0) {
            return DOWN;
        } else if (name.compareTo(INACTIVE.getName()) == 0) {
            return INACTIVE;
        } else if (name.compareTo(ACTIVE.getName()) == 0) {
            return ACTIVE;
        } else if (name.compareTo(UP_SENT.getName()) == 0) {
            return UP_SENT;
        } else if (name.compareTo(DOWN_SENT.getName()) == 0) {
            return DOWN_SENT;
        } else if (name.compareTo(INACTIVE_SENT.getName()) == 0) {
            return INACTIVE_SENT;
        } else if (name.compareTo(ACTIVE_SENT.getName()) == 0) {
            return ACTIVE_SENT;
        }

        return null;
    }
}
