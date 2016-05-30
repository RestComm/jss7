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

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
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
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
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
import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 * @author mnowa
 *
 */
public class TestCheckImeiClientMan extends TesterBase implements TestCheckImeiClientManMBean, Stoppable, MAPDialogListener, MAPServiceMobilityListener {

    public static String SOURCE_NAME = "TestCheckImeiClient";

    private final String name;
    private MapMan mapMan;

    MAPDialogMobility currentDialog = null;

    private boolean isStarted = false;
    private int countCheckImeiReq = 0;
    private int countCheckImeiResp = 0;
    private String currentRequestDef = "";
    private boolean needSendSend = false;
    private boolean needSendClose = false;

    private AtomicInteger nbConcurrentDialogs = new AtomicInteger();
    private MessageSender sender = null;

    public TestCheckImeiClientMan() {
        super(SOURCE_NAME);
        this.name = "???";
    }

    public TestCheckImeiClientMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
    }

    public void setTesterHost(TesterHost testerHost) {
        this.testerHost = testerHost;
    }

    public void setMapMan(MapMan val) {
        this.mapMan = val;
    }

    @Override
    public String closeCurrentDialog() {
        if (!isStarted)
            return "The tester is not started";
        if (this.sender != null)
            return "The tester is not in a manual mode";

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

    @Override
    public String performCheckImeiRequest(String imei) {
        if (!isStarted)
            return "The tester is not started";

        if (this.sender != null)
            return "The tester is not in manual mode";

        MAPDialogMobility curDialog = currentDialog;
        if (curDialog != null)
            return "The current dialog exists. Finish it previousely";

        if (imei == null || imei.equals(""))
            return "Imei is empty";

        currentRequestDef = "";

        return doCheckImeiRequest(imei, true);
    }

    private String doCheckImeiRequest(String imeiString, boolean manualMode) {

        MAPApplicationContextVersion mapAppCtxVersion;
        switch (this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getMapProtocolVersion().intValue()) {
        case MapProtocolVersion.VAL_MAP_V1:
            mapAppCtxVersion = MAPApplicationContextVersion.version1;
            break;
        case MapProtocolVersion.VAL_MAP_V2:
            mapAppCtxVersion = MAPApplicationContextVersion.version2;
            break;
        default:
            mapAppCtxVersion = MAPApplicationContextVersion.version3;
            break;
        }
        MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext, mapAppCtxVersion);

        try {
            MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

            MAPDialogMobility dialog = mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext,
                    this.mapMan.createOrigAddress(), null,
                    this.mapMan.createDestAddress(), null);

            if (manualMode)
                currentDialog = dialog;

            MAPParameterFactory mapParameterFactory = mapProvider.getMAPParameterFactory();

            IMEI imei = mapParameterFactory.createIMEI(imeiString);
            RequestedEquipmentInfo requestedEquipmentInfo = mapParameterFactory.createRequestedEquipmentInfo(true, false);
            MAPExtensionContainer extensionContainer = null;
            //MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

            dialog.addCheckImeiRequest(imei, requestedEquipmentInfo, extensionContainer);

            dialog.send();

            this.countCheckImeiReq++;

            if (manualMode || !this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().isOneNotificationFor100Dialogs()) {

                currentRequestDef += "Sent CheckImeiRequest: " + imei + ";";
                String data = createCheckImeiReqData(dialog.getLocalDialogId(), imeiString);
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: CheckImeiRequest: " + imei, data, Level.DEBUG);

            } else if (!manualMode && this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().isOneNotificationFor100Dialogs()) {

                if (countCheckImeiReq %100 == 0) {
                    currentRequestDef += "Sent: CheckImeiRequest: " + countCheckImeiReq + " messages sent;";
                    this.testerHost.sendNotif(SOURCE_NAME, "Sent: CheckImeiRequest: " + countCheckImeiReq + " messages sent", "", Level.DEBUG);
                }
            }
            return "checkImeiRequest has been sent";
        } catch (MAPException ex) {
            return "Exception when sending CheckImeiRequest: " + ex.toString();
        }
    }

    private String createCheckImeiReqData(Long dialogId, String imei) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", imei=\"");
        sb.append(imei);
        sb.append("\"");
        return sb.toString();
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

        nbConcurrentDialogs.decrementAndGet();
        if (this.sender != null) {
            if (nbConcurrentDialogs.get() < this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData()
                    .getMaxConcurrentDialogs() / 2)
                this.sender.notify();
        }
    }

    private class MessageSender implements Runnable {

        private boolean needStop = false;

        public void stop() {
            needStop = true;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            while (true) {
                if (needStop)
                    break;

                if (nbConcurrentDialogs.get() < testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData()
                        .getMaxConcurrentDialogs()) {
                    doCheckImeiRequest(testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData()
                            .getImei(), false);
                    nbConcurrentDialogs.incrementAndGet();
                }

                if (nbConcurrentDialogs.get() >= testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData()
                        .getMaxConcurrentDialogs()) {
                    try {
                        this.wait(100);
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }

    public boolean start() {
        this.countCheckImeiReq = 0;
        this.countCheckImeiResp = 0;
        this.currentRequestDef = "";

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        mapProvider.getMAPServiceMobility().acivate();
        mapProvider.getMAPServiceMobility().addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "CHECK IMEI Client has been started", "", Level.INFO);
        isStarted = true;

        if (this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getCheckImeiClientAction().intValue() == CheckImeiClientAction.VAL_AUTO_SendCheckImeiRequest) {
            nbConcurrentDialogs = new AtomicInteger();
            this.sender = new MessageSender();
            Thread thr = new Thread(this.sender);
            thr.start();
        }

        return true;
    }

    // Stoppable interface methods

    @Override
    public void stop() {
        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        isStarted = false;

        if (this.sender != null) {
            this.sender.stop();
            try {
                this.sender.notify();
            } catch (Exception e) {
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            this.sender = null;
        }

        this.doRemoveDialog();
        mapProvider.getMAPServiceMobility().deactivate();
        mapProvider.getMAPServiceMobility().removeMAPServiceListener(this);
        mapProvider.removeMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "CHECK IMEI Client has been stopped", "", Level.INFO);
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

    // TestCheckImeiClientManMBean interface methods

    @Override
    public String getImei() {
        return this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getImei();
    }

    @Override
    public void setImei(String imei) {
        this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().setImei(imei);
        this.testerHost.markStore();
    }

    @Override
    public MapProtocolVersion getMapProtocolVersion() {
        return this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getMapProtocolVersion();
    }

    @Override
    public String getMapProtocolVersion_Value() {
        return this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getMapProtocolVersion().toString();
    }

    @Override
    public void setMapProtocolVersion(MapProtocolVersion mapVersion) {
        this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().setMapProtocolVersion(mapVersion);
        this.testerHost.markStore();
    }

    @Override
    public void putMapProtocolVersion(String val) {
        MapProtocolVersion x = MapProtocolVersion.createInstance(val);
        if (x != null)
            this.setMapProtocolVersion(x);
    }

    @Override
    public CheckImeiClientAction getCheckImeiClientAction() {
        return this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getCheckImeiClientAction();
    }

    @Override
    public String getCheckImeiClientAction_Value() {
        return this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getCheckImeiClientAction().toString();
    }

    @Override
    public void setCheckImeiClientAction(CheckImeiClientAction val) {
        this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().setCheckImeiClientAction(val);
        this.testerHost.markStore();
    }

    @Override
    public void putCheckImeiClientAction(String val) {
        CheckImeiClientAction x = CheckImeiClientAction.createInstance(val);
        if (x != null)
            this.setCheckImeiClientAction(x);
    }


    @Override
    public int getMaxConcurrentDialogs() {
        return this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getMaxConcurrentDialogs();
    }

    @Override
    public void setMaxConcurrentDialogs(int val) {
        this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().setMaxConcurrentDialogs(val);
        this.testerHost.markStore();
    }

    @Override
    public boolean isOneNotificationFor100Dialogs() {
        return this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().isOneNotificationFor100Dialogs();
    }

    @Override
    public void setOneNotificationFor100Dialogs(boolean val) {
        this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().setOneNotificationFor100Dialogs(val);
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
    public void onCheckImeiResponse(CheckImeiResponse response) {
        if (!isStarted)
            return;

        this.countCheckImeiResp++;

        if (this.sender == null) {
            MAPDialogMobility curDialog = currentDialog;
            if (curDialog != response.getMAPDialog())
                return;

            currentRequestDef += "Rsvd CheckImeiResp=\"" + "equipmentStatus=" + response.getEquipmentStatus() + "\";";
        }

        long invokeId = response.getMAPDialog().getLocalDialogId();

        if (CheckImeiClientAction.VAL_MANUAL_OPERATION == this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getCheckImeiClientAction().intValue()
                || !this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().isOneNotificationFor100Dialogs()) {

            String uData = this.createCheckImeiRespData(invokeId, response);
            this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: CheckImeiResp: " + "equipmentStatus=" + response.getEquipmentStatus(), uData, Level.DEBUG);

        } else if (CheckImeiClientAction.VAL_AUTO_SendCheckImeiRequest == this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().getCheckImeiClientAction().intValue()
                && this.testerHost.getConfigurationData().getTestCheckImeiClientConfigurationData().isOneNotificationFor100Dialogs()) {

            if (countCheckImeiReq %100 == 0) {
                currentRequestDef += "Rcvd: CheckImeiResp: " + countCheckImeiResp + " messages received ("+countCheckImeiReq+" requests sent);";
                this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: CheckImeiResp: " + countCheckImeiResp + " messages received("+countCheckImeiReq+" requests sent)", "", Level.DEBUG);
            }
        }
        this.doRemoveDialog();
    }

    private String createCheckImeiRespData(long dialogId, CheckImeiResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", response=\"");
        sb.append(response);
        sb.append("\"");
        return sb.toString();
    }

    @Override
    public void onCheckImeiRequest(CheckImeiRequest request) {
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
