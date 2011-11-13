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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.Cause;
import org.mobicents.protocols.ss7.cap.api.primitives.DateAndTime;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class RequestedInformationImpl implements RequestedInformation, CAPAsnPrimitive {

	public static final int _ID_requestedInformationType = 0;
	public static final int _ID_requestedInformationValue = 1;

	public static final int _ID_callAttemptElapsedTimeValue = 0;
	public static final int _ID_callStopTimeValue = 1;
	public static final int _ID_callConnectedElapsedTimeValue = 2;
	public static final int _ID_releaseCauseValue = 30;
	                                               
	public static final String _PrimitiveName = "RequestedInformation";

	private RequestedInformationType tequestedInformationType;
	private Integer callAttemptElapsedTimeValue;
	private DateAndTime callStopTimeValue;
	private Integer callConnectedElapsedTimeValue;
	private Cause releaseCauseValue;

	
	@Override
	public RequestedInformationType getRequestedInformationType() {
		return tequestedInformationType;
	}

	@Override
	public Integer getCallAttemptElapsedTimeValue() {
		return callAttemptElapsedTimeValue;
	}

	@Override
	public DateAndTime getCallStopTimeValue() {
		return callStopTimeValue;
	}

	@Override
	public Integer getCallConnectedElapsedTimeValue() {
		return callConnectedElapsedTimeValue;
	}

	@Override
	public Cause getReleaseCauseValue() {
		return releaseCauseValue;
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

		this.tequestedInformationType = null;
		this.callAttemptElapsedTimeValue = null;
		this.callStopTimeValue = null;
		this.callConnectedElapsedTimeValue = null;
		this.releaseCauseValue = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		boolean valueReceived = false;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_requestedInformationType:
					int i1 = (int) ais.readInteger();
					this.tequestedInformationType = RequestedInformationType.getInstance(i1);
					break;
				case _ID_requestedInformationValue:
					valueReceived = true;
					AsnInputStream ais2 = ais.readSequenceStream();
					int tag2 = ais2.readTag();
					if (ais2.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
						throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad RequestedInformationValue tagClass",
								CAPParsingComponentExceptionReason.MistypedParameter);

					switch (tag2) {
					case _ID_callAttemptElapsedTimeValue:
						ais2.advanceElement(); // TODO: implement it
						break;
					case _ID_callStopTimeValue:
						ais2.advanceElement(); // TODO: implement it
						break;
					case _ID_callConnectedElapsedTimeValue:
						ais2.advanceElement(); // TODO: implement it
						break;
					case _ID_releaseCauseValue:
						ais2.advanceElement(); // TODO: implement it
						break;
					default:
						if (ais2.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
							throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad RequestedInformationValue tag",
									CAPParsingComponentExceptionReason.MistypedParameter);
					}
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else {
				ais.advanceElement();
			}
		}

		if (this.tequestedInformationType == null || !valueReceived)
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
					+ ": requestedInformationType and requestedInformationValue are mandatory but not found",
					CAPParsingComponentExceptionReason.MistypedParameter);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws CAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws CAPException {
		// TODO Auto-generated method stub
		
	}
}
