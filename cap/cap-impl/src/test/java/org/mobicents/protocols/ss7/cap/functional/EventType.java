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

package org.mobicents.protocols.ss7.cap.functional;

import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequest;


/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public enum EventType {
    // Dialog EventType
    DialogDelimiter, DialogRequest, DialogAccept, DialogUserAbort, DialogProviderAbort, DialogClose, DialogNotice, DialogRelease, DialogTimeout, // DialogReject,

    // Service EventType
    ErrorComponent, ProviderErrorComponent, RejectComponent, InvokeTimeout,

    // CircuitSwitchedCall EventType
    InitialDpRequest, ApplyChargingReportRequest, ApplyChargingRequest, CallInformationReportRequest, CallInformationRequestRequest, ConnectRequest, ContinueRequest, ContinueWithArgumentRequest, EventReportBCSMRequest, RequestReportBCSMEventRequest, ReleaseCallRequest, ActivityTestRequest, ActivityTestResponse, AssistRequestInstructionsRequest, EstablishTemporaryConnectionRequest, DisconnectForwardConnectionRequest, ConnectToResourceRequest, ResetTimerRequest, FurnishChargingInformationRequest, SendChargingInformationRequest, SpecializedResourceReportRequest, PlayAnnouncementRequest, PromptAndCollectUserInformationRequest, PromptAndCollectUserInformationResponse, CancelRequest, DisconnectLegRequest, DisconnectLegResponse, DisconnectForwardConnectionWithArgumentRequest, InitiateCallAttemptRequest, InitiateCallAttemptResponse, MoveLegRequest, MoveLegResponse,

    // gprs EventType
    InitialDpGprsRequest, RequestReportGPRSEventRequest, ApplyChargingGPRSRequest, EntityReleasedGPRSRequest, ConnectGPRSRequest, ContinueGPRSRequest, ReleaseGPRSRequest, ResetTimerGPRSRequest, FurnishChargingInformationGPRSRequest, CancelGPRSRequest, SendChargingInformationGPRSRequest, applyChargingReportGPRS, EventReportGPRSRequest, EventReportGPRSResponse, ApplyChargingReportGPRSResponse, ApplyChargingReportGPRSRequest, EntityReleasedGPRSResponse, ActivityTestGPRSRequest, ActivityTestGPRSResponse,

    // sms event Type
    ConnectSMSRequest, EventReportSMSRequest, FurnishChargingInformationSMSRequest, InitialDPSMSRequest, ReleaseSMSRequest, RequestReportSMSEventRequest, ResetTimerSMSRequest, ContinueSMSRequest
}
