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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessage;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPDialogState;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextScf;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.CapMan;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapScfMan extends TesterBase implements TestCapScfManMBean, Stoppable, CAPDialogListener,
        CAPServiceCircuitSwitchedCallListener {

    public static String SOURCE_NAME = "TestCapScf";

    private final String name;
    private CapMan capMan;

    private boolean isStarted = false;
    private int countInitialDp = 0;
    private int countAssistRequestInstructions = 0;
    private int countApplyChargingReport = 0;
    private int countEventReportBCSM = 0;
    private int countApplyCharging = 0;
    private int countCancel = 0;
    private int countConnect = 0;
    private int countContinue = 0;
    private int countReleaseCall = 0;
    private int countRequestReportBCSMEvent = 0;
    private int countInitiateCallAttempt = 0;

    private String currentRequestDef = "";
    private CAPDialogCircuitSwitchedCall currentDialog = null;

    public TestCapScfMan() {
        super(SOURCE_NAME);
        this.name = "???";
    }

    public TestCapScfMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
    }

    public void setTesterHost(TesterHost testerHost) {
        this.testerHost = testerHost;
    }

    public void setCapMan(CapMan val) {
        this.capMan = val;
    }

    @Override
    public CapApplicationContextScf getCapApplicationContext() {
        return this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getCapApplicationContext();
    }

    @Override
    public String getCapApplicationContext_Value() {
        return this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getCapApplicationContext().toString();
    }

    @Override
    public void setCapApplicationContext(CapApplicationContextScf val) {
        this.testerHost.getConfigurationData().getTestCapScfConfigurationData().setCapApplicationContext(val);
        this.testerHost.markStore();
    }

    @Override
    public void putCapApplicationContext(String val) {
        CapApplicationContextScf x = CapApplicationContextScf.createInstance(val);
        if (x != null)
            this.setCapApplicationContext(x);
    }

    @Override
    public String getCurrentRequestDef() {
        return "LastDialog: " + currentRequestDef;
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(SOURCE_NAME);
        sb.append(": ");
        if (this.currentDialog != null) {
            sb.append(", curDialog: ");
            sb.append(this.currentDialog.getState());
        }
        sb.append("<br>Count: countInitialDp-");
        sb.append(countInitialDp);
        sb.append(", countInitiateCallAttempt-");
        sb.append(countInitiateCallAttempt);
        sb.append(", countAssistRequestInstructions-");
        sb.append(countAssistRequestInstructions);
        sb.append("<br>countApplyChargingReport-");
        sb.append(countApplyChargingReport);
        sb.append(", countEventReportBCSM-");
        sb.append(countEventReportBCSM);
        sb.append(", countApplyCharging-");
        sb.append(countApplyCharging);
        sb.append("<br>countCancel-");
        sb.append(countCancel);
        sb.append(", countConnect-");
        sb.append(countConnect);
        sb.append(", countContinue-");
        sb.append(countContinue);
        sb.append("<br>countReleaseCall-");
        sb.append(countReleaseCall);
        sb.append(", countRequestReportBCSMEvent-");
        sb.append(countRequestReportBCSMEvent);
        sb.append("</html>");
        return sb.toString();
    }

    public boolean start() {
        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();
        capProvider.getCAPServiceCircuitSwitchedCall().acivate();
        capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
        capProvider.addCAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "CAP SCF has been started", "", Level.INFO);
        currentDialog = null;
        isStarted = true;

        return true;
    }

    @Override
    public void stop() {
        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();
        isStarted = false;
        capProvider.getCAPServiceCircuitSwitchedCall().deactivate();
        capProvider.getCAPServiceCircuitSwitchedCall().removeCAPServiceListener(this);
        capProvider.removeCAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "CAP SCF has been stopped", "", Level.INFO);
    }

    @Override
    public void execute() {
    }

    @Override
    public String closeCurrentDialog() {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog != null) {
            try {
                if (curDialog.getState() == CAPDialogState.Active)
                    curDialog.close(false);
                else
                    curDialog.abort(CAPUserAbortReason.no_reason_given);
                this.doRemoveDialog();
                return "The current dialog has been closed";
            } catch (CAPException e) {
                this.doRemoveDialog();
                this.testerHost.sendNotif(SOURCE_NAME, "Exception when closing a dialog", e.toString(), Level.DEBUG);
                return "Exception when closing the current dialog: " + e.toString();
            }
        } else {
            return "No current dialog";
        }
    }

    private void doRemoveDialog() {
        currentDialog = null;
    }

    @Override
    public String performInitiateCallAttempt(String msg) {
        return "Not yet supported";
        // TODO: implement it for CAP V4
    }

    @Override
    public String performApplyCharging(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics = capProvider.getCAPParameterFactory()
                    .createCAMELAChBillingChargingCharacteristics(1000, false, null, null, null, false);
            SendingSideID partyToCharge = capProvider.getCAPParameterFactory().createSendingSideID(LegType.leg1);
            curDialog.addApplyChargingRequest(aChBillingChargingCharacteristics, partyToCharge, null, null);

            curDialog.send();

            currentRequestDef += "Sent applyCharging;";
            this.countApplyCharging++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: applyCharging", uData, Level.DEBUG);

            return "applyCharging has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending applyCharging", ex.toString(), Level.DEBUG);
            return "Exception when sending applyCharging: " + ex.toString();
        }
    }

    @Override
    public String performCancel(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            curDialog.addCancelRequest_InvokeId(1);

            curDialog.send();

            currentRequestDef += "Sent cancel;";
            this.countCancel++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: cancel", uData, Level.DEBUG);

            return "cancel has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending cancel", ex.toString(), Level.DEBUG);
            return "Exception when sending cancel: " + ex.toString();
        }
    }

    @Override
    public String performConnect(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            ArrayList<CalledPartyNumberCap> calledPartyNumber = new ArrayList<CalledPartyNumberCap>();
            CalledPartyNumber cpnIsup = capProvider.getISUPParameterFactory().createCalledPartyNumber();
            cpnIsup.setAddress("999911119");
            cpnIsup.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
            cpnIsup.setNatureOfAddresIndicator(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
            cpnIsup.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
            CalledPartyNumberCap cpn = capProvider.getCAPParameterFactory().createCalledPartyNumberCap(cpnIsup);
            calledPartyNumber.add(cpn);
            DestinationRoutingAddress destinationRoutingAddress = capProvider.getCAPParameterFactory()
                    .createDestinationRoutingAddress(calledPartyNumber);
            curDialog.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null, null, null, null, null,
                    null, null, false, false, false, null, false);

            curDialog.send();

            currentRequestDef += "Sent connect;";
            this.countConnect++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: connect", uData, Level.DEBUG);

            return "cancel has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending connect", ex.toString(), Level.DEBUG);
            return "Exception when sending connect: " + ex.toString();
        }
    }

    @Override
    public String performContinue(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            curDialog.addContinueRequest();

            curDialog.send();

            currentRequestDef += "Sent continue;";
            this.countContinue++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: continue", uData, Level.DEBUG);

            return "Continue has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending continue", ex.toString(), Level.DEBUG);
            return "Exception when sending continue: " + ex.toString();
        }
    }

    @Override
    public String performReleaseCall(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            CauseIndicators causeIndicators = capProvider.getISUPParameterFactory().createCauseIndicators();
            causeIndicators.setCauseValue(CauseIndicators._CV_ADDRESS_INCOMPLETE);
            causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
            causeIndicators.setLocation(CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
            causeIndicators.setRecommendation(0);
            CauseCap cause = capProvider.getCAPParameterFactory().createCauseCap(causeIndicators);
            curDialog.addReleaseCallRequest(cause);

            curDialog.send();

            currentRequestDef += "Sent releaseCall;";
            this.countReleaseCall++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: releaseCall", uData, Level.DEBUG);

            return "ReleaseCall has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending releaseCall", ex.toString(), Level.DEBUG);
            return "Exception when sending releaseCall: " + ex.toString();
        }
    }

    @Override
    public String performRequestReportBCSMEvent(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            ArrayList<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
            BCSMEvent ev = capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oAnswer, MonitorMode.transparent,
                    null, null, false);
            bcsmEventList.add(ev);
            curDialog.addRequestReportBCSMEventRequest(bcsmEventList, null);

            curDialog.send();

            currentRequestDef += "Sent requestReportBCSMEvent;";
            this.countRequestReportBCSMEvent++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: requestReportBCSMEvent", uData, Level.DEBUG);

            return "RequestReportBCSMEvent has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending requestReportBCSMEvent", ex.toString(), Level.DEBUG);
            return "Exception when sending requestReportBCSMEvent: " + ex.toString();
        }
    }

    @Override
    public void onCAPMessage(CAPMessage msg) {
        this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: " + msg.getMessageType().toString(), msg.toString(), Level.DEBUG);
    }

    @Override
    public void onErrorComponent(CAPDialog dlg, Long invokeId, CAPErrorMessage msg) {
        this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: Error, InvokeId=" + invokeId + ", Error=" + msg.getErrorCode(),
                msg.toString(), Level.DEBUG);
    }

    @Override
    public void onInvokeTimeout(CAPDialog arg0, Long arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRejectComponent(CAPDialog dlg, Long invokeId, Problem problem, boolean isLocalOriginated) {
        this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: Reject, InvokeId=" + invokeId
                + (isLocalOriginated ? ", local" : ", remote"), problem.toString(), Level.DEBUG);
    }

    @Override
    public void onActivityTestRequest(ActivityTestRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingReportRequest(ApplyChargingReportRequest arg0) {
        this.countApplyChargingReport++;
        currentRequestDef += "Rsvd applyChargingReport;";
    }

    @Override
    public void onApplyChargingRequest(ApplyChargingRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallInformationReportRequest(CallInformationReportRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallInformationRequestRequest(CallInformationRequestRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelRequest(CancelRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectRequest(ConnectRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectToResourceRequest(ConnectToResourceRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueRequest(ContinueRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventReportBCSMRequest(EventReportBCSMRequest arg0) {
        this.countEventReportBCSM++;
        currentRequestDef += "Rsvd eventReportBCSM;";
    }

    @Override
    public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitialDPRequest(InitialDPRequest ind) {
        this.countInitialDp++;
        currentRequestDef += "Rsvd initialDp;";
    }

    @Override
    public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest arg0) {
        this.countAssistRequestInstructions++;
        currentRequestDef += "Rsvd assistRequestInstructions;";
    }

    @Override
    public void onPlayAnnouncementRequest(PlayAnnouncementRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleaseCallRequest(ReleaseCallRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetTimerRequest(ResetTimerRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendChargingInformationRequest(SendChargingInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogAccept(CAPDialog dlg, CAPGprsReferenceNumber arg1) {
        this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgAccept", "TrId=" + dlg.getLocalDialogId(), Level.DEBUG);
    }

    @Override
    public void onDialogClose(CAPDialog dlg) {
        this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgClose", "TrId=" + dlg.getLocalDialogId(), Level.DEBUG);
    }

    @Override
    public void onDialogDelimiter(CAPDialog dlg) {
        try {
            if (dlg.getState() == CAPDialogState.InitialReceived)
                dlg.send();
        } catch (CAPException e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending a first response", e.toString(), Level.DEBUG);
        }
    }

    @Override
    public void onDialogNotice(CAPDialog dlg, CAPNoticeProblemDiagnostic problem) {
        this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgNotice", "Problem: " + problem, Level.DEBUG);
    }

    @Override
    public void onDialogProviderAbort(CAPDialog dlg, PAbortCauseType problem) {
        this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgProviderAbort", "Problem: " + problem, Level.DEBUG);
    }

    @Override
    public void onDialogRelease(CAPDialog arg0) {
        this.doRemoveDialog();
        this.testerHost.sendNotif(SOURCE_NAME, "DlgClosed:", "", Level.DEBUG);
    }

    @Override
    public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber referenceNumber) {
        synchronized (this) {
            if (capDialog instanceof CAPDialogCircuitSwitchedCall) {
                CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

                CAPDialogCircuitSwitchedCall curDialog = this.currentDialog;
                currentRequestDef = "";
                if (curDialog == null) {
                    this.currentDialog = dlg;
                    this.testerHost
                            .sendNotif(SOURCE_NAME, "DlgAccepted:", "TrId=" + capDialog.getRemoteDialogId(), Level.DEBUG);
                } else {
                    try {
                        capDialog.abort(CAPUserAbortReason.congestion);
                    } catch (CAPException e) {
                        this.testerHost.sendNotif(SOURCE_NAME, "Exception when rejecting Dialog", e.toString(), Level.DEBUG);
                    }
                    this.testerHost.sendNotif(SOURCE_NAME, "Rejected incoming Dialog:",
                            "TrId=" + capDialog.getRemoteDialogId(), Level.DEBUG);
                }
            }
        }
    }

    @Override
    public void onDialogTimeout(CAPDialog dlg) {
        dlg.keepAlive();
    }

    @Override
    public void onDialogUserAbort(CAPDialog dlg, CAPGeneralAbortReason reason, CAPUserAbortReason userAbort) {
        this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgProviderAbort", reason + " - " + userAbort, Level.DEBUG);
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

}
