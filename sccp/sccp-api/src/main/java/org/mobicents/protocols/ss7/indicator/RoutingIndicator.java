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

package org.mobicents.protocols.ss7.indicator;

/**
 * The Routing Indicator (RI) specifies whether GTT is required; it determines whether routing based on PC+SSN or GT. If routing
 * is based on the GT, the GT in the address is used for routing. If routing is based on PC+SSN, the PC and SSN in the
 * Called/Calling Party Address are used.
 *
 * @author amit bhayani
 * @author kulikov
 */
public enum RoutingIndicator {

    // Route on GT
    ROUTING_BASED_ON_GLOBAL_TITLE(0),

    // Route on PC + SSN
    ROUTING_BASED_ON_DPC_AND_SSN(1);

    private int ind;

    private RoutingIndicator(int ind) {
        this.ind = ind;
    }

    public int getIndicator() {
        return this.ind;
    }

    public static RoutingIndicator valueOf(int v) {
        switch (v) {
            case 0:
                return ROUTING_BASED_ON_GLOBAL_TITLE;
            case 1:
                return ROUTING_BASED_ON_DPC_AND_SSN;
            default:
                return null;
        }

    }
}
