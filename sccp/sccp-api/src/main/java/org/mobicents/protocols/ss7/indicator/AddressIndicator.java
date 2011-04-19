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
 * The AI is the first field within Calling Party Address (CgPA) and Called
 * Party Address (CdPA) and is one octet in length. Its function is to indicate
 * which information elements are present so that the address can be interpreted
 * in other words, it indicates the type of addressing information that is to be
 * found in the address field so the receiving node knows how to interpret that
 * data.
 * 
 * @author kulikov
 */
public class AddressIndicator {
    // Global title indicator
    private GlobalTitleIndicator globalTitleIndicator;
    // point code indicator
    private boolean pcPresent;
    // ssn indicator
    private boolean ssnPresent;
    // routing indicator
    private RoutingIndicator routingIndicator;

    public AddressIndicator(boolean pcPresent, boolean ssnPresent,
            RoutingIndicator rti, GlobalTitleIndicator gti) {
        this.pcPresent = pcPresent;
        this.ssnPresent = ssnPresent;
        this.routingIndicator = rti;
        this.globalTitleIndicator = gti;
    }

    public AddressIndicator(byte v) {
        pcPresent = (v & 0x01) == 0x01;
        ssnPresent = (v & 0x02) == 0x02;
        globalTitleIndicator = GlobalTitleIndicator.valueOf((v >> 2) & 0x0f);

        routingIndicator = ((v >> 6) & 0x01) == 0x01 ? RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN
                : RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE;
    }

    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return globalTitleIndicator;
    }

    public boolean pcPresent() {
        return pcPresent;
    }

    public boolean ssnPresent() {
        return ssnPresent;
    }

    public RoutingIndicator getRoutingIndicator() {
        return routingIndicator;
    }

    public byte getValue() {
        int b = 0;

        if (pcPresent) {
            b |= 0x01;
        }

        if (ssnPresent) {
            b |= 0x02;
        }

        b |= (globalTitleIndicator.getValue() << 2);

        if (routingIndicator == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
            b |= 0x40;
        }

        return (byte) b;
    }
}
