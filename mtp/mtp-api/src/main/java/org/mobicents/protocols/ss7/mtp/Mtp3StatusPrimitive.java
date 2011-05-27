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

package org.mobicents.protocols.ss7.mtp;

/**
 * @author amit bhayani
 * 
 */
public class Mtp3StatusPrimitive extends Mtp3Primitive {

	private byte[] value = new byte[11];

	/**
	 * One of the following: <br/>
	 * • 1 = Remote User Unavailable <br/>
	 * • 2 = Signaling Network Congestion
	 */
	private int status;

	/**
	 * Congestion status (if status = 0x02).<br/>
	 * This field is set to the current congestion level in the range 0 to 3, <br/>
	 * where 0 means no congestion and 3 means maximum congestion. <br/>
	 * Many networks use only a single level of congestion (that is, 1). <br/>
	 */
	private int congestionStatus;

	/**
	 * (if status = Remote User Unavailable(1))<br/>
	 * The unavailabilty cause may be one of the following:<br/>
	 * 0 = Unknown<br/>
	 * 1 = Unequipped User<br/>
	 * 2 = Inaccessible User<br/>
	 */
	private int unavailabiltyCause;

	public Mtp3StatusPrimitive(byte[] rawData) {
		this.type = STATUS;
		
		this.status = rawData[2] & 0xff;

		this.affectedDpc = (((rawData[3] & 0xff) << 24) + ((rawData[4] & 0xff) << 16) + ((rawData[5] & 0xff) << 8) + (rawData[6] & 0xff));

		this.congestionStatus = (((rawData[7] & 0xff) << 8) + (rawData[8] & 0xff));

		this.unavailabiltyCause = (((rawData[9] & 0xff) << 8) + (rawData[10] & 0xff));
	}

	/**
	 * @param type
	 * @param affectedDpc
	 */
	public Mtp3StatusPrimitive(int affectedDpc, int status, int congestionStatus, int unavailabiltyCause) {
		super(STATUS, affectedDpc);
		this.status = status;
		this.congestionStatus = congestionStatus;
		this.unavailabiltyCause = unavailabiltyCause;
	}

	public int getStatus() {
		return status;
	}

	public int getCongestionStatus() {
		return congestionStatus;
	}

	public int getUnavailabiltyCause() {
		return unavailabiltyCause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.Mtp3Primitive#getValue()
	 */
	@Override
	public byte[] getValue() {
		value[0] = (byte) SERVICE_INDICATOR;
		value[1] = (byte) STATUS;

		value[2] = (byte) this.status;

		value[3] = (byte) (this.affectedDpc >> 24);
		value[4] = (byte) (this.affectedDpc >> 16);
		value[5] = (byte) (this.affectedDpc >> 8);
		value[6] = (byte) (this.affectedDpc);

		value[7] = (byte) (this.congestionStatus >> 8);
		value[8] = (byte) (this.congestionStatus);

		value[9] = (byte) (this.unavailabiltyCause >> 8);
		value[10] = (byte) (this.unavailabiltyCause);
		return value;
	}

}
