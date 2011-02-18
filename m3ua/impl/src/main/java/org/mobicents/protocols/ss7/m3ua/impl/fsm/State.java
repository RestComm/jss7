package org.mobicents.protocols.ss7.m3ua.impl.fsm;

import javolution.util.FastList;

public class State {
    private String name;
    private FSM fsm;

    private FastList<Transition> transitions = new FastList<Transition>();
    protected long timeout;
    private long activated;

    private StateEventHandler enterEventHandler;
    private StateEventHandler exitEventHandler;
    private StateEventHandler timeOutEventHandler;

    protected State(FSM fsm, String name) {
        this.name = name;
        this.fsm = fsm;
        this.timeout = 0;
    }

    public State setOnEnter(StateEventHandler handler) {
        this.enterEventHandler = handler;
        return this;
    }

    public State setOnExit(StateEventHandler handler) {
        this.exitEventHandler = handler;
        return this;
    }

    public State setOnTimeOut(StateEventHandler handler, long timeout) {
        this.timeOutEventHandler = handler;
        this.timeout = timeout;
        return this;
    }

    protected void enter() {
        this.activated = System.currentTimeMillis();
        if (this.enterEventHandler != null) {
            this.enterEventHandler.onEvent(this);
        }
    }

    protected void leave() {
        activated = 0;
        if (this.exitEventHandler != null) {
            this.exitEventHandler.onEvent(this);
        }
    }

    protected void tick(long now) {
        if (timeout > 0 && activated > 0 && (now - activated) > timeout) {
            // Call Time Out Event Handler if defined
            if (this.timeOutEventHandler != null) {
                this.timeOutEventHandler.onEvent(this);
            }

            // Now do the Transition
            try {
                fsm.signal("timeout");
            } catch (UnknownTransitionException e) {
            }
        }
    }

    public String getName() {
        return name;
    }

    public FSM getFSM() {
        return fsm;
    }

    protected void add(Transition t) {
        transitions.add(t);
    }

    /**
     * Signals to leave this state over specified transition
     * 
     * @param name
     *            the name of the transition.
     */
    public State signal(String namem) throws UnknownTransitionException {
        Transition t = find(namem);
        if (t != null) {
            return t.process(this);
        }
        throw new UnknownTransitionException(String.format("Transition=%s. %s", namem, this.fsm.toString()));
    }

    /**
     * Searches transition with specified name.
     * 
     * @param name
     *            the name of the transition.
     * @return the transition or null if not found.
     */
    private Transition find(String name) {
        for (Transition t : transitions) {
            if (t.getName().matches(name)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
