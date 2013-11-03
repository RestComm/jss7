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

import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class Mtp3ServiceAccessPoint implements XMLSerializable {
    private static final String MTP3_ID = "mtp3Id";
    private static final String OPC = "opc";
    private static final String NI = "ni";
    // private static final String DPC_LIST = "dpcList";

    private int mtp3Id;
    private int opc;
    private int ni;

    private Mtp3DestinationMap<Integer, Mtp3Destination> dpcList = new Mtp3DestinationMap<Integer, Mtp3Destination>();

    public Mtp3ServiceAccessPoint() {
    }

    public Mtp3ServiceAccessPoint(int mtp3Id, int opc, int ni) {
        this.mtp3Id = mtp3Id;
        this.opc = opc;
        this.ni = ni;
    }

    public int getMtp3Id() {
        return mtp3Id;
    }

    public int getOpc() {
        return opc;
    }

    public int getNi() {
        return ni;
    }

    public Mtp3Destination getMtp3Destination(int destId) {
        return this.dpcList.get(destId);
    }

    public FastMap<Integer, Mtp3Destination> getMtp3Destinations() {
        return this.dpcList;
    }

    public void addMtp3Destination(int destId, Mtp3Destination dest) {
        synchronized (this) {
            Mtp3DestinationMap<Integer, Mtp3Destination> newDpcList = new Mtp3DestinationMap<Integer, Mtp3Destination>();
            newDpcList.putAll(this.dpcList);
            newDpcList.put(destId, dest);
            this.dpcList = newDpcList;
        }
    }

    public void removeMtp3Destination(int destId) {
        synchronized (this) {
            Mtp3DestinationMap<Integer, Mtp3Destination> newDpcList = new Mtp3DestinationMap<Integer, Mtp3Destination>();
            newDpcList.putAll(this.dpcList);
            newDpcList.remove(destId);
            this.dpcList = newDpcList;
        }
    }

    public boolean matches(int dpc, int sls) {
        for (FastMap.Entry<Integer, Mtp3Destination> e = this.dpcList.head(), end = this.dpcList.tail(); (e = e.getNext()) != end;) {
            if (e.getValue().match(dpc, sls))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("mtp3Id=").append(this.mtp3Id).append(", opc=").append(this.opc).append(", ni=").append(this.ni)
                .append(", dpcList=[");

        boolean isFirst = true;
        for (FastMap.Entry<Integer, Mtp3Destination> e = this.dpcList.head(), end = this.dpcList.tail(); (e = e.getNext()) != end;) {
            Integer id = e.getKey();
            Mtp3Destination dest = e.getValue();
            if (isFirst)
                isFirst = false;
            else
                sb.append(", ");
            sb.append("[key=");
            sb.append(id);
            sb.append(", ");
            sb.append(dest.toString());
            sb.append("], ");
        }
        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<Mtp3ServiceAccessPoint> XML = new XMLFormat<Mtp3ServiceAccessPoint>(
            Mtp3ServiceAccessPoint.class) {

        public void write(Mtp3ServiceAccessPoint sap, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(MTP3_ID, sap.mtp3Id);
            xml.setAttribute(OPC, sap.opc);
            xml.setAttribute(NI, sap.ni);

            // XMLStreamWriter writer = xml.getStreamWriter();

            xml.add(sap.dpcList);
        }

        public void read(InputElement xml, Mtp3ServiceAccessPoint sap) throws XMLStreamException {
            sap.mtp3Id = xml.getAttribute(MTP3_ID).toInt();
            sap.opc = xml.getAttribute(OPC).toInt();
            sap.ni = xml.getAttribute(NI).toInt();

            sap.dpcList = xml.getNext();
        }
    };
}
