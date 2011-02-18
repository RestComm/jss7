package org.mobicents.protocols.ss7.m3ua.impl.fsm;

import javolution.util.FastMap;

import org.apache.log4j.Logger;

/**
 * @author amit bhayani
 */
public class FSM {

    static final protected Logger logger = Logger.getLogger(FSM.class);

    public static final String ATTRIBUTE_MESSAGE = "message";

    private String name;

    // first and last states in fsm
    protected State start;
    protected State end;

    // intermediate states
    private FastMap<String, State> states = new FastMap<String, State>();

    protected State currentState;

    private FastMap attributes = new FastMap();

    private State oldState;

    public FSM(String name) {
        this.name = name;
    }

    public State getState() {
        return currentState;
    }

    public void setStart(String name) {
        // the start state already has value which differs from current state?
        if (this.start != null && currentState != null) {
            throw new IllegalStateException("Start state can't be changed now");
        }
        this.start = states.get(name);
        this.currentState = start;
    }

    public void setEnd(String name) {
        this.end = states.get(name);
    }

    public State createState(String name) {
        State s = new State(this, name);
        states.put(name, s);
        return s;
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public Transition createTransition(String name, String from, String to) {
        if (name.equals("timeout")) {
            throw new IllegalArgumentException("timeout is illegal name for transition");
        }

        if (!states.containsKey(from)) {
            throw new IllegalStateException("Unknown state: " + from);
        }

        if (!states.containsKey(to)) {
            throw new IllegalStateException("Unknown state: " + to);
        }

        Transition t = new Transition(name, states.get(to));
        states.get(from).add(t);

        return t;
    }

    public Transition createTimeoutTransition(String from, String to, long timeout) {
        if (!states.containsKey(from)) {
            throw new IllegalStateException("Unknown state: " + from);
        }

        if (!states.containsKey(to)) {
            throw new IllegalStateException("Unknown state: " + to);
        }

        Transition t = new Transition("timeout", states.get(to));
        states.get(from).timeout = timeout;
        states.get(from).add(t);

        return t;
    }

    /**
     * Processes transition.
     * 
     * @param name
     *            the name of transition.
     */
    public void signal(String name) throws UnknownTransitionException {

        // check that start state defined
        if (start == null) {
            throw new IllegalStateException("The start sate is not defined");
        }

        // check that end state defined
        if (end == null) {
            throw new IllegalStateException("The end sate is not defined");
        }

        // ignore any signals if fsm reaches end state
        // if (state == end) {
        // return;
        // }

        oldState = currentState;
        // switch to next state
        currentState = currentState.signal(name);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("%s Transition to=%s", toString(), name));
        }
    }

    public void tick() {
        // if (state != null && state != start && state != end) {
        if (currentState != null) {
            currentState.tick(System.currentTimeMillis());
        }
    }

    @Override
    public String toString() {
        return String.format("FSM.name=%s old state=%s, current state=%s", this.name, this.oldState.getName(),
                currentState.getName());
    }

}
