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

package org.restcomm.protocols.ss7.m3ua.impl.fsm;

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
