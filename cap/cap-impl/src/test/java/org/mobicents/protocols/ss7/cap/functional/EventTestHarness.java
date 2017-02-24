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

package org.mobicents.protocols.ss7.cap.functional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPMessage;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CollectInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegResponse;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSResponse;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ApplyChargingGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSResponse;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPServiceGprsListener;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CancelGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ConnectGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ContinueGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSResponse;
import org.mobicents.protocols.ss7.cap.api.service.gprs.EventReportGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.EventReportGPRSResponse;
import org.mobicents.protocols.ss7.cap.api.service.gprs.FurnishChargingInformationGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ReleaseGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ResetTimerGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.SendChargingInformationGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPServiceSmsListener;
import org.mobicents.protocols.ss7.cap.api.service.sms.ConnectSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.ContinueSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.EventReportSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.ReleaseSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.RequestReportSMSEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.ResetTimerSMSRequest;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 *
 * @author amit bhayani
 * @author servey vetyutnev
 *
 */
public class EventTestHarness implements CAPDialogListener, CAPServiceCircuitSwitchedCallListener, CAPServiceGprsListener,CAPServiceSmsListener {

    private Logger logger = null;

    protected List<TestEvent> observerdEvents = new ArrayList<TestEvent>();
    protected int sequence = 0;
    protected boolean invokeTimeoutSuppressed = false;

    EventTestHarness(Logger logger) {
        this.logger = logger;
    }

    public void suppressInvokeTimeout() {
        invokeTimeoutSuppressed = true;
    }

    public void compareEvents(List<TestEvent> expectedEvents) {

        if (expectedEvents.size() != this.observerdEvents.size()) {
            fail("Size of received events: " + this.observerdEvents.size() + ", does not equal expected events: "
                    + expectedEvents.size() + "\n" + doStringCompare(expectedEvents, observerdEvents));
        }

        for (int index = 0; index < expectedEvents.size(); index++) {
            assertEquals(expectedEvents.get(index), observerdEvents.get(index), "Received event does not match, index[" + index
                    + "]");
        }
    }

    protected String doStringCompare(List expectedEvents, List observerdEvents) {
        StringBuilder sb = new StringBuilder();
        int size1 = expectedEvents.size();
        int size2 = observerdEvents.size();
        int count = size1;
        if (count < size2) {
            count = size2;
        }

        for (int index = 0; count > index; index++) {
            String s1 = size1 > index ? expectedEvents.get(index).toString() : "NOP";
            String s2 = size2 > index ? observerdEvents.get(index).toString() : "NOP";
            sb.append(s1).append(" - ").append(s2).append("\n\n");
        }
        return sb.toString();
    }

