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

/**
 *
 * @author amit bhayani
 * @author kulikov
 *
 */
public class Transition {

    private String name;
    protected FSMState destination;

    private TransitionHandler handler;

    protected Transition(String name, FSMState destination) {
        this.name = name;
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setHandler(TransitionHandler handler) {
        this.handler = handler;
    }

    protected FSMState process(FSMState state) {
        // leave current state
        state.leave();

        // new Thread(this).start();
        if (handler != null) {
            if (!handler.process(state)) {
                // If handler couldn't process this transition successfully,
                // return the old state. But this means the LeaveAction could
                // have been executed and we are ok with that. Also we call
                // cancelLeave on State so as to assign back the last timeout
                // value
                state.cancelLeave();
                return state;
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
