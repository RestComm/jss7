package org.mobicents.protocols.ss7.m3ua.impl.fsm;


public interface TransitionHandler {
    public boolean process(State state);
}
