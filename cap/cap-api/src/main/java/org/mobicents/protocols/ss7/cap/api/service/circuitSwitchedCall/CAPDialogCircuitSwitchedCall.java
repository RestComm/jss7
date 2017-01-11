/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
import org.mobicents.protocols.ss7.cap.api.gap.GapCriteria;
import org.mobicents.protocols.ss7.cap.api.gap.GapIndicators;
import org.mobicents.protocols.ss7.cap.api.gap.GapTreatment;
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ControlType;
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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 *
 */
public interface CAPDialogCircuitSwitchedCall extends CAPDialog {

    Long addInitialDPRequest(int serviceKey, CalledPartyNumberCap calledPartyNumber,
            CallingPartyNumberCap callingPartyNumber, CallingPartysCategoryInap callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberCap locationNumber,
            OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
            HighLayerCompatibilityInap highLayerCompatibility, Digits additionalCallingPartyNumber,
            BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, CauseCap cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress, CalledPartyBCDNumber calledPartyBCDNumber,
            TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtension initialDPArgExtension)
            throws CAPException;

    Long addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberCap calledPartyNumber,
            CallingPartyNumberCap callingPartyNumber, CallingPartysCategoryInap callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberCap locationNumber,
            OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
            HighLayerCompatibilityInap highLayerCompatibility, Digits additionalCallingPartyNumber,
            BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, CauseCap cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress, CalledPartyBCDNumber calledPartyBCDNumber,
            TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtension initialDPArgExtension)
            throws CAPException;

    Long addApplyChargingReportRequest(TimeDurationChargingResult timeDurationChargingResult) throws CAPException;

    Long addApplyChargingReportRequest(int customInvokeTimeout, TimeDurationChargingResult timeDurationChargingResult)
            throws CAPException;

    Long addApplyChargingRequest(CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics,
            SendingSideID partyToCharge, CAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException;

    Long addApplyChargingRequest(int customInvokeTimeout,
            CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics, SendingSideID partyToCharge,
            CAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException;

    Long addCallInformationReportRequest(ArrayList<RequestedInformation> requestedInformationList,
            CAPExtensions extensions, ReceivingSideID legID) throws CAPException;

    Long addCallInformationReportRequest(int customInvokeTimeout,
            ArrayList<RequestedInformation> requestedInformationList, CAPExtensions extensions, ReceivingSideID legID)
            throws CAPException;

    Long addCallInformationRequestRequest(ArrayList<RequestedInformationType> requestedInformationTypeList,
            CAPExtensions extensions, SendingSideID legID) throws CAPException;

    Long addCallInformationRequestRequest(int customInvokeTimeout,
            ArrayList<RequestedInformationType> requestedInformationTypeList, CAPExtensions extensions, SendingSideID legID)
            throws CAPException;

    Long addConnectRequest(DestinationRoutingAddress destinationRoutingAddress, AlertingPatternCap alertingPattern,
            OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions, Carrier carrier,
            CallingPartysCategoryInap callingPartysCategory, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, ArrayList<GenericNumberCap> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException;

    Long addConnectRequest(int customInvokeTimeout, DestinationRoutingAddress destinationRoutingAddress,
            AlertingPatternCap alertingPattern, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
            Carrier carrier, CallingPartysCategoryInap callingPartysCategory, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, ArrayList<GenericNumberCap> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException;

    Long addContinueRequest() throws CAPException;

    Long addContinueRequest(int customInvokeTimeout) throws CAPException;

    Long addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, ReceivingSideID legID, MiscCallInfo miscCallInfo,
            CAPExtensions extensions) throws CAPException;

    Long addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, ReceivingSideID legID, MiscCallInfo miscCallInfo,
            CAPExtensions extensions) throws CAPException;

    Long addRequestReportBCSMEventRequest(ArrayList<BCSMEvent> bcsmEventList, CAPExtensions extensions)
            throws CAPException;

    Long addRequestReportBCSMEventRequest(int customInvokeTimeout, ArrayList<BCSMEvent> bcsmEventList,
            CAPExtensions extensions) throws CAPException;

    Long addReleaseCallRequest(CauseCap cause) throws CAPException;

    Long addReleaseCallRequest(int customInvokeTimeout, CauseCap cause) throws CAPException;

    Long addActivityTestRequest() throws CAPException;

    Long addActivityTestRequest(int customInvokeTimeout) throws CAPException;

    void addActivityTestResponse(long invokeId) throws CAPException;

    Long addAssistRequestInstructionsRequest(Digits correlationID, IPSSPCapabilities ipSSPCapabilities,
            CAPExtensions extensions) throws CAPException;

    Long addAssistRequestInstructionsRequest(int customInvokeTimeout, Digits correlationID,
            IPSSPCapabilities ipSSPCapabilities, CAPExtensions extensions) throws CAPException;

    Long addEstablishTemporaryConnectionRequest(Digits assistingSSPIPRoutingAddress, Digits correlationID, ScfID scfID,
            CAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            Integer callSegmentID, NAOliInfo naOliInfo, LocationNumberCap chargeNumber,
            OriginalCalledNumberCap originalCalledPartyID, CallingPartyNumberCap callingPartyNumber) throws CAPException;

    Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, Digits assistingSSPIPRoutingAddress,
            Digits correlationID, ScfID scfID, CAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID, NAOliInfo naOliInfo,
            LocationNumberCap chargeNumber, OriginalCalledNumberCap originalCalledPartyID,
            CallingPartyNumberCap callingPartyNumber) throws CAPException;

    Long addDisconnectForwardConnectionRequest() throws CAPException;

    Long addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws CAPException;

    Long addDisconnectForwardConnectionWithArgumentRequest(
            Integer callSegmentID, CAPExtensions extensions)
            throws CAPException;

    Long addDisconnectForwardConnectionWithArgumentRequest(
            int customInvokeTimeout, Integer callSegmentID,
            CAPExtensions extensions) throws CAPException;

    Long addDisconnectLegRequest(LegID logToBeReleased, CauseCap releaseCause,
            CAPExtensions extensions) throws CAPException;

    Long addDisconnectLegRequest(int customInvokeTimeout,
            LegID logToBeReleased, CauseCap releaseCause,
            CAPExtensions extensions) throws CAPException;

    void addDisconnectLegResponse(long invokeId) throws CAPException;

    Long addConnectToResourceRequest(CalledPartyNumberCap resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException;

    Long addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberCap resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException;

    Long addResetTimerRequest(TimerID timerID, int timerValue, CAPExtensions extensions, Integer callSegmentID)
            throws CAPException;

    Long addResetTimerRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPExtensions extensions,
            Integer callSegmentID) throws CAPException;

    Long addFurnishChargingInformationRequest(FCIBCCCAMELsequence1 FCIBCCCAMELsequence1) throws CAPException;

    Long addFurnishChargingInformationRequest(int customInvokeTimeout, FCIBCCCAMELsequence1 FCIBCCCAMELsequence1)
            throws CAPException;

    Long addSendChargingInformationRequest(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
            SendingSideID partyToCharge, CAPExtensions extensions) throws CAPException;

    Long addSendChargingInformationRequest(int customInvokeTimeout,
            SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, SendingSideID partyToCharge,
            CAPExtensions extensions) throws CAPException;

    Long addSpecializedResourceReportRequest_CapV23(Long linkedId) throws CAPException;

    Long addSpecializedResourceReportRequest_CapV4(Long linkedId, boolean isAllAnnouncementsComplete,
            boolean isFirstAnnouncementStarted) throws CAPException;

    Long addSpecializedResourceReportRequest_CapV23(Long linkedId, int customInvokeTimeout) throws CAPException;

    Long addSpecializedResourceReportRequest_CapV4(Long linkedId, int customInvokeTimeout,
            boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted) throws CAPException;

    Long addPlayAnnouncementRequest(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException;

    Long addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSend informationToSend,
            Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, CAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException;

    Long addPromptAndCollectUserInformationRequest(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException;

    Long addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfo collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException;

    void addPromptAndCollectUserInformationResponse_DigitsResponse(long invokeId, Digits digitsResponse)
            throws CAPException;

    Long addCancelRequest_InvokeId(Integer invokeID) throws CAPException;

    Long addCancelRequest_AllRequests() throws CAPException;

    Long addCancelRequest_CallSegmentToCancel(CallSegmentToCancel callSegmentToCancel) throws CAPException;

    Long addCancelRequest_InvokeId(int customInvokeTimeout, Integer invokeID) throws CAPException;

    Long addCancelRequest_AllRequests(int customInvokeTimeout) throws CAPException;

    Long addCancelRequest_CallSegmentToCancel(int customInvokeTimeout, CallSegmentToCancel callSegmentToCancel)
            throws CAPException;

    Long addInitiateCallAttemptRequest(
            DestinationRoutingAddress destinationRoutingAddress,
            CAPExtensions extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberCap callingPartyNumber,
            CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi)
            throws CAPException;

    Long addInitiateCallAttemptRequest(int customInvokeTimeout,
            DestinationRoutingAddress destinationRoutingAddress,
            CAPExtensions extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberCap callingPartyNumber,
            CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi)
            throws CAPException;

    void addInitiateCallAttemptResponse(long invokeId,
            SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4Functionalities offeredCamel4Functionalities,
            CAPExtensions extensions, boolean releaseCallArgExtensionAllowed)
            throws CAPException;

    Long addContinueWithArgumentRequest(AlertingPatternCap alertingPattern,
            CAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInap callingPartysCategory,
            ArrayList<GenericNumberCap> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess,
            LocationNumberCap chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo,
            boolean borInterrogationRequested, boolean suppressOCsi,
            ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws CAPException;

    Long addContinueWithArgumentRequest(int customInvokeTimeout,
            AlertingPatternCap alertingPattern, CAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInap callingPartysCategory,
            ArrayList<GenericNumberCap> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess,
            LocationNumberCap chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo,
            boolean borInterrogationRequested, boolean suppressOCsi,
            ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws CAPException;

    Long addMoveLegRequest(LegID logIDToMove, CAPExtensions extensions)
            throws CAPException;

    Long addMoveLegRequest(int customInvokeTimeout, LegID logIDToMove,
            CAPExtensions extensions) throws CAPException;

    void addMoveLegResponse(long invokeId) throws CAPException;

    Long addCollectInformationRequest() throws CAPException;

    Long addCollectInformationRequest(int customInvokeTimeout) throws CAPException;

    Long addSplitLegRequest(LegID legIDToSplit, Integer newCallSegmentId, CAPExtensions extensions) throws CAPException;

    Long addSplitLegRequest(int customInvokeTimeout, LegID legIDToSplit, Integer newCallSegmentId,
            CAPExtensions extensions) throws CAPException;

    void addSplitLegResponse(long invokeId) throws CAPException;

    Long addCallGapRequest(GapCriteria gapCriteria, GapIndicators gapIndicators,
                           ControlType controlType, GapTreatment gapTreatment,
                           CAPExtensions capExtension) throws CAPException;

    Long addCallGapRequest(int customInvokeTimeout, GapCriteria gapCriteria,
                           GapIndicators gapIndicators, ControlType controlType,
                           GapTreatment gapTreatment, CAPExtensions capExtension) throws CAPException;

}