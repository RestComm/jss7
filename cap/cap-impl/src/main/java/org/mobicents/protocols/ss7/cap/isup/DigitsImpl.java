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
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class DigitsImpl implements Digits, CAPAsnPrimitive {

    public static final String _PrimitiveName = "Digits";

    private static final String ISUP_GENERIC_DIGITS_XML = "genericDigits";
    private static final String ISUP_GENERIC_NUMBER_XML = "genericNumber";

    private byte[] data;
    private boolean isGenericDigits;
    private boolean isGenericNumber;

    public DigitsImpl() {
    }

    public DigitsImpl(byte[] data) {
        this.data = data;
    }

    public DigitsImpl(GenericDigits genericDigits) throws CAPException {
        this.setGenericDigits(genericDigits);
    }

    public DigitsImpl(GenericNumber genericNumber) throws CAPException {
        this.setGenericNumber(genericNumber);
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public GenericDigits getGenericDigits() throws CAPException {
        if (this.data == null)
            throw new CAPException("The data has not been filled");
        if (!this.isGenericDigits)
            throw new CAPException("Primitive is not marked as GenericDigits (use setGenericDigits() before)");

        try {
            GenericDigitsImpl ocn = new GenericDigitsImpl();
            ocn.decode(this.data);
            return ocn;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding GenericDigits: " + e.getMessage(), e);
        }
    }

    @Override
    public GenericNumber getGenericNumber() throws CAPException {
        if (this.data == null)
            throw new CAPException("The data has not been filled");
        if (!this.isGenericNumber)
            throw new CAPException("Primitive is not marked as GenericNumber (use setGenericNumber() before)");

        try {
            GenericNumberImpl ocn = new GenericNumberImpl();
            ocn.decode(this.data);
            return ocn;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding GenericNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setGenericDigits(GenericDigits genericDigits) throws CAPException {

        if (genericDigits == null)
            throw new CAPException("The genericDigits parameter must not be null");
        try {
            this.data = ((GenericDigitsImpl) genericDigits).encode();
            setIsGenericDigits();
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding genericDigits: " + e.getMessage(), e);
        }
    }

    @Override
    public void setGenericNumber(GenericNumber genericNumber) throws CAPException {

        if (genericNumber == null)
            throw new CAPException("The genericNumber parameter must not be null");
        try {
            this.data = ((GenericNumberImpl) genericNumber).encode();
            setIsGenericNumber();
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding genericNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean getIsGenericDigits() {
        return isGenericDigits;
    }

    @Override
    public boolean getIsGenericNumber() {
        return isGenericNumber;
    }

    @Override
    public void setIsGenericDigits() {
        isGenericDigits = true;
        isGenericNumber = false;
    }

    @Override
    public void setIsGenericNumber() {
        isGenericNumber = true;
        isGenericDigits = false;
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
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException,
            IOException, AsnException {

        this.data = ansIS.readOctetStringData(length);
        if (this.data.length < 2 || this.data.length > 16)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": data must be from 2 to 16 bytes length, found: " + this.data.length,
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
        if (this.data.length < 2 && this.data.length > 16)
            throw new CAPException("data field length must be from 2 to 16");

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
                if (this.getIsGenericNumber()) {
                    GenericNumber gn = this.getGenericNumber();
                    sb.append(", genericNumber");
                    sb.append(gn.toString());
                }

                if (this.getIsGenericDigits()) {
                    GenericDigits gd = this.getGenericDigits();
                    sb.append(", genericDigits");
                    sb.append(gd.toString());
                }
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
    protected static final XMLFormat<DigitsImpl> DIGITS_XML = new XMLFormat<DigitsImpl>(DigitsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, DigitsImpl digits) throws XMLStreamException {
            try {
                GenericDigits gd = xml.get(ISUP_GENERIC_DIGITS_XML, GenericDigitsImpl.class);
                if (gd != null)
                    digits.setGenericDigits(gd);
                GenericNumber gn = xml.get(ISUP_GENERIC_NUMBER_XML, GenericNumberImpl.class);
                if (gn != null)
                    digits.setGenericNumber(gn);
            } catch (CAPException e) {
                throw new XMLStreamException(e);
            }
        }

        @Override
        public void write(DigitsImpl digits, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            try {
                if (digits.getIsGenericDigits())
                    xml.add(((GenericDigitsImpl) digits.getGenericDigits()), ISUP_GENERIC_DIGITS_XML, GenericDigitsImpl.class);
                else if (digits.getIsGenericNumber())
                    xml.add(((GenericNumberImpl) digits.getGenericNumber()), ISUP_GENERIC_NUMBER_XML, GenericNumberImpl.class);
                else
                    throw new XMLStreamException(
                            "Error when serializing Digits: primitive is marked neither GenericDigits nor GenericNumber");
            } catch (CAPException e) {
                throw new XMLStreamException(e);
            }
        }
    };

}
