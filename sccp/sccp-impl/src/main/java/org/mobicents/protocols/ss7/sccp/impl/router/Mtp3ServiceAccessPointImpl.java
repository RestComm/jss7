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

package org.mobicents.protocols.ss7.sccp.impl.router;

import java.util.HashMap;
import java.util.Map;

import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.sccp.Mtp3Destination;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.impl.oam.SccpOAMMessage;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class Mtp3ServiceAccessPointImpl implements Mtp3ServiceAccessPoint, XMLSerializable {
    private static final String MTP3_ID = "mtp3Id";
    private static final String OPC = "opc";
    private static final String NI = "ni";
    private static final String NETWORK_ID = "networkId";
    private static final String LOCAL_GT_DIGITS = "localGtDigits";

    private static final String STRING_EMPTY = null;

    private int mtp3Id;
    private int opc;
    private int ni;
    private String stackName;
    private int networkId;
    private String localGtDigits;

    private Mtp3DestinationMap<Integer, Mtp3Destination> dpcList = new Mtp3DestinationMap<Integer, Mtp3Destination>();

    public Mtp3ServiceAccessPointImpl() {
    }

    public Mtp3ServiceAccessPointImpl(int mtp3Id, int opc, int ni, String stackName, int networkId, String localGtDigits) {
        this.mtp3Id = mtp3Id;
        this.opc = opc;
        this.ni = ni;
        this.stackName = stackName;
        this.networkId = networkId;
        this.localGtDigits = localGtDigits;
    }

    /**
     * @param stackName the stackName to set
     */
    protected void setStackName(String stackName) {
        this.stackName = stackName;
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

    public int getNetworkId() {
        return networkId;
    }

    @Override
    public String getLocalGtDigits() {
        return localGtDigits;
    }

    public void setLocalGtDigits(String val) {
        localGtDigits = val;
    }

    public Mtp3Destination getMtp3Destination(int destId) {
        return this.dpcList.get(destId);
    }

    public Map<Integer, Mtp3Destination> getMtp3Destinations() {
        Map<Integer, Mtp3Destination> dpcListTmp = new HashMap<Integer, Mtp3Destination>();
        dpcListTmp.putAll(dpcList);
        return dpcListTmp;
    }

    public void addMtp3Destination(int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception {

        if (getMtp3Destination(destId) != null) {
            throw new Exception(SccpOAMMessage.DEST_ALREADY_EXIST);
        }

        Mtp3DestinationImpl dest = new Mtp3DestinationImpl(firstDpc, lastDpc, firstSls, lastSls, slsMask);

        synchronized (this) {
            Mtp3DestinationMap<Integer, Mtp3Destination> newDpcList = new Mtp3DestinationMap<Integer, Mtp3Destination>();
            newDpcList.putAll(this.dpcList);
            newDpcList.put(destId, dest);
            this.dpcList = newDpcList;
        }
    }

    public void modifyMtp3Destination(int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception {
        if (getMtp3Destination(destId) == null) {
            throw new Exception(String.format(SccpOAMMessage.DEST_DOESNT_EXIST, this.stackName));
        }

        Mtp3DestinationImpl dest = new Mtp3DestinationImpl(firstDpc, lastDpc, firstSls, lastSls, slsMask);

        synchronized (this) {
            Mtp3DestinationMap<Integer, Mtp3Destination> newDpcList = new Mtp3DestinationMap<Integer, Mtp3Destination>();
            newDpcList.putAll(this.dpcList);
            newDpcList.put(destId, dest);
            this.dpcList = newDpcList;
        }
    }

    public void removeMtp3Destination(int destId) throws Exception {

        if (getMtp3Destination(destId) == null) {
            throw new Exception(String.format(SccpOAMMessage.DEST_DOESNT_EXIST, this.stackName));
        }

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

    public boolean matches(int dpc) {
        for (FastMap.Entry<Integer, Mtp3Destination> e = this.dpcList.head(), end = this.dpcList.tail(); (e = e.getNext()) != end;) {
            if (e.getValue().match(dpc))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("mtp3Id=").append(this.mtp3Id).append(", opc=").append(this.opc).append(", ni=").append(this.ni)
                .append(", networkId=").append(this.networkId).append(", localGtDigits=").append(this.localGtDigits)
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

    protected static final XMLFormat<Mtp3ServiceAccessPointImpl> XML = new XMLFormat<Mtp3ServiceAccessPointImpl>(
            Mtp3ServiceAccessPointImpl.class) {

        public void write(Mtp3ServiceAccessPointImpl sap, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(MTP3_ID, sap.mtp3Id);
            xml.setAttribute(OPC, sap.opc);
            xml.setAttribute(NI, sap.ni);
            xml.setAttribute(NETWORK_ID, sap.networkId);
            if (sap.localGtDigits != null)
                xml.setAttribute(LOCAL_GT_DIGITS, sap.localGtDigits);

            xml.add(sap.dpcList);
        }

        public void read(InputElement xml, Mtp3ServiceAccessPointImpl sap) throws XMLStreamException {
            sap.mtp3Id = xml.getAttribute(MTP3_ID).toInt();
            sap.opc = xml.getAttribute(OPC).toInt();
            sap.ni = xml.getAttribute(NI).toInt();
            sap.networkId = xml.getAttribute(NETWORK_ID, 0);
            String vals = xml.getAttribute(LOCAL_GT_DIGITS, STRING_EMPTY);
            if (vals != null)
                sap.localGtDigits = vals;

            sap.dpcList = xml.getNext();
        }
    };
}
