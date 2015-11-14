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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMap;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationNumberMapImpl extends OctetStringBase implements LocationNumberMap {

    private static final String LOCATION_NUMBER = "locationNumber";

    public LocationNumberMapImpl() {
        super(2, 10, "LocationNumberMap");
    }

    public LocationNumberMapImpl(byte[] data) {
        super(2, 10, "LocationNumberMap", data);
    }

    public LocationNumberMapImpl(LocationNumber locationNumber) throws MAPException {
        super(2, 10, "LocationNumberMap");
        this.setLocationNumber(locationNumber);
    }

    public void setLocationNumber(LocationNumber locationNumber) throws MAPException {
        if (locationNumber == null)
            throw new MAPException("The locationNumber parameter must not be null");
        try {
            this.data = ((LocationNumberImpl) locationNumber).encode();
        } catch (ParameterException e) {
            throw new MAPException("ParameterException when encoding locationNumber: " + e.getMessage(), e);
        }
    }

    public byte[] getData() {
        return data;
    }

    public LocationNumber getLocationNumber() throws MAPException {
        if (this.data == null)
            throw new MAPException("The data has not been filled");

        try {
            LocationNumberImpl ln = new LocationNumberImpl();
            ln.decode(this.data);
            return ln;
        } catch (ParameterException e) {
            throw new MAPException("ParameterException when decoding locationNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationNumberMap [");

        if (this.data != null) {
            try {
                sb.append(this.getLocationNumber().toString());
            } catch (MAPException e) {
                sb.append("data=");
                sb.append(this.printDataArr(this.data));
                sb.append("\n");
            }
        }

        sb.append("]");

        return sb.toString();
    }

    private String printDataArr(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int b : arr) {
            sb.append(b);
            sb.append(", ");
        }

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<LocationNumberMapImpl> LOCATION_NUMBER_MAP_XML = new XMLFormat<LocationNumberMapImpl>(
            LocationNumberMapImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, LocationNumberMapImpl locationNumberMap)
                throws XMLStreamException {
            try {
                locationNumberMap.setLocationNumber(xml.get(LOCATION_NUMBER, LocationNumberImpl.class));
            } catch (MAPException e) {
                throw new XMLStreamException("MAPException when deserializing LocationNumberMapImpl", e);
            }
        }

        @Override
        public void write(LocationNumberMapImpl locationNumberMap, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            try {
                if (locationNumberMap.getLocationNumber() != null) {
                    xml.add((LocationNumberImpl) locationNumberMap.getLocationNumber(), LOCATION_NUMBER,
                            LocationNumberImpl.class);
                }
            } catch (MAPException e) {
                throw new XMLStreamException("MAPException when serializing LocationNumberMapImpl", e);
            }
        }
    };
}
