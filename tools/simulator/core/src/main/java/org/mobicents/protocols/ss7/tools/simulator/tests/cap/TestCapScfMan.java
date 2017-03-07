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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest;
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedDigits;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FreeFormatData;
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

    private int countConnectToResource = 0;
    private int countFurnishChargingInformation = 0;
    private int countPromptAndCollectUserInformation = 0;
    private int countPromptAndCollectUserInformation_Resp = 0;
    private int countActivityTest = 0;
    private int countActivityTest_Resp = 0;

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
    public String getConnectDestinationRoutingAddressAddress() {
        return this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getConDestRouteAddrAddress();
    }

    @Override
    public void setConnectDestinationRoutingAddressAddress(String calledPartyNumberAddress) {
        this.testerHost.getConfigurationData().getTestCapScfConfigurationData().setConDestRouteAddrAddress(calledPartyNumberAddress);
        this.testerHost.markStore();
    }

    @Override
    public IsupNatureOfAddressIndicatorType getConnectDestinationRoutingAddressNatureOfAddress() {
        return new IsupNatureOfAddressIndicatorType (this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getConDestRouteAddrNatureOfAddress().getCode());
    }

    @Override
    public String getConnectDestinationRoutingAddressNatureOfAddress_Value(){
        return new IsupNatureOfAddressIndicatorType (this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getConDestRouteAddrNatureOfAddress().getCode()).toString();
    }

    @Override
    public void setConnectDestinationRoutingAddressNatureOfAddress(IsupNatureOfAddressIndicatorType val) {
        this.testerHost.getConfigurationData().getTestCapScfConfigurationData().setConDestRouteAddrNatureOfAddress(IsupNatureOfAddressIndicator.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putConDestRouteAddrNatureOfAddress(String s) {
        IsupNatureOfAddressIndicatorType x = IsupNatureOfAddressIndicatorType.createInstance(s);
        if (x != null)
            this.setConnectDestinationRoutingAddressNatureOfAddress(x);
    }

    @Override
    public IsupNumberingPlanIndicatorType getConnectDestinationRoutingAddressNumberingPlan() {
        return new IsupNumberingPlanIndicatorType(this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getConDestRouteAddrNumberingPlan().getCode());
    }

    @Override
    public String getConnectDestinationRoutingAddressNumberingPlan_Value() {
        return new IsupNumberingPlanIndicatorType(this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getConDestRouteAddrNumberingPlan().getCode()).toString();
    }

    @Override
    public void setConnectDestinationRoutingAddressNumberingPlan(IsupNumberingPlanIndicatorType val) {
        this.testerHost.getConfigurationData().getTestCapScfConfigurationData().setConDestRouteAddrNumberingPlan(IsupNumberingPlanIndicator.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putConDestRouteAddrNumberingPlan(String s) {
        IsupNumberingPlanIndicatorType x = IsupNumberingPlanIndicatorType.createInstance(s);
        if (x != null)
            this.setConnectDestinationRoutingAddressNumberingPlan(x);
    }

    @Override
    public IsupCauseIndicatorCauseValueType getReleaseCauseValue() {
        return new IsupCauseIndicatorCauseValueType(this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getRelCauseValue().getCode());
    }

    @Override
    public String getReleaseCauseValue_Value() {
        return new IsupCauseIndicatorCauseValueType(this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getRelCauseValue().getCode()).toString();
    }

    @Override
    public void setReleaseCauseValue(IsupCauseIndicatorCauseValueType value) {
        this.testerHost.getConfigurationData().getTestCapScfConfigurationData().setRelCauseValue(IsupCauseIndicatorCauseValue.getInstance(value.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putReleaseCauseValue(String s) {
        IsupCauseIndicatorCauseValueType x = IsupCauseIndicatorCauseValueType.createInstance(s);
        if (x != null)
            this.setReleaseCauseValue(x);
    }

    @Override
    public IsupCauseIndicatorCodingStandardType getReleaseCauseCodingStandardIndicator() {
        return new IsupCauseIndicatorCodingStandardType(this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getRelCodingStandardInd().getCode());
    }

    @Override
    public String getReleaseCauseCodingStandardIndicator_Value() {
        return new IsupCauseIndicatorCodingStandardType(this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getRelCodingStandardInd().getCode()).toString();

    }

    @Override
    public void setReleaseCauseCodingStandardIndicator(IsupCauseIndicatorCodingStandardType value) {
        this.testerHost.getConfigurationData().getTestCapScfConfigurationData().setRelCodingStandardInd(IsupCauseIndicatorCodingStandard.getInstance(value.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putReleaseCauseCodingStandardIndicator(String s) {
        IsupCauseIndicatorCodingStandardType x = IsupCauseIndicatorCodingStandardType.createInstance(s);
        if (x != null)
            this.setReleaseCauseCodingStandardIndicator(x);
    }

    @Override
    public IsupCauseIndicatorLocationType getReleaseCauseLocationIndicator() {
        return new IsupCauseIndicatorLocationType(this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getRelLocationInd().getCode());
    }

    @Override
    public String getReleaseCauseLocationIndicator_Value() {
        return new IsupCauseIndicatorLocationType(this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getRelLocationInd().getCode()).toString();
    }

    @Override
    public void setReleaseCauseLocationIndicator(IsupCauseIndicatorLocationType value) {
        this.testerHost.getConfigurationData().getTestCapScfConfigurationData().setRelLocationInd(IsupCauseIndicatorLocation.getInstance(value.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putReleaseCauseLocationIndicator(String s) {
        IsupCauseIndicatorLocationType x = IsupCauseIndicatorLocationType.createInstance(s);
        if (x != null)
            this.setReleaseCauseLocationIndicator(x);
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

        sb.append("<br>Count: IDP-");
        sb.append(countInitialDp);
        sb.append(", ICA-");
        sb.append(countInitiateCallAttempt);
        sb.append(", ARI-");
        sb.append(countAssistRequestInstructions);
        sb.append(", ACR-");
        sb.append(countApplyChargingReport);
        sb.append(", ERB-");
        sb.append(countEventReportBCSM);
        sb.append(", ACH-");
        sb.append(countApplyCharging);
        sb.append(", CAN-");
        sb.append(countCancel);
        sb.append(", CON-");
        sb.append(countConnect);
        sb.append("<br>CUE-");
        sb.append(countContinue);
        sb.append(", RC-");
        sb.append(countReleaseCall);
        sb.append(", RRB-");
        sb.append(countRequestReportBCSMEvent);

        sb.append(", CTR-");
        sb.append(countConnectToResource);
        sb.append(", FCI-");
        sb.append(countFurnishChargingInformation);
        sb.append(", PC-");
        sb.append(countPromptAndCollectUserInformation);
        sb.append(", PC_Resp-");
        sb.append(countPromptAndCollectUserInformation_Resp);
        sb.append(", AT-");
        sb.append(countActivityTest);
        sb.append(", AT_Resp-");
        sb.append(countActivityTest_Resp);

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
                    .createCAMELAChBillingChargingCharacteristics(1000, false, null, null, null, 2);
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

        TestCapScfConfigurationData scfConfigData = testerHost.getConfigurationData().getTestCapScfConfigurationData();
        try {
            ArrayList<CalledPartyNumberCap> calledPartyNumber = new ArrayList<CalledPartyNumberCap>();
            CalledPartyNumber cpnIsup = capProvider.getISUPParameterFactory().createCalledPartyNumber();
            cpnIsup.setAddress(scfConfigData.getConDestRouteAddrAddress());
            cpnIsup.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
            cpnIsup.setNatureOfAddresIndicator(scfConfigData.getConDestRouteAddrNatureOfAddress().getCode());
            cpnIsup.setNumberingPlanIndicator(scfConfigData.getConDestRouteAddrNumberingPlan().getCode());
            CalledPartyNumberCap cpn = capProvider.getCAPParameterFactory().createCalledPartyNumberCap(cpnIsup);
            calledPartyNumber.add(cpn);
            DestinationRoutingAddress destinationRoutingAddress = capProvider.getCAPParameterFactory()
                    .createDestinationRoutingAddress(calledPartyNumber);
            curDialog.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null, null, null, null, null,
                    null, null, false, false, false, null, false, false);

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

        TestCapScfConfigurationData scfConfigData = testerHost.getConfigurationData().getTestCapScfConfigurationData();
        try {
            CauseIndicators causeIndicators = capProvider.getISUPParameterFactory().createCauseIndicators();
            causeIndicators.setCauseValue(scfConfigData.getRelCauseValue().getCode());
            causeIndicators.setCodingStandard(scfConfigData.getRelCodingStandardInd().getCode());
            causeIndicators.setLocation(scfConfigData.getRelLocationInd().getCode());
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
    public String performConnectToResource(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            CalledPartyNumber calledPartyNumber = capProvider.getISUPParameterFactory().createCalledPartyNumber();
            calledPartyNumber.setAddress("999111");
            calledPartyNumber.setNatureOfAddresIndicator(4);
            calledPartyNumber.setNumberingPlanIndicator(1);
            CalledPartyNumberCap resourceAddress_IPRoutingAddress = capProvider.getCAPParameterFactory().createCalledPartyNumberCap(calledPartyNumber);
            curDialog.addConnectToResourceRequest(resourceAddress_IPRoutingAddress, false, null, null, null);

            curDialog.send();

            currentRequestDef += "Sent CTR;";
            this.countConnectToResource++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: connectToResource", uData, Level.DEBUG);

            return "ConnectToResource has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending ConnectToResource", ex.toString(), Level.DEBUG);
            return "Exception when sending ConnectToResource: " + ex.toString();
        }
    }

    @Override
    public String performFurnishChargingInformation(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            FreeFormatData freeFormatData = capProvider.getCAPParameterFactory().createFreeFormatData(
                    new byte[] { 1, 2, 3, 4, 5 });
            FCIBCCCAMELsequence1 fciBCCCAMELsequence1 = capProvider.getCAPParameterFactory().createFCIBCCCAMELsequence1(
                    freeFormatData, null, null);
            curDialog.addFurnishChargingInformationRequest(fciBCCCAMELsequence1);

            curDialog.send();

            currentRequestDef += "Sent FCI;";
            this.countFurnishChargingInformation++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: furnishChargingInformation", uData, Level.DEBUG);

            return "FurnishChargingInformation has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending FurnishChargingInformation", ex.toString(), Level.DEBUG);
            return "Exception when sending FurnishChargingInformation: " + ex.toString();
        }
    }

    @Override
    public String performPromptAndCollectUserInformation(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

        try {
            CollectedDigits collectedDigits = capProvider.getCAPParameterFactory().createCollectedDigits(7, 8, null, null,
                    null, null, null, null, null, null, null);
            CollectedInfo collectedInfo = capProvider.getCAPParameterFactory().createCollectedInfo(collectedDigits);
            curDialog.addPromptAndCollectUserInformationRequest(collectedInfo, null, null, null, null, null);

            curDialog.send();

            currentRequestDef += "Sent PC;";
            this.countPromptAndCollectUserInformation++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: promptAndCollectUserInformation", uData, Level.DEBUG);

            return "PromptAndCollectUserInformation has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending PromptAndCollectUserInformation", ex.toString(), Level.DEBUG);
            return "Exception when sending PromptAndCollectUserInformation: " + ex.toString();
        }
    }

    @Override
    public String performActivityTest(String msg) {
        if (!isStarted)
            return "The tester is not started";

        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not exist. Start it previousely or wait of starting by a peer";

        try {
            curDialog.addActivityTestRequest();

            curDialog.send();

            currentRequestDef += "Sent AT;";
            this.countActivityTest++;
            String uData = "";
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: activityTest", uData, Level.DEBUG);

            return "ActivityTest has been sent";
        } catch (CAPException ex) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending ActivityTest", ex.toString(), Level.DEBUG);
            return "Exception when sending ActivityTest: " + ex.toString();
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

    @Override
    public void onCollectInformationRequest(CollectInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
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
