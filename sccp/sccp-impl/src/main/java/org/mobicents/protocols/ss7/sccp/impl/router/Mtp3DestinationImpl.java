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

package org.mobicents.protocols.ss7.sccp.impl.router;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.sccp.Mtp3Destination;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 */
public class Mtp3DestinationImpl implements XMLSerializable, Mtp3Destination {
    private static final String FIRST_DPC = "firstDpc";
    private static final String LAST_DPC = "lastDpc";
    private static final String FIRST_SLS = "firstSls";
    private static final String LAST_SLS = "lastSls";
    private static final String SLS_MASK = "slsMask";

    private int firstDpc;
    private int lastDpc;
    private int firstSls;
    private int lastSls;
    private int slsMask;

    public Mtp3DestinationImpl() {
    }

    public Mtp3DestinationImpl(int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask) {
        this.firstDpc = firstDpc;
        this.lastDpc = lastDpc;
        this.firstSls = firstSls;
        this.lastSls = lastSls;
        this.slsMask = slsMask;
    }

    public int getFirstDpc() {
        return this.firstDpc;
    }

    public int getLastDpc() {
        return this.lastDpc;
    }

    public int getFirstSls() {
        return this.firstSls;
    }

    public int getLastSls() {
        return this.lastSls;
    }

    public int getSlsMask() {
        return this.slsMask;
    }

    public boolean match(int dpc, int sls) {
        sls = (sls & this.slsMask);
        if (dpc >= this.firstDpc && dpc <= this.lastDpc && sls >= this.firstSls && sls <= this.lastSls)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("firstDpc=").append(this.firstDpc).append(", lastDpc=").append(this.lastDpc).append(", firstSls=")
                .append(this.firstSls).append(", lastSls=").append(this.lastSls).append(", slsMask=").append(this.slsMask);
        return sb.toString();
    }

    protected static final XMLFormat<Mtp3DestinationImpl> XML = new XMLFormat<Mtp3DestinationImpl>(Mtp3DestinationImpl.class) {

        public void write(Mtp3DestinationImpl dest, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(FIRST_DPC, dest.firstDpc);
            xml.setAttribute(LAST_DPC, dest.lastDpc);
            xml.setAttribute(FIRST_SLS, dest.firstSls);
            xml.setAttribute(LAST_SLS, dest.lastSls);
            xml.setAttribute(SLS_MASK, dest.slsMask);
        }

        public void read(InputElement xml, Mtp3DestinationImpl dest) throws XMLStreamException {
            dest.firstDpc = xml.getAttribute(FIRST_DPC).toInt();
            dest.lastDpc = xml.getAttribute(LAST_DPC).toInt();
            dest.firstSls = xml.getAttribute(FIRST_SLS).toInt();
            dest.lastSls = xml.getAttribute(LAST_SLS).toInt();
            dest.slsMask = xml.getAttribute(SLS_MASK).toInt();
        }
    };
}
