package org.mobicents.protocols.ss7.cap;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessage;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

public class CallSsfExample implements CAPDialogListener, CAPServiceCircuitSwitchedCallListener {

    private CAPProvider capProvider;
    private CAPParameterFactory paramFact;
    private CAPDialogCircuitSwitchedCall currentCapDialog;
    private CallContent cc;

    public CallSsfExample() throws NamingException {
        InitialContext ctx = new InitialContext();
        try {
            String providerJndiName = "java:/mobicents/ss7/cap";
            this.capProvider = ((CAPProvider) ctx.lookup(providerJndiName));
        } finally {
            ctx.close();
        }

        paramFact = capProvider.getCAPParameterFactory();

        capProvider.addCAPDialogListener(this);
        capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
    }

    public CAPProvider getCAPProvider() {
        return capProvider;
    }

    public void start() {
        // Make the circuitSwitchedCall service activated
        capProvider.getCAPServiceCircuitSwitchedCall().acivate();

        currentCapDialog = null;
    }

    public void stop() {
        capProvider.getCAPServiceCircuitSwitchedCall().deactivate();
    }

    public void sendInitialDP(SccpAddress origAddress, SccpAddress remoteAddress, int serviceKey,
            CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber, LocationNumberCap locationNumber,
            EventTypeBCSM eventTypeBCSM, LocationInformation locationInformation) throws CAPException {
        // First create Dialog
        CAPApplicationContext acn = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
        currentCapDialog = capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(acn, origAddress, remoteAddress);

        currentCapDialog.addInitialDPRequest(serviceKey, calledPartyNumber, callingPartyNumber, null, null, null,
                locationNumber, null, null, null, null, null, eventTypeBCSM, null, null, null, null, null, null, null, false,
                null, null, locationInformation, null, null, null, null, null, false, null);
        // This will initiate the TC-BEGIN with INVOKE component
        currentCapDialog.send();

        this.cc.step = Step.initialDPSent;
        this.cc.calledPartyNumber = calledPartyNumber;
        this.cc.callingPartyNumber = callingPartyNumber;

    }

    public void sendEventReportBCSM_OAnswer(OAnswerSpecificInfo oAnswerSpecificInfo, ReceivingSideID legID,
            MiscCallInfo miscCallInfo) throws CAPException {
        if (currentCapDialog != null && this.cc != null) {
            EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capProvider.getCAPParameterFactory()
                    .createEventSpecificInformationBCSM(oAnswerSpecificInfo);
            currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, eventSpecificInformationBCSM, legID,
                    miscCallInfo, null);
            currentCapDialog.send();
            this.cc.step = Step.answered;
        }
    }

    public void sendEventReportBCSM_ODisconnect(ODisconnectSpecificInfo oDisconnectSpecificInfo, ReceivingSideID legID,
            MiscCallInfo miscCallInfo) throws CAPException {
        if (currentCapDialog != null && this.cc != null) {
            EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capProvider.getCAPParameterFactory()
                    .createEventSpecificInformationBCSM(oDisconnectSpecificInfo);
            currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oDisconnect, eventSpecificInformationBCSM, legID,
                    miscCallInfo, null);
            currentCapDialog.send();
            this.cc.step = Step.disconnected;
        }
    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            this.cc.requestReportBCSMEventRequest = ind;

            // initiating BCSM events processing
        }
        ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
    }

    @Override
    public void onActivityTestRequest(ActivityTestRequest ind) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            this.cc.activityTestInvokeId = ind.getInvokeId();
        }
    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueRequest(ContinueRequest ind) {
        this.cc.step = Step.callAllowed;
        ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        // sending Continue to use the original calledPartyAddress
    }

    @Override
    public void onConnectRequest(ConnectRequest ind) {
        this.cc.step = Step.callAllowed;
        this.cc.destinationRoutingAddress = ind.getDestinationRoutingAddress();
        ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        // sending Connect to force routing the call to a new number
    }

    @Override
    public void onDialogTimeout(CAPDialog capDialog) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            // if the call is still up - keep the sialog alive
            currentCapDialog.keepAlive();
        }
    }

    @Override
    public void onDialogDelimiter(CAPDialog capDialog) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            if (this.cc.activityTestInvokeId != null) {
                try {
                    currentCapDialog.addActivityTestResponse(this.cc.activityTestInvokeId);
                    this.cc.activityTestInvokeId = null;
                    currentCapDialog.send();
                } catch (CAPException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onErrorComponent(CAPDialog capDialog, Long invokeId, CAPErrorMessage capErrorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRejectComponent(CAPDialog capDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInvokeTimeout(CAPDialog capDialog, Long invokeId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCAPMessage(CAPMessage capMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitialDPRequest(InitialDPRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingRequest(ApplyChargingRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleaseCallRequest(ReleaseCallRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallInformationRequestRequest(CallInformationRequestRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallInformationReportRequest(CallInformationReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetTimerRequest(ResetTimerRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelRequest(CancelRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogClose(CAPDialog capDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRelease(CAPDialog capDialog) {
        this.currentCapDialog = null;
        this.cc = null;
    }

    @Override
    public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        // TODO Auto-generated method stub

    }

    private enum Step {
        initialDPSent, callAllowed, answered, disconnected;
    }

    private class CallContent {
        public Step step;
        public Long activityTestInvokeId;

        public CalledPartyNumberCap calledPartyNumber;
        public CallingPartyNumberCap callingPartyNumber;
        public RequestReportBCSMEventRequest requestReportBCSMEventRequest;
        public DestinationRoutingAddress destinationRoutingAddress;
    }

    @Override
    public void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectLegRequest(DisconnectLegRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectLegResponse(DisconnectLegResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectForwardConnectionWithArgumentRequest(DisconnectForwardConnectionWithArgumentRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest initiateCallAttemptRequest) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitiateCallAttemptResponse(InitiateCallAttemptResponse initiateCallAttemptResponse) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMoveLegRequest(MoveLegRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMoveLegResponse(MoveLegResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCollectInformationRequest(CollectInformationRequest ind) {
        // TODO Auto-generated method stub
        
    }

    public void onSplitLegRequest(SplitLegRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSplitLegResponse(SplitLegResponse ind) {
        // TODO Auto-generated method stub

    }
}
