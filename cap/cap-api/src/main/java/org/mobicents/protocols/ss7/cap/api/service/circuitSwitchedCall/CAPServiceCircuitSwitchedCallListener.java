/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.CAPServiceListener;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPServiceCircuitSwitchedCallListener extends CAPServiceListener {

    void onInitialDPRequest(InitialDPRequest ind);

    void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind);

    void onApplyChargingRequest(ApplyChargingRequest ind);

    void onEventReportBCSMRequest(EventReportBCSMRequest ind);

    void onContinueRequest(ContinueRequest ind);

    void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind);

    void onApplyChargingReportRequest(ApplyChargingReportRequest ind);

    void onReleaseCallRequest(ReleaseCallRequest ind);

    void onConnectRequest(ConnectRequest ind);

    void onCallInformationRequestRequest(CallInformationRequestRequest ind);

    void onCallInformationReportRequest(CallInformationReportRequest ind);

    void onActivityTestRequest(ActivityTestRequest ind);

    void onActivityTestResponse(ActivityTestResponse ind);

    void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind);

    void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind);

    void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind);

    void onDisconnectLegRequest(DisconnectLegRequest ind);

    void onDisconnectLegResponse(DisconnectLegResponse ind);

    void onDisconnectForwardConnectionWithArgumentRequest(DisconnectForwardConnectionWithArgumentRequest ind);

    void onConnectToResourceRequest(ConnectToResourceRequest ind);

    void onResetTimerRequest(ResetTimerRequest ind);

    void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind);

    void onSendChargingInformationRequest(SendChargingInformationRequest ind);

    void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind);

    void onPlayAnnouncementRequest(PlayAnnouncementRequest ind);

    void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind);

    void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind);

    void onCancelRequest(CancelRequest ind);

    void onInitiateCallAttemptRequest(InitiateCallAttemptRequest initiateCallAttemptRequest);

    void onInitiateCallAttemptResponse(InitiateCallAttemptResponse initiateCallAttemptResponse);

    void onMoveLegRequest(MoveLegRequest ind);

    void onMoveLegResponse(MoveLegResponse ind);

}