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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartPrice;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VariablePartPriceImpl implements VariablePartPrice, CAPAsnPrimitive {

    public static final String _PrimitiveName = "VariablePartPrice";

    private byte[] data;

    public VariablePartPriceImpl() {
    }

    public VariablePartPriceImpl(byte[] data) {
        this.data = data;
    }

    public VariablePartPriceImpl(double price) {
        this.data = new byte[4];

        long val = (long) (price * 100);
        if (val < 0)
            val = -val;
        this.data[0] = (byte) this.encodeByte((int) (val / 1000000 - (val / 100000000) * 100));
        this.data[1] = (byte) this.encodeByte((int) (val / 10000 - (val / 1000000) * 100));
        this.data[2] = (byte) this.encodeByte((int) (val / 100 - (val / 10000) * 100));
        this.data[3] = (byte) this.encodeByte((int) (val - (val / 100) * 100));
    }

    public VariablePartPriceImpl(int integerPart, int hundredthPart) {
        this.data = new byte[4];

        long val = ((long) integerPart * 100 + hundredthPart);
        if (val < 0)
            val = -val;
        this.data[0] = (byte) this.encodeByte((int) (val / 1000000 - (val / 100000000) * 100));
        this.data[1] = (byte) this.encodeByte((int) (val / 10000 - (val / 1000000) * 100));
        this.data[2] = (byte) this.encodeByte((int) (val / 100 - (val / 10000) * 100));
        this.data[3] = (byte) this.encodeByte((int) (val - (val / 100) * 100));
    }

    @Override
    public byte[] getData() {
        return this.data;
    }

    @Override
    public double getPrice() {

        if (this.data == null || this.data.length != 4)
            return Double.NaN;

        double res = this.decodeByte(data[0]) * 10000 + this.decodeByte(data[1]) * 100 + this.decodeByte(data[2])
                + (double) this.decodeByte(data[3]) / 100.0;
        return res;
    }

    @Override
    public int getPriceIntegerPart() {

        if (this.data == null || this.data.length != 4)
            return 0;

        int res = this.decodeByte(data[0]) * 10000 + this.decodeByte(data[1]) * 100 + this.decodeByte(data[2]);
        return res;
    }

    @Override
    public int getPriceHundredthPart() {

        if (this.data == null || this.data.length != 4)
            return 0;

        int res = this.decodeByte(data[3]);
        return res;
    }

    private int decodeByte(int bt) {
        return (bt & 0x0F) * 10 + ((bt & 0xF0) >> 4);
    }

    private int encodeByte(int val) {
        return (val / 10) | (val % 10) << 4;
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

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.data = ansIS.readOctetStringData(length);
        if (this.data.length < 4 || this.data.length > 4)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": data must be from 4 to 4 bytes length, found: " + this.data.length,
                    CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
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

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.data == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": data field must not be null");
        if (this.data.length != 4)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": data field length must be equal 4");

        asnOs.writeOctetStringData(data);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        double val = this.getPrice();
        if (!Double.isNaN(val)) {
            sb.append("price=");
            sb.append(val);
            sb.append(", integerPart=");
            sb.append(this.getPriceIntegerPart());
            sb.append(", hundredthPart=");
            sb.append(this.getPriceHundredthPart());
        }

        sb.append("]");

        return sb.toString();
    }
}
