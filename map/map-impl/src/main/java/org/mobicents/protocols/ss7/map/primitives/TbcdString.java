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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class TbcdString implements MAPAsnPrimitive {

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

    public int getTag() throws MAPException {
        return Tag.STRING_OCTET;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return true;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException {

        if (!ansIS.isTagPrimitive())
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName + ": field must be primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (length < this.minLength || length > this.maxLength)
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName + ": the field must contain from "
                    + this.minLength + " to " + this.maxLength + " octets. Contains: " + length,
                    MAPParsingComponentExceptionReason.MistypedParameter);

        try {
            this.data = decodeString(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.data == null)
            throw new MAPException("Error while encoding the " + _PrimitiveName + ": data is not defined");

        encodeString(asnOs, this.data);
    }

    public static String decodeString(InputStream ansIS, int length) throws IOException, MAPParsingComponentException {
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

    public static void encodeString(OutputStream asnOs, String data) throws MAPException {
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
                throw new MAPException("Error when encoding TbcdString: " + e.getMessage(), e);
            }
        }

    }

    protected static int encodeNumber(char c) throws MAPException {
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
            case 'A':
                return 12;
            case 'b':
            case 'B':
                return 13;
            case 'c':
            case 'C':
                return 14;
            default:
                throw new MAPException(
                        "char should be between 0 - 9, *, #, a, b, c for Telephony Binary Coded Decimal String. Received " + c);

        }
    }

    protected static char decodeNumber(int i) throws MAPParsingComponentException {
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
                throw new MAPParsingComponentException(
                        "Integer should be between 0 - 15 for Telephony Binary Coded Decimal String. Received " + i,
                        MAPParsingComponentExceptionReason.MistypedParameter);

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
