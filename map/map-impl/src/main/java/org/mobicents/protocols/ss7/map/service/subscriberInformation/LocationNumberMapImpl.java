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

package org.mobicents.protocols.ss7.map.service.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationNumberMap;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class LocationNumberMapImpl implements LocationNumberMap, MAPAsnPrimitive {

	public static final String _PrimitiveName = "LocationNumber";
	
	private byte[] data;

	public LocationNumberMapImpl() {
	}

	public LocationNumberMapImpl(byte[] data) {
		this.data = data;
	}

	public LocationNumberMapImpl(LocationNumber locationNumber) throws MAPException {
		if (locationNumber == null)
			throw new MAPException("The locationNumber parameter must not be null");
		try {
			this.data = ((LocationNumberImpl) locationNumber).encode();
		} catch (ParameterException e) {
			throw new MAPException("ParameterException when encoding locationNumber: " + e.getMessage(), e);
		}
	}
	
	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public LocationNumber getLocationNumber() throws MAPException {
		if (this.data == null)
			throw new MAPException("The data has not been filled");
		
		try {
			LocationNumberImpl ln = new LocationNumberImpl();
			ln.decode(this.data);
			return ln;
		} catch (ParameterException e) {
			throw new MAPException("ParameterException when decoding locationNumber: " + e.getMessage(), e);
		}
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
		return true;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.data = ais.readOctetStringData(length);
		if (this.data.length < 2 || this.data.length > 10)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": value must be from 2 to 10 bytes length, found: "
					+ this.data.length, MAPParsingComponentExceptionReason.MistypedParameter);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		
		if (this.data == null)
			throw new MAPException("Data must not be null");
		if (this.data.length < 2 || this.data.length > 10)
			throw new MAPException("Data length must be from 2 to 10");
		
		asnOs.writeOctetStringData(data);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LocationNumberMap [");

		if (this.data != null) {
			sb.append("data=");
			sb.append(this.printDataArr(this.data));
			sb.append("\n");
			try {
				sb.append(this.getLocationNumber().toString());
			} catch (MAPException e) {
			}
		}

		sb.append("]");

		return sb.toString();
	}
	
	private String printDataArr(byte[] arr) {
		StringBuilder sb = new StringBuilder();
		for (int b : arr) {
			sb.append(b);
			sb.append(", ");
		}

		return sb.toString();
	}
}
