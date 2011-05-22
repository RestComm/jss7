/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.mtp;

/**
 * @author amit bhayani
 * 
 */
public class Mtp3ResumePrimitive extends Mtp3Primitive {

	private byte[] value = new byte[6];

	public Mtp3ResumePrimitive(byte[] rawData) {
		this.type = RESUME;

		this.affectedDpc = ((rawData[2] << 24) + ((rawData[3] & 0xFF) << 16) + ((rawData[4] & 0xFF) << 8) + (rawData[5] & 0xFF));
	}

	/**
	 * @param type
	 * @param affectedDpc
	 */
	public Mtp3ResumePrimitive(int affectedDpc) {
		super(RESUME, affectedDpc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.MtpPrimitive#getValue()
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
