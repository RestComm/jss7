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
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class CAPGprsReferenceNumberImpl implements CAPGprsReferenceNumber, CAPAsnPrimitive {

	protected static final int DESTINATION_REF_TAG = 0;
	protected static final int ORIGINATION_REF_TAG = 1;

	public static final long[] CAP_Dialogue_OId = new long[] { 0, 4, 0, 0, 1, 1, 5, 2 };

	private Integer destinationReference;
	private Integer originationReference;

	
	public CAPGprsReferenceNumberImpl() {
	}
	
	public CAPGprsReferenceNumberImpl(Integer destinationReference, Integer originationReference) {
		this.destinationReference = destinationReference;
		this.originationReference = originationReference;
	}
	
	@Override
	public Integer getDestinationReference() {
		return this.destinationReference;
	}

	@Override
	public Integer getOriginationReference() {
		return this.originationReference;
	}

	@Override
	public void setDestinationReference(Integer destinationReference) {
		this.destinationReference = destinationReference;
	}

	@Override
	public void setOriginationReference(Integer originationReference) {
		this.originationReference = originationReference;
	}


	@Override
	public int getTag() throws CAPException {
		return Tag.SEQUENCE;
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
	public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding CAPGprsReferenceNumber: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding CAPGprsReferenceNumber: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding CAPGprsReferenceNumber: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding CAPGprsReferenceNumber: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}


	private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException, AsnException {

		this.destinationReference = null;
		this.originationReference = null;

		AsnInputStream localAis = ais.readSequenceStreamData(length);

		while (localAis.available() > 0) {
			int tag = localAis.readTag();

			switch (localAis.getTagClass()) {
			case Tag.CLASS_CONTEXT_SPECIFIC:
				switch (tag) {
				case DESTINATION_REF_TAG:
					this.destinationReference = (int)ais.readInteger();
					break;

				case ORIGINATION_REF_TAG:
					this.originationReference = (int)ais.readInteger();
					break;

				default:
					localAis.advanceElement();
					break;
				}
				break;

			default:
				localAis.advanceElement();
				break;
			}
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws CAPException {

		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {
		
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding CAPGprsReferenceNumber: " + e.getMessage(), e);
		}
	}
	
	@Override
	public void encodeData(AsnOutputStream aos) throws CAPException {

		try {
			if (this.destinationReference != null)
				aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, DESTINATION_REF_TAG, this.destinationReference);

			if (this.originationReference != null)
				aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, ORIGINATION_REF_TAG, this.originationReference);
		} catch (IOException e) {
			throw new CAPException("IOException when encoding CAPGprsReferenceNumber: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding CAPGprsReferenceNumber: " + e.getMessage(), e);
		}
	}
}
