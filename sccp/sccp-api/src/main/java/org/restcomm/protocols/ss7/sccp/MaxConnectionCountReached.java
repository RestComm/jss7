package org.restcomm.protocols.ss7.sccp;

public class MaxConnectionCountReached extends Exception {
    public MaxConnectionCountReached() {
    }

    public MaxConnectionCountReached(String message) {
        super(message);
    }
}
