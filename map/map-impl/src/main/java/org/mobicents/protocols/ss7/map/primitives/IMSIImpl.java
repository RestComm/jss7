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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class IMSIImpl extends TbcdString implements IMSI {

	private Long MCC;
	private Long MNC;
	private String MSIN;

	
	public IMSIImpl() {
	}
	
	public IMSIImpl(Long MCC, Long MNC, String MSIN) {
		this.MCC = MCC;
		this.MNC = MNC;
		this.MSIN = MSIN;
	}

	@Override
	public Long getMCC() {
		return this.MCC;
	}

	@Override
	public Long getMNC() {
		return this.MNC;
	}

	@Override
	public String getMSIN() {
		return this.MSIN;
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
			throw new MAPParsingComponentException("IOException when decoding IMSI: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding IMSI: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException {

		if (length < 3 || length > 8)
			throw new MAPParsingComponentException("Error decoding IMSI: the IMSI field must contain from 3 to 8 octets. Contains: " + length,
					MAPParsingComponentExceptionReason.MistypedParameter);

		try {
			String res = this.decodeString(ansIS, length);

			String sMcc = res.substring(0, 3);
			String sMnc = res.substring(3, 5);
			this.MSIN = res.substring(5);

			this.MCC = (long) Integer.parseInt(sMcc);
			this.MNC = (long) Integer.parseInt(sMnc);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding IMSI: " + e.getMessage(), e,
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
			throw new MAPException("AsnException when encoding IMSI: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.MCC == null || this.MNC == null || this.MSIN == null)
			throw new MAPException("Error while encoding the IMSI: MMC, MNC or MSIN is not defined");

		if (this.MCC < 0 || this.MCC > 999)
			throw new MAPException("Error while encoding the IMSI: Bad MCC value");
		if (this.MNC < 0 || this.MNC > 99)
			throw new MAPException("Error while encoding the IMSI: Bad MNC value");
		if (this.MSIN.length() < 1 || this.MSIN.length() > 11)
			throw new MAPException("Error while encoding the IMSI: Bad MSIN value");

		StringBuilder sb = new StringBuilder();
		if (this.MCC < 100)
			sb.append("0");
		if (this.MCC < 10)
			sb.append("0");
		sb.append(this.MCC);
		if (this.MNC < 10)
			sb.append("0");
		sb.append(this.MNC);
		sb.append(this.MSIN);

		this.encodeString(asnOs, sb.toString());
	}

	@Deprecated
	public void decode(AsnInputStream ansIS, int tagClass, boolean isPrimitive, int tag, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);

		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding IMSI: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Deprecated
	public void encode(AsnOutputStream asnOs) throws MAPException {
		
		this.encodeData(asnOs);
	}

	
	@Override
	public String toString() {
		return "IMSI [MCC=" + this.MCC + ", MNC=" + this.MNC + ", MSIN=" + this.MSIN + "]";
	}
}
