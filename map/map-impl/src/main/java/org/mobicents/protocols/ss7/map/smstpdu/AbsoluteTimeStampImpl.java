/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.map.smstpdu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class AbsoluteTimeStampImpl implements AbsoluteTimeStamp {

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int timeZone;

	private AbsoluteTimeStampImpl() {
	}

	public AbsoluteTimeStampImpl(int year, int month, int day, int hour, int minute, int second, int timeZone) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.timeZone = timeZone;
	}

	public static AbsoluteTimeStampImpl createMessage(InputStream stm) throws MAPException {

		if (stm == null)
			throw new MAPException("Error creating ServiceCentreTimeStamp: stream must not be null");

		AbsoluteTimeStampImpl res = new AbsoluteTimeStampImpl();
		
		try {
			byte[] buf = new byte[7];
			int i1 = stm.read(buf);
			if (i1 < 7)
				throw new MAPException("Error creating ServiceCentreTimeStamp: not enouph data in the stream");

			res.year = constractDigitVal(buf[0]);
			res.month = constractDigitVal(buf[1]);
			res.day = constractDigitVal(buf[2]);			
			res.hour = constractDigitVal(buf[3]);			
			res.minute = constractDigitVal(buf[4]);			
			res.second = constractDigitVal(buf[5]);			
			res.timeZone = constractDigitVal((byte)(buf[6] & 0xF7));
			if ((buf[6] & 0x08) != 0)
				res.timeZone = -res.timeZone;
		} catch (IOException e) {
			throw new MAPException("IOException when creating ServiceCentreTimeStamp: " + e.getMessage(), e);
		}

		return res;
	}

	private static int constractDigitVal(byte bt) {
		return (bt & 0xF) * 10 + ((bt & 0xF0) >>> 4);
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

	public int getTimeZone() {
		return timeZone;
	}
	
	private static int constractEncodesVal(int val) {
		int i1 = val % 10;		
		int i2 = val / 10;
		return (i1 << 4) | i2;
	}

	public void encodeData(OutputStream stm) throws MAPException {

		try {
			stm.write(constractEncodesVal(this.year));
			stm.write(constractEncodesVal(this.month));
			stm.write(constractEncodesVal(this.day));
			stm.write(constractEncodesVal(this.hour));
			stm.write(constractEncodesVal(this.minute));
			stm.write(constractEncodesVal(this.second));
			if (this.timeZone >= 0)
				stm.write(constractEncodesVal(this.timeZone));
			else
				stm.write(constractEncodesVal((-this.timeZone)) | 0x08);
		} catch (IOException e) {
			// This can no occur
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("AbsoluteTimeStamp [");

		int yr;
		if (this.year > 90)
			yr = 1900 + this.year;
		else
			yr = 2000 + this.year;
		sb.append(this.month);
		sb.append("/");
		sb.append(this.day);
		sb.append("/");
		sb.append(yr);

		sb.append(" ");
		sb.append(this.hour);
		sb.append(":");
		sb.append(this.minute);
		sb.append(":");
		sb.append(this.second);

		sb.append(" GMT");
		if (this.timeZone >= 0)
			sb.append("+");
		sb.append(this.timeZone / 4);
		sb.append(":");
		sb.append((this.timeZone % 4) * 15);

		sb.append("]");

		return sb.toString();
	}
}
