/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.inap.isup;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.primitives.INAPAsnPrimitive;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class HighLayerCompatibilityInapImpl implements HighLayerCompatibilityInap, INAPAsnPrimitive {

    public static final String _PrimitiveName = "HighLayerCompatibilityInap";

    private static final String ISUP_USER_TELESERVICE_INFORMATION_XML = "isupUserTeleserviceInformation";

    private byte[] data;

    public HighLayerCompatibilityInapImpl() {
    }

    public HighLayerCompatibilityInapImpl(byte[] data) {
        this.data = data;
    }

    public HighLayerCompatibilityInapImpl(UserTeleserviceInformation highLayerCompatibility) throws INAPException {
        setHighLayerCompatibility(highLayerCompatibility);
    }

    public void setHighLayerCompatibility(UserTeleserviceInformation highLayerCompatibility) throws INAPException {
        if (highLayerCompatibility == null)
            throw new INAPException("The callingPartyCategory parameter must not be null");
        try {
            this.data = ((UserTeleserviceInformationImpl) highLayerCompatibility).encode();
        } catch (ParameterException e) {
            throw new INAPException("ParameterException when encoding highLayerCompatibility: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public UserTeleserviceInformation getHighLayerCompatibility() throws INAPException {
        if (this.data == null)
            throw new INAPException("The data has not been filled");

        try {
            UserTeleserviceInformationImpl cpc = new UserTeleserviceInformationImpl();
            cpc.decode(this.data);
            return cpc;
        } catch (ParameterException e) {
            throw new INAPException("ParameterException when decoding HighLayerCompatibility: " + e.getMessage(), e);
        }
    }

    @Override
    public int getTag() throws INAPException {
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
    public void decodeAll(AsnInputStream ansIS) throws INAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new INAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    INAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new INAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    INAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws INAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new INAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    INAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new INAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    INAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws INAPParsingComponentException, IOException, AsnException {

        this.data = ansIS.readOctetStringData(length);
        if (this.data.length < 2 || this.data.length > 2)
            throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": data must be from 2 to 2 bytes length, found: " + this.data.length,
                    INAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws INAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws INAPException {

        try {
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new INAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws INAPException {

        if (this.data == null)
            throw new INAPException("data field must not be null");
        if (this.data.length < 2 && this.data.length > 2)
            throw new INAPException("data field length must be from 2 to 2");

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
                UserTeleserviceInformation cpc = this.getHighLayerCompatibility();
                sb.append(", ");
                sb.append(cpc.toString());
            } catch (INAPException e) {
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
    protected static final XMLFormat<HighLayerCompatibilityInapImpl> HIGH_LAYER_COMPATIBILITY_INAP_XML = new XMLFormat<HighLayerCompatibilityInapImpl>(
            HighLayerCompatibilityInapImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, HighLayerCompatibilityInapImpl highLayerCompatibilityInap)
                throws XMLStreamException {
            try {
                highLayerCompatibilityInap.setHighLayerCompatibility(xml.get(ISUP_USER_TELESERVICE_INFORMATION_XML,
                        UserTeleserviceInformationImpl.class));
            } catch (INAPException e) {
                throw new XMLStreamException(e);
            }
        }

        @Override
        public void write(HighLayerCompatibilityInapImpl highLayerCompatibilityInap, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            try {
                xml.add(((UserTeleserviceInformationImpl) highLayerCompatibilityInap.getHighLayerCompatibility()),
                        ISUP_USER_TELESERVICE_INFORMATION_XML, UserTeleserviceInformationImpl.class);
            } catch (INAPException e) {
                throw new XMLStreamException(e);
            }
        }
    };
}
