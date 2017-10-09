package org.mobicents.protocols.ss7.sccp;

public class MaxConnectionCountReached extends Exception {
    public MaxConnectionCountReached() {
    }

    public MaxConnectionCountReached(String message) {
        super(message);
    }
}
