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

package org.mobicents.protocols.ss7.tools.simulator.tests.ussd;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestUssdServerMan extends TesterBase implements TestUssdServerManMBean, Stoppable, MAPDialogListener,
        MAPServiceSupplementaryListener {

    public static String SOURCE_NAME = "TestUssdServer";

    private final String name;
    // private TesterHost testerHost;
    private MapMan mapMan;

    private int countProcUnstReq = 0;
    private int countProcUnstResp = 0;
    private int countProcUnstRespNot = 0;
    private int countUnstReq = 0;
    private int countUnstResp = 0;
    private int countUnstNotifReq = 0;
    private MAPDialogSupplementary currentDialog = null;
    private Queue<MAPDialogSupplementary> currentDialogQuere = new ConcurrentLinkedQueue<MAPDialogSupplementary>();
    private boolean isStarted = false;
    private String currentRequestDef = "";
    private boolean needSendSend = false;
    private boolean needSendClose = false;

    public TestUssdServerMan() {
        super(SOURCE_NAME);
        this.name = "???";
    }

    public TestUssdServerMan(String name) {
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
    public String getMsisdnAddress() {
        return this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnAddress();
    }

    @Override
    public void setMsisdnAddress(String val) {
        this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().setMsisdnAddress(val);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getMsisdnAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                .getMsisdnAddressNature().getIndicator());
    }

    @Override
    public String getMsisdnAddressNature_Value() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                .getMsisdnAddressNature().getIndicator()).toString();
    }

    @Override
    public void setMsisdnAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                .setMsisdnAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public NumberingPlanMapType getMsisdnNumberingPlan() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                .getMsisdnNumberingPlan().getIndicator());
    }

    @Override
    public String getMsisdnNumberingPlan_Value() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                .getMsisdnNumberingPlan().getIndicator()).toString();
    }

    @Override
    public void setMsisdnNumberingPlan(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                .setMsisdnNumberingPlan(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public int getDataCodingScheme() {
        return this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getDataCodingScheme();
    }

    @Override
    public void setDataCodingScheme(int val) {
        this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().setDataCodingScheme(val);
        this.testerHost.markStore();
    }

    @Override
    public int getAlertingPattern() {
        return this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getAlertingPattern();
    }

    @Override
    public void setAlertingPattern(int val) {
        this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().setAlertingPattern(val);
        this.testerHost.markStore();
    }

    @Override
    public ProcessSsRequestAction getProcessSsRequestAction() {
        return this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getProcessSsRequestAction();
    }

    @Override
    public String getProcessSsRequestAction_Value() {
        return this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getProcessSsRequestAction()
                .toString();
    }

    @Override
    public void setProcessSsRequestAction(ProcessSsRequestAction val) {
        this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().setProcessSsRequestAction(val);
        this.testerHost.markStore();
    }

    @Override
    public String getAutoResponseString() {
        return this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getAutoResponseString();
    }

    @Override
    public void setAutoResponseString(String val) {
        this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().setAutoResponseString(val);
        this.testerHost.markStore();
    }

    @Override
    public String getAutoUnstructured_SS_RequestString() {
        return this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                .getAutoUnstructured_SS_RequestString();
    }

    @Override
    public void setAutoUnstructured_SS_RequestString(String val) {
        this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().setAutoUnstructured_SS_RequestString(val);
        this.testerHost.markStore();
    }

    @Override
    public boolean isOneNotificationFor100Dialogs() {
        return this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().isOneNotificationFor100Dialogs();
    }

    @Override
    public void setOneNotificationFor100Dialogs(boolean val) {
        this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().setOneNotificationFor100Dialogs(val);
        this.testerHost.markStore();
    }

    @Override
    public void putMsisdnAddressNature(String val) {
        AddressNatureType x = AddressNatureType.createInstance(val);
        if (x != null)
            this.setMsisdnAddressNature(x);
    }

    @Override
    public void putMsisdnNumberingPlan(String val) {
        NumberingPlanMapType x = NumberingPlanMapType.createInstance(val);
        if (x != null)
            this.setMsisdnNumberingPlan(x);
    }

    @Override
    public void putProcessSsRequestAction(String val) {
        ProcessSsRequestAction x = ProcessSsRequestAction.createInstance(val);
        if (x != null)
            this.setProcessSsRequestAction(x);
    }

    @Override
    public String getCurrentRequestDef() {
        if (this.currentDialog != null)
            return "CurDialog: " + currentRequestDef;
        else
            return "PrevDialog: " + currentRequestDef;
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(SOURCE_NAME);
        sb.append(": CurDialog=");
        MAPDialogSupplementary curDialog = currentDialog;
        if (curDialog != null)
            sb.append(curDialog.getLocalDialogId());
        else
            sb.append("No");
        sb.append(", Pending dialogs=");
        sb.append(this.currentDialogQuere.size());
        sb.append("<br>Count: processUnstructuredSSRequest-");
        sb.append(countProcUnstReq);
        sb.append(", processUnstructuredSSResponse-");
        sb.append(countProcUnstResp);
        sb.append("<br>unstructuredSSRequest-");
        sb.append(countUnstReq);
        sb.append(", unstructuredSSResponse-");
        sb.append(countUnstResp);
        sb.append(", unstructuredSSNotify-");
        sb.append(countUnstNotifReq);
        sb.append("</html>");
        return sb.toString();
    }

    public boolean start() {
        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        mapProvider.getMAPServiceSupplementary().acivate();
        mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "USSD Server has been started", "", Level.INFO);
        isStarted = true;

        return true;
    }

    @Override
    public void stop() {
        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        isStarted = false;
        this.doRemoveDialog();
        mapProvider.getMAPServiceSupplementary().deactivate();
        mapProvider.getMAPServiceSupplementary().removeMAPServiceListener(this);
        mapProvider.removeMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "USSD Server has been stopped", "", Level.INFO);
    }

    @Override
    public void execute() {
    }

    @Override
    public String closeCurrentDialog() {
        if (isStarted) {
            MAPDialogSupplementary curDialog = currentDialog;
            if (curDialog != null) {
                try {
                    curDialog.close(false);
                    this.doRemoveDialog();
                    return "The current dialog has been closed";
                } catch (MAPException e) {
                    this.doRemoveDialog();
                    return "Exception when closing the current dialog: " + e.toString();
                }
            } else {
                return "No current dialog";
            }
        } else {
            return "The tester is not started";
        }
    }

    private void doRemoveDialog() {
        synchronized (this) {
            currentDialog = null;
            // currentRequestDef = "";

            currentDialog = this.currentDialogQuere.poll();
            if (currentDialog != null) {
                DialogData dd = (DialogData) currentDialog.getUserObject();
                if (dd != null)
                    currentRequestDef = dd.currentRequestDef;
                this.sendRcvdNotice(currentRequestDef, "CurDialog: ", "");
            }
        }
    }

    private String createUssdMessageData(long dialogId, int dataCodingScheme, ISDNAddressString msisdn,
            AlertingPattern alPattern) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(" DataCodingSchema=");
        sb.append(dataCodingScheme);
        sb.append(" ");
        if (msisdn != null) {
            sb.append(msisdn.toString());
            sb.append(" ");
        }
        if (alPattern != null) {
            sb.append(alPattern.toString());
            sb.append(" ");
        }
        return sb.toString();
    }

    public String sendProcessUnstructuredResponse(MAPDialogSupplementary curDialog, String msg, long invokeId) {

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

        USSDString ussdString = null;
        try {
            ussdString = mapProvider.getMAPParameterFactory().createUSSDString(
                    msg,
                    new CBSDataCodingSchemeImpl(this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                            .getDataCodingScheme()), null);
        } catch (MAPException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            curDialog.addProcessUnstructuredSSResponse(invokeId, new CBSDataCodingSchemeImpl(this.testerHost
                    .getConfigurationData().getTestUssdServerConfigurationData().getDataCodingScheme()), ussdString);
        } catch (MAPException e) {
            this.testerHost.sendNotif(SOURCE_NAME,
                    "Exception when invoking addProcessUnstructuredSSResponse() : " + e.getMessage(), e, Level.ERROR);
            return "Exception when sending ProcessUnstructuredSSResponse: " + e.toString();
        }

        currentRequestDef += "procUnstrSsResp=\"" + msg + "\";";
        DialogData dd = (DialogData) curDialog.getUserObject();
        if (dd != null) {
            dd.currentRequestDef = currentRequestDef;
        }
        this.countProcUnstResp++;
        if (this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().isOneNotificationFor100Dialogs()) {
            int i1 = countProcUnstResp / 100;
            if (countProcUnstRespNot < i1) {
                countProcUnstRespNot = i1;
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: procUnstrSsResp: " + (countProcUnstRespNot * 100)
                        + " messages sent", "", Level.DEBUG);
            }
        } else {
            String uData = this.createUssdMessageData(curDialog.getLocalDialogId(), this.testerHost.getConfigurationData()
                    .getTestUssdServerConfigurationData().getDataCodingScheme(), null, null);
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: procUnstrSsResp: " + msg, uData, Level.DEBUG);
        }

        return "ProcessUnstructuredSSResponse has been sent";
    }

    public String sendUnstructuredRequest(MAPDialogSupplementary curDialog, String msg) {

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

        USSDString ussdString = null;
        try {
            ussdString = mapProvider.getMAPParameterFactory().createUSSDString(
                    msg,
                    new CBSDataCodingSchemeImpl(this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                            .getDataCodingScheme()), null);
        } catch (MAPException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ISDNAddressString msisdn = null;
        if (this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnAddress() != null
                && !this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnAddress().equals("")) {
            msisdn = mapProvider.getMAPParameterFactory().createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnAddressNature(),
                    this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnNumberingPlan(),
                    this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnAddress());
        }
        AlertingPattern alPattern = null;
        if (this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getAlertingPattern() >= 0
                && this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getAlertingPattern() <= 255)
            alPattern = new AlertingPatternImpl((byte) this.testerHost.getConfigurationData()
                    .getTestUssdServerConfigurationData().getAlertingPattern());

        try {
            curDialog.addUnstructuredSSRequest(new CBSDataCodingSchemeImpl(this.testerHost.getConfigurationData()
                    .getTestUssdServerConfigurationData().getDataCodingScheme()), ussdString, alPattern, msisdn);
        } catch (MAPException e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking addUnstructuredSSRequest() : " + e.getMessage(), e,
                    Level.ERROR);
            return "Exception when sending UnstructuredSSRequest: " + e.toString();
        }

        currentRequestDef += "unstrSsReq=\"" + msg + "\";";
        DialogData dd = (DialogData) curDialog.getUserObject();
        if (dd != null) {
            dd.currentRequestDef = currentRequestDef;
        }
        this.countUnstReq++;
        String uData = this.createUssdMessageData(curDialog.getLocalDialogId(), this.testerHost.getConfigurationData()
                .getTestUssdServerConfigurationData().getDataCodingScheme(), null, null);
        this.testerHost.sendNotif(SOURCE_NAME, "Sent: unstrSsReq: " + msg, uData, Level.DEBUG);

        return "UnstructuredSSRequest has been sent";
    }

    @Override
    public String performProcessUnstructuredResponse(String msg) {

        if (!isStarted)
            return "The tester is not started";

        MAPDialogSupplementary curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not opened. Open a dialog by UssdClient";

        DialogData dd = (DialogData) curDialog.getUserObject();
        if (dd == null || dd.invokeId == null)
            return "No pending dialog. Open a dialog by UssdClient";
        long invokeId = dd.invokeId;

        if (msg == null || msg.equals(""))
            return "USSD message is empty";

        String res = this.sendProcessUnstructuredResponse(curDialog, msg, invokeId);

        try {
            curDialog.close(false);
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking close() : " + e.getMessage(), e, Level.ERROR);
        }

        return res;
    }

    @Override
    public String performUnstructuredRequest(String msg) {

        if (!isStarted)
            return "The tester is not started";

        MAPDialogSupplementary curDialog = currentDialog;
        if (curDialog == null)
            return "The current dialog does not opened. Open a dialog by UssdClient";

        if (msg == null || msg.equals(""))
            return "USSD message is empty";

        String res = this.sendUnstructuredRequest(curDialog, msg);

        try {
            curDialog.send();
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking send() : " + e.getMessage(), e, Level.ERROR);
        }

        return res;
    }

    @Override
    public String performUnstructuredNotify(String msg) {
        if (!isStarted)
            return "The tester is not started";

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        if (msg == null || msg.equals(""))
            return "USSD message is empty";

        USSDString ussdString = null;
        try {
            ussdString = mapProvider.getMAPParameterFactory().createUSSDString(
                    msg,
                    new CBSDataCodingSchemeImpl(this.testerHost.getConfigurationData().getTestUssdServerConfigurationData()
                            .getDataCodingScheme()), null);
        } catch (MAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MAPApplicationContext mapUssdAppContext = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        ISDNAddressString msisdn = null;
        if (this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnAddress() != null
                && !this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnAddress().equals("")) {
            msisdn = mapProvider.getMAPParameterFactory().createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnAddressNature(),
                    this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnNumberingPlan(),
                    this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getMsisdnAddress());
        }

        AlertingPattern alPattern = null;
        if (this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getAlertingPattern() >= 0
                && this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().getAlertingPattern() <= 255)
            alPattern = new AlertingPatternImpl((byte) this.testerHost.getConfigurationData()
                    .getTestUssdServerConfigurationData().getAlertingPattern());

        try {
            MAPDialogSupplementary dlg = mapProvider.getMAPServiceSupplementary().createNewDialog(mapUssdAppContext,
                    this.mapMan.createOrigAddress(), this.mapMan.createOrigReference(), this.mapMan.createDestAddress(),
                    this.mapMan.createDestReference());
            dlg.addUnstructuredSSNotifyRequest(new CBSDataCodingSchemeImpl(this.testerHost.getConfigurationData()
                    .getTestUssdServerConfigurationData().getDataCodingScheme()), ussdString, alPattern, msisdn);

            dlg.send();

            this.countUnstNotifReq++;
            String uData = this.createUssdMessageData(dlg.getLocalDialogId(), this.testerHost.getConfigurationData()
                    .getTestUssdServerConfigurationData().getDataCodingScheme(), msisdn, alPattern);
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: unstrSsNotify: " + msg, uData, Level.DEBUG);

            return "UnstructuredSSNotify has been sent";
        } catch (MAPException ex) {
            return "Exception when sending UnstructuredSSNotify: " + ex.toString();
        }
    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest ind) {

        if (!isStarted)
            return;

        DialogData dd = (DialogData) ind.getMAPDialog().getUserObject();
        if (dd == null) {
            dd = new DialogData();
            ind.getMAPDialog().setUserObject(dd);
        }

        try {
            dd.currentRequestDef += "procUnstrSsReq=\"" + ind.getUSSDString().getString(null) + "\";";
        } catch (MAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String pref = "PendingDialog: ";
        if (this.currentDialog == ind.getMAPDialog()
                || this.getProcessSsRequestAction().intValue() != ProcessSsRequestAction.VAL_MANUAL_RESPONSE) {
            this.currentRequestDef = dd.currentRequestDef;
            pref = "CurDialog: ";
        }
        this.countProcUnstReq++;
        if (!this.testerHost.getConfigurationData().getTestUssdServerConfigurationData().isOneNotificationFor100Dialogs()) {
            String uData = this.createUssdMessageData(ind.getMAPDialog().getLocalDialogId(), ind.getDataCodingScheme()
                    .getCode(), ind.getMSISDNAddressString(), ind.getAlertingPattern());
            try {
                this.sendRcvdNotice("Rcvd: procUnstrSsReq: " + ind.getUSSDString().getString(null), pref, uData);
            } catch (MAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        switch (this.getProcessSsRequestAction().intValue()) {
            case ProcessSsRequestAction.VAL_MANUAL_RESPONSE: {
                dd.invokeId = ind.getInvokeId();
            }
                break;

            case ProcessSsRequestAction.VAL_AUTO_ProcessUnstructuredSSResponse: {
                String msg = this.getAutoResponseString();
                if (msg == null || msg.equals(""))
                    msg = "???";
                this.sendProcessUnstructuredResponse(ind.getMAPDialog(), msg, ind.getInvokeId());
                this.needSendClose = true;
            }
                break;

            case ProcessSsRequestAction.VAL_AUTO_Unstructured_SS_Request_Then_ProcessUnstructuredSSResponse: {
                String msg = this.getAutoUnstructured_SS_RequestString();
                if (msg == null || msg.equals(""))
                    msg = "???";
                this.sendUnstructuredRequest(ind.getMAPDialog(), msg);
                this.needSendSend = true;
            }
                break;
        }
    }

    private void sendRcvdNotice(String msg, String pref, String uData) {
        this.testerHost.sendNotif(SOURCE_NAME, pref + msg, uData, Level.DEBUG);
    }

    @Override
    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest ind) {
    }

    @Override
    public void onUnstructuredSSResponse(UnstructuredSSResponse ind) {

        if (!isStarted)
            return;

        DialogData dd = (DialogData) ind.getMAPDialog().getUserObject();
        if (dd == null)
            return;

        String str = null;
        try {
//          dd.currentRequestDef += "unstrSsResp=\"" + ind.getUSSDString().getString(null) + "\";";
            USSDString ussdString = ind.getUSSDString();
            if (ussdString != null)
                str = ind.getUSSDString().getString(null);
            dd.currentRequestDef += "unstrSsResp=\"" + str + "\";";
        } catch (MAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (this.currentDialog == ind.getMAPDialog())
            currentRequestDef = dd.currentRequestDef;
        String pref = "CurDialog: ";
        this.countUnstResp++;
        CBSDataCodingScheme dcs = ind.getDataCodingScheme();
        int dstr = -1;
        if (dcs != null)
            dstr = dcs.getCode();
        String uData = this.createUssdMessageData(ind.getMAPDialog().getLocalDialogId(), dstr, null, null);
        this.sendRcvdNotice("Rcvd: unstrSsResp: " + str, pref, uData);

        switch (this.getProcessSsRequestAction().intValue()) {
            case ProcessSsRequestAction.VAL_MANUAL_RESPONSE: {
                if (ind.getMAPDialog() != this.currentDialog)
                    return;
            }
                break;

            case ProcessSsRequestAction.VAL_AUTO_ProcessUnstructuredSSResponse: {
            }
                break;

            case ProcessSsRequestAction.VAL_AUTO_Unstructured_SS_Request_Then_ProcessUnstructuredSSResponse: {
                String msg = this.getAutoResponseString();
                if (msg == null || msg.equals(""))
                    msg = "???";
                this.sendProcessUnstructuredResponse(ind.getMAPDialog(), msg, ind.getInvokeId());
                this.needSendClose = true;
            }
                break;
        }
    }

    @Override
    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        try {
            if (needSendSend) {
                needSendSend = false;
                mapDialog.send();
            }
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking send() : " + e.getMessage(), e, Level.ERROR);
        }
        try {
            if (needSendClose) {
                needSendClose = false;
                mapDialog.close(false);
            }
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking close() : " + e.getMessage(), e, Level.ERROR);
        }
    }

    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {
        synchronized (this) {
            if (mapDialog instanceof MAPDialogSupplementary) {
                MAPDialogSupplementary dlg = (MAPDialogSupplementary) mapDialog;

                if (this.getProcessSsRequestAction().intValue() == ProcessSsRequestAction.VAL_MANUAL_RESPONSE) {
                    MAPDialogSupplementary curDialog = this.currentDialog;
                    if (curDialog == null) {
                        this.currentDialog = dlg;
                    } else {
                        this.currentDialogQuere.add(dlg);
                    }
                }
            }
        }
    }

    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        if (this.currentDialog == mapDialog)
            this.doRemoveDialog();
        else {
            if (this.getProcessSsRequestAction().intValue() != ProcessSsRequestAction.VAL_MANUAL_RESPONSE) {
                DialogData dd = (DialogData) mapDialog.getUserObject();
                if (dd != null) {
                    currentRequestDef = dd.currentRequestDef;
                }
            }
        }
    }

    private class DialogData {
        public Long invokeId;
        public String currentRequestDef = "";
    }
}
