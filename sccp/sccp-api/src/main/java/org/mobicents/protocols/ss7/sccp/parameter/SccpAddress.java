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

package org.mobicents.protocols.ss7.sccp.parameter;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;

/**
 *
 * @author amit bhayani
 * @author baranowb
 * @author kulikov
 */
public class SccpAddress implements Parameter, XMLSerializable { // impl? pfff

    public static final String GLOBAL_TITLE = "gt";
    public static final String POINT_CODE = "pc";
    public static final String SUBSYSTEM_NUMBER = "ssn";
    public static final String AI = "ai";

    private GlobalTitle gt;
    private int pc = 0;
    private int ssn = -1;

    private AddressIndicator ai;

    // If this SccpAddress is translated address
    private boolean translated;

    public SccpAddress() {
    }

    /**
     *
     * @param ri
     * @param dpc
     * @param gt
     * @param ssn
     */
    public SccpAddress(RoutingIndicator ri, int dpc, GlobalTitle gt, int ssn) {
        this.gt = gt;
        this.pc = dpc;
        this.ssn = ssn;
        this.ai = new AddressIndicator(dpc > 0, ssn > 0, ri, gt == null ? GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED
                : gt.getIndicator());

    }

    public boolean isTranslated() {
        return translated;
    }

    public void setTranslated(boolean translated) {
        this.translated = translated;
    }

    public AddressIndicator getAddressIndicator() {
        return this.ai;
    }

    public int getSignalingPointCode() {
        return pc;
    }

    public int getSubsystemNumber() {
        return ssn;
    }

    public GlobalTitle getGlobalTitle() {
        return gt;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof SccpAddress)) {
            return false;
        }

        SccpAddress address = (SccpAddress) other;

        boolean res = false;

        if (address.gt != null) {
            res = gt != null && address.gt.equals(gt);
            return res;
        }

        return address.ssn == ssn && address.pc == pc;
    }

    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.gt != null ? this.gt.hashCode() : 0);
        hash = 37 * hash + this.pc;
        hash = 37 * hash + this.ssn;
        return hash;
    }

    public String toString() {
        return ((new StringBuffer()).append("pc=").append(pc).append(",ssn=").append(ssn).append(",AI=").append(ai.getValue())
                .append(",gt=").append(gt)).toString();
    }

    protected static final XMLFormat<SccpAddress> XML = new XMLFormat<SccpAddress>(SccpAddress.class) {

        public void write(SccpAddress ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(POINT_CODE, ai.pc);
            xml.setAttribute(SUBSYSTEM_NUMBER, ai.ssn);
            xml.add(ai.ai, AI, AddressIndicator.class);
            xml.add(ai.gt, GLOBAL_TITLE);

        }

        public void read(InputElement xml, SccpAddress ai) throws XMLStreamException {
            ai.pc = xml.getAttribute(POINT_CODE).toInt();
            ai.ssn = xml.getAttribute(SUBSYSTEM_NUMBER).toInt();
            ai.ai = xml.get(AI, AddressIndicator.class);
            ai.gt = xml.get(GLOBAL_TITLE);
        }
    };
}
