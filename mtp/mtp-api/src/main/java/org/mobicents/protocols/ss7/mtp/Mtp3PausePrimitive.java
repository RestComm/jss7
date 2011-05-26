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
public class Mtp3PausePrimitive extends Mtp3Primitive {

	private byte[] value = new byte[6];

	public Mtp3PausePrimitive(byte[] rawData) {
		this.type = PAUSE;

		this.affectedDpc = ((rawData[2] << 24) + ((rawData[3] & 0xFF) << 16) + ((rawData[4] & 0xFF) << 8) + (rawData[5] & 0xFF));
	}

	/**
	 * @param type
	 * @param affectedDpc
	 */
	public Mtp3PausePrimitive(int affectedDpc) {
		super(PAUSE, affectedDpc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.Mtp3Primitive#getValue()
	 */
	@Override
	public byte[] getValue() {
		value[0] = (byte) SERVICE_INDICATOR;
		value[1] = (byte) PAUSE;
		value[2] = (byte) (this.affectedDpc >> 24);
		value[3] = (byte) (this.affectedDpc >> 16);
		value[4] = (byte) (this.affectedDpc >> 8);
		value[5] = (byte) (this.affectedDpc);
		return value;
	}

}
