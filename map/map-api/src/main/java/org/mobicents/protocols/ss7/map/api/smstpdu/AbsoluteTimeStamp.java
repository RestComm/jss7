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

package org.mobicents.protocols.ss7.map.api.smstpdu;

import java.io.OutputStream;
import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.MAPException;

/**
The TP-Service-Centre-Time-Stamp field is given in semi-octet representation, and represents the local time in the 
following way:
 
Year:  Month:  Day:  Hour:  Minute:  Second:  Time Zone 
Digits:  2  2  2  2  2  2  2 (Semi-octets) 

The Time Zone indicates the difference, expressed in quarters of an hour, between the local time and GMT. In the first 
of the two semi-octets, the first bit (bit 3 of the seventh octet of the TP-Service-Centre-Time-Stamp field) represents the 
algebraic sign of this difference (0: positive, 1: negative).
 * 
 * @author sergey vetyutnev
 * 
 */
public interface AbsoluteTimeStamp extends Serializable {

	public int getYear();

	public int getMonth();

	public int getDay();

	public int getHour();

	public int getMinute();

	public int getSecond();

	/**
	 * @return the timeZone in in quarters of an hour
	 */
	public int getTimeZone();

	public void encodeData(OutputStream stm) throws MAPException;

}
