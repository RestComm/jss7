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

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

/**
 * The AI is the first field within Calling Party Address (CgPA) and Called Party Address (CdPA) and is one octet in length. Its
 * function is to indicate which information elements are present so that the address can be interpreted in other words, it
 * indicates the type of addressing information that is to be found in the address field so the receiving node knows how to
 * interpret that data.
 *
 * @author amit bhayani
 * @author kulikov
 */
public class AddressIndicator implements XMLSerializable {

    private static final String VALUE = "value";

    // Global title indicator
    private GlobalTitleIndicator globalTitleIndicator;
    // point code indicator
    private boolean pcPresent;
    // ssn indicator
    private boolean ssnPresent;
    // routing indicator
    private RoutingIndicator routingIndicator;

    public AddressIndicator() {
    }

    public AddressIndicator(boolean pcPresent, boolean ssnPresent, RoutingIndicator rti, GlobalTitleIndicator gti) {
        this.pcPresent = pcPresent;
        this.ssnPresent = ssnPresent;
        this.routingIndicator = rti;
        this.globalTitleIndicator = gti;
    }

    public AddressIndicator(byte v) {
        init(v);
    }

    private void init(byte v) {
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

    // default XML representation.
    protected static final XMLFormat<AddressIndicator> XML = new XMLFormat<AddressIndicator>(AddressIndicator.class) {

        public void write(AddressIndicator ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(VALUE, ai.getValue());
        }

        public void read(InputElement xml, AddressIndicator ai) throws XMLStreamException {
            byte b = (byte) xml.getAttribute(VALUE).toInt();
            ai.init(b);
        }
    };
}
