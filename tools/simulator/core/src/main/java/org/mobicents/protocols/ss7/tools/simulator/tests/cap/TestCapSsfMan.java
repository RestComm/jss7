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
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeInformation;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextSsf;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.CapMan;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
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

    private int countConnectToResource = 0;
    private int countFurnishChargingInformation = 0;
    private int countPromptAndCollectUserInformation = 0;
    private int countPromptAndCollectUserInformation_Resp = 0;
    private int countActivityTest = 0;
    private int countActivityTest_Resp = 0;

    private String currentRequestDef = "";
    private CAPDialogCircuitSwitchedCall currentDialog = null;
    private boolean needPerformSend = false;

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
    public int getServiceKey() {
        return this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getServiceKey();
    }

    @Override
    public void setServiceKey(int serviceKey) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setServiceKey(serviceKey);
        this.testerHost.markStore();
    }

    @Override
    public EventTypeBCSMType getIdpEventTypeBCSM() {
        return new EventTypeBCSMType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getIdpEventTypeBCSM().getCode());
    }

    @Override
    public String getIdpEventTypeBCSM_Value() {
          return new EventTypeBCSMType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getIdpEventTypeBCSM().getCode()).toString();
    }

    @Override
    public void setIdpEventTypeBCSM(EventTypeBCSMType value) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setIdpEventTypeBCSM(EventTypeBCSM.getInstance(value.intValue()));
        this.testerHost.markStore();
    }


    @Override
    public void putIdpEventTypeBCSM(String s) {
        EventTypeBCSMType x = EventTypeBCSMType.createInstance(s);
        if (x != null)
            this.setIdpEventTypeBCSM(x);
    }

    @Override
    public boolean isUseCldInsteadOfCldBCDNumber() {
        return this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().isUseCldInsteadOfCldBCDNumber();
    }

    @Override
    public void setUseCldInsteadOfCldBCDNumber(boolean value) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setUseCldInsteadOfCldBCDNumber(value);
        this.testerHost.markStore();
    }

    @Override
    public String getCallingPartyNumberAddress() {
        return this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCallingPartyNumberAddress();
    }

    @Override
    public void setCallingPartyNumberAddress(String callingPartyNumberAddress) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCallingPartyNumberAddress(callingPartyNumberAddress);
        this.testerHost.markStore();
    }

    @Override
    public IsupNatureOfAddressIndicatorType getCallingPartyNumberNatureOfAddress() {
        return new IsupNatureOfAddressIndicatorType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCallingPartyNumberNatureOfAddress().getCode());
    }

    @Override
    public String getCallingPartyNumberNatureOfAddress_Value() {
        return new IsupNatureOfAddressIndicatorType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCallingPartyNumberNatureOfAddress().getCode()).toString();
    }


    @Override
    public void setCallingPartyNumberNatureOfAddress(IsupNatureOfAddressIndicatorType value) {

        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCallingPartyNumberNatureOfAddress(IsupNatureOfAddressIndicator.getInstance(value.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putCallingPartyNumberNatureOfAddress(String s) {
        IsupNatureOfAddressIndicatorType x = IsupNatureOfAddressIndicatorType.createInstance(s);
        if (x != null)
            this.setCallingPartyNumberNatureOfAddress(x);
    }

    @Override
    public IsupNumberingPlanIndicatorType getCallingPartyNumberNumberingPlan() {
        return new IsupNumberingPlanIndicatorType (this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCallingPartyNumberNumberingPlan().getCode());
    }
    @Override
    public String getCallingPartyNumberNumberingPlan_Value (){
        return new IsupNumberingPlanIndicatorType (this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCallingPartyNumberNumberingPlan().getCode()).toString();
    }

    @Override
    public void setCallingPartyNumberNumberingPlan(IsupNumberingPlanIndicatorType value) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCallingPartyNumberNumberingPlan(IsupNumberingPlanIndicator.getInstance(value.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putCallingPartyNumberNumberingPlan(String s) {
        IsupNumberingPlanIndicatorType x = IsupNumberingPlanIndicatorType.createInstance(s);
        if (x != null)
            this.setCallingPartyNumberNumberingPlan(x);
    }

    @Override
    public String getCalledPartyBCDNumberAddress() {
        return this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyBCDNumberAddress();
    }

    @Override
    public void setCalledPartyBCDNumberAddress(String calledPartyBCDNumberAddress) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCalledPartyBCDNumberAddress(calledPartyBCDNumberAddress);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getCalledPartyBCDNumberAddressNature() {
           return new AddressNatureType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyBCDNumberAddressNature().getIndicator());
    }

    @Override
    public String getCalledPartyBCDNumberAddressNature_Value() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyBCDNumberAddressNature().getIndicator()).toString();
    }

    @Override
    public void setCalledPartyBCDNumberAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCalledPartyBCDNumberAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putCalledPartyBCDNumberAddressNature(String val) {
        AddressNatureType x = AddressNatureType.createInstance(val);
        if (x != null)
            this.setCalledPartyBCDNumberAddressNature(x);
    }

    @Override
    public NumberingPlanMapType getCalledPartyBCDNumberNumberingPlan() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyBCDNumberNumberingPlan().getIndicator());
    }

    @Override
    public String getCalledPartyBCDNumberNumberingPlan_Value() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyBCDNumberNumberingPlan().getIndicator()).toString();
    }

    @Override
    public void setCalledPartyBCDNumberNumberingPlan(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCalledPartyBCDNumberNumberingPlan(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putCalledPartyBCDNumberNumberingPlan(String s) {
        NumberingPlanMapType x = NumberingPlanMapType.createInstance(s);
        if (x != null)
            this.setCalledPartyBCDNumberNumberingPlan(x);
    }

    @Override
    public String getCalledPartyNumberAddress() {
        return this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyNumberAddress();
    }

    @Override
    public void setCalledPartyNumberAddress(String calledPartyNumberAddress) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCalledPartyNumberAddress(calledPartyNumberAddress);
        this.testerHost.markStore();
    }

    @Override
    public IsupNatureOfAddressIndicatorType getCalledPartyNumberNatureOfAddress() {
        return new IsupNatureOfAddressIndicatorType (this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyNumberNatureOfAddress().getCode());
    }

    @Override
    public String getCalledPartyNumberNatureOfAddress_Value(){
        return new IsupNatureOfAddressIndicatorType (this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyNumberNatureOfAddress().getCode()).toString();
    }

    @Override
    public void setCalledPartyNumberNatureOfAddress(IsupNatureOfAddressIndicatorType val) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCalledPartyNumberNatureOfAddress(IsupNatureOfAddressIndicator.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putCalledPartyNumberNatureOfAddress(String s) {
        IsupNatureOfAddressIndicatorType x = IsupNatureOfAddressIndicatorType.createInstance(s);
        if (x != null)
            this.setCalledPartyNumberNatureOfAddress(x);
    }

    @Override
    public IsupNumberingPlanIndicatorType getCalledPartyNumberNumberingPlan() {
        return new IsupNumberingPlanIndicatorType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyNumberNumberingPlan().getCode());
    }

    @Override
    public String getCalledPartyNumberNumberingPlan_Value() {
        return new IsupNumberingPlanIndicatorType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getCalledPartyNumberNumberingPlan().getCode()).toString();
    }

    @Override
    public void setCalledPartyNumberNumberingPlan(IsupNumberingPlanIndicatorType val) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setCalledPartyNumberNumberingPlan(IsupNumberingPlanIndicator.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putCalledPartyNumberNumberingPlan(String s) {
        IsupNumberingPlanIndicatorType x = IsupNumberingPlanIndicatorType.createInstance(s);
        if (x != null)
            this.setCalledPartyNumberNumberingPlan(x);
    }

    @Override
    public String getMscAddressAddress() {
        return this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getMscAddressAddress();
    }

    @Override
    public void setMscAddressAddress(String mscAddressAddress) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setMscAddressAddress(mscAddressAddress);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getMscAddressNatureOfAddress() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getMscAddressNatureOfAddress().getIndicator());
    }

    @Override
    public String getMscAddressNatureOfAddress_Value() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getMscAddressNatureOfAddress().getIndicator()).toString();
    }

    @Override
    public void setMscAddressNatureOfAddress(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setMscAddressNatureOfAddress(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putMscAddressNatureOfAddress(String val) {
        AddressNatureType x = AddressNatureType.createInstance(val);
        if (x != null)
            this.setMscAddressNatureOfAddress(x);
    }

    @Override
    public NumberingPlanMapType getMscAddressNumberingPlan() {
           return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getMscAddressNumberingPlan().getIndicator());
    }

    @Override
    public String getMscAddressNumberingPlan_Value() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().getMscAddressNumberingPlan().getIndicator()).toString();
    }

    @Override
    public void setMscAddressNumberingPlan(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getTestCapSsfConfigurationData().setMscAddressNumberingPlan(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putMscAddressNumberingPlan(String s) {
        NumberingPlanMapType x = NumberingPlanMapType.createInstance(s);
        if (x != null)
            this.setMscAddressNumberingPlan(x);
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

    private boolean isMtEventTypeBCSM(EventTypeBCSM eventTypeBCSM) {
        return eventTypeBCSM == EventTypeBCSM.termAttemptAuthorized || eventTypeBCSM == EventTypeBCSM.tBusy
                || eventTypeBCSM == EventTypeBCSM.tNoAnswer || eventTypeBCSM == EventTypeBCSM.tAnswer
                || eventTypeBCSM == EventTypeBCSM.tMidCall || eventTypeBCSM == EventTypeBCSM.tDisconnect
                || eventTypeBCSM == EventTypeBCSM.tAbandon || eventTypeBCSM == EventTypeBCSM.tChangeOfPosition
                || eventTypeBCSM == EventTypeBCSM.tServiceChange;
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
        TestCapSsfConfigurationData ssfConfigData = this.testerHost.getConfigurationData().getTestCapSsfConfigurationData();
        CAPApplicationContext capAppContext = ssfConfigData.getCapApplicationContext().getCAPApplicationContext();

        try {
            CAPDialogCircuitSwitchedCall curDialog = capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
                    capAppContext, this.capMan.createOrigAddress(), this.capMan.createDestAddress());
            currentDialog = curDialog;
            this.testerHost.sendNotif(SOURCE_NAME, "DlgStarted:", "TrId=" + curDialog.getLocalDialogId(), Level.DEBUG);

            int serviceKey = ssfConfigData.getServiceKey() != null ? ssfConfigData.getServiceKey() : 1;
            EventTypeBCSM eventTypeBCSM = ssfConfigData.getIdpEventTypeBCSM();

            CalledPartyBCDNumber calledPartyBCDNumber = null;
            CalledPartyNumberCap calledPartyNumber = null;
            String calledPartyBCDNumberAddr = ssfConfigData.getCalledPartyBCDNumberAddress();

            if (isMtEventTypeBCSM(eventTypeBCSM) || ssfConfigData.isUseCldInsteadOfCldBCDNumber()) {
                // MT & MF calls
                CalledPartyNumber calledPartyNumberIsup = capProvider.getISUPParameterFactory().createCalledPartyNumber();
                String calledPartyNumberAddr = ssfConfigData.getCalledPartyNumberAddress();
                calledPartyNumberIsup.setAddress(calledPartyNumberAddr);
                calledPartyNumberIsup.setNatureOfAddresIndicator(ssfConfigData.getCalledPartyNumberNatureOfAddress().getCode());
                calledPartyNumberIsup.setNumberingPlanIndicator(ssfConfigData.getCalledPartyNumberNumberingPlan().getCode());
                calledPartyNumberIsup.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
                calledPartyNumber = capProvider.getCAPParameterFactory().createCalledPartyNumberCap(
                        calledPartyNumberIsup);
            } else {
                // MO call
                AddressNature addressNature = ssfConfigData.getCalledPartyBCDNumberAddressNature();
                NumberingPlan numberingPlan = ssfConfigData.getCalledPartyBCDNumberNumberingPlan();
                calledPartyBCDNumber = capProvider.getCAPParameterFactory().createCalledPartyBCDNumber(addressNature, numberingPlan, calledPartyBCDNumberAddr);
            }
            String callingPartyNumberAddr = ssfConfigData.getCallingPartyNumberAddress();
            IsupNatureOfAddressIndicator callingPartyNumberNAI = ssfConfigData.getCallingPartyNumberNatureOfAddress();
            IsupNumberingPlanIndicator callingPartyNumberNP = ssfConfigData.getCallingPartyNumberNumberingPlan();
            CallingPartyNumber callingPartyNumberIsup = capProvider.getISUPParameterFactory().createCallingPartyNumber();
            callingPartyNumberIsup.setAddress(callingPartyNumberAddr);
            callingPartyNumberIsup.setNatureOfAddresIndicator(callingPartyNumberNAI.getCode());
            callingPartyNumberIsup.setNumberingPlanIndicator(callingPartyNumberNP.getCode());
            callingPartyNumberIsup.setNumberIncompleteIndicator(CallingPartyNumber._NI_COMPLETE);
            callingPartyNumberIsup.setAddressRepresentationREstrictedIndicator(CallingPartyNumber._APRI_ALLOWED);
            callingPartyNumberIsup.setScreeningIndicator(CallingPartyNumber._SI_NETWORK_PROVIDED);
            CallingPartyNumberCap callingPartyNumber = capProvider.getCAPParameterFactory().createCallingPartyNumberCap(
                    callingPartyNumberIsup);

            String mscAddressAddr = ssfConfigData.getMscAddressAddress();
            AddressNature mscAddressNature = ssfConfigData.getMscAddressNatureOfAddress();
            NumberingPlan mscAddressNP = ssfConfigData.getMscAddressNumberingPlan();
            ISDNAddressString mscAddress = capProvider.getMAPParameterFactory().createISDNAddressString(mscAddressNature, mscAddressNP, mscAddressAddr);

            curDialog.addInitialDPRequest(serviceKey, calledPartyNumber, callingPartyNumber, null, null, null, null, null, null, null, null,
                    null, eventTypeBCSM, null, null, null, null, null, null, null, false, null, null, null, null, null, mscAddress, calledPartyBCDNumber,
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
    public void onActivityTestRequest(ActivityTestRequest request) {
        this.countActivityTest++;
        currentRequestDef += "Rsvd AT;";

        // adding of auto response
        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog != null) {
            // CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

            try {
                curDialog.addActivityTestResponse(request.getInvokeId());

                currentRequestDef += "Sent AT_Resp;";
                this.countActivityTest_Resp++;
                String uData = "";
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: activityTestResponse", uData, Level.DEBUG);
            } catch (CAPException ex) {
                this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending activityTestResponse", ex.toString(),
                        Level.DEBUG);
            }
            needPerformSend = true;
        }
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
    public void onConnectToResourceRequest(ConnectToResourceRequest request) {
        this.countConnectToResource++;
        currentRequestDef += "Rsvd CTR;";
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
        this.countFurnishChargingInformation++;
        currentRequestDef += "Rsvd FCI;";
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
    public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest request) {
        this.countPromptAndCollectUserInformation++;
        currentRequestDef += "Rsvd PC;";

        // adding of auto response
        CAPDialogCircuitSwitchedCall curDialog = currentDialog;
        if (curDialog != null) {
            CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();

            try {
                GenericDigits genericDigits = capProvider.getISUPParameterFactory().createGenericDigits();
                genericDigits.setEncodedDigits(new byte[] { 0x13, 0x34 });
                genericDigits.setEncodingScheme(0);
                genericDigits.setTypeOfDigits(1);
                Digits digitsResponse = capProvider.getCAPParameterFactory().createDigits_GenericDigits(genericDigits);
                curDialog.addPromptAndCollectUserInformationResponse_DigitsResponse(request.getInvokeId(), digitsResponse);

                currentRequestDef += "Sent PC_Resp;";
                this.countPromptAndCollectUserInformation_Resp++;
                String uData = "";
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: promptAndCollectUserInformation", uData, Level.DEBUG);
            } catch (CAPException ex) {
                this.testerHost.sendNotif(SOURCE_NAME, "Exception when sending promptAndCollectUserInformation", ex.toString(),
                        Level.DEBUG);
            }
            needPerformSend = true;
        }
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
            if (dlg.getState() == CAPDialogState.InitialReceived || needPerformSend) {
                needPerformSend = false;
                dlg.send();
            }

            // ........................
//            CAPErrorMessage capErrorMessage = this.capMan.getCAPStack().getCAPProvider().getCAPErrorMessageFactory()
//                    .createCAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
//            CAPErrorMessage capErrorMessage = this.capMan.getCAPStack().getCAPProvider().getCAPErrorMessageFactory()
//                    .createCAPErrorMessageParameterless((long) CAPErrorCode.improperCallerResponse);
//            dlg.sendErrorComponent(1L, capErrorMessage);
//            dlg.send();

            // dlg.abort(CAPUserAbortReason.application_timer_expired);
            // ........................

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
