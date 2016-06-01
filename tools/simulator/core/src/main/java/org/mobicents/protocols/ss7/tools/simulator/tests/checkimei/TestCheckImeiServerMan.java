/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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


package org.mobicents.protocols.ss7.tools.simulator.tests.checkimei;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.dialog.MAPUserAbortChoiceImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;

/**
 * @author mnowa
 *
 */
public class TestCheckImeiServerMan extends TesterBase implements TestCheckImeiServerManMBean, Stoppable, MAPDialogListener, MAPServiceMobilityListener {

    public static String SOURCE_NAME = "TestCheckImeiServer";

    private final String name;
    private MapMan mapMan;

    MAPDialogMobility currentDialog = null;

    private boolean isStarted = false;
    private int countCheckImeiReq = 0;
    private int countCheckImeiResp = 0;
    private String currentRequestDef = "";
    private boolean needSendSend = false;
    private boolean needSendClose = false;

    public TestCheckImeiServerMan() {
        super(SOURCE_NAME);
        this.name = "???";
    }

    public TestCheckImeiServerMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
    }

    public void setMapMan(MapMan val) {
        this.mapMan = val;
    }

    @Override
    public String closeCurrentDialog() {
        if (!isStarted)
            return "The tester is not started";

        MAPDialogMobility curDialog = currentDialog;
        if (curDialog != null) {
            try {
                MAPUserAbortChoice choice = new MAPUserAbortChoiceImpl();
                choice.setUserSpecificReason();
                curDialog.abort(choice);
                this.doRemoveDialog();
                return "The current dialog has been closed";
            } catch (MAPException e) {
                this.doRemoveDialog();
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

    // Dialog messgaes

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
        if (isLocalOriginated)
            needSendClose = true;
    }

    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        try {
            if (needSendSend) {
                needSendSend = false;
                mapDialog.send();
                return;
            }
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking send() : " + e.getMessage(), e, Level.ERROR);
            return;
        }
        try {
            if (needSendClose) {
                needSendClose = false;
                mapDialog.close(false);
                return;
            }
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking close() : " + e.getMessage(), e, Level.ERROR);
            return;
        }
    }

    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        if (this.currentDialog == mapDialog)
            this.doRemoveDialog();
    }

    public boolean start() {
        this.countCheckImeiReq = 0;
        this.countCheckImeiResp = 0;
        this.currentRequestDef = "";

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        mapProvider.getMAPServiceMobility().acivate();
        mapProvider.getMAPServiceMobility().addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "CHECK IMEI Server has been started", "", Level.INFO);
        isStarted = true;

        return true;
    }

    // Stoppable interface methods

    @Override
    public void stop() {
        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        isStarted = false;

        this.doRemoveDialog();
        mapProvider.getMAPServiceMobility().deactivate();
        mapProvider.getMAPServiceMobility().removeMAPServiceListener(this);
        mapProvider.removeMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "CHECK IMEI Server has been stopped", "", Level.INFO);

    }

    @Override
    public void execute() {
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(SOURCE_NAME);
        sb.append(": CurDialog=");
        MAPDialogMobility curDialog = currentDialog;
        if (curDialog != null)
            sb.append(curDialog.getLocalDialogId());
        else
            sb.append("No");
        sb.append("<br>Count: countCheckImeiReq-");
        sb.append(countCheckImeiReq);
        sb.append(", countCheckImeiResp-");
        sb.append(countCheckImeiResp);
        sb.append("</html>");
        return sb.toString();
    }

    // TestCheckImeiServerManMBean interface methods

    @Override
    public EquipmentStatusType getAutoEquipmentStatus() {
        return new EquipmentStatusType(this.testerHost.getConfigurationData().getTestCheckImeiServerConfigurationData().getAutoEquipmentStatus().getCode());
    }

    @Override
    public String getAutoEquipmentStatus_Value() {
        return new EquipmentStatusType(this.testerHost.getConfigurationData().getTestCheckImeiServerConfigurationData().getAutoEquipmentStatus().getCode()).toString();
    }

    @Override
    public void setAutoEquipmentStatus(EquipmentStatusType equipmentStatusType) {
        this.testerHost.getConfigurationData().getTestCheckImeiServerConfigurationData().setAutoEquipmentStatus(EquipmentStatus.getInstance(equipmentStatusType.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putAutoEquipmentStatus(String val) {
        EquipmentStatusType x = EquipmentStatusType.createInstance(val);
        if (x != null)
            this.setAutoEquipmentStatus(x);
    }

    @Override
    public boolean isOneNotificationFor100Dialogs() {
        return this.testerHost.getConfigurationData().getTestCheckImeiServerConfigurationData().isOneNotificationFor100Dialogs();
    }

    @Override
    public void setOneNotificationFor100Dialogs(boolean val) {
        this.testerHost.getConfigurationData().getTestCheckImeiServerConfigurationData().setOneNotificationFor100Dialogs(val);
        this.testerHost.markStore();
    }

    @Override
    public String getCurrentRequestDef() {
        if (this.currentDialog != null)
            return "CurDialog: " + currentRequestDef;
        else
            return "PrevDialog: " + currentRequestDef;
    }

    // key MAPServiceMobilityListener interface methods


    @Override
    public void onCheckImeiRequest(CheckImeiRequest request) {
        if (!isStarted)
            return;

        currentRequestDef = "";

        this.countCheckImeiReq++;

        MAPDialogMobility curDialog = request.getMAPDialog();

        if (!this.testerHost.getConfigurationData().getTestCheckImeiServerConfigurationData().isOneNotificationFor100Dialogs()) {
            currentRequestDef += "Rcvd: CheckImeiReq: =\"" + request+ "\";";

            String uData = this.createCheckImeiReqData(request);
            this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: CheckImeiReq: " + "imei=" + request.getIMEI(), uData, Level.DEBUG);
        }

        try {

            MAPExtensionContainer extensionContainer = null;
            UESBIIu bmuef = null;
            EquipmentStatus equipmentStatus = this.testerHost.getConfigurationData().getTestCheckImeiServerConfigurationData().getAutoEquipmentStatus();

            curDialog.addCheckImeiResponse(request.getInvokeId(), equipmentStatus, bmuef, extensionContainer);

            this.countCheckImeiResp++;

            this.needSendClose = true;

            if (!this.testerHost.getConfigurationData().getTestCheckImeiServerConfigurationData().isOneNotificationFor100Dialogs()) {
                String uData = this.createCheckImeiRespData(curDialog.getLocalDialogId());
                this.testerHost.sendNotif(SOURCE_NAME, "Sent CheckImeiResponse: " + "equipmentStatus=" + equipmentStatus, uData, Level.DEBUG);
            }


        } catch (MAPException e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking addCheckImeiResponse() : " + e.getMessage(), e, Level.ERROR);

        } finally {
            if (this.testerHost.getConfigurationData().getTestCheckImeiServerConfigurationData().isOneNotificationFor100Dialogs() && (countCheckImeiReq %100 == 0) ) {
                currentRequestDef += "Rcvd: CheckImeiReq: " + countCheckImeiReq + " messages received ("+countCheckImeiResp+" responses sent);";
                this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: CheckImeiReq: " + countCheckImeiReq + " messages received("+countCheckImeiResp+" responses sent)", "", Level.DEBUG);
            }
        }

    }

    private String createCheckImeiReqData(CheckImeiRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(request.getMAPDialog().getLocalDialogId());
        sb.append(", request=\"");
        sb.append(request);
        sb.append("\"");

        sb.append(",\nRemoteAddress=");
        sb.append(request.getMAPDialog().getRemoteAddress());
        sb.append(",\nLocalAddress=");
        sb.append(request.getMAPDialog().getLocalAddress());

        return sb.toString();
    }

    private String createCheckImeiRespData(long dialogId) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        return sb.toString();
    }

    @Override
    public void onCheckImeiResponse(CheckImeiResponse response) {
     // TODO Auto-generated method stub
    }

    // other MAPServiceMobilityListener interface methods

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateLocationResponse(UpdateLocationResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelLocationRequest(CancelLocationRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendIdentificationRequest(SendIdentificationRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendIdentificationResponse(SendIdentificationResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPurgeMSRequest(PurgeMSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPurgeMSResponse(PurgeMSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetRequest(ResetRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRestoreDataRequest(RestoreDataRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRestoreDataResponse(RestoreDataResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse response) {
        // TODO Auto-generated method stub

    }

    public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest request) {

    }

    public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse response) {

    }

    @Override
    public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility ind) {
        // TODO Auto-generated method stub

    }

}
