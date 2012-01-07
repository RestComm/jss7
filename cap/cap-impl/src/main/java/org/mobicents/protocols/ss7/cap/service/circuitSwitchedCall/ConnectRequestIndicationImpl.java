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
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPattern;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.GenericNumberCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.CUGInterlock;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ConnectRequestIndicationImpl extends CircuitSwitchedCallMessageImpl implements ConnectRequestIndication {

	public static final int _ID_destinationRoutingAddress = 0;
	public static final int _ID_alertingPattern = 1;
	public static final int _ID_originalCalledPartyID = 6;
	public static final int _ID_extensions = 10;
	public static final int _ID_carrier = 11;
	public static final int _ID_callingPartysCategory = 28;
	public static final int _ID_redirectingPartyID = 29;
	public static final int _ID_redirectionInformation = 30;
	public static final int _ID_genericNumbers = 14;
	public static final int _ID_serviceInteractionIndicatorsTwo = 15;
	public static final int _ID_chargeNumber = 19;
	public static final int _ID_legToBeConnected = 21;
	public static final int _ID_cug_Interlock = 31;
	public static final int _ID_cug_OutgoingAccess = 32;
	public static final int _ID_suppressionOfAnnouncement = 55;
	public static final int _ID_oCSIApplicable = 56;
	public static final int _ID_naOliInfo = 57;
	public static final int _ID_bor_InterrogationRequested = 58;

	public static final String _PrimitiveName = "ConnectRequestIndication";
	
	private DestinationRoutingAddress destinationRoutingAddress;
	private AlertingPattern alertingPattern;
	private OriginalCalledNumberCap originalCalledPartyID;
	private CAPExtensions extensions;
	private Carrier carrier;
	private CallingPartysCategoryInap callingPartysCategory;
	private RedirectingPartyIDCap redirectingPartyID;
	private RedirectionInformationInap redirectionInformation;
	private ArrayList<GenericNumberCap> genericNumbers;
	private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
	private LocationNumberCap chargeNumber;
	private LegID legToBeConnected;
	private CUGInterlock cugInterlock;
	private boolean cugOutgoingAccess;
	private boolean suppressionOfAnnouncement;
	private boolean ocsIApplicable;
	private NAOliInfo naoliInfo;
	private boolean borInterrogationRequested;
	

	@Override
	public DestinationRoutingAddress getDestinationRoutingAddress() {
		return destinationRoutingAddress;
	}

	@Override
	public AlertingPattern getAlertingPattern() {
		return alertingPattern;
	}

	@Override
	public OriginalCalledNumberCap getOriginalCalledPartyID() {
		return originalCalledPartyID;
	}

	@Override
	public CAPExtensions getExtensions() {
		return extensions;
	}

	@Override
	public Carrier getCarrier() {
		return carrier;
	}

	@Override
	public CallingPartysCategoryInap getCallingPartysCategory() {
		return callingPartysCategory;
	}

	@Override
	public RedirectingPartyIDCap getRedirectingPartyID() {
		return redirectingPartyID;
	}

	@Override
	public RedirectionInformationInap getRedirectionInformation() {
		return redirectionInformation;
	}

	@Override
	public ArrayList<GenericNumberCap> getGenericNumbers() {
		return genericNumbers;
	}

	@Override
	public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
		return serviceInteractionIndicatorsTwo;
	}

	@Override
	public LocationNumberCap getChargeNumber() {
		return chargeNumber;
	}

	@Override
	public LegID getLegToBeConnected() {
		return legToBeConnected;
	}

	@Override
	public CUGInterlock getCUGInterlock() {
		return cugInterlock;
	}

	@Override
	public boolean getCugOutgoingAccess() {
		return cugOutgoingAccess;
	}

	@Override
	public boolean getSuppressionOfAnnouncement() {
		return suppressionOfAnnouncement;
	}

	@Override
	public boolean getOCSIApplicable() {
		return ocsIApplicable;
	}

	@Override
	public NAOliInfo getNAOliInfo() {
		return naoliInfo;
	}

	@Override
	public boolean getBorInterrogationRequested() {
		return borInterrogationRequested;
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

		this.destinationRoutingAddress = null;
		this.alertingPattern = null;
		this.originalCalledPartyID = null;
		this.extensions = null;
		this.carrier = null;
		this.callingPartysCategory = null;
		this.redirectingPartyID = null;
		this.redirectionInformation = null;
		this.genericNumbers = null;
		this.serviceInteractionIndicatorsTwo = null;
		this.chargeNumber = null;
		this.legToBeConnected = null;
		this.cugInterlock = null;
		this.cugOutgoingAccess = false;
		this.suppressionOfAnnouncement = false;
		this.ocsIApplicable = false;
		this.naoliInfo = null;
		this.borInterrogationRequested = false;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_destinationRoutingAddress:
					this.destinationRoutingAddress = new DestinationRoutingAddressImpl();
					((DestinationRoutingAddressImpl) this.destinationRoutingAddress).decodeAll(ais);
					break;
				case _ID_alertingPattern:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_originalCalledPartyID:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_extensions:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_carrier:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_callingPartysCategory:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_redirectingPartyID:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_redirectionInformation:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_genericNumbers:
					this.genericNumbers = new ArrayList<GenericNumberCap>();
					AsnInputStream ais2 = ais.readSequenceStreamData(length);
					while (true) {
						if (ais2.available() == 0)
							break;

						int tag2 = ais2.readTag();
						if (ais2.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC && tag2 != Tag.STRING_OCTET)
							throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
									+ " genericNumbers parameter SET must consist of OCTET_STRING elements",
									CAPParsingComponentExceptionReason.MistypedParameter);

						GenericNumberCapImpl elem = new GenericNumberCapImpl();
						elem.decodeAll(ais2);
						this.genericNumbers.add(elem);
					}
					break;
				case _ID_serviceInteractionIndicatorsTwo:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_chargeNumber:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_legToBeConnected:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_cug_Interlock:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_cug_OutgoingAccess:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_suppressionOfAnnouncement:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_oCSIApplicable:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_naOliInfo:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_bor_InterrogationRequested:
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

		if (this.destinationRoutingAddress == null)
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": destinationRoutingAddress is mandatory but not found ",
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
