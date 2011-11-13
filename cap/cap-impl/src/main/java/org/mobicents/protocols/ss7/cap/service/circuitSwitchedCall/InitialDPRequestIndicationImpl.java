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
import org.mobicents.protocols.ss7.cap.api.isup.AdditionalCallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CallingPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.OriginalCalledNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.RedirectingPartyIDCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.inap.isup.RedirectionInformationInapImpl;
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
import org.mobicents.protocols.ss7.map.service.subscriberManagement.ExtBasicServiceCodeImpl;

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
	
	public static final String _PrimitiveName = "InitialDPRequestIndication";

	private int serviceKey;
	private CalledPartyNumberCap calledPartyNumber;
	private CallingPartyNumberCap callingPartyNumber;
	private CallingPartysCategoryInap callingPartysCategory;
	private CGEncountered CGEncountered;
	private IPSSPCapabilities IPSSPCapabilities;
	private LocationNumberCap locationNumber;
	private OriginalCalledNumberCap originalCalledPartyID;
	private CAPExtensions extensions;
	private HighLayerCompatibilityInap highLayerCompatibility;
	private AdditionalCallingPartyNumberCap additionalCallingPartyNumber;
	private BearerCapability bearerCapability;
	private EventTypeBCSM eventTypeBCSM;
	private RedirectingPartyIDCap redirectingPartyID;
	private RedirectionInformationInap redirectionInformation;
	private CauseCap cause;
	private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
	private Carrier carrier;
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
	public CalledPartyNumberCap getCalledPartyNumber() {
		return this.calledPartyNumber;
	}

	@Override
	public CallingPartyNumberCap getCallingPartyNumber() {
		return callingPartyNumber;
	}

	@Override
	public CallingPartysCategoryInap getCallingPartysCategory() {
		return callingPartysCategory;
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
	public LocationNumberCap getLocationNumber() {
		return locationNumber;
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
	public HighLayerCompatibilityInap getHighLayerCompatibility() {
		return highLayerCompatibility;
	}

	@Override
	public AdditionalCallingPartyNumberCap getAdditionalCallingPartyNumber() {
		return additionalCallingPartyNumber;
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
	public RedirectingPartyIDCap getRedirectingPartyID() {
		return redirectingPartyID;
	}

	@Override
	public RedirectionInformationInap getRedirectionInformation() {
		return redirectionInformation;
	}

	@Override
	public CauseCap getCause() {
		return cause;
	}

	@Override
	public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
		return serviceInteractionIndicatorsTwo;
	}

	@Override
	public Carrier getCarrier() {
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
						byte[] buf = ais.readOctetString();
						if (buf.length < 2 || buf.length > 18)
							throw new CAPParsingComponentException(
									"Error while decoding " + _PrimitiveName + ": calledPartyNumber must be from 2 to 18 bytes length, found: " + buf.length,
									CAPParsingComponentExceptionReason.MistypedParameter);
						this.calledPartyNumber = new CalledPartyNumberCapImpl(buf);
						break;
					case _ID_callingPartyNumber:
						buf = ais.readOctetString();
						if (buf.length < 2 || buf.length > 10)
							throw new CAPParsingComponentException(
									"Error while decoding InitialDPRequest: callingPartyNumber must be from 2 to 10 bytes length, found: " + buf.length,
									CAPParsingComponentExceptionReason.MistypedParameter);
						this.callingPartyNumber = new CallingPartyNumberCapImpl(buf);
						break;
					case _ID_callingPartysCategory:
						buf = ais.readOctetString();
						if (buf.length < 1 || buf.length > 1)
							throw new CAPParsingComponentException(
									"Error while decoding InitialDPRequest: callingPartysCategory must be from 1 to 1 bytes length, found: " + buf.length,
									CAPParsingComponentExceptionReason.MistypedParameter);
						this.callingPartysCategory = new CallingPartysCategoryInapImpl(buf);
						break;
					case _ID_cGEncountered:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_iPSSPCapabilities:
						ais.advanceElement(); // TODO: implement it
						break;
					case _ID_locationNumber:
						buf = ais.readOctetString();
						if (buf.length < 2 || buf.length > 10)
							throw new CAPParsingComponentException(
									"Error while decoding InitialDPRequest: locationNumber must be from 2 to 10 bytes length, found: " + buf.length,
									CAPParsingComponentExceptionReason.MistypedParameter);
						this.locationNumber = new LocationNumberCapImpl(buf);
						break;
					case _ID_originalCalledPartyID:
						buf = ais.readOctetString();
						if (buf.length < 2 || buf.length > 10)
							throw new CAPParsingComponentException(
									"Error while decoding InitialDPRequest: originalCalledPartyID must be from 2 to 10 bytes length, found: " + buf.length,
									CAPParsingComponentExceptionReason.MistypedParameter);
						this.originalCalledPartyID = new OriginalCalledNumberCapImpl(buf);
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
						AsnInputStream ais2 = ais.readSequenceStream();
						ais2.readTag();
						this.bearerCapability = new BearerCapabilityImpl();
						((BearerCapabilityImpl)this.bearerCapability).decodeAll(ais2);
						break;
					case _ID_eventTypeBCSM:
						i1 = (int) ais.readInteger();
						this.eventTypeBCSM = EventTypeBCSM.getInstance(i1);
						break;
					case _ID_redirectingPartyID:
						buf = ais.readOctetString();
						if (buf.length < 2 || buf.length > 10)
							throw new CAPParsingComponentException(
									"Error while decoding InitialDPRequest: redirectingPartyID must be from 2 to 10 bytes length, found: " + buf.length,
									CAPParsingComponentExceptionReason.MistypedParameter);
						this.redirectingPartyID = new RedirectingPartyIDCapImpl(buf);
						break;
					case _ID_redirectionInformation:
						buf = ais.readOctetString();
						if (buf.length < 2 || buf.length > 2)
							throw new CAPParsingComponentException(
									"Error while decoding InitialDPRequest: redirectionInformation must be from 2 to 2 bytes length, found: " + buf.length,
									CAPParsingComponentExceptionReason.MistypedParameter);
						this.redirectionInformation = new RedirectionInformationInapImpl(buf);
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
						ais2 = ais.readSequenceStream();
						ais2.readTag();
						this.extBasicServiceCode = new ExtBasicServiceCodeImpl();
						((ExtBasicServiceCodeImpl)this.extBasicServiceCode).decodeAll(ais2);
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
						this.initialDPArgExtension = new InitialDPArgExtensionImpl();
						((InitialDPArgExtensionImpl)this.initialDPArgExtension).decodeAll(ais);
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
