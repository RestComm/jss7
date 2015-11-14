/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

    public int getValue() {
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
