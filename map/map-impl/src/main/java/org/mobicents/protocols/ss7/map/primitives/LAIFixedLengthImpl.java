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
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;


/**
*
* 
* @author sergey vetyutnev
* 
*/
public class LAIFixedLengthImpl implements LAIFixedLength, MAPAsnPrimitive {
	
	private byte[] data;
	
	public LAIFixedLengthImpl() {
	}
	
	public LAIFixedLengthImpl(byte[] data) {
		this.data = data;
	}

	public LAIFixedLengthImpl(int mcc, int mnc, int lac) throws MAPException {

		if (mcc < 1 || mcc > 999)
			throw new MAPException("Bad mcc value");
		if (mnc < 0 || mnc > 999)
			throw new MAPException("Bad mnc value");

		this.data = new byte[5];

		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		if (mcc < 100)
			sb.append("0");
		if (mcc < 10)
			sb.append("0");
		sb.append(mcc);
		
		if (mnc < 100) {
			if (mnc < 10)
				sb2.append("0");
			sb2.append(mnc);
		} else {
			sb.append(mnc % 10);
			sb2.append(mnc / 10);
		}

		AsnOutputStream asnOs = new AsnOutputStream();
		TbcdString.encodeString(asnOs, sb.toString());
		System.arraycopy(asnOs.toByteArray(), 0, this.data, 0, 2);

		asnOs = new AsnOutputStream();
		TbcdString.encodeString(asnOs, sb2.toString());
		System.arraycopy(asnOs.toByteArray(), 0, this.data, 2, 1);

		data[3] = (byte)(lac / 256);
		data[4] = (byte)(lac % 256);
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public int getMCC() throws MAPException {
		
		if (data == null)
			throw new MAPException("Data must not be empty");
		if (data.length != 5)
			throw new MAPException("Data length must be equal 7");
		
		AsnInputStream ansIS = new AsnInputStream(data);
		String res = null;
		try {
			res = TbcdString.decodeString(ansIS, 3);
		} catch (IOException e) {
			throw new MAPException("IOException when decoding TbcdString: " + e.getMessage(), e);
		} catch (MAPParsingComponentException e) {
			throw new MAPException("MAPParsingComponentException when decoding TbcdString: " + e.getMessage(), e);
		}

		if (res.length() < 5 || res.length() > 6)
			throw new MAPException("Decoded TbcdString must be equal 5 or 6");
		
		String sMcc = res.substring(0, 3);

		return Integer.parseInt(sMcc);
	}

	@Override
	public int getMNC() throws MAPException {
		
		if (data == null)
			throw new MAPException("Data must not be empty");
		if (data.length != 5)
			throw new MAPException("Data length must be equal 7");
		
		AsnInputStream ansIS = new AsnInputStream(data);
		String res = null;
		try {
			res = TbcdString.decodeString(ansIS, 3);
		} catch (IOException e) {
			throw new MAPException("IOException when decoding TbcdString: " + e.getMessage(), e);
		} catch (MAPParsingComponentException e) {
			throw new MAPException("MAPParsingComponentException when decoding TbcdString: " + e.getMessage(), e);
		}

		if (res.length() < 5 || res.length() > 6)
			throw new MAPException("Decoded TbcdString must be equal 5 or 6");
		
		String sMnc;
		if (res.length() == 5) {
			sMnc = res.substring(3);
		} else {
			sMnc = res.substring(4) + res.substring(3, 4);
		}

		return Integer.parseInt(sMnc);
	}

	@Override
	public int getLac() throws MAPException {

		if (data == null)
			throw new MAPException("Data must not be empty");
		if (data.length != 5)
			throw new MAPException("Data length must be equal 7");

		int res = (data[3] & 0xFF) * 256 + (data[4] & 0xFF);
		return res;
	}

	
	@Override
	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LAIFixedLength: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding LAIFixedLength: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LAIFixedLength: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding LAIFixedLength: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		try {
			this.data = ansIS.readOctetStringData(length);
			if (this.data.length != 5)
				throw new MAPParsingComponentException("Error decoding LAIFixedLength: the LAIFixedLength field must contain from 5 to 5 octets. Contains: " + length,
						MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LAIFixedLength: " + e.getMessage(), e,
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
			throw new MAPException("AsnException when encoding LAIFixedLength: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.data == null)
			throw new MAPException("Error while encoding the LAIFixedLength: data is not defined");
		if (this.data.length != 5)
			throw new MAPException("Error while encoding the LAIFixedLength: field length must be equal 5");

		asnOs.writeOctetStringData(this.data);
	}
	
	@Override
	public String toString() {
		
		int mcc = 0;
		int mnc = 0;
		int lac = 0;
		boolean goodData = false;
		
		try {
			mcc = this.getMCC();
			mnc = this.getMNC();
			lac = this.getLac();
			goodData = true;
		} catch (MAPException e) {
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("LAIFixedLength [");
		if (goodData) {
			sb.append("MCC=");
			sb.append(mcc);
			sb.append(", MNC=");
			sb.append(mnc);
			sb.append(", Lac=");
			sb.append(lac);
		} else {
			sb.append("Data=");
			sb.append(this.printDataArr());
		}
		sb.append("]");
		
		return sb.toString();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LAIFixedLengthImpl other = (LAIFixedLengthImpl) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}
}

