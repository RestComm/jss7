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
package org.mobicents.protocols.ss7.map.primitives;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.mobicents.protocols.ss7.map.api.primitives.Time;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TimeImpl extends OctetStringBase implements Time {

    private static final long msbZero = 2085978496000L;

    private static final long msbOne = -2208988800000L;

    public TimeImpl() {
        super(4, 4, "Time");
    }

    public TimeImpl(byte[] data) {
        super(4, 4, "Time", data);
    }

    public TimeImpl(int year, int month, int day, int hour, int minute, int second) {
        super(4, 4, "Time");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(year, month - 1, day, hour, minute, second);
        long ntpTime = getNtpTime(cal.getTimeInMillis());
        this.data = new byte[4];
        System.arraycopy(longToBytes(ntpTime), 4, this.data, 0, 4);
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public int getYear() {
        long time = bytesToLong(this.data);
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.YEAR);
    }

    @Override
    public int getMonth() {
        long time = bytesToLong(this.data);
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.MONTH) + 1;
    }

    @Override
    public int getDay() {
        long time = bytesToLong(this.data);
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.DATE);
    }

    @Override
    public int getHour() {
        long time = bytesToLong(this.data);
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public int getMinute() {
        long time = bytesToLong(this.data);
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.MINUTE);
    }

    @Override
    public int getSecond() {
        long time = bytesToLong(this.data);
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.SECOND);
    }

    private long getNtpTime(long time) {
        boolean isMSBSet = time < msbZero;
        long timeWithMills;
        if (isMSBSet) {
            timeWithMills = time - msbOne;
        } else {
            timeWithMills = time - msbZero;
        }
        long seconds = timeWithMills / 1000;
        if (isMSBSet) {
            seconds |= 0x80000000L;
        }
        return seconds;
    }

    public long getTime(long ntpTime) {
        long msb = ntpTime & 0x80000000L;
        if (msb == 0) {
            return msbZero + (ntpTime * 1000);
        } else {
            return msbOne + (ntpTime * 1000);
        }
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        byte[] eightbytes = new byte[8];
        System.arraycopy(bytes, 0, eightbytes, 4, 4);
        buffer.put(eightbytes);
        buffer.flip();
        return buffer.getLong();
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
