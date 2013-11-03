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
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.primitives.INAPAsnPrimitive;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.RedirectionInformationImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class RedirectionInformationInapImpl implements RedirectionInformationInap, INAPAsnPrimitive {

    public static final String _PrimitiveName = "RedirectionInformationInap";

    private static final String ISUP_REDIRECTION_INFORMATION_XML = "isupRedirectionInformation";

    private byte[] data;

    public RedirectionInformationInapImpl() {
    }

    public RedirectionInformationInapImpl(byte[] data) {
        this.data = data;
    }

    public RedirectionInformationInapImpl(RedirectionInformation redirectionInformation) throws INAPException {
        setRedirectionInformation(redirectionInformation);
    }

    public void setRedirectionInformation(RedirectionInformation redirectionInformation) throws INAPException {
        if (redirectionInformation == null)
            throw new INAPException("The redirectionInformation parameter must not be null");
        try {
            this.data = ((RedirectionInformationImpl) redirectionInformation).encode();
        } catch (ParameterException e) {
            throw new INAPException("ParameterException when encoding redirectionInformation: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public RedirectionInformation getRedirectionInformation() throws INAPException {
        if (this.data == null)
            throw new INAPException("The data has not been filled");

        try {
            RedirectionInformationImpl cpc = new RedirectionInformationImpl();
            cpc.decode(this.data);
            return cpc;
        } catch (ParameterException e) {
            throw new INAPException("ParameterException when decoding RedirectionInformation: " + e.getMessage(), e);
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
                RedirectionInformation ri = this.getRedirectionInformation();
                sb.append(", ");
                sb.append(ri.toString());
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
    protected static final XMLFormat<RedirectionInformationInapImpl> REDIRECTION_INFORMATION_INAP_XML = new XMLFormat<RedirectionInformationInapImpl>(
            RedirectionInformationInapImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, RedirectionInformationInapImpl redirectionInformationInap)
                throws XMLStreamException {
            try {
                redirectionInformationInap.setRedirectionInformation(xml.get(ISUP_REDIRECTION_INFORMATION_XML,
                        RedirectionInformationImpl.class));
            } catch (INAPException e) {
                throw new XMLStreamException(e);
            }
        }

        @Override
        public void write(RedirectionInformationInapImpl redirectionInformationInap, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            try {
                xml.add(((RedirectionInformationImpl) redirectionInformationInap.getRedirectionInformation()),
                        ISUP_REDIRECTION_INFORMATION_XML, RedirectionInformationImpl.class);
            } catch (INAPException e) {
                throw new XMLStreamException(e);
            }
        }
    };
}
