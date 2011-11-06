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
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.Digits;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CallingPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.CUGIndex;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.subscriberInformation.LocationInformationImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class InitialDPRequestIndicationImpl extends CircuitSwitchedCallMessageImpl implements InitialDPRequestIndication {

	public static final int _ID_serviceKey = 0;
	public static final int _ID_calledPartyNumber = 2;
	public static final int _ID_callingPartyNumber = 3;
	public static final int _ID_callingPartysCategory = 5;
	public static final int _ID_cGEncountered = 7;
	public static final int _ID_iPSSPCapabilities = 8;
	public static final int _ID_locationNumber = 10;
	public static final int _ID_originalCalledPartyID = 12;
	public static final int _ID_extensions = 15;
	public static final int _ID_highLayerCompatibility = 23;
	public static final int _ID_additionalCallingPartyNumber = 25;
	public static final int _ID_bearerCapability = 27;
	public static final int _ID_eventTypeBCSM = 28;
	public static final int _ID_redirectingPartyID = 29;
	public static final int _ID_redirectionInformation = 30;
	public static final int _ID_cause = 17;
	public static final int _ID_serviceInteractionIndicatorsTwo = 32;
	public static final int _ID_carrier = 37;
	public static final int _ID_cug_Index = 45;
	public static final int _ID_cug_Interlock = 46;
	public static final int _ID_cug_OutgoingAccess = 47;
	public static final int _ID_iMSI = 50;
	public static final int _ID_subscriberState = 51;
	public static final int _ID_locationInformation = 52;
	public static final int _ID_ext_basicServiceCode = 53;
	public static final int _ID_callReferenceNumber = 54;
	public static final int _ID_mscAddress = 55;
	public static final int _ID_calledPartyBCDNumber = 56;
	public static final int _ID_timeAndTimezone = 57;
	public static final int _ID_callForwardingSS_Pending = 58;
	public static final int _ID_initialDPArgExtension = 59;

	private int serviceKey;
	private byte[] calledPartyNumber;
	private byte[] callingPartyNumber;
	private byte[] callingPartysCategory;
	private CGEncountered CGEncountered;
	private IPSSPCapabilities IPSSPCapabilities;
	private byte[] locationNumber;
	private byte[] originalCalledPartyID;
	private CAPExtensions extensions;
	private byte[] highLayerCompatibility;
	private Digits additionalCallingPartyNumber;
	private BearerCapability bearerCapability;
	private EventTypeBCSM eventTypeBCSM;
	private byte[] redirectingPartyID;
	private byte[] redirectionInformation;
	private byte[] cause;
	private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
	private byte[] carrier;
	private CUGIndex cugIndex;
	private CUGInterlock cugInterlock;
	private boolean cugOutgoingAccess;
	private IMSI imsi;
	private SubscriberState subscriberState;
	private LocationInformation locationInformation;
	private ExtBasicServiceCode extBasicServiceCode;
	private CallReferenceNumber callReferenceNumber;
	private ISDNAddressString mscAddress;
	private CalledPartyBCDNumber calledPartyBCDNumber;
	private TimeAndTimezone timeAndTimezone;
	private boolean callForwardingSSPending;
	private InitialDPArgExtension initialDPArgExtension;
	

	@Override
	public int getServiceKey() {
		return this.serviceKey;
	}

	@Override
	public byte[] getCalledPartyNumber() {
		return this.calledPartyNumber;
	}

	@Override
	public CalledPartyNumber getCalledPartyNumberIsup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getCallingPartyNumber() {
		return callingPartyNumber;
	}

	@Override
	public CallingPartyNumber getCallingPartyNumberIsup() throws CAPException {
		
		if (this.callingPartyNumber == null)
			return null;
		
		CallingPartyNumberImpl res = new CallingPartyNumberImpl();
		try {
			res.decode(this.callingPartyNumber);
		} catch (ParameterException e) {
			throw new CAPException("ParameterException when decoding CallingPartyNumber: " + e.getLocalizedMessage(), e);
		}
		return res;
	}

	@Override
	public byte[] getCallingPartysCategory() {
		return callingPartysCategory;
	}

	@Override
	public CallingPartyCategory getCallingPartysCategoryIsup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CGEncountered getCGEncountered() {
		return CGEncountered;
	}

	@Override
	public IPSSPCapabilities getIPSSPCapabilities() {
		return IPSSPCapabilities;
	}

	@Override
	public byte[] getLocationNumber() {
		return locationNumber;
	}

	@Override
	public LocationNumber getLocationNumberIsup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getOriginalCalledPartyID() {
		return originalCalledPartyID;
	}

	@Override
	public OriginalCalledNumber getOriginalCalledPartyIDIsup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CAPExtensions getExtensions() {
		return extensions;
	}

	@Override
	public byte[] getHighLayerCompatibility() {
		return highLayerCompatibility;
	}

	@Override
	public Digits getAdditionalCallingPartyNumber() {
		return additionalCallingPartyNumber;
	}

	@Override
	public GenericNumber getAdditionalCallingPartyNumberIsup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BearerCapability getBearerCapability() {
		return bearerCapability;
	}

	@Override
	public EventTypeBCSM getEventTypeBCSM() {
		return eventTypeBCSM;
	}

	@Override
	public byte[] getRedirectingPartyID() {
		return redirectingPartyID;
	}

	@Override
	public RedirectingNumber getRedirectingPartyIDIsup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getRedirectionInformation() {
		return redirectionInformation;
	}

	@Override
	public RedirectionInformation getRedirectionInformationIsup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getCause() {
		return cause;
	}

	@Override
	public CauseIndicators getCauseIsup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
		return serviceInteractionIndicatorsTwo;
	}

	@Override
	public byte[] getCarrier() {
		return carrier;
	}

	@Override
	public CUGIndex getCugIndex() {
		return cugIndex;
	}

	@Override
	public CUGInterlock getCugInterlock() {
		return cugInterlock;
	}

	@Override
	public boolean getCugOutgoingAccess() {
		return cugOutgoingAccess;
	}

	@Override
	public IMSI getIMSI() {
		return imsi;
	}

	@Override
	public SubscriberState getSubscriberState() {
		return subscriberState;
	}

	@Override
	public LocationInformation getLocationInformation() {
		return locationInformation;
	}

	@Override
	public ExtBasicServiceCode getExtBasicServiceCode() {
		return extBasicServiceCode;
	}

	@Override
	public CallReferenceNumber getCallReferenceNumber() {
		return callReferenceNumber;
	}

	@Override
	public ISDNAddressString getMscAddress() {
		return mscAddress;
	}

	@Override
	public CalledPartyBCDNumber getCalledPartyBCDNumber() {
		return calledPartyBCDNumber;
	}

	@Override
	public TimeAndTimezone getTimeAndTimezone() {
		return timeAndTimezone;
	}

	@Override
	public boolean getCallForwardingSSPending() {
		return callForwardingSSPending;
	}

	@Override
	public InitialDPArgExtension getInitialDPArgExtension() {
		return initialDPArgExtension;
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
			throw new CAPParsingComponentException("IOException when decoding InitialDPRequest: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding InitialDPRequest: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (MAPParsingComponentException e) {
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding InitialDPRequest: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding InitialDPRequest: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding InitialDPRequest: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (MAPParsingComponentException e) {
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding InitialDPRequest: " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException, IOException, AsnException {

		this.serviceKey = 0;
		this.calledPartyNumber = null;
		this.callingPartyNumber = null;
		this.callingPartysCategory = null;
		this.CGEncountered = null;
		this.IPSSPCapabilities = null;
		this.locationNumber = null;
		this.originalCalledPartyID = null;
		this.extensions = null;
		this.highLayerCompatibility = null;
		this.additionalCallingPartyNumber = null;
		this.bearerCapability = null;
		this.eventTypeBCSM = null;
		this.redirectingPartyID = null;
		this.redirectionInformation = null;
		this.cause = null;
		this.serviceInteractionIndicatorsTwo = null;
		this.carrier = null;
		this.cugIndex = null;
		this.cugInterlock = null;
		this.cugOutgoingAccess = false;
		this.imsi = null;
		this.subscriberState = null;
		this.locationInformation = null;
		this.extBasicServiceCode = null;
		this.callReferenceNumber = null;
		this.mscAddress = null;
		this.calledPartyBCDNumber = null;
		this.timeAndTimezone = null;
		this.callForwardingSSPending = false;
		this.initialDPArgExtension = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();
			int i1;

			switch (num) {
			case 0:
				// serviceKey
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || tag != _ID_serviceKey || !ais.isTagPrimitive())
					throw new CAPParsingComponentException("Error while decoding InitialDPRequest: Parameter 0 bad tag or tag class or not primitive",
							CAPParsingComponentExceptionReason.MistypedParameter);
				this.serviceKey = (int) ais.readInteger();
				break;

			default:
				if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					switch (tag) {
					case _ID_calledPartyNumber:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_callingPartyNumber:
						this.callingPartyNumber = ais.readOctetString();
						if (this.callingPartyNumber.length < 2 || this.callingPartyNumber.length > 16)
							throw new CAPParsingComponentException(
									"Error while decoding InitialDPRequest: Parameter callingPartyNumber must have the length from 2 to 16, found"
											+ this.callingPartyNumber.length, CAPParsingComponentExceptionReason.MistypedParameter);
						
						
						// TODO: remove it ......................
						try {
							CallingPartyNumber x1 =  this.getCallingPartyNumberIsup();
							int zz=0;
							zz++;
						} catch (CAPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// TODO: remove it ......................
						
						
						break;
					case _ID_callingPartysCategory:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_cGEncountered:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_iPSSPCapabilities:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_locationNumber:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_originalCalledPartyID:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_extensions:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_highLayerCompatibility:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_additionalCallingPartyNumber:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_bearerCapability:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_eventTypeBCSM:
						i1 = (int) ais.readInteger();
						this.eventTypeBCSM = EventTypeBCSM.getInstance(i1);
						break;
					case _ID_redirectingPartyID:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_redirectionInformation:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_cause:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_serviceInteractionIndicatorsTwo:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_carrier:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_cug_Index:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_cug_Interlock:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_cug_OutgoingAccess:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_iMSI:
						this.imsi = new IMSIImpl();
						((IMSIImpl)this.imsi).decodeAll(ais);
						break;
					case _ID_subscriberState:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_locationInformation:
						this.locationInformation = new LocationInformationImpl();
						((LocationInformationImpl)this.locationInformation).decodeAll(ais);
						break;
					case _ID_ext_basicServiceCode:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_callReferenceNumber:
						this.callReferenceNumber = new CallReferenceNumberImpl();
						((CallReferenceNumberImpl)this.callReferenceNumber).decodeAll(ais);
						break;
					case _ID_mscAddress:
						this.mscAddress = new ISDNAddressStringImpl();
						((ISDNAddressStringImpl)this.mscAddress).decodeAll(ais);
						break;
					case _ID_calledPartyBCDNumber:
						this.calledPartyBCDNumber = new CalledPartyBCDNumberImpl();
						((CalledPartyBCDNumberImpl)this.calledPartyBCDNumber).decodeAll(ais);
						break;
					case _ID_timeAndTimezone:
						this.timeAndTimezone = new TimeAndTimezoneImpl();
						((TimeAndTimezoneImpl)this.timeAndTimezone).decodeAll(ais);
						break;
					case _ID_callForwardingSS_Pending:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_initialDPArgExtension:
						ais.advanceElement(); // TODO: implement it
						break;
						
					default:
						ais.advanceElement();
						break;
					}

					// if (tag == Tag.NULL && ais.getTagClass() ==
					// Tag.CLASS_UNIVERSAL) {
					// if (!ais.isTagPrimitive())
					// throw new MAPParsingComponentException(
					// "Error while decoding forwardShortMessageRequest: Parameter moreMessagesToSend is not primitive",
					// MAPParsingComponentExceptionReason.MistypedParameter);
					// ais.readNull();
					// this.moreMessagesToSend = true;

				} else {
					ais.advanceElement();
				}
				break;
			}

			num++;
		}

		if (num < 1)
			throw new CAPParsingComponentException("Error while decoding InitialDPRequest: Needs at least 1 mandatory parameters, found " + num,
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
