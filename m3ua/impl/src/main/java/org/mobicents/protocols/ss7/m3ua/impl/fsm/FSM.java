/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.m3ua.impl.fsm;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.scheduler.M3UATask;

/**
 * @author amit bhayani
 */
public class FSM extends M3UATask {

    protected static final Logger logger = Logger.getLogger(FSM.class);

    public static final String ATTRIBUTE_MESSAGE = "message";

    private String name;

    // first and last states in fsm
    protected FSMState start;
    protected FSMState end;

    // intermediate states
    private FastMap<String, FSMState> states = new FastMap<String, FSMState>();

    protected FSMState currentState;

    private FastMap attributes = new FastMap();

    private FSMState oldState;

    public FSM(String name) {
        this.name = name;
    }

    public FSMState getState() {
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

    public FSMState createState(String name) {
        FSMState s = new FSMState(this, name);
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
     * @param name the name of transition.
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

    public void tick(long now) {
        // if (state != null && state != start && state != end) {
        if (currentState != null) {
            currentState.tick(now);
        }
    }

    @Override
    public String toString() {
        return String.format("FSM.name=%s old state=%s, current state=%s", this.name,
                (this.oldState != null) ? this.oldState.getName() : "",
                (this.currentState != null) ? this.currentState.getName() : "");
    }

}
