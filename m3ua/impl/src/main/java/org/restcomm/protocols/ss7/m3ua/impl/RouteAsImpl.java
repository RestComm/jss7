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
package org.restcomm.protocols.ss7.m3ua.impl;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.RouteAs;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSM;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.TrafficModeTypeImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * @author amit bhayani
 *
 */
public class RouteAsImpl implements XMLSerializable, RouteAs {

    private static final String TRAFFIC_MODE_TYPE = "trafficModeType";
    private static final String AS_ARRAY = "as";

    private M3UAManagementImpl m3uaManagement;
    private As[] asArray = null;
    private TrafficModeType trafficModeType = new TrafficModeTypeImpl(TrafficModeType.Loadshare);

    // After reading comma separted value from xml file, its stored here. And them M3USManagement will do the necessary setup.
    private String asArraytemp = null;

    public RouteAsImpl() {
        // TODO Auto-generated constructor stub
    }

    public TrafficModeType getTrafficModeType() {
        return trafficModeType;
    }

    public void setTrafficModeType(TrafficModeType trafficModeType) {
        this.trafficModeType = trafficModeType;
    }

    protected void setM3uaManagement(M3UAManagementImpl m3uaManagement) {
        this.m3uaManagement = m3uaManagement;
    }

    protected String getAsArraytemp() {
        return asArraytemp;
    }

    protected void addRoute(int dpc, int opc, int si, AsImpl asImpl, int traffmode) throws Exception {
        if (this.trafficModeType.getMode() != traffmode) {
            throw new Exception(
                    String.format(
                            "Route already setup for dpc=%d opc=%d si=%d with trafficModeType=%d. Cannot assign new trafficModeType=%d",
                            dpc, opc, si, this.trafficModeType.getMode(), traffmode));
        }

        if (asArray != null) {
            // check is this As is already added
            for (int count = 0; count < asArray.length; count++) {
                AsImpl asTemp = (AsImpl) asArray[count];
                if (asTemp != null && asImpl.equals(asTemp)) {
                    throw new Exception(String.format("As=%s already added for dpc=%d opc=%d si=%d", asImpl.getName(), dpc,
                            opc, si));
                }
            }
        } else {
            asArray = new AsImpl[this.m3uaManagement.maxAsForRoute];
        }

        // Add to first empty slot
        for (int count = 0; count < asArray.length; count++) {
            if (asArray[count] == null) {
                asArray[count] = asImpl;
                this.m3uaManagement.store();
                return;
            }
        }

        throw new Exception(String.format("dpc=%d opc=%d si=%d combination already has maximum possible As", dpc, opc, si));
    }

    protected void removeRoute(int dpc, int opc, int si, AsImpl asImpl) throws Exception {
        for (int count = 0; count < asArray.length; count++) {
            AsImpl asTemp = (AsImpl) asArray[count];
            if (asTemp != null && asImpl.equals(asTemp)) {
                asArray[count] = null;
                return;
            }
        }

        throw new Exception(String.format("No AS=%s configured  for dpc=%d opc=%d si=%d", asImpl.getName(), dpc, opc, si));
    }

    protected AsImpl getAsForRoute(int count) {

        if (this.trafficModeType.getMode() == TrafficModeType.Override) {
            // For Override we always try with first available AS
            count = 0;
        }

        // First attempt
        AsImpl asImpl = (AsImpl) asArray[count];
        if (this.isAsActive(asImpl)) {
            return asImpl;
        }

        // Second recursive Attempt
        for (int i = 0; i < this.m3uaManagement.getMaxAsForRoute(); i++) {
            count = count + 1;
            if (count == this.m3uaManagement.getMaxAsForRoute()) {
                // If count reaches same value as total As available for route,
                // restart from 0
                count = 0;
            }
            asImpl = (AsImpl) asArray[count];
            if (this.isAsActive(asImpl)) {
                return asImpl;
            }

        }
        return null;
    }

    private boolean isAsActive(AsImpl asImpl) {
        FSM fsm = null;
        if (asImpl != null) {
            if (asImpl.getFunctionality() == Functionality.AS
                    || (asImpl.getFunctionality() == Functionality.SGW && asImpl.getExchangeType() == ExchangeType.DE)
                    || (asImpl.getFunctionality() == Functionality.IPSP && asImpl.getExchangeType() == ExchangeType.DE)
                    || (asImpl.getFunctionality() == Functionality.IPSP && asImpl.getExchangeType() == ExchangeType.SE && asImpl
                            .getIpspType() == IPSPType.CLIENT)) {
                fsm = asImpl.getPeerFSM();
            } else {
                fsm = asImpl.getLocalFSM();
            }

            AsState asState = AsState.getState(fsm.getState().getName());

            return (asState == AsState.ACTIVE);
        }// if (as != null)
        return false;
    }

    protected boolean hasAs(AsImpl asImpl) {
        for (int count = 0; count < asArray.length; count++) {
            AsImpl asTemp = (AsImpl) asArray[count];
            if (asTemp != null && asTemp.equals(asImpl)) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasAs() {
        for (int count = 0; count < asArray.length; count++) {
            AsImpl asTemp = (AsImpl) asArray[count];
            if (asTemp != null){
                return true;
            }
        }
        return false;
    }

    public As[] getAsArray() {
        return this.asArray;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<RouteAsImpl> AS_XML = new XMLFormat<RouteAsImpl>(RouteAsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, RouteAsImpl routeAs) throws XMLStreamException {
            int trafficMode = xml.getAttribute(TRAFFIC_MODE_TYPE, TrafficModeType.Loadshare);
            routeAs.trafficModeType = new TrafficModeTypeImpl(trafficMode);
            routeAs.asArraytemp = xml.getAttribute(AS_ARRAY, "");
        }

        @Override
        public void write(RouteAsImpl routeAs, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(TRAFFIC_MODE_TYPE, routeAs.trafficModeType.getMode());

            As[] asList = routeAs.asArray;
            StringBuffer sb = new StringBuffer();
            for (int count = 0; count < asList.length; count++) {
                AsImpl asImpl = (AsImpl) asList[count];
                if (asImpl != null) {
                    sb.append(asImpl.getName()).append(",");
                }
            }

            String value = sb.toString();

            if (!value.equals("")) {
                // remove last comma
                value = value.substring(0, (value.length() - 1));
            }

            xml.setAttribute(AS_ARRAY, value);

        }
    };

    protected void reset() {
        AsImpl[] asList = new AsImpl[this.m3uaManagement.getMaxAsForRoute()];

        if (asArraytemp != null && !asArraytemp.equals("")) {
            String[] asNames = asArraytemp.split(",");
            for (int count = 0; count < asList.length && count < asNames.length; count++) {
                String asName = asNames[count];
                As as = this.m3uaManagement.getAs(asName);
                if (as == null) {
                    // TODO add warning
                    continue;
                }
                asList[count] = (AsImpl) as;
            }
        }// if (value != null && !value.equals(""))

        this.asArray = asList;
    }

}
