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

package org.mobicents.protocols.ss7.indicator;

import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;

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
 * @author sergey vetyutnev
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
    // reservedForNationalBit value - usually it is false
    private boolean reservedForNationalUseBit;

    public AddressIndicator() {
    }

    public AddressIndicator(boolean pcPresent, boolean ssnPresent, RoutingIndicator rti, GlobalTitleIndicator gti) {
        this.pcPresent = pcPresent;
        this.ssnPresent = ssnPresent;
        this.routingIndicator = rti;
        this.globalTitleIndicator = gti;
    }

    public AddressIndicator(byte v, SccpProtocolVersion sccpProtocolVersion) {
        init(v, sccpProtocolVersion);
    }

    private void init(byte v, SccpProtocolVersion sccpProtocolVersion) {
        if (sccpProtocolVersion == SccpProtocolVersion.ANSI) {
            ssnPresent = (v & 0x01) == 0x01;
            pcPresent = (v & 0x02) == 0x02;
            int gtiCode = ((v >> 2) & 0x0f);
            switch (gtiCode) {
            case 1:
                globalTitleIndicator = GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME;
                break;
            case 2:
                globalTitleIndicator = GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY;
                break;
            default:
                globalTitleIndicator = GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED;
                break;
            }
        } else {
            pcPresent = (v & 0x01) == 0x01;
            ssnPresent = (v & 0x02) == 0x02;
            globalTitleIndicator = GlobalTitleIndicator.valueOf((v >> 2) & 0x0f);
            if (globalTitleIndicator == null) // making of NO_GLOBAL_TITLE_INCLUDED for an unknown value
                globalTitleIndicator = GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED;
        }

        routingIndicator = ((v >> 6) & 0x01) == 0x01 ? RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN
                : RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE;
        reservedForNationalUseBit = (v & 0x80) == 0x80;
    }

    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return globalTitleIndicator;
    }

    public boolean isPCPresent() {
        return pcPresent;
    }

    public boolean isSSNPresent() {
        return ssnPresent;
    }

    public RoutingIndicator getRoutingIndicator() {
        return routingIndicator;
    }

    public boolean isReservedForNationalUseBit() {
        return reservedForNationalUseBit;
    }

    public byte getValue(SccpProtocolVersion sccpProtocolVersion) {
        int b = 0;

        if (sccpProtocolVersion == SccpProtocolVersion.ANSI) {
            if (ssnPresent) {
                b |= 0x01;
            }
            if (pcPresent) {
                b |= 0x02;
            }
            int gtiCode = 0;
            switch (globalTitleIndicator) {
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
                gtiCode = 1;
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
                gtiCode = 2;
                break;
            }
            b |= (gtiCode << 2);
        } else {
            if (pcPresent) {
                b |= 0x01;
            }
            if (ssnPresent) {
                b |= 0x02;
            }
            b |= (globalTitleIndicator.getValue() << 2);
        }

        if (routingIndicator == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
            b |= 0x40;
        }

        if (sccpProtocolVersion == SccpProtocolVersion.ANSI) {
            b |= 0x80;
        }

        return (byte) b;
    }

    // default XML representation.
    protected static final XMLFormat<AddressIndicator> XML = new XMLFormat<AddressIndicator>(AddressIndicator.class) {

        public void write(AddressIndicator ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(VALUE, ai.getValue(SccpProtocolVersion.ITU));
        }

        public void read(InputElement xml, AddressIndicator ai) throws XMLStreamException {
            byte b = (byte) xml.getAttribute(VALUE).toInt();
            ai.init(b, SccpProtocolVersion.ITU);
        }
    };

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((globalTitleIndicator == null) ? 0 : globalTitleIndicator.hashCode());
        result = prime * result + (pcPresent ? 1231 : 1237);
        result = prime * result + ((routingIndicator == null) ? 0 : routingIndicator.hashCode());
        result = prime * result + (ssnPresent ? 1231 : 1237);
        result = prime * result + (reservedForNationalUseBit ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AddressIndicator other = (AddressIndicator) obj;
        if (globalTitleIndicator != other.globalTitleIndicator)
            return false;
        if (pcPresent != other.pcPresent)
            return false;
        if (routingIndicator != other.routingIndicator)
            return false;
        if (ssnPresent != other.ssnPresent)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AddressIndicator[");

        sb.append("globalTitleIndicator=");
        sb.append(globalTitleIndicator);
        sb.append(", pcPresent=");
        sb.append(pcPresent);
        sb.append(", ssnPresent=");
        sb.append(ssnPresent);
        sb.append(", routingIndicator=");
        sb.append(routingIndicator);
        if (reservedForNationalUseBit)
            sb.append(", reservedForNationalUseBit");

        sb.append("]");
        return sb.toString();
    }

}
