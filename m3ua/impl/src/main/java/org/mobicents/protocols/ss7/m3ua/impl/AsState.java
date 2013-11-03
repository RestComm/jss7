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

package org.mobicents.protocols.ss7.m3ua.impl;

import org.mobicents.protocols.ss7.m3ua.State;

/**
 *
 * @author amit bhayani
 *
 */
public enum AsState implements State {
    DOWN("DOWN"), INACTIVE("INACTIVE"), ACTIVE("ACTIVE"), PENDING("PENDING");

    private String name;

    private AsState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static AsState getState(String name) {
        if (name.equals(DOWN.getName())) {
            return DOWN;
        } else if (name.equals(INACTIVE.getName())) {
            return INACTIVE;
        } else if (name.equals(ACTIVE.getName())) {
            return ACTIVE;
        } else if (name.equals(PENDING.getName())) {
            return PENDING;
        }

        return null;
    }

}
