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
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.OriginalCalledNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class OriginalCalledNumberCapImpl implements OriginalCalledNumberCap, CAPAsnPrimitive {

    public static final String _PrimitiveName = "OriginalCalledNumberCap";

    private static final String ISUP_ORIGINAL_CALLED_NUMBER_XML = "isupOriginalCalledNumber";

    private byte[] data;

    public OriginalCalledNumberCapImpl() {
    }

    public OriginalCalledNumberCapImpl(byte[] data) {
        this.data = data;
    }

    public OriginalCalledNumberCapImpl(OriginalCalledNumber originalCalledNumber) throws CAPException {
        setOriginalCalledNumber(originalCalledNumber);
    }

    public void setOriginalCalledNumber(OriginalCalledNumber originalCalledNumber) throws CAPException {
        if (originalCalledNumber == null)
            throw new CAPException("The originalCalledNumber parameter must not be null");
        try {
            this.data = ((OriginalCalledNumberImpl) originalCalledNumber).encode();
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding originalCalledNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public OriginalCalledNumber getOriginalCalledNumber() throws CAPException {
        if (this.data == null)
            throw new CAPException("The data has not been filled");

        try {
            OriginalCalledNumberImpl ocn = new OriginalCalledNumberImpl();
            ocn.decode(this.data);
            return ocn;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding OriginalCalledNumber: " + e.getMessage(), e);
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
                OriginalCalledNumber ocn = this.getOriginalCalledNumber();
                sb.append(", ");
                sb.append(ocn.toString());
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
    protected static final XMLFormat<OriginalCalledNumberCapImpl> ORIGINAL_CALLED_NUMBER_CAP_XML = new XMLFormat<OriginalCalledNumberCapImpl>(
            OriginalCalledNumberCapImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, OriginalCalledNumberCapImpl originalCalledNumber)
                throws XMLStreamException {
            try {
                originalCalledNumber.setOriginalCalledNumber(xml.get(ISUP_ORIGINAL_CALLED_NUMBER_XML,
                        OriginalCalledNumberImpl.class));
            } catch (CAPException e) {
                throw new XMLStreamException(e);
            }
        }

        @Override
        public void write(OriginalCalledNumberCapImpl originalCalledNumber, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            try {
                xml.add(((OriginalCalledNumberImpl) originalCalledNumber.getOriginalCalledNumber()),
                        ISUP_ORIGINAL_CALLED_NUMBER_XML, OriginalCalledNumberImpl.class);
            } catch (CAPException e) {
                throw new XMLStreamException(e);
            }
        }
    };
}
