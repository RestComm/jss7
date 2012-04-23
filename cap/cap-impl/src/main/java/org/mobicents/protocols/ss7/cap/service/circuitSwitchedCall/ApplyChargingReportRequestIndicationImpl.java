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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeDurationChargingResultImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ApplyChargingReportRequestIndicationImpl extends CircuitSwitchedCallMessageImpl implements ApplyChargingReportRequestIndication {

	public static final int _ID_timeDurationChargingResult = 0;

	public static final int _ID_partyToCharge = 0;

	public static final String _PrimitiveName = "ApplyChargingReportRequestIndication";

	private TimeDurationChargingResult timeDurationChargingResult;
	
	
	public ApplyChargingReportRequestIndicationImpl() {
	}

	public ApplyChargingReportRequestIndicationImpl(TimeDurationChargingResult timeDurationChargingResult) {
		this.timeDurationChargingResult = timeDurationChargingResult;
	}

	@Override
	public CAPMessageType getMessageType() {
		return CAPMessageType.applyChargingReport_Request;
	}

	@Override
	public int getOperationCode() {
		return CAPOperationCode.applyChargingReport;
	}

	@Override
	public TimeDurationChargingResult getTimeDurationChargingResult() {
		return timeDurationChargingResult;
	}

	
	@Override
	public int getTag() throws CAPException {
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

	private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

		this.timeDurationChargingResult = null;

		byte[] buf = ansIS.readOctetStringData(length);
		AsnInputStream aiss = new AsnInputStream(buf);

		int tag = aiss.readTag();

		if (tag != _ID_timeDurationChargingResult || aiss.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || aiss.isTagPrimitive())
			throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
					+ ": bad tag or tagClass or is primitive of the choice timeDurationChargingResult", CAPParsingComponentExceptionReason.MistypedParameter);

		this.timeDurationChargingResult = new TimeDurationChargingResultImpl();
		((TimeDurationChargingResultImpl) this.timeDurationChargingResult).decodeAll(aiss);
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

		if (this.timeDurationChargingResult == null)
			throw new CAPException("Error while encoding " + _PrimitiveName + ": timeDurationChargingResult must not be null");

		try {
			asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_timeDurationChargingResult);
			int pos = asnOs.StartContentDefiniteLength();
			((TimeDurationChargingResultImpl) this.timeDurationChargingResult).encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");

		if (this.timeDurationChargingResult != null) {
			sb.append("timeDurationChargingResult=");
			sb.append(timeDurationChargingResult.toString());
		}

		sb.append("]");

		return sb.toString();
	}
}