    @Override
    public void onDialogDelimiter(CAPDialog capDialog) {
        this.logger.debug("onDialogDelimiter");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
        this.logger.debug("onDialogRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRequest, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
        this.logger.debug("onDialogAccept");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogAccept, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
        this.logger.debug("onDialogUserAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
        this.logger.debug("onDialogProviderAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogClose(CAPDialog capDialog) {
        this.logger.debug("onDialogClose");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogClose, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogRelease(CAPDialog capDialog) {
        this.logger.debug("onDialogRelease");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRelease, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogTimeout(CAPDialog capDialog) {
        this.logger.debug("onDialogTimeout");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        this.logger.debug("onDialogNotice");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogNotice, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onErrorComponent(CAPDialog capDialog, Long invokeId, CAPErrorMessage capErrorMessage) {
        this.logger.debug("onErrorComponent");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ErrorComponent, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRejectComponent(CAPDialog capDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        this.logger.debug("onRejectComponent");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RejectComponent, capDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInvokeTimeout(CAPDialog capDialog, Long invokeId) {
        this.logger.debug("onInvokeTimeout");
        if (!invokeTimeoutSuppressed) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, capDialog, sequence++);
            this.observerdEvents.add(te);
        }
    }

    @Override
    public void onCAPMessage(CAPMessage capMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitialDPRequest(InitialDPRequest ind) {
        this.logger.debug("onInitialDPRequestIndication");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
        this.logger.debug("onRequestReportBCSMEventRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RequestReportBCSMEventRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onApplyChargingRequest(ApplyChargingRequest ind) {
        this.logger.debug("ApplyChargingRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ApplyChargingRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
        this.logger.debug("EventReportBCSMRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EventReportBCSMRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onContinueRequest(ContinueRequest ind) {
        this.logger.debug("ContinueRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ContinueRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
        this.logger.debug("ApplyChargingReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ApplyChargingReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onReleaseCallRequest(ReleaseCallRequest ind) {
        this.logger.debug("ReleaseCallRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReleaseCallRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onConnectRequest(ConnectRequest ind) {
        this.logger.debug("ConnectRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ConnectRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCallInformationRequestRequest(CallInformationRequestRequest ind) {
        this.logger.debug("CallInformationRequestRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CallInformationRequestRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCallInformationReportRequest(CallInformationReportRequest ind) {
        this.logger.debug("CallInformationReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CallInformationReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivityTestRequest(ActivityTestRequest ind) {
        this.logger.debug("ActivityTestRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse ind) {
        this.logger.debug("ActivityTestResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivityTestResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
        this.logger.debug("AssistRequestInstructionsRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AssistRequestInstructionsRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
        this.logger.debug("EstablishTemporaryConnectionRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EstablishTemporaryConnectionRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
        this.logger.debug("DisconnectForwardConnectionRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DisconnectForwardConnectionRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
        this.logger.debug("ConnectToResourceRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ConnectToResourceRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onResetTimerRequest(ResetTimerRequest ind) {
        this.logger.debug("ResetTimerRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ResetTimerRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
        this.logger.debug("FurnishChargingInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
        this.logger.debug("SendChargingInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendChargingInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
        this.logger.debug("SpecializedResourceReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
        this.logger.debug("PlayAnnouncementRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PlayAnnouncementRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
        this.logger.debug("PromptAndCollectUserInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
        this.logger.debug("PromptAndCollectUserInformationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCancelRequest(CancelRequest ind) {
        this.logger.debug("CancelRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CancelRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind) {
        this.logger.debug("ContinueWithArgumentRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ContinueWithArgumentRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDisconnectLegRequest(DisconnectLegRequest ind) {
        this.logger.debug("DisconnectLegRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DisconnectLegRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDisconnectLegResponse(DisconnectLegResponse ind) {
        this.logger.debug("DisconnectLegResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DisconnectLegResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDisconnectForwardConnectionWithArgumentRequest(DisconnectForwardConnectionWithArgumentRequest ind) {
        this.logger.debug("DisconnectForwardConnectionWithArgumentRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DisconnectForwardConnectionWithArgumentRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest ind) {
        this.logger.debug("InitiateCallAttemptRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InitiateCallAttemptRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInitiateCallAttemptResponse(InitiateCallAttemptResponse ind) {
        this.logger.debug("InitiateCallAttemptResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InitiateCallAttemptResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onMoveLegRequest(MoveLegRequest ind) {
        this.logger.debug("MoveLegRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.MoveLegRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onMoveLegResponse(MoveLegResponse ind) {
        this.logger.debug("MoveLegResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.MoveLegResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSplitLegRequest(SplitLegRequest ind) {
        this.logger.debug("SplitLegRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SplitLegRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSplitLegResponse(SplitLegResponse ind) {
        this.logger.debug("SplitLegResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SplitLegResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInitialDpGprsRequest(InitialDpGprsRequest ind) {
        this.logger.debug("InitialDpGprsRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InitialDpGprsRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRequestReportGPRSEventRequest(RequestReportGPRSEventRequest ind) {
        this.logger.debug("RequestReportGPRSEventRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RequestReportGPRSEventRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onApplyChargingGPRSRequest(ApplyChargingGPRSRequest ind) {
        this.logger.debug("ApplyChargingGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ApplyChargingGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEntityReleasedGPRSRequest(EntityReleasedGPRSRequest ind) {
        this.logger.debug("EntityReleasedGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EntityReleasedGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEntityReleasedGPRSResponse(EntityReleasedGPRSResponse ind) {
        this.logger.debug("EntityReleasedGPRSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EntityReleasedGPRSResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onConnectGPRSRequest(ConnectGPRSRequest ind) {
        this.logger.debug("ConnectGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ConnectGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onContinueGPRSRequest(ContinueGPRSRequest ind) {
        this.logger.debug("ContinueGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ContinueGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onReleaseGPRSRequest(ReleaseGPRSRequest ind) {
        this.logger.debug("ReleaseGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReleaseGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onResetTimerGPRSRequest(ResetTimerGPRSRequest ind) {
        this.logger.debug("ResetTimerGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ResetTimerGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onFurnishChargingInformationGPRSRequest(FurnishChargingInformationGPRSRequest ind) {
        this.logger.debug("FurnishChargingInformationGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCancelGPRSRequest(CancelGPRSRequest ind) {
        this.logger.debug("CancelGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CancelGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendChargingInformationGPRSRequest(SendChargingInformationGPRSRequest ind) {
        this.logger.debug("SendChargingInformationGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendChargingInformationGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onApplyChargingReportGPRSRequest(ApplyChargingReportGPRSRequest ind) {
        this.logger.debug("ApplyChargingReportGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ApplyChargingReportGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onApplyChargingReportGPRSResponse(ApplyChargingReportGPRSResponse ind) {
        this.logger.debug("ApplyChargingReportGPRSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ApplyChargingReportGPRSResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEventReportGPRSRequest(EventReportGPRSRequest ind) {
        this.logger.debug("EventReportGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EventReportGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEventReportGPRSResponse(EventReportGPRSResponse ind) {
        this.logger.debug("EventReportGPRSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EventReportGPRSResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivityTestGPRSRequest(ActivityTestGPRSRequest ind) {
        this.logger.debug("ActivityTestGPRSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivityTestGPRSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivityTestGPRSResponse(ActivityTestGPRSResponse ind) {
        this.logger.debug("ActivityTestGPRSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivityTestGPRSResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onConnectSMSRequest(ConnectSMSRequest ind) {
        this.logger.debug("ConnectSMSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ConnectSMSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEventReportSMSRequest(EventReportSMSRequest ind) {
        this.logger.debug("EventReportSMSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EventReportSMSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onFurnishChargingInformationSMSRequest(FurnishChargingInformationSMSRequest ind) {
        this.logger.debug("FurnishChargingInformationSMSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationSMSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInitialDPSMSRequest(InitialDPSMSRequest ind) {
        this.logger.debug("InitialDPSMSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InitialDPSMSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onReleaseSMSRequest(ReleaseSMSRequest ind) {
        this.logger.debug("ReleaseSMSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReleaseSMSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRequestReportSMSEventRequest(RequestReportSMSEventRequest ind) {
        this.logger.debug("RequestReportSMSEventRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RequestReportSMSEventRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onResetTimerSMSRequest(ResetTimerSMSRequest ind) {
        this.logger.debug("ResetTimerSMSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ResetTimerSMSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onContinueSMSRequest(ContinueSMSRequest ind) {
        this.logger.debug("ContinueSMSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ContinueSMSRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCollectInformationRequest(CollectInformationRequest ind) {
        this.logger.debug("CollectInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CollectInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCallGapRequest(org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest ind) {
        this.logger.debug("CallGapRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CallGapRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }
}
