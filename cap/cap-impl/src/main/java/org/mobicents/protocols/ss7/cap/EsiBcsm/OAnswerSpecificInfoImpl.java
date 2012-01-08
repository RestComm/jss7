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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ChargeIndicator;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBasicServiceCode;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class OAnswerSpecificInfoImpl implements OAnswerSpecificInfo, CAPAsnPrimitive {

	public static final int _ID_destinationAddress = 50;
	public static final int _ID_orCall = 51;
	public static final int _ID_forwardedCall = 52;
	public static final int _ID_chargeIndicator = 53;
	public static final int _ID_extbasicServiceCode = 54;
	public static final int _ID_extbasicServiceCode2 = 55;

	public static final String _PrimitiveName = "OAnswerSpecificInfo";

	private CalledPartyNumberCap destinationAddress;
	private boolean orCall;
	private boolean forwardedCall;
	private ChargeIndicator chargeIndicator;
	private ExtBasicServiceCode extBasicServiceCode;
	private ExtBasicServiceCode extBasicServiceCode2;

	public OAnswerSpecificInfoImpl() {
	}

	public OAnswerSpecificInfoImpl(CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall, ChargeIndicator chargeIndicator,
			ExtBasicServiceCode extBasicServiceCode, ExtBasicServiceCode extBasicServiceCode2) {
		this.destinationAddress = destinationAddress;
		this.orCall = orCall;
		this.forwardedCall = forwardedCall;
		this.chargeIndicator = chargeIndicator;
		this.extBasicServiceCode = extBasicServiceCode;
		this.extBasicServiceCode2 = extBasicServiceCode2;
	}

	@Override
	public CalledPartyNumberCap getDestinationAddress() {
		return destinationAddress;
	}

	@Override
	public boolean getOrCall() {
		return orCall;
	}

	@Override
	public boolean getForwardedCall() {
		return forwardedCall;
	}

	@Override
	public ChargeIndicator getChargeIndicator() {
		return chargeIndicator;
	}

	@Override
	public ExtBasicServiceCode getExtBasicServiceCode() {
		return extBasicServiceCode;
	}

	@Override
	public ExtBasicServiceCode getExtBasicServiceCode2() {
		return extBasicServiceCode2;
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

		this.destinationAddress = null;
		this.orCall = false;
		this.forwardedCall = false;
		this.chargeIndicator = null;
		this.extBasicServiceCode = null;
		this.extBasicServiceCode2 = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_destinationAddress:
					ais.advanceElement();
					// TODO: implement it
					break;
				case _ID_orCall:
					ais.advanceElement();
					// TODO: implement it
					break;
				case _ID_forwardedCall:
					ais.advanceElement();
					// TODO: implement it
					break;
				case _ID_chargeIndicator:
					ais.advanceElement();
					// TODO: implement it
					break;
				case _ID_extbasicServiceCode:
					ais.advanceElement();
					// TODO: implement it
					break;
				case _ID_extbasicServiceCode2:
					ais.advanceElement();
					// TODO: implement it
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else {
				ais.advanceElement();
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
			throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws CAPException {

		if (this.destinationAddress != null) {
			// TODO: implement it
		}
		if (this.orCall) {
			// TODO: implement it
		}
		if (this.forwardedCall) {
			// TODO: implement it
		}
		if (this.chargeIndicator != null) {
			// TODO: implement it
		}
		if (this.extBasicServiceCode != null) {
			// TODO: implement it
		}
		if (this.extBasicServiceCode2 != null) {
			// TODO: implement it
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");
		
		if (this.destinationAddress != null) {
			sb.append("destinationAddress= [");
			sb.append(destinationAddress.toString());
			sb.append("]");
		}
		if (this.orCall) {
			sb.append(", orCall");
		}
		if (this.forwardedCall) {
			sb.append(", forwardedCall");
		}
		if (this.chargeIndicator != null) {
			sb.append(", chargeIndicator= [");
			sb.append(chargeIndicator.toString());
			sb.append("]");
		}
		if (this.extBasicServiceCode != null) {
			sb.append(", extBasicServiceCode= [");
			sb.append(extBasicServiceCode.toString());
			sb.append("]");
		}
		if (this.extBasicServiceCode2 != null) {
			sb.append(", extBasicServiceCode2= [");
			sb.append(extBasicServiceCode2.toString());
			sb.append("]");
		}

		sb.append("]");

		return sb.toString();
	}
}
