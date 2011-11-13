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
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LowLayerCompatibility;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UUData;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.SupportedCamelPhases;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class InitialDPArgExtensionImpl implements InitialDPArgExtension, CAPAsnPrimitive {

	public static final int _ID_gmscAddress = 0;
	public static final int _ID_forwardingDestinationNumber = 1;
	public static final int _ID_ms_Classmark2 = 2;
	public static final int _ID_iMEI = 3;
	public static final int _ID_supportedCamelPhases = 4;
	public static final int _ID_offeredCamel4Functionalities = 5;
	public static final int _ID_bearerCapability2 = 6;
	public static final int _ID_ext_basicServiceCode2 = 7;
	public static final int _ID_highLayerCompatibility2 = 8;
	public static final int _ID_lowLayerCompatibility = 9;
	public static final int _ID_lowLayerCompatibility2 = 10;
	public static final int _ID_enhancedDialledServicesAllowed = 11;
	public static final int _ID_uu_Data = 12;

	public static final String _PrimitiveName = "InitialDPArgExtension";

	private ISDNAddressString gmscAddress;
	private CalledPartyNumberCap forwardingDestinationNumber;
	private MSClassmark2 msClassmark2;
	private IMEI imei;
	private SupportedCamelPhases supportedCamelPhases;
	private OfferedCamel4Functionalities offeredCamel4Functionalities;
	private BearerCapability bearerCapability2;
	private ExtBasicServiceCode extBasicServiceCode2;
	private HighLayerCompatibilityInap highLayerCompatibility2;
	private LowLayerCompatibility lowLayerCompatibility;
	private LowLayerCompatibility lowLayerCompatibility2;
	private boolean enhancedDialledServicesAllowed;
	private UUData uuData;	
	
	

	@Override
	public ISDNAddressString getGmscAddress() {
		return gmscAddress;
	}

	@Override
	public CalledPartyNumberCap getForwardingDestinationNumber() {
		return forwardingDestinationNumber;
	}

	@Override
	public MSClassmark2 getMSClassmark2() {
		return msClassmark2;
	}

	@Override
	public IMEI getIMEI() {
		return imei;
	}

	@Override
	public SupportedCamelPhases getSupportedCamelPhases() {
		return supportedCamelPhases;
	}

	@Override
	public OfferedCamel4Functionalities getOfferedCamel4Functionalities() {
		return offeredCamel4Functionalities;
	}

	@Override
	public BearerCapability getBearerCapability2() {
		return bearerCapability2;
	}

	@Override
	public ExtBasicServiceCode getExtBasicServiceCode2() {
		return extBasicServiceCode2;
	}

	@Override
	public HighLayerCompatibilityInap getHighLayerCompatibility2() {
		return highLayerCompatibility2;
	}

	@Override
	public LowLayerCompatibility getLowLayerCompatibility() {
		return lowLayerCompatibility;
	}

	@Override
	public LowLayerCompatibility getLowLayerCompatibility2() {
		return lowLayerCompatibility2;
	}

	@Override
	public boolean getEnhancedDialledServicesAllowed() {
		return enhancedDialledServicesAllowed;
	}

	@Override
	public UUData getUUData() {
		return uuData;
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

		this.gmscAddress = null;
		this.forwardingDestinationNumber = null;
		this.msClassmark2 = null;
		this.imei = null;
		this.supportedCamelPhases = null;
		this.offeredCamel4Functionalities = null;
		this.bearerCapability2 = null;
		this.extBasicServiceCode2 = null;
		this.highLayerCompatibility2 = null;
		this.lowLayerCompatibility = null;
		this.lowLayerCompatibility2 = null;
		this.enhancedDialledServicesAllowed = false;
		this.uuData = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_gmscAddress:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_forwardingDestinationNumber:
					byte[] buf = ais.readOctetString();
					if (buf.length < 2 || buf.length > 18)
						throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ": forwardingDestinationNumber must be from 2 to 18 bytes length, found: " + buf.length,
								CAPParsingComponentExceptionReason.MistypedParameter);
					this.forwardingDestinationNumber = new CalledPartyNumberCapImpl(buf);
					break;
				case _ID_ms_Classmark2:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_iMEI:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_supportedCamelPhases:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_offeredCamel4Functionalities:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_bearerCapability2:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_ext_basicServiceCode2:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_highLayerCompatibility2:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_lowLayerCompatibility:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_lowLayerCompatibility2:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_enhancedDialledServicesAllowed:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_uu_Data:
					ais.advanceElement(); // TODO: implement it
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else {
				ais.advanceElement();
			}
			break;
		}
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
