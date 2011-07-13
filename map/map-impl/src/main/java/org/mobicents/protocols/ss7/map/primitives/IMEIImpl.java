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

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class IMEIImpl extends TbcdString implements IMEI {

	private String imei;

	public IMEIImpl() {
	}

	public IMEIImpl(String imei) {
		this.imei = imei;
	}

	@Override
	public String getIMEI() {
		return this.imei;
	}
	
	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
	}

	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	public boolean getIsPrimitive() {
		return true;
	}

	
	public void decode(AsnInputStream ansIS, int tagClass, boolean isPrimitive, int tag, int length) throws MAPParsingComponentException {

		if (length != 8)
			throw new MAPParsingComponentException("Error decoding IMEI: the IMEI field must contain from 8 octets. Contains: " + length,
					MAPParsingComponentExceptionReason.MistypedParameter);

		try {
			this.imei = this.decodeString(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding IMEI: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.imei == null)
			throw new MAPException("Error while encoding the IMEI: IMEI must not be null");

		if (this.imei.length() < 15 || this.imei.length() > 16)
			throw new MAPException("Error while encoding the IMEI: Bad IMEI length - must be 15 or 16");

		this.encodeString(asnOs, this.imei);
	}

	@Override
	public String toString() {
		return "IMEI [IMEI=" + this.imei + "]";
	}


}
