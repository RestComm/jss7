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

package org.mobicents.protocols.ss7.cap.dialog;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class CAPUserAbortPrimitiveImpl implements CAPAsnPrimitive {

	public static final long[] CAP_AbortReason_OId = { 0, 4, 0, 0, 1, 1, 2, 2 };
	
	private CAPUserAbortReason reason;
	
	public CAPUserAbortPrimitiveImpl(CAPUserAbortReason reason) {
		this.reason = reason;
	}
	
	public CAPUserAbortReason getCAPUserAbortReason() {
		return this.reason;
	}
	
	@Override
	public int getTag() throws CAPException {
		return Tag.ENUMERATED;
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
	public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding CAPUserAbortPrimitive: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding CAPUserAbortPrimitive: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding CAPUserAbortPrimitive: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding CAPUserAbortPrimitive: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}


	private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException, AsnException {

		int code = (int)ais.readInteger();
		
		this.reason = CAPUserAbortReason.getInstatse(code);
		if (this.reason == null)
			this.reason = CAPUserAbortReason.no_reason_given;

		if (ais.available() > 0)
			throw new AsnException("Too much source data");
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws CAPException {

		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.ENUMERATED);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {
		
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding CAPUserAbortPrimitive: " + e.getMessage(), e);
		}
	}
	
	@Override
	public void encodeData(AsnOutputStream aos) throws CAPException {

		if (this.reason == null)
			throw new CAPException("reason field must not be empty");

		try {
			aos.writeIntegerData(this.reason.getCode());
		} catch (IOException e) {
			throw new CAPException("IOException when encoding CAPUserAbortPrimitive: " + e.getMessage(), e);
		}
	}
}

