package org.mobicents.protocols.ss7.m3ua.impl.fsm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;

public class Transition {

    private String name;
    protected State destination;

    private TransitionHandler handler;
    private State state;

    protected Transition(String name, State destination) {
        this.name = name;
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setHandler(TransitionHandler handler) {
        this.handler = handler;
    }

    protected State process(State state) {
        this.state = state;
        // leave current state
        state.leave();

        // new Thread(this).start();
        if (handler != null) {
            if (!handler.process(state)) {
                // If handler couldn't process this transition successfully,
                // return the old state. But this means the LeaveAction could
                // have been executed and we are ok with that
                return this.state;
            }
        }

        // enter to the destination
        this.destination.enter();
        return this.destination;
    }

    @Override
    public String toString() {
        return name;
    }
}
