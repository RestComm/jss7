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

import java.sql.Date;
import java.util.Calendar;

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
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, hour, minute, second);
		long ntpTime = getNtpTime(cal.getTimeInMillis());
		this.data = hexToBytes(Long.toHexString(ntpTime));
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public int getYear() {
		long time = Long.parseLong(bytesToHex(data),16);
		time = getTime(time);
		Date d = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.YEAR);
	}

	@Override
	public int getMonth() {
		long time = Long.parseLong(bytesToHex(data),16);
		time = getTime(time);
		Date d = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.MONTH) + 1;
	}

	@Override
	public int getDay() {
		long time = Long.parseLong(bytesToHex(data),16);
		time = getTime(time);
		Date d = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.DATE);
	}

	@Override
	public int getHour() {
		long time = Long.parseLong(bytesToHex(data),16);
		time = getTime(time);
		Date d = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	@Override
	public int getMinute() {
		long time = Long.parseLong(bytesToHex(data),16);
		time = getTime(time);
		Date d = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.MINUTE);
	}

	@Override
	public int getSecond() {
		long time = Long.parseLong(bytesToHex(data),16);
		time = getTime(time);
		Date d = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.SECOND);
	}

    private long getNtpTime(long time)
    {
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
    
    
    public long getTime(long ntpTime)
    {
        long msb = ntpTime & 0x80000000L;
        if (msb == 0) {
            return msbZero +  (ntpTime * 1000) ;
        } else {
            return msbOne + (ntpTime * 1000);
        }
    }
    
    private byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[4];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    private String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
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
