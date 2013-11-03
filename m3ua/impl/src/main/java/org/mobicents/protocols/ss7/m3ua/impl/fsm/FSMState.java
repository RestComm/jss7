/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.m3ua.impl.fsm;

import javolution.util.FastList;

/**
 *
 * @author amit bhayani
 * @author kulikov
 *
 */
public class FSMState {
    private String name;
    private FSM fsm;

    private FastList<Transition> transitions = new FastList<Transition>();
    protected long timeout;

    // The time in ms when this sate was entered
    private long activated;

    // The time in ms when this states was entered. Its possible that
    // TransitionHandler cancels the transition in which case original activated
    // time is to be maintained
    private long oldActivated;

    private FSMStateEventHandler enterEventHandler;
    private FSMStateEventHandler exitEventHandler;
    private FSMStateEventHandler timeOutEventHandler;

    protected FSMState(FSM fsm, String name) {
        this.name = name;
        this.fsm = fsm;
        this.timeout = 0;
    }

    public FSMState setOnEnter(FSMStateEventHandler handler) {
        this.enterEventHandler = handler;
        return this;
    }

    public FSMState setOnExit(FSMStateEventHandler handler) {
        this.exitEventHandler = handler;
        return this;
    }

    public FSMState setOnTimeOut(FSMStateEventHandler handler, long timeout) {
        this.timeOutEventHandler = handler;
        this.timeout = timeout;
        return this;
    }

    protected void enter() {
        this.activated = System.currentTimeMillis();
        this.oldActivated = this.activated;
        if (this.enterEventHandler != null) {
            this.enterEventHandler.onEvent(this);
        }
    }

    protected void leave() {
        this.activated = 0;
        if (this.exitEventHandler != null) {
            this.exitEventHandler.onEvent(this);
        }
    }

    protected void cancelLeave() {
        this.activated = this.oldActivated;
    }

    protected void tick(long now) {
        if (this.timeout > 0 && this.activated > 0 && (now - this.activated) > this.timeout) {
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
     * @param name the name of the transition.
     */
    public FSMState signal(String namem) throws UnknownTransitionException {
        Transition t = find(namem);
        if (t != null) {
            return t.process(this);
        }
        throw new UnknownTransitionException(String.format("Transition=%s. %s", namem, this.fsm.toString()));
    }

    /**
     * Searches transition with specified name.
     *
     * @param name the name of the transition.
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
