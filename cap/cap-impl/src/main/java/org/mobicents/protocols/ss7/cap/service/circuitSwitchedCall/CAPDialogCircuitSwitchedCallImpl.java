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

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPServiceBase;
import org.mobicents.protocols.ss7.cap.api.isup.AdditionalCallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.CUGIndex;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CAPDialogCircuitSwitchedCallImpl extends CAPDialogImpl implements CAPDialogCircuitSwitchedCall {

	protected CAPDialogCircuitSwitchedCallImpl(CAPApplicationContext appCntx, Dialog tcapDialog, CAPProviderImpl capProviderImpl, CAPServiceBase capService) {
		super(appCntx, tcapDialog, capProviderImpl, capService);
	}

	@Override
	public Long addInitialDPRequest(int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber,
			CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities,
			LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
			HighLayerCompatibilityInap highLayerCompatibility, AdditionalCallingPartyNumberCap additionalCallingPartyNumber, BearerCapability bearerCapability,
			EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, CauseCap cause,
			ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock cugInterlock,
			boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
			ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
			CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
			InitialDPArgExtension initialDPArgExtension) throws CAPException {

		return addInitialDPRequest(_Timer_Default, serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory, CGEncountered, IPSSPCapabilities,
				locationNumber, originalCalledPartyID, extensions, highLayerCompatibility, additionalCallingPartyNumber, bearerCapability, eventTypeBCSM,
				redirectingPartyID, redirectionInformation, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock, cugOutgoingAccess, imsi,
				subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber, mscAddress, calledPartyBCDNumber, timeAndTimezone,
				callForwardingSSPending, initialDPArgExtension);
	}

	@Override
	public Long addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberCap calledPartyNumber,
			CallingPartyNumberCap callingPartyNumber, CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered,
			IPSSPCapabilities IPSSPCapabilities, LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
			HighLayerCompatibilityInap highLayerCompatibility, AdditionalCallingPartyNumberCap additionalCallingPartyNumber, BearerCapability bearerCapability,
			EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, CauseCap cause,
			ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock cugInterlock,
			boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
			ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
			CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
			InitialDPArgExtension initialDPArgExtension) throws CAPException {

		if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
				&& this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric)
			throw new CAPException(
					"Bad application context name for addInitialDPRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");
	
		// TODO: implement it - making parameter false and null for low application contexts ???
//		if (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
//			moreMessagesToSend = false;

		Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
		if (customInvokeTimeout == _Timer_Default)
			invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
		else
			invoke.setTimeout(customInvokeTimeout);

		OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long)CAPOperationCode.initialDP);
		invoke.setOperationCode(oc);

		InitialDPRequestIndicationImpl req = new InitialDPRequestIndicationImpl(serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory,
				CGEncountered, IPSSPCapabilities, locationNumber, originalCalledPartyID, extensions, highLayerCompatibility, additionalCallingPartyNumber,
				bearerCapability, eventTypeBCSM, redirectingPartyID, redirectionInformation, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex,
				cugInterlock, cugOutgoingAccess, imsi, subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber, mscAddress,
				calledPartyBCDNumber, timeAndTimezone, callForwardingSSPending, initialDPArgExtension, this.appCntx.getVersion().getVersion() >= 3);
		AsnOutputStream aos = new AsnOutputStream();
		req.encodeData(aos);

		Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(req.getTagClass());
		p.setPrimitive(req.getIsPrimitive());
		p.setTag(req.getTag());
		p.setData(aos.toByteArray());
		invoke.setParameter(p);

		Long invokeId;
		try {
			invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);
		} catch (TCAPException e) {
			throw new CAPException(e.getMessage(), e);
		}

		this.sendInvokeComponent(invoke);
		
		return invokeId;
	}

}

