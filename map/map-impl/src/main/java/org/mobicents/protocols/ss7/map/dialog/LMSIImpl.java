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

package org.mobicents.protocols.ss7.map.dialog;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.LMSI;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class LMSIImpl implements LMSI {
	
	private byte[] data;

	@Override
	public byte[] getData() {
		return this.data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}

	public void decode(AsnInputStream ansIS) throws MAPException, IOException {

		int digCnt = ansIS.available();
		if (digCnt != 4)
			throw new MAPException("Error decoding LMSI: the LMSI field must contain 4 octets. Contains: " + digCnt);

		this.data = new byte[4];
		ansIS.read(this.data);
	}

	public void encode(AsnOutputStream asnOs) throws MAPException, IOException {

		if (this.data == null)
			throw new MAPException("Error while encoding the LMSI: data is not defined");

		if (this.data.length != 4)
			throw new MAPException("Error while encoding the LMSI: data field length must equale 4");

		asnOs.write(this.data);
	}

	@Override
	public String toString() {
		return "LMCI {Data=" + this.printDataArr() + "]";
	}
	
	private String printDataArr() {
		StringBuilder sb = new StringBuilder();
		if( this.data!=null ) {
			for( int b : this.data ) {
				sb.append(b);
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}
}


