package org.mobicents.protocols.ss7.m3ua.impl;

public interface CommunicationListener {

    public void onCommStateChange(CommunicationState state);

    public enum CommunicationState {
        UP, LOST, SHUTDOWN;
    }

}
