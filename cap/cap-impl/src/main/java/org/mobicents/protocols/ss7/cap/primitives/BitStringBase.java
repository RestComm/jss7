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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;

/**
 *
 * Super class for implementing primitives that are BIT STRING (SIZE (x..y))
 *
 * @author sergey vetyutnev
 *
 */
public abstract class BitStringBase implements CAPAsnPrimitive {

    protected BitSetStrictLength bitString;

    protected int minLength;
    protected int maxLength;
    protected int curLength;
    protected String _PrimitiveName;

    public BitStringBase(int minLength, int maxLength, int curLength, String _PrimitiveName) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.curLength = curLength;
        this._PrimitiveName = _PrimitiveName;

        this.bitString = new BitSetStrictLength(curLength);
    }

    public BitStringBase(int minLength, int maxLength, int curLength, String _PrimitiveName, BitSetStrictLength data) {
        this(minLength, maxLength, curLength, _PrimitiveName);

        this.bitString = data;
    }

    public int getTag() throws CAPException {
        return Tag.STRING_BIT;
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
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

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

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        if (!ansIS.isTagPrimitive())
            throw new CAPParsingComponentException("Error decoding " + _PrimitiveName + ": field must be primitive",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        int minLen = (this.minLength - 1) / 8 + 2;
        int maxLen = (this.maxLength - 1) / 8 + 2;
        if (length < minLen || length > maxLen)
            throw new CAPParsingComponentException("Error decoding " + _PrimitiveName + ": the field must contain from "
                    + minLen + " to " + maxLen + " octets. Contains: " + length,
                    CAPParsingComponentExceptionReason.MistypedParameter);

        this.bitString = ansIS.readBitStringData(length);
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

        if (this.bitString == null)
            throw new CAPException("Error while encoding the " + _PrimitiveName + ": data is not defined");

        try {
            asnOs.writeBitStringData(this.bitString);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bitString == null) ? 0 : bitString.hashCode());
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
        BitStringBase other = (BitStringBase) obj;
        if (bitString == null) {
            if (other.bitString != null)
                return false;
        } else if (!bitString.equals(other.bitString))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [Data=");
        if (this.bitString != null) {
            for (int i = 0; i < this.bitString.getStrictLength(); i++) {
                if (i % 8 == 0) {
                    sb.append(" ");
                }
                if (this.bitString.get(i))
                    sb.append("1");
                else
                    sb.append("0");
            }
        }
        sb.append("]");

        return sb.toString();
    }
}
