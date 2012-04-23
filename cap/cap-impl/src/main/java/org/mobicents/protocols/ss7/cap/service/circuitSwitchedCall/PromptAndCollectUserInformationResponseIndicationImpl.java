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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessageType;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponseIndication;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class PromptAndCollectUserInformationResponseIndicationImpl extends CircuitSwitchedCallMessageImpl implements PromptAndCollectUserInformationResponseIndication {

	public static final int _ID_digitsResponse = 0;

	public static final String _PrimitiveName = "PromptAndCollectUserInformationResponseIndication";

	public Digits digitsResponse;


	public PromptAndCollectUserInformationResponseIndicationImpl() {
	}

	public PromptAndCollectUserInformationResponseIndicationImpl(Digits digitsResponse) {
		this.digitsResponse = digitsResponse;
	}

	@Override
	public CAPMessageType getMessageType() {
		return CAPMessageType.promptAndCollectUserInformation_Response;
	}

	@Override
	public int getOperationCode() {
		return CAPOperationCode.promptAndCollectUserInformation;
	}

	@Override
	public Digits getDigitsResponse() {
		return digitsResponse;
	}

	@Override
	public int getTag() throws CAPException {
		return _ID_digitsResponse;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
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
			throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException, AsnException {

		this.digitsResponse = null;

		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tagClass or is not primitive",
					CAPParsingComponentExceptionReason.MistypedParameter);

		switch (ais.getTag()) {
		case _ID_digitsResponse:
			this.digitsResponse = new DigitsImpl();
			((DigitsImpl) this.digitsResponse).decodeData(ais, length);
			break;
		default:
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tag: " + ais.getTag(),
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws CAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws CAPException {

		int choiceCnt = 0;
		if (this.digitsResponse != null)
			choiceCnt++;
		
		if (choiceCnt != 1)
			throw new CAPException("Error while encoding " + _PrimitiveName + ": only one choice must be definite, found: " + choiceCnt);

		if (this.digitsResponse != null)
			((DigitsImpl) this.digitsResponse).encodeData(asnOs);
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");
		
		if (this.digitsResponse != null) {
			sb.append("digitsResponse=");
			sb.append(digitsResponse.toString());
		}

		sb.append("]");

		return sb.toString();
	}
}
