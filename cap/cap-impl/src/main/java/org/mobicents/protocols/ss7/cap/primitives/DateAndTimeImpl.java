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
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.DateAndTime;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class DateAndTimeImpl implements DateAndTime, CAPAsnPrimitive {

    public static final String _PrimitiveName = "DateAndTime";

    private byte[] data;

    public DateAndTimeImpl() {
    }

    public DateAndTimeImpl(byte[] data) {
        this.data = data;
    }

    public DateAndTimeImpl(int year, int month, int day, int hour, int minute, int second) {
        this.data = new byte[7];
        this.data[0] = (byte) encodeByte(year / 100);
        this.data[1] = (byte) encodeByte(year % 100);
        this.data[2] = (byte) encodeByte(month);
        this.data[3] = (byte) encodeByte(day);
        this.data[4] = (byte) encodeByte(hour);
        this.data[5] = (byte) encodeByte(minute);
        this.data[6] = (byte) encodeByte(second);
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public int getYear() {

        if (this.data == null || this.data.length != 7)
            return 0;

        return this.decodeByte((int) data[0]) * 100 + (int) this.decodeByte(data[1]);
    }

    @Override
    public int getMonth() {

        if (this.data == null || this.data.length != 7)
            return 0;

        return this.decodeByte((int) data[2]);
    }

    @Override
    public int getDay() {

        if (this.data == null || this.data.length != 7)
            return 0;

        return this.decodeByte((int) data[3]);
    }

    @Override
    public int getHour() {

        if (this.data == null || this.data.length != 7)
            return 0;

        return this.decodeByte((int) data[4]);
    }

    @Override
    public int getMinute() {

        if (this.data == null || this.data.length != 7)
            return 0;

        return this.decodeByte((int) data[5]);
    }

    @Override
    public int getSecond() {

        if (this.data == null || this.data.length != 7)
            return 0;

        return this.decodeByte((int) data[6]);
    }

    @Override
    public void setYear(int year) {

        if (this.data == null || this.data.length != 7)
            this.data = new byte[7];

        this.data[0] = (byte) encodeByte(year / 100);
        this.data[1] = (byte) encodeByte(year % 100);
    }

    @Override
    public void setMonth(int month) {

        if (this.data == null || this.data.length != 7)
            this.data = new byte[7];

        this.data[2] = (byte) encodeByte(month);
    }

    @Override
    public void setDay(int day) {

        if (this.data == null || this.data.length != 7)
            this.data = new byte[7];

        this.data[3] = (byte) encodeByte(day);
    }

    @Override
    public void setHour(int hour) {

        if (this.data == null || this.data.length != 7)
            this.data = new byte[7];

        this.data[4] = (byte) encodeByte(hour);
    }

    @Override
    public void setMinute(int minute) {

        if (this.data == null || this.data.length != 7)
            this.data = new byte[7];

        this.data[5] = (byte) encodeByte(minute);
    }

    @Override
    public void setSecond(int second) {

        if (this.data == null || this.data.length != 7)
            this.data = new byte[7];

        this.data[6] = (byte) encodeByte(second);
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
        if (this.data.length < 7 || this.data.length > 7)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": data must be from 7 to 7 bytes length, found: " + this.data.length,
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
        if (this.data.length != 7)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": data field length must be equal 7");

        asnOs.writeOctetStringData(data);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (data != null) {
            sb.append("year=");
            sb.append(this.getYear());
            sb.append(", month=");
            sb.append(this.getMonth());
            sb.append(", day=");
            sb.append(this.getDay());
            sb.append(", hour=");
            sb.append(this.getHour());
            sb.append(", minite=");
            sb.append(this.getMinute());
            sb.append(", second=");
            sb.append(this.getSecond());
        }
        sb.append("]");

        return sb.toString();
    }
}
