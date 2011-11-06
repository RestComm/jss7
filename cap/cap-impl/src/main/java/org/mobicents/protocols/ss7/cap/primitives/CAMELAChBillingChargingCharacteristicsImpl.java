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

package org.mobicents.protocols.ss7.cap.primitives;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AudibleIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CAMELAChBillingChargingCharacteristicsImpl implements CAMELAChBillingChargingCharacteristics, CAPAsnPrimitive {

	public static final int _ID_timeDurationCharging = 0;

	public static final int _ID_maxCallPeriodDuration = 0;
	public static final int _ID_releaseIfdurationExceeded = 1;
	public static final int _ID_tariffSwitchInterval = 2;
	public static final int _ID_audibleIndicator = 3;
	public static final int _ID_extensions = 4;

	public static final String _PrimitiveName = "CAMELAChBillingChargingCharacteristics";

	private byte[] data;
	private long maxCallPeriodDuration;
	private boolean releaseIfdurationExceeded;
	private Long tariffSwitchInterval;
	private AudibleIndicator audibleIndicator;
	private CAPExtensions extensions;
	

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public long getMaxCallPeriodDuration() {
		return maxCallPeriodDuration;
	}

	@Override
	public boolean getReleaseIfdurationExceeded() {
		return releaseIfdurationExceeded;
	}

	@Override
	public Long getTariffSwitchInterval() {
		return tariffSwitchInterval;
	}

	@Override
	public AudibleIndicator getAudibleIndicator() {
		return audibleIndicator;
	}

	@Override
	public CAPExtensions getExtensions() {
		return extensions;
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

		this.data = null;
		this.maxCallPeriodDuration = -1;
		this.releaseIfdurationExceeded = false;
		this.tariffSwitchInterval = 0L;
		this.audibleIndicator = null; // TODO: DEFAULT tone: FALSE
		this.extensions = null;
		
		this.data = ansIS.readOctetStringData(length);

		AsnInputStream aiss = new AsnInputStream(this.data);
		int tag = aiss.readTag();
		if (tag != _ID_timeDurationCharging || aiss.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || aiss.isTagPrimitive())
			throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
					+ ": CAMEL-AChBillingChargingCharacteristics choice has bad tag oe tagClass or is primitive, tag=" + tag + ", tagClass="
					+ aiss.getTagClass(), CAPParsingComponentExceptionReason.MistypedParameter);
		
		AsnInputStream ais = aiss.readSequenceStream();
		while (true) {
			if (ais.available() == 0)
				break;

			tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_maxCallPeriodDuration:
					this.maxCallPeriodDuration = ais.readInteger();
					break;
				case _ID_releaseIfdurationExceeded:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_tariffSwitchInterval:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_audibleIndicator:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_extensions:
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

		if (this.maxCallPeriodDuration == -1)
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": maxCallPeriodDuration is mandatory but not found",
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
