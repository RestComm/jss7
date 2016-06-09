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

package org.mobicents.protocols.ss7.cap.primitives;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;

/**
 *
 * Super class for implementing primitives that are OCTET STRING (SIZE (x..y))
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 */
public class OctetStringBase implements CAPAsnPrimitive {
    private static final char[] digits = "0123456789ABCDEF".toCharArray();

    protected byte[] data;

    protected int minLength;
    protected int maxLength;
    protected String _PrimitiveName;

    public OctetStringBase(int minLength, int maxLength, String _PrimitiveName) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this._PrimitiveName = _PrimitiveName;
    }

    public OctetStringBase(int minLength, int maxLength, String _PrimitiveName, byte[] data) {
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

        data = new byte[length];
        ansIS.read(data);
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

        if (this.data.length < this.minLength || this.data.length > this.maxLength)
            throw new CAPException("Error while encoding the " + _PrimitiveName + ": data field length must be from "
                    + this.minLength + " to " + this.maxLength + " octets");

        asnOs.write(this.data);
    }

    @Override
    public int hashCode() {
        if (data == null)
            return 0;

        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(data);
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
        OctetStringBase other = (OctetStringBase) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!Arrays.equals(data, other.data))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return _PrimitiveName + " [Data= " + this.printDataArr() + "]";
    }

    protected String printDataArr() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (this.data != null) {
            for (int b : this.data) {
                if (first)
                    first = false;
                else
                    sb.append(", ");
                sb.append(b);
            }
        }

        return sb.toString();
    }

    public static String bytesToHex(byte[] data) {
        if (data == null)
            return null;
        char[] c = new char[data.length * 2];
        for (int i = 0, j = i; i < data.length; i++, j += 2) {
            c[j] = digits[data[i] >> 4 & 0x0F];
            c[j + 1] = digits[data[i] & 0x0F];
        }
        return new String(c);
    }

    public static byte byteFromHexChar(char c) {
        return (byte)Character.digit(c, 16);
    }

    public static byte[] hexToBytes(String hex) {
        if (hex == null)
            return null;
        return hexToBytes(hex.toCharArray());
    }

    public static byte[] hexToBytes(char[] hex) {
        if (hex == null)
            return null;
        return hexToBytes(hex, 0, hex.length);
    }

    public static byte[] hexToBytes(char[] hex, int offset, int length) {
        if (hex == null)
            return null;
        if (offset < 0 || length < 1 || hex.length - offset - length + 1 < 0)
            throw new IndexOutOfBoundsException("Array of length " + hex.length + " has no subarray with offset "
                    + offset + " and length " + length);
        if ((length & 1) > 0)
            throw new IllegalArgumentException("Hex string must be 2n characters long!");
        byte[] b = new byte[length / 2];
        for (int i = 0, j = offset; i < b.length; i++, j += 2) {
            b[i] = (byte) ((byteFromHexChar(hex[j]) << 4) + byteFromHexChar(hex[j + 1]));
        }
        return b;
    }

}
