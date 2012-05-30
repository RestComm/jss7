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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.AChChargingAddress;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancel;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndex;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface CAPDialogCircuitSwitchedCall extends CAPDialog {

	public Long addInitialDPRequest(int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber,
			CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities,
			LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
			HighLayerCompatibilityInap highLayerCompatibility, Digits additionalCallingPartyNumber, BearerCapability bearerCapability,
			EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, CauseCap cause,
			ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock cugInterlock,
			boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
			ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
			CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
			InitialDPArgExtension initialDPArgExtension) throws CAPException;

	public Long addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber,
			CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities,
			LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
			HighLayerCompatibilityInap highLayerCompatibility, Digits additionalCallingPartyNumber, BearerCapability bearerCapability,
			EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, CauseCap cause,
			ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock cugInterlock,
			boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
			ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
			CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
			InitialDPArgExtension initialDPArgExtension) throws CAPException;

	public Long addApplyChargingReportRequest(TimeDurationChargingResult timeDurationChargingResult) throws CAPException;

	public Long addApplyChargingReportRequest(int customInvokeTimeout, TimeDurationChargingResult timeDurationChargingResult) throws CAPException;

	public Long addApplyChargingRequest(CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics, SendingSideID partyToCharge,
			CAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException;

	public Long addApplyChargingRequest(int customInvokeTimeout, CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics,
			SendingSideID partyToCharge, CAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException;

	public Long addCallInformationReportRequest(ArrayList<RequestedInformation> requestedInformationList, CAPExtensions extensions, ReceivingSideID legID)
			throws CAPException;

	public Long addCallInformationReportRequest(int customInvokeTimeout, ArrayList<RequestedInformation> requestedInformationList, CAPExtensions extensions,
			ReceivingSideID legID) throws CAPException;

	public Long addCallInformationRequestRequest(ArrayList<RequestedInformationType> requestedInformationTypeList, CAPExtensions extensions, SendingSideID legID)
			throws CAPException;

	public Long addCallInformationRequestRequest(int customInvokeTimeout, ArrayList<RequestedInformationType> requestedInformationTypeList,
			CAPExtensions extensions, SendingSideID legID) throws CAPException;

	public Long addConnectRequest(DestinationRoutingAddress destinationRoutingAddress, AlertingPatternCap alertingPattern,
			OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions, Carrier carrier, CallingPartysCategoryInap callingPartysCategory,
			RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, ArrayList<GenericNumberCap> genericNumbers,
			ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber, LegID legToBeConnected, CUGInterlock cugInterlock,
			boolean cugOutgoingAccess, boolean suppressionOfAnnouncement, boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested)
			throws CAPException;

	public Long addConnectRequest(int customInvokeTimeout, DestinationRoutingAddress destinationRoutingAddress, AlertingPatternCap alertingPattern,
			OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions, Carrier carrier, CallingPartysCategoryInap callingPartysCategory,
			RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, ArrayList<GenericNumberCap> genericNumbers,
			ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber, LegID legToBeConnected, CUGInterlock cugInterlock,
			boolean cugOutgoingAccess, boolean suppressionOfAnnouncement, boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested)
			throws CAPException;

	public Long addContinueRequest() throws CAPException;

	public Long addContinueRequest(int customInvokeTimeout) throws CAPException;

	public Long addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM, EventSpecificInformationBCSM eventSpecificInformationBCSM, ReceivingSideID legID,
			MiscCallInfo miscCallInfo, CAPExtensions extensions) throws CAPException;

	public Long addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM, EventSpecificInformationBCSM eventSpecificInformationBCSM,
			ReceivingSideID legID, MiscCallInfo miscCallInfo, CAPExtensions extensions) throws CAPException;

	public Long addRequestReportBCSMEventRequest(ArrayList<BCSMEvent> bcsmEventList, CAPExtensions extensions) throws CAPException;

	public Long addRequestReportBCSMEventRequest(int customInvokeTimeout, ArrayList<BCSMEvent> bcsmEventList, CAPExtensions extensions) throws CAPException;

	public Long addReleaseCallRequest(CauseCap cause) throws CAPException;

	public Long addReleaseCallRequest(int customInvokeTimeout, CauseCap cause) throws CAPException;

	public Long addActivityTestRequest() throws CAPException;

	public Long addActivityTestRequest(int customInvokeTimeout) throws CAPException;

	public void addActivityTestResponse(long invokeId) throws CAPException;

	public Long addAssistRequestInstructionsRequest(Digits correlationID, IPSSPCapabilities ipSSPCapabilities, CAPExtensions extensions) throws CAPException;

	public Long addAssistRequestInstructionsRequest(int customInvokeTimeout, Digits correlationID, IPSSPCapabilities ipSSPCapabilities, CAPExtensions extensions) throws CAPException;

	public Long addEstablishTemporaryConnectionRequest(Digits assistingSSPIPRoutingAddress, Digits correlationID, ScfID scfID, CAPExtensions extensions,
			Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID, NAOliInfo naOliInfo,
			LocationNumberCap chargeNumber, OriginalCalledNumberCap originalCalledPartyID, CallingPartyNumberCap callingPartyNumber) throws CAPException;

	public Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, Digits assistingSSPIPRoutingAddress, Digits correlationID, ScfID scfID,
			CAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID,
			NAOliInfo naOliInfo, LocationNumberCap chargeNumber, OriginalCalledNumberCap originalCalledPartyID, CallingPartyNumberCap callingPartyNumber)
			throws CAPException;

	public Long addDisconnectForwardConnectionRequest() throws CAPException;

	public Long addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws CAPException;

	public Long addConnectToResourceRequest(CalledPartyNumberCap resourceAddress_IPRoutingAddress, boolean resourceAddress_Null, CAPExtensions extensions,
			ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException;

	public Long addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberCap resourceAddress_IPRoutingAddress, boolean resourceAddress_Null,
			CAPExtensions extensions, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException;

	public Long addResetTimerRequest(TimerID timerID, int timerValue, CAPExtensions extensions, Integer callSegmentID) throws CAPException;

	public Long addResetTimerRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPExtensions extensions, Integer callSegmentID)
			throws CAPException;

	public Long addFurnishChargingInformationRequest(FCIBCCCAMELsequence1 FCIBCCCAMELsequence1) throws CAPException;

	public Long addFurnishChargingInformationRequest(int customInvokeTimeout, FCIBCCCAMELsequence1 FCIBCCCAMELsequence1) throws CAPException;

	public Long addSendChargingInformationRequest(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, SendingSideID partyToCharge,
			CAPExtensions extensions) throws CAPException;

	public Long addSendChargingInformationRequest(int customInvokeTimeout, SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
			SendingSideID partyToCharge, CAPExtensions extensions) throws CAPException;

	public Long addSpecializedResourceReportRequest_CapV23() throws CAPException;

	public Long addSpecializedResourceReportRequest_CapV4(boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted) throws CAPException;

	public Long addSpecializedResourceReportRequest_CapV23(int customInvokeTimeout) throws CAPException;

	public Long addSpecializedResourceReportRequest_CapV4(int customInvokeTimeout, boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted) throws CAPException;

	public Long addPlayAnnouncementRequest(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
			Boolean requestAnnouncementCompleteNotification, CAPExtensions extensions, Integer callSegmentID, Boolean requestAnnouncementStartedNotification)
			throws CAPException;

	public Long addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
			Boolean requestAnnouncementCompleteNotification, CAPExtensions extensions, Integer callSegmentID, Boolean requestAnnouncementStartedNotification)
			throws CAPException;

	public Long addPromptAndCollectUserInformationRequest(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden, InformationToSend informationToSend,
			CAPExtensions extensions, Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException;

	public Long addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
			InformationToSend informationToSend, CAPExtensions extensions, Integer callSegmentID, Boolean requestAnnouncementStartedNotification)
			throws CAPException;

	public void addPromptAndCollectUserInformationResponse_DigitsResponse(long invokeId, Digits digitsResponse) throws CAPException;

	public Long addCancelRequest_InvokeId(Integer invokeID) throws CAPException;

	public Long addCancelRequest_AllRequests() throws CAPException;

	public Long addCancelRequest_CallSegmentToCancel(CallSegmentToCancel callSegmentToCancel) throws CAPException;

	public Long addCancelRequest_InvokeId(int customInvokeTimeout, Integer invokeID) throws CAPException;

	public Long addCancelRequest_AllRequests(int customInvokeTimeout) throws CAPException;

	public Long addCancelRequest_CallSegmentToCancel(int customInvokeTimeout, CallSegmentToCancel callSegmentToCancel) throws CAPException;

}

