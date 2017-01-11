package org.mobicents.protocols.ss7.cap;

import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessage;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NAINumber;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

public class CallScfExample implements CAPDialogListener, CAPServiceCircuitSwitchedCallListener {

    private CAPProvider capProvider;
    private CAPParameterFactory paramFact;
    private CAPDialogCircuitSwitchedCall currentCapDialog;
    private CallContent cc;

    public CallScfExample() throws NamingException {
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

    @Override
    public void onInitialDPRequest(InitialDPRequest ind) {
        this.cc = new CallContent();
        this.cc.idp = ind;
        this.cc.step = Step.initialDPRecieved;

        ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
    }

    @Override
    public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
        if (this.cc != null) {
            this.cc.eventList.add(ind);

            switch (ind.getEventTypeBCSM()) {
                case oAnswer:
                    this.cc.step = Step.answered;
                    break;
                case oDisconnect:
                    this.cc.step = Step.disconnected;
                    break;
            }
        }

        ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
    }

    @Override
    public void onDialogDelimiter(CAPDialog capDialog) {
        try {
            if (this.cc != null) {
                switch (this.cc.step) {
                    case initialDPRecieved:
                        // informing SSF of BCSM events processing
                        ArrayList<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
                        BCSMEvent ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(
                                EventTypeBCSM.routeSelectFailure, MonitorMode.notifyAndContinue, null, null, false);
                        bcsmEventList.add(ev);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oCalledPartyBusy,
                                MonitorMode.interrupted, null, null, false);
                        bcsmEventList.add(ev);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oNoAnswer,
                                MonitorMode.interrupted, null, null, false);
                        bcsmEventList.add(ev);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oAnswer,
                                MonitorMode.notifyAndContinue, null, null, false);
                        bcsmEventList.add(ev);
                        LegID legId = this.capProvider.getINAPParameterFactory().createLegID(true, LegType.leg1);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oDisconnect,
                                MonitorMode.notifyAndContinue, legId, null, false);
                        bcsmEventList.add(ev);
                        legId = this.capProvider.getINAPParameterFactory().createLegID(true, LegType.leg2);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oDisconnect,
                                MonitorMode.interrupted, legId, null, false);
                        bcsmEventList.add(ev);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oAbandon,
                                MonitorMode.notifyAndContinue, null, null, false);
                        bcsmEventList.add(ev);
                        currentCapDialog.addRequestReportBCSMEventRequest(bcsmEventList, null);

                        // calculating here a new called party number if it is needed
                        String newNumber = "22123124";
                        if (newNumber != null) {
                            // sending Connect to force routing the call to a new number
                            ArrayList<CalledPartyNumberCap> calledPartyNumber = new ArrayList<CalledPartyNumberCap>();
                            CalledPartyNumber cpn = this.capProvider.getISUPParameterFactory().createCalledPartyNumber();
                            cpn.setAddress("5599999988");
                            cpn.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
                            cpn.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
                            cpn.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
                            CalledPartyNumberCap cpnc = this.capProvider.getCAPParameterFactory().createCalledPartyNumberCap(
                                    cpn);
                            calledPartyNumber.add(cpnc);
                            DestinationRoutingAddress destinationRoutingAddress = this.capProvider.getCAPParameterFactory()
                                    .createDestinationRoutingAddress(calledPartyNumber);
                            currentCapDialog.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null,
                                    null, null, null, null, null, null, false, false, false, null, false, false);
                        } else {
                            // sending Continue to use the original calledPartyAddress
                            currentCapDialog.addContinueRequest();
                        }

                        currentCapDialog.send();
                        break;

                    case disconnected:
                        // the call is terminated - close dialog
                        currentCapDialog.close(false);
                        break;
                }
            }
        } catch (CAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogTimeout(CAPDialog capDialog) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected
                && this.cc.activityTestInvokeId == null) {
            // check the SSF if the call is still alive
            currentCapDialog.keepAlive();
            try {
                this.cc.activityTestInvokeId = currentCapDialog.addActivityTestRequest();
                currentCapDialog.send();
            } catch (CAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse ind) {
        if (currentCapDialog != null && this.cc != null) {
            this.cc.activityTestInvokeId = null;
        }
    }

    @Override
    public void onInvokeTimeout(CAPDialog capDialog, Long invokeId) {
        if (currentCapDialog != null && this.cc != null) {
            if (this.cc.activityTestInvokeId == invokeId) { // activityTest failure
                try {
                    currentCapDialog.close(true);
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
    public void onCAPMessage(CAPMessage capMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingRequest(ApplyChargingRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueRequest(ContinueRequest ind) {
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
    public void onConnectRequest(ConnectRequest ind) {
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
    public void onActivityTestRequest(ActivityTestRequest ind) {
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
        initialDPRecieved, answered, disconnected;
    }

    private class CallContent {
        public Step step;
        public InitialDPRequest idp;
        public ArrayList<EventReportBCSMRequest> eventList = new ArrayList<EventReportBCSMRequest>();
        public Long activityTestInvokeId;
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

    @Override
    public void onCallGapRequest(CallGapRequest ind) {
        // TODO Auto-generated method stub

    }
}
