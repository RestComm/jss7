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

package org.mobicents.protocols.ss7.cap.isup;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class LocationNumberCapImpl implements LocationNumberCap, CAPAsnPrimitive {

    public static final String _PrimitiveName = "LocationNumberCap";

    private static final String ISUP_LOCATION_NUMBER = "isupLocationNumber";

    private byte[] data;

    public LocationNumberCapImpl() {
    }

    public LocationNumberCapImpl(byte[] data) {
        this.data = data;
    }

    public LocationNumberCapImpl(LocationNumber locationNumber) throws CAPException {
        setLocationNumber(locationNumber);
    }

    public void setLocationNumber(LocationNumber locationNumber) throws CAPException {
        if (locationNumber == null)
            throw new CAPException("The locationNumber parameter must not be null");
        try {
            this.data = ((LocationNumberImpl) locationNumber).encode();
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding locationNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public LocationNumber getLocationNumber() throws CAPException {
        if (this.data == null)
            throw new CAPException("The data has not been filled");

        try {
            LocationNumberImpl ln = new LocationNumberImpl();
            ln.decode(this.data);
            return ln;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding LocationNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.STRING_OCTET;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (CAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (CAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException,
            IOException, AsnException {

        this.data = ansIS.readOctetStringData(length);
        if (this.data.length < 2 || this.data.length > 10)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": data must be from 2 to 10 bytes length, found: " + this.data.length,
                    CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.data == null)
            throw new CAPException("data field must not be null");
        if (this.data.length < 2 && this.data.length > 10)
            throw new CAPException("data field length must be from 2 to 10");

        asnOs.writeOctetStringData(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.data != null) {
            sb.append("data=[");
            sb.append(printDataArr(this.data));
            sb.append("]");
            try {
                LocationNumber ln = this.getLocationNumber();
                sb.append(", ");
                sb.append(ln.toString());
            } catch (CAPException e) {
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
    protected static final XMLFormat<LocationNumberCapImpl> LOCATION_NUMBER_CAP_XML = new XMLFormat<LocationNumberCapImpl>(
            LocationNumberCapImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, LocationNumberCapImpl locationNumberCap)
                throws XMLStreamException {
            try {
                locationNumberCap.setLocationNumber(xml.get(ISUP_LOCATION_NUMBER, LocationNumberImpl.class));
            } catch (CAPException e) {
                throw new XMLStreamException(e);
            }
        }

        @Override
        public void write(LocationNumberCapImpl locationNumberCap, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            try {
                xml.add(((LocationNumberImpl) locationNumberCap.getLocationNumber()), ISUP_LOCATION_NUMBER,
                        LocationNumberImpl.class);
            } catch (CAPException e) {
                throw new XMLStreamException(e);
            }
        }
    };
}
