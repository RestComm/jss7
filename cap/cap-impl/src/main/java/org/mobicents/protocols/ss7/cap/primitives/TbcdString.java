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

package org.mobicents.protocols.ss7.cap.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class TbcdString implements CAPAsnPrimitive {

    protected static int DIGIT_1_MASK = 0x0F;
    protected static int DIGIT_2_MASK = 0xF0;

    protected String data;

    protected int minLength;
    protected int maxLength;
    protected String _PrimitiveName;

    public TbcdString(int minLength, int maxLength, String _PrimitiveName) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this._PrimitiveName = _PrimitiveName;
    }

    public TbcdString(int minLength, int maxLength, String _PrimitiveName, String data) {
        this(minLength, maxLength, _PrimitiveName);

        this.data = data;
    }

    public int getTag() throws CAPException {
        return Tag.STRING_OCTET;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

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
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException {

        if (!ansIS.isTagPrimitive())
            throw new CAPParsingComponentException("Error decoding " + _PrimitiveName + ": field must be primitive",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (length < this.minLength || length > this.maxLength)
            throw new CAPParsingComponentException("Error decoding " + _PrimitiveName + ": the field must contain from "
                    + this.minLength + " to " + this.maxLength + " octets. Contains: " + length,
                    CAPParsingComponentExceptionReason.MistypedParameter);

        try {
            this.data = decodeString(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding IMSI: " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws CAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.data == null)
            throw new CAPException("Error while encoding the " + _PrimitiveName + ": data is not defined");

        encodeString(asnOs, this.data);
    }

    public static String decodeString(InputStream ansIS, int length) throws IOException, CAPParsingComponentException {
        StringBuilder s = new StringBuilder();
        for (int i1 = 0; i1 < length; i1++) {
            int b = ansIS.read();

            int digit1 = (b & DIGIT_1_MASK);
            if (digit1 == 15) {
                // this is mask
            } else {
                s.append(decodeNumber(digit1));
            }

            int digit2 = ((b & DIGIT_2_MASK) >> 4);
            if (digit2 == 15) {
                // this is mask
            } else {
                s.append(decodeNumber(digit2));
            }
        }

        return s.toString();
    }

    public static void encodeString(OutputStream asnOs, String data) throws CAPException {
        char[] chars = data.toCharArray();
        for (int i = 0; i < chars.length; i = i + 2) {
            char a = chars[i];

            int digit1 = encodeNumber(a);
            int digit2;
            if ((i + 1) == chars.length) {
                // add the filler instead
                digit2 = 15;
            } else {
                char b = chars[i + 1];
                digit2 = encodeNumber(b);
            }

            int digit = (digit2 << 4) | digit1;

            try {
                asnOs.write(digit);
            } catch (IOException e) {
                throw new CAPException("Error when encoding TbcdString: " + e.getMessage(), e);
            }
        }

    }

    protected static int encodeNumber(char c) throws CAPException {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case '*':
                return 10;
            case '#':
                return 11;
            case 'a':
                return 12;
            case 'b':
                return 13;
            case 'c':
                return 14;
            default:
                throw new CAPException(
                        "char should be between 0 - 9, *, #, a, b, c for Telephony Binary Coded Decimal String. Received " + c);

        }
    }

    protected static char decodeNumber(int i) throws CAPParsingComponentException {
        switch (i) {
            case 0:
                return '0';
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
            case 10:
                return '*';
            case 11:
                return '#';
            case 12:
                return 'a';
            case 13:
                return 'b';
            case 14:
                return 'c';
                // case 15:
                // return 'd';
            default:
                throw new CAPParsingComponentException(
                        "Integer should be between 0 - 15 for Telephony Binary Coded Decimal String. Received " + i,
                        CAPParsingComponentExceptionReason.MistypedParameter);

        }
    }

    @Override
    public String toString() {
        return _PrimitiveName + " [" + this.data + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TbcdString other = (TbcdString) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        return true;
    }
}
