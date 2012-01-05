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
import org.mobicents.protocols.ss7.cap.api.primitives.AChChargingAddress;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.primitives.CAMELAChBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.primitives.ReceivingSideIDImpl;
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ApplyChargingRequestIndicationImpl extends CircuitSwitchedCallMessageImpl implements ApplyChargingRequestIndication {

	public static final int _ID_aChBillingChargingCharacteristics = 0;
	public static final int _ID_partyToCharge = 2;
	public static final int _ID_extensions = 3;
	public static final int _ID_aChChargingAddress = 50;

	public static final String _PrimitiveName = "ApplyChargingRequestIndication";

	private CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics;
	private SendingSideID partyToCharge;
	private CAPExtensions extensions;
	private AChChargingAddress aChChargingAddress;
	
	
	@Override
	public CAMELAChBillingChargingCharacteristics getAChBillingChargingCharacteristics() {
		return aChBillingChargingCharacteristics;
	}

	@Override
	public SendingSideID getPartyToCharge() {
		return partyToCharge;
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
		
		this.aChBillingChargingCharacteristics = null;
		this.partyToCharge = null; // TODO: DEFAULT sendingSideID : leg1
		this.extensions = null;
		this.aChChargingAddress = null; // TODO: DEFAULT legID:sendingSideID:leg1

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();
			
			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_aChBillingChargingCharacteristics:
					this.aChBillingChargingCharacteristics = new CAMELAChBillingChargingCharacteristicsImpl();
					((CAMELAChBillingChargingCharacteristicsImpl) this.aChBillingChargingCharacteristics).decodeAll(ais);
					break;
				case _ID_partyToCharge:
					AsnInputStream ais2 = ais.readSequenceStream();
					ais2.readTag();
					this.partyToCharge = new SendingSideIDImpl();
					((SendingSideIDImpl) this.partyToCharge).decodeAll(ais2);
					break;
				case _ID_extensions:
					ais.advanceElement(); // TODO: implement it
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

		if (this.aChBillingChargingCharacteristics==null)
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": aChBillingChargingCharacteristics is mandatory but not found ",
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
