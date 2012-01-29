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
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancel;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CallSegmentToCancelImpl;

/**
*
* 
* @author sergey vetyutnev
* 
*/
public class CancelRequestIndicationImpl extends CircuitSwitchedCallMessageImpl implements CancelRequestIndication {

	public static final int _ID_invokeID = 0;
	public static final int _ID_allRequests = 1;
	public static final int _ID_callSegmentToCancel = 2;

	public static final String _PrimitiveName = "CancelRequest";

	private Integer invokeID;
	private boolean allRequests;
	private CallSegmentToCancel callSegmentToCancel;
	
	public CancelRequestIndicationImpl() {
	}

	public CancelRequestIndicationImpl(Integer invokeID) {
		this.invokeID = invokeID;
	}

	public CancelRequestIndicationImpl(boolean allRequests) {
		this.allRequests = allRequests;
	}

	public CancelRequestIndicationImpl(CallSegmentToCancel callSegmentToCancel) {
		this.callSegmentToCancel = callSegmentToCancel;
	}
	
	@Override
	public Integer getInvokeID() {
		return invokeID;
	}

	@Override
	public boolean getAllRequests() {
		return allRequests;
	}

	@Override
	public CallSegmentToCancel getCallSegmentToCancel() {
		return callSegmentToCancel;
	}

	@Override
	public int getTag() throws CAPException {

		if (this.invokeID != null) {
			return _ID_invokeID;
		} else if (this.allRequests) {
			return _ID_allRequests;
		} else if (this.callSegmentToCancel != null) {
			return _ID_callSegmentToCancel;
		} else {
			throw new CAPException("Error while encoding " + _PrimitiveName + ": no of choices has been definite");
		}
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}

	@Override
	public boolean getIsPrimitive() {
		if (this.callSegmentToCancel != null)
			return false;
		else
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

		this.invokeID = null;
		this.allRequests = false;
		this.callSegmentToCancel = null;

		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tagClass",
					CAPParsingComponentExceptionReason.MistypedParameter);

		switch (ais.getTag()) {
		case _ID_invokeID:
			this.invokeID = (int) ais.readIntegerData(length);
			break;
		case _ID_allRequests:
			ais.readNullData(length);
			this.allRequests = true;
			break;
		case _ID_callSegmentToCancel:
			this.callSegmentToCancel = new CallSegmentToCancelImpl();
			((CallSegmentToCancelImpl) this.callSegmentToCancel).decodeData(ais, length);
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
	public void encodeData(AsnOutputStream aos) throws CAPException {

		int choiceCnt = 0;
		if (this.invokeID != null)
			choiceCnt++;
		if (this.allRequests)
			choiceCnt++;
		if (this.callSegmentToCancel != null)
			choiceCnt++;
		
		if (choiceCnt != 1)
			throw new CAPException("Error while encoding " + _PrimitiveName + ": only one choice must be definite, found: " + choiceCnt);

		try {
			if (this.invokeID != null)
				aos.writeIntegerData(this.invokeID);
			if (this.allRequests)
				aos.writeNullData();
			if (this.callSegmentToCancel != null)
				((CallSegmentToCancelImpl) this.callSegmentToCancel).encodeData(aos);
		} catch (IOException e) {
			throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");

		if (this.invokeID != null) {
			sb.append("invokeID=");
			sb.append(invokeID);
		}
		if (this.allRequests) {
			sb.append(" allRequests");
		}
		if (this.callSegmentToCancel != null) {
			sb.append(" callSegmentToCancel=");
			sb.append(callSegmentToCancel.toString());
		}

		sb.append("]");

		return sb.toString();
	}
}

