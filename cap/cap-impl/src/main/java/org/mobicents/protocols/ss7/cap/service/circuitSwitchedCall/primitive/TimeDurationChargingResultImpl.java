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
import org.mobicents.protocols.ss7.cap.api.primitives.AChChargingAddress;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeInformation;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.ReceivingSideIDImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TimeDurationChargingResultImpl implements TimeDurationChargingResult, CAPAsnPrimitive {

	public static final int _ID_partyToCharge = 0;
	public static final int _ID_timeInformation = 1;
	public static final int _ID_legActive = 2;
	public static final int _ID_callLegReleasedAtTcpExpiry = 3;
	public static final int _ID_extensions = 4;
	public static final int _ID_aChChargingAddress = 5;
	
	public static final String _PrimitiveName = "TimeDurationChargingResult";

	private ReceivingSideID partyToCharge;
	private TimeInformation timeInformation;
	private boolean legActive;
	private boolean callLegReleasedAtTcpExpiry;
	private CAPExtensions extensions;
	private AChChargingAddress aChChargingAddress;

	
	public TimeDurationChargingResultImpl(){
	}
	
	public TimeDurationChargingResultImpl(ReceivingSideID partyToCharge, TimeInformation timeInformation, boolean legActive,
			boolean callLegReleasedAtTcpExpiry, CAPExtensions extensions, AChChargingAddress aChChargingAddress) {
		this.partyToCharge = partyToCharge;
		this.timeInformation = timeInformation;
		this.legActive = legActive;
		this.callLegReleasedAtTcpExpiry = callLegReleasedAtTcpExpiry;
		this.extensions = extensions;
		this.aChChargingAddress = aChChargingAddress;
	}
	
	@Override
	public ReceivingSideID getPartyToCharge() {
		return partyToCharge;
	}

	@Override
	public TimeInformation getTimeInformation() {
		return timeInformation;
	}

	@Override
	public boolean getLegActive() {
		return legActive;
	}

	@Override
	public boolean getCallLegReleasedAtTcpExpiry() {
		return callLegReleasedAtTcpExpiry;
	}

	@Override
	public CAPExtensions getExtensions() {
		return extensions;
	}

	@Override
	public AChChargingAddress getAChChargingAddress() {
		return aChChargingAddress;
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
		} catch (MAPParsingComponentException e) {
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
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
		} catch (MAPParsingComponentException e) {
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException, IOException, AsnException {

		this.partyToCharge = null;
		this.timeInformation = null;
		this.legActive = true;
		this.callLegReleasedAtTcpExpiry = false;
		this.extensions = null;
		this.aChChargingAddress = null; // TODO: DEFAULT legID:receivingSideID:leg1
		
		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_partyToCharge:
					AsnInputStream ais2 = ais.readSequenceStream();
					ais2.readTag();
					this.partyToCharge = new ReceivingSideIDImpl();
					((ReceivingSideIDImpl)this.partyToCharge).decodeAll(ais2);
					break;
				case _ID_timeInformation:
					ais2 = ais.readSequenceStream();
					ais2.readTag();
					this.timeInformation = new TimeInformationImpl();
					((TimeInformationImpl)this.timeInformation).decodeAll(ais2);
					break;
				case _ID_legActive:
					this.legActive = ais.readBoolean();
					break;
				case _ID_callLegReleasedAtTcpExpiry:
					ais.readNull();
					this.callLegReleasedAtTcpExpiry = true;
					break;
				case _ID_extensions:
					this.extensions = new CAPExtensionsImpl();
					((CAPExtensionsImpl) this.extensions).decodeAll(ais);
					break;
				case _ID_aChChargingAddress:
					ais.advanceElement(); // TODO: implement it
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else {
				ais.advanceElement();
			}
		}

		if (this.partyToCharge == null || this.timeInformation == null)
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": partyToCharge and timeInformation are mandatory but not found",
					CAPParsingComponentExceptionReason.MistypedParameter);
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

		if (this.partyToCharge == null || this.timeInformation == null)
			throw new CAPException("Error while encoding " + _PrimitiveName + ": partyToCharge and timeInformation must not be null");

		try {
			aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_partyToCharge);
			int pos = aos.StartContentDefiniteLength();
			((ReceivingSideIDImpl) this.partyToCharge).encodeAll(aos);
			aos.FinalizeContent(pos);

			aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_timeInformation);
			pos = aos.StartContentDefiniteLength();
			((TimeInformationImpl) this.timeInformation).encodeAll(aos);
			aos.FinalizeContent(pos);

			if (this.legActive == false)
				aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, _ID_legActive, this.legActive);

			if (this.callLegReleasedAtTcpExpiry)
				aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callLegReleasedAtTcpExpiry);

			if (this.extensions != null)
				((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

			if (this.aChChargingAddress != null) {
				// TODO: implement it
			}
		} catch (IOException e) {
			throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");

		if (this.partyToCharge != null) {
			sb.append("partyToCharge=");
			sb.append(partyToCharge.toString());
		}
		if (this.timeInformation != null) {
			sb.append(", timeInformation=");
			sb.append(timeInformation.toString());
		}
		if (this.legActive) {
			sb.append(", legActive");
		}
		if (this.callLegReleasedAtTcpExpiry) {
			sb.append(", callLegReleasedAtTcpExpiry");
		}
		if (this.extensions != null) {
			sb.append(", extensions=");
			sb.append(extensions.toString());
		}
		if (this.aChChargingAddress != null) {
			sb.append(", aChChargingAddress=");
			sb.append(aChChargingAddress.toString());
		}

		sb.append("]");

		return sb.toString();
	}
}

