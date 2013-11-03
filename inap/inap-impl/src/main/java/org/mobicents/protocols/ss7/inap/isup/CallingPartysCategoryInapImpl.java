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
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.primitives.INAPAsnPrimitive;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CallingPartyCategoryImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CallingPartysCategoryInapImpl implements CallingPartysCategoryInap, INAPAsnPrimitive {

    public static final String _PrimitiveName = "CallingPartysCategoryInap";

    private static final String ISUP_CALLING_PARTYS_CATEGORY_XML = "isupCallingPartysCategory";

    private byte[] data;

    public CallingPartysCategoryInapImpl() {
    }

    public CallingPartysCategoryInapImpl(byte[] data) {
        this.data = data;
    }

    public CallingPartysCategoryInapImpl(CallingPartyCategory callingPartyCategory) throws INAPException {
        setCallingPartysCategory(callingPartyCategory);
    }

    public void setCallingPartysCategory(CallingPartyCategory callingPartyCategory) throws INAPException {
        if (callingPartyCategory == null)
            throw new INAPException("The callingPartyCategory parameter must not be null");
        try {
            this.data = ((CallingPartyCategoryImpl) callingPartyCategory).encode();
        } catch (ParameterException e) {
            throw new INAPException("ParameterException when encoding callingPartyCategory: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public CallingPartyCategory getCallingPartyCategory() throws INAPException {
        if (this.data == null)
            throw new INAPException("The data has not been filled");

        try {
            CallingPartyCategoryImpl cpc = new CallingPartyCategoryImpl();
            cpc.decode(this.data);
            return cpc;
        } catch (ParameterException e) {
            throw new INAPException("ParameterException when decoding CallingPartyCategory: " + e.getMessage(), e);
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
        if (this.data.length < 1 || this.data.length > 1)
            throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": data must be from 1 to 1 bytes length, found: " + this.data.length,
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
        if (this.data.length < 1 && this.data.length > 1)
            throw new INAPException("data field length must be from 1 to 1");

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
                CallingPartyCategory cpc = this.getCallingPartyCategory();
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
    protected static final XMLFormat<CallingPartysCategoryInapImpl> CALLING_PARTYS_CATEGORY_INAP_XML = new XMLFormat<CallingPartysCategoryInapImpl>(
            CallingPartysCategoryInapImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CallingPartysCategoryInapImpl callingPartysCategory)
                throws XMLStreamException {
            try {
                callingPartysCategory.setCallingPartysCategory(xml.get(ISUP_CALLING_PARTYS_CATEGORY_XML,
                        CallingPartyCategoryImpl.class));
            } catch (INAPException e) {
                throw new XMLStreamException(e);
            }
        }

        @Override
        public void write(CallingPartysCategoryInapImpl callingPartysCategory, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            try {
                xml.add(((CallingPartyCategoryImpl) callingPartysCategory.getCallingPartyCategory()),
                        ISUP_CALLING_PARTYS_CATEGORY_XML, CallingPartyCategoryImpl.class);
            } catch (INAPException e) {
                throw new XMLStreamException(e);
            }
        }
    };

}
