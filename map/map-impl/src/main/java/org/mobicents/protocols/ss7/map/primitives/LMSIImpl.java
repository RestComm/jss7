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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class LMSIImpl implements LMSI, MAPAsnPrimitive {
	
	private byte[] data;

	
	public LMSIImpl() {
	}
	
	public LMSIImpl(byte[] data) {
		this.data = data;
	}


	public int getTag() {
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
	public byte[] getData() {
		return this.data;
	}
	

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LMSI: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LMSI: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException {
		
		if (length != 4)
			throw new MAPParsingComponentException("Error decoding LMSI: the LMSI field must contain 4 octets. Contains: " + length,
					MAPParsingComponentExceptionReason.MistypedParameter);

		try {
			this.data = new byte[4];
			ansIS.read(this.data);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LMSI: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding LMSI: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.data == null)
			throw new MAPException("Error while encoding the LMSI: data is not defined");

		if (this.data.length != 4)
			throw new MAPException("Error while encoding the LMSI: data field length must equale 4");

		asnOs.write(this.data);
	}

	@Override
	public String toString() {
		return "LMCI [Data= " + this.printDataArr() + "]";
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


