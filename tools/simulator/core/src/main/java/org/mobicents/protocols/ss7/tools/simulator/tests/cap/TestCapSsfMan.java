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

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
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
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeInformation;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextSsf;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.CapMan;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapSsfMan extends TesterBase implements TestCapSsfManMBean, Stoppable, CAPDialogListener,
        CAPServiceCircuitSwitchedCallListener {

    public static String SOURCE_NAME = "TestCapSsf";

    private final String name;
    private CapMan capMan;

    private boolean isStarted = false;
    private int countInitialDp = 0;
    private int countAssistRequestInstructions = 0;
    private int countApplyChargingReport = 0;
    private int countEventReportBCSM = 0;
    private int countInitiateCallAttempt = 0;
    private int countApplyCharging = 0;
    private int countCancel = 0;
    private int countConnect = 0;
    private int countContinue = 0;
    private int countReleaseCall = 0;
    private int countRequestReportBCSMEvent = 0;
    private String currentRequestDef = "";
    private CAPDialogCircuitSwitchedCall currentDialog = null;

    public TestCapSsfMan() {
        super(SOURCE_NAME);
        this.name = "???";
    }

    public TestCapSsfMan(String name) {
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
    public CapApplicationContextSsf getCapApplicationContext() {
        return this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCapApplicationContext();
    }

    @Override
    public String getCapApplicationContext_Value() {
        return this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCapApplicationContext().toString();
    }

    @Override
    public void setCapApplicationContext(CapApplicationContextSsf val) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCapApplicationContext(val);
        this.testerHost.markStore();
    }

    @Override
    public void putCapApplicationContext(String val) {
        CapApplicationContextSsf x = CapApplicationContextSsf.createInstance(val);
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
        this.testerHost.sendNotif(SOURCE_NAME, "CAP SSF has been started", "", Level.INFO);
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
        this.testerHost.sendNotif(SOURCE_NAME, "CAP SSF has been stopped", "", Level.INFO);
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
        // currentRequestDef = "";
    }

    @Override
    public String performInitialDp(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog2 = currentDialog;
        if (curDialog2 != null)
            return "The current dialog exists. Finish it previousely";

        currentRequestDef = "";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();
        CAPApplicationContext capAppContext = this.testerHost.getConfigurationData().getTestCapSsfConfigurationData()
                .getCapApplicationContext().getCAPApplicationContext();

        try {
            CAPDialogCircuitSwitchedCall curDialog = capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
                    capAppContext, this.capMan.createOrigAddress(), this.capMan.createDestAddress());
            currentDialog = curDialog;
            this.testerHost.sendNotif(SOURCE_NAME, "DlgStarted:", "TrId=" + curDialog.getLocalDialogId(), Level.DEBUG);

            int serviceKey = 1;
            CalledPartyNumber calledPartyNumberIsup = capProvider.getISUPParameterFactory().createCalledPartyNumber();
            calledPartyNumberIsup.setAddress("111222");
            calledPartyNumberIsup.setInternalNetworkNumberIndicator(calledPartyNumberIsup._INN_ROUTING_ALLOWED);
            calledPartyNumberIsup.setNatureOfAddresIndicator(calledPartyNumberIsup._NAI_INTERNATIONAL_NUMBER);
            calledPartyNumberIsup.setNumberingPlanIndicator(calledPartyNumberIsup._NPI_ISDN);
            CalledPartyNumberCap calledPartyNumber = capProvider.getCAPParameterFactory().createCalledPartyNumberCap(
                    calledPartyNumberIsup);

            curDialog.addInitialDPRequest(serviceKey, calledPartyNumber, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null,
                    null, false, null);

            curDialog.send();

            currentRequestDef += "Sent initialDp;";
            this.countInitialDp++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: initialDP", uData, Level.DEBUG);

            return "initialDP has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending initialDP", ex.toString(), Level.DEBUG);
            return "Exception when sending initialDP: " + ex.toString();
        }
    }

    @Override
    public String performAssistRequestInstructions(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog2 = currentDialog;
        if (curDialog2 != null)
            return "The current dialog exists. Finish it previousely";

        currentRequestDef = "";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();
        CAPApplicationContext capAppContext = this.testerHost.getConfigurationData().getTestCapSsfConfigurationData()
                .getCapApplicationContext().getCAPApplicationContext();

        try {
            CAPDialogCircuitSwitchedCall curDialog = capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
                    capAppContext, this.capMan.createOrigAddress(), this.capMan.createDestAddress());
            currentDialog = curDialog;
            this.testerHost.sendNotif(SOURCE_NAME, "DlgStarted:", "TrId=" + curDialog.getLocalDialogId(), Level.DEBUG);

            GenericNumber genericNumber = capProvider.getISUPParameterFactory().createGenericNumber();
            genericNumber.setAddress("111333");
            genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
            genericNumber.setNatureOfAddresIndicator(GenericNumber._NAI_INTERNATIONAL_NUMBER);
            genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
            genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
            genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
            genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
            Digits correlationID = capProvider.getCAPParameterFactory().createDigits_GenericNumber(genericNumber);

            IPSSPCapabilities ipSSPCapabilities = capProvider.getCAPParameterFactory().createIPSSPCapabilities(false, false,
                    false, false, false, null);

            curDialog.addAssistRequestInstructionsRequest(correlationID, ipSSPCapabilities, null);

            curDialog.send();

            currentRequestDef += "Sent assistRequestInstructions;";
            this.countAssistRequestInstructions++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: assistRequestInstructions", uData, Level.DEBUG);

            return "assistRequestInstructions has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending assistRequestInstructions", ex.toString(),
                    Level.DEBUG);
            return "Exception when sending assistRequestInstructions: " + ex.toString();
        }
    }

    @Override
    public String performApplyChargingReport(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            ReceivingSideID partyToCharge = capProvider.getCAPParameterFactory().createReceivingSideID(LegType.leg1);
            TimeIfTariffSwitch timeIfNoTariffSwitch = capProvider.getCAPParameterFactory().createTimeIfTariffSwitch(200, null);
            TimeInformation timeInformation = capProvider.getCAPParameterFactory().createTimeInformation(timeIfNoTariffSwitch);
            TimeDurationChargingResult timeDurationChargingResult = capProvider.getCAPParameterFactory()
                    .createTimeDurationChargingResult(partyToCharge, timeInformation, true, false, null, null);
            curDialog.addApplyChargingReportRequest(timeDurationChargingResult);

            curDialog.send();

            currentRequestDef += "Sent applyChargingReport;";
            this.countApplyChargingReport++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: applyChargingReport", uData, Level.DEBUG);

            return "applyChargingReport has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending applyChargingReport", ex.toString(), Level.DEBUG);
            return "Exception when sending applyChargingReport: " + ex.toString();
        }
    }

    @Override
    public String performEventReportBCSM(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            curDialog.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, null, null, null, null);

            curDialog.send();

            currentRequestDef += "Sent eventReportBCSM;";
            this.countEventReportBCSM++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: eventReportBCSM", uData, Level.DEBUG);

            return "eventReportBCSM has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending eventReportBCSM", ex.toString(), Level.DEBUG);
            return "Exception when sending eventReportBCSM: " + ex.toString();
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
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingRequest(ApplyChargingRequest arg0) {
        this.countApplyCharging++;
        currentRequestDef += "Rsvd applyCharging;";
    }

    @Override
    public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest arg0) {
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
        this.countCancel++;
        currentRequestDef += "Rsvd cancel;";
    }

    @Override
    public void onConnectRequest(ConnectRequest arg0) {
        this.countConnect++;
        currentRequestDef += "Rsvd Connect;";
    }

    @Override
    public void onConnectToResourceRequest(ConnectToResourceRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueRequest(ContinueRequest arg0) {
        this.countContinue++;
        currentRequestDef += "Rsvd continue;";
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
        // TODO Auto-generated method stub

    }

    @Override
    public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitialDPRequest(InitialDPRequest arg0) {
        // TODO Auto-generated method stub

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
        this.countReleaseCall++;
        currentRequestDef += "Rsvd releaseCall;";
    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest arg0) {
        this.countRequestReportBCSMEvent++;
        currentRequestDef += "Rsvd requestReportBCSMEvent;";
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
    public void onDialogAccept(CAPDialog dlg, CAPGprsReferenceNumber referenceNumber) {
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
                        e.printStackTrace();
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
