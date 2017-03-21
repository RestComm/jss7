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

package org.mobicents.protocols.ss7.tools.simulator.tests.ussd;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.AbsentSubscriberDiagnosticSM;
import org.mobicents.protocols.ss7.map.api.errors.CallBarringCause;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkResource;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ActivateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ActivateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.EraseSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GetPasswordResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPUserAbortChoiceImpl;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.SRIReaction;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestUssdClientMan extends TesterBase implements TestUssdClientManMBean, Stoppable, MAPDialogListener,
        MAPServiceSupplementaryListener, MAPServiceSmsListener {

    public static String SOURCE_NAME = "TestUssdClient";

    private final String name;
    // private TesterHost testerHost;
    private MapMan mapMan;

    private int countProcUnstReq = 0;
    private int countProcUnstResp = 0;
    private int countProcUnstReqNot = 0;
    private int countUnstReq = 0;
    private int countUnstResp = 0;
    private int countUnstNotifReq = 0;
    private int countSriReq = 0;
    private int countSriResp = 0;
    private int countErrSent = 0;
    private MAPDialogSupplementary currentDialog = null;
    private Long invokeId = null;
    private boolean isStarted = false;
    private String currentRequestDef = "";
    private boolean needSendSend = false;
    private boolean needSendClose = false;

    private AtomicInteger nbConcurrentDialogs = new AtomicInteger();
    private MessageSender sender = null;

    private int ussdEmptyDialogBeginFlag;

    public TestUssdClientMan() {
        super(SOURCE_NAME);
        this.name = "???";
    }

    public TestUssdClientMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
    }

    public void setMapMan(MapMan val) {
        this.mapMan = val;
    }

    @Override
    public String getMsisdnAddress() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getMsisdnAddress();
    }

    @Override
    public void setMsisdnAddress(String val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setMsisdnAddress(val);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getMsisdnAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                .getMsisdnAddressNature().getIndicator());
    }

    @Override
    public String getMsisdnAddressNature_Value() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                .getMsisdnAddressNature().getIndicator()).toString();
    }

    @Override
    public void setMsisdnAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                .setMsisdnAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public NumberingPlanMapType getMsisdnNumberingPlan() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                .getMsisdnNumberingPlan().getIndicator());
    }

    @Override
    public String getMsisdnNumberingPlan_Value() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                .getMsisdnNumberingPlan().getIndicator()).toString();
    }

    @Override
    public void setMsisdnNumberingPlan(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                .setMsisdnNumberingPlan(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public int getDataCodingScheme() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getDataCodingScheme();
    }

    @Override
    public void setDataCodingScheme(int val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setDataCodingScheme(val);
        this.testerHost.markStore();
    }

    @Override
    public int getAlertingPattern() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getAlertingPattern();
    }

    @Override
    public void setAlertingPattern(int val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setAlertingPattern(val);
        this.testerHost.markStore();
    }

    @Override
    public UssdClientAction getUssdClientAction() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getUssdClientAction();
    }

    @Override
    public String getUssdClientAction_Value() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getUssdClientAction().toString();
    }

    @Override
    public void setUssdClientAction(UssdClientAction val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setUssdClientAction(val);
        this.testerHost.markStore();
    }

    @Override
    public String getAutoRequestString() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getAutoRequestString();
    }

    @Override
    public void setAutoRequestString(String val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setAutoRequestString(val);
        this.testerHost.markStore();
    }

    @Override
    public int getMaxConcurrentDialogs() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getMaxConcurrentDialogs();
    }

    @Override
    public void setMaxConcurrentDialogs(int val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setMaxConcurrentDialogs(val);
        this.testerHost.markStore();
    }

    @Override
    public boolean isOneNotificationFor100Dialogs() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().isOneNotificationFor100Dialogs();
    }

    @Override
    public void setOneNotificationFor100Dialogs(boolean val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setOneNotificationFor100Dialogs(val);
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
    public void putUssdClientAction(String val) {
        UssdClientAction x = UssdClientAction.createInstance(val);
        if (x != null)
            this.setUssdClientAction(x);
    }
    @Override
    public String getSRIResponseImsi() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getSriResponseImsi();
    }

    @Override
    public void setSRIResponseImsi(String val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setSriResponseImsi(val);
        this.testerHost.markStore();
    }

    @Override
    public String getSRIResponseVlr() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getSriResponseVlr();
    }

    @Override
    public void setSRIResponseVlr(String val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setSriResponseVlr(val);
        this.testerHost.markStore();
    }

    @Override
    public SRIReaction getSRIReaction() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getSRIReaction();
    }

    @Override
    public String getSRIReaction_Value() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getSRIReaction().toString();
    }

    @Override
    public void setSRIReaction(SRIReaction val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setSRIReaction(val);
        this.testerHost.markStore();
    }

    @Override
    public boolean isReturn20PersDeliveryErrors() {
        return this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().isReturn20PersDeliveryErrors();
    }

    @Override
    public void setReturn20PersDeliveryErrors(boolean val) {
        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().setReturn20PersDeliveryErrors(val);
        this.testerHost.markStore();
    }

    @Override
    public void putSRIReaction(String val) {
        SRIReaction x = SRIReaction.createInstance(val);
        if (x != null)
            this.setSRIReaction(x);
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
        sb.append(", countSriReq-");
        sb.append(countSriReq);
        sb.append(", countSriResp-");
        sb.append(countSriResp);
        sb.append(", countErrSent-");
        sb.append(countErrSent);
        sb.append("</html>");
        return sb.toString();
    }

    public boolean start() {
        this.countSriReq = 0;
        this.countSriResp = 0;
        this.countErrSent = 0;
        this.countProcUnstReq = 0;
        this.countProcUnstResp = 0;
        this.countProcUnstReqNot = 0;
        this.countUnstReq = 0;
        this.countUnstResp = 0;
        this.countUnstNotifReq = 0;
        this.countErrSent = 0;
        this.countErrSent = 0;

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        mapProvider.getMAPServiceSupplementary().acivate();
        mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
        mapProvider.getMAPServiceSms().acivate();
        mapProvider.getMAPServiceSms().addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "USSD Client has been started", "", Level.INFO);
        isStarted = true;

        if (this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getUssdClientAction().intValue() == UssdClientAction.VAL_AUTO_SendProcessUnstructuredSSRequest) {
            nbConcurrentDialogs = new AtomicInteger();
            this.sender = new MessageSender();
            Thread thr = new Thread(this.sender);
            thr.start();
        }

        return true;
    }

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
        mapProvider.getMAPServiceSupplementary().deactivate();
        mapProvider.getMAPServiceSupplementary().removeMAPServiceListener(this);
        mapProvider.removeMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "USSD Client has been stopped", "", Level.INFO);
    }

    @Override
    public void execute() {
    }

    @Override
    public String closeCurrentDialog() {
        if (!isStarted)
            return "The tester is not started";
        if (this.sender != null)
            return "The tester is not in a manual mode";

        MAPDialogSupplementary curDialog = currentDialog;
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
    public String performProcessUnstructuredRequest(String msg) {
        if (!isStarted)
            return "The tester is not started";
        if (this.sender != null)
            return "The tester is not in manual mode";

        MAPDialogSupplementary curDialog = currentDialog;
        if (curDialog != null)
            return "The current dialog exists. Finish it previousely";
        if (msg == null || msg.equals(""))
            return "USSD message is empty";

        currentRequestDef = "";

        return this.doPerformProcessUnstructuredRequest(msg, true);
    }

    private String doPerformProcessUnstructuredRequest(String msg, boolean manualMode) {

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        USSDString ussdString = null;
        try {
            ussdString = mapProvider.getMAPParameterFactory().createUSSDString(
                    msg,
                    new CBSDataCodingSchemeImpl(this.testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                            .getDataCodingScheme()), null);
        } catch (MAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MAPApplicationContext mapUssdAppContext = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        try {
            MAPDialogSupplementary curDialog = mapProvider.getMAPServiceSupplementary().createNewDialog(mapUssdAppContext,
                    this.mapMan.createOrigAddress(), this.mapMan.createOrigReference(), this.mapMan.createDestAddress(),
                    this.mapMan.createDestReference());
            if (manualMode)
                currentDialog = curDialog;
            invokeId = null;

            boolean eriExtTest = false;
//            eriExtTest = true;
            if (eriExtTest) {
                AddressString eriMsisdn = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                        "99000000000001");
                AddressString eriVlrNo = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                        "99000000000002");
                curDialog.addEricssonData(eriMsisdn, eriVlrNo);
            }

            ISDNAddressString msisdn = null;
            if (this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getMsisdnAddress() != null
                    && !this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getMsisdnAddress()
                            .equals("")) {
                msisdn = mapProvider.getMAPParameterFactory().createISDNAddressString(
                        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getMsisdnAddressNature(),
                        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getMsisdnNumberingPlan(),
                        this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getMsisdnAddress());
            }

            AlertingPattern alPattern = null;
            if (this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getAlertingPattern() >= 0
                    && this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getAlertingPattern() <= 255)
                alPattern = new AlertingPatternImpl((byte) this.testerHost.getConfigurationData()
                        .getTestUssdClientConfigurationData().getAlertingPattern());
            curDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(this.testerHost.getConfigurationData()
                    .getTestUssdClientConfigurationData().getDataCodingScheme()), ussdString, alPattern, msisdn);

            curDialog.send();

            if (manualMode)
                currentRequestDef += "Sent procUnstrSsReq=\"" + msg + "\";";
            this.countProcUnstReq++;
            if (this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().isOneNotificationFor100Dialogs()) {
                int i1 = countProcUnstReq / 100;
                if (countProcUnstReqNot < i1) {
                    countProcUnstReqNot = i1;
                    this.testerHost.sendNotif(SOURCE_NAME, "Sent: procUnstrSsReq: " + (countProcUnstReqNot * 100)
                            + " messages sent", "", Level.DEBUG);
                }
            } else {
                String uData = this.createUssdMessageData(curDialog.getLocalDialogId(), this.testerHost.getConfigurationData()
                        .getTestUssdClientConfigurationData().getDataCodingScheme(), msisdn, alPattern);
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: procUnstrSsReq: " + msg, uData, Level.DEBUG);
            }

            return "ProcessUnstructuredSSRequest has been sent";
        } catch (MAPException ex) {
            return "Exception when sending ProcessUnstructuredSSRequest: " + ex.toString();
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

    @Override
    public String performUnstructuredResponse(String msg) {
        if (!isStarted)
            return "The tester is not started";
        if (this.sender != null)
            return "The tester is not ion manual mode";

        MAPDialogSupplementary curDialog = currentDialog;
        if (curDialog == null)
            return "No current dialog exists. Start it previousely";
        if (invokeId == null)
            return "No pending unstructured request";

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
//        if (msg == null || msg.equals(""))
//            return "USSD message is empty";
        USSDString ussdString = null;
        if (msg != null && !msg.equals("")) {
            try {
                ussdString = mapProvider.getMAPParameterFactory().createUSSDString(msg,
                        new CBSDataCodingSchemeImpl(this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getDataCodingScheme()), null);
            } catch (MAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            curDialog.addUnstructuredSSResponse(invokeId, new CBSDataCodingSchemeImpl(this.testerHost.getConfigurationData()
                    .getTestUssdClientConfigurationData().getDataCodingScheme()), ussdString);

            curDialog.send();

            invokeId = null;

            currentRequestDef += "Sent unstrSsResp=\"" + msg + "\";";
            this.countUnstResp++;
            String uData = this.createUssdMessageData(curDialog.getLocalDialogId(), this.testerHost.getConfigurationData()
                    .getTestUssdClientConfigurationData().getDataCodingScheme(), null, null);
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: unstrSsResp: " + msg, uData, Level.DEBUG);

            return "UnstructuredSSResponse has been sent";
        } catch (MAPException ex) {
            return "Exception when sending UnstructuredSSResponse: " + ex.toString();
        }
    }

    @Override
    public String sendUssdBusyResponse() {
        if (!isStarted)
            return "The tester is not started";
        if (this.sender != null)
            return "The tester is not in manual mode";

        MAPDialogSupplementary curDialog = currentDialog;
        if (curDialog == null)
            return "No current dialog exists. Start it previousely";
        if (invokeId == null)
            return "No pending unstructured request";

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

        try {
//            MAPUserAbortChoice mapUserAbortChoice = new MAPUserAbortChoiceImpl();
//            mapUserAbortChoice.setUserSpecificReason();
//            curDialog.abort(mapUserAbortChoice);

            MAPErrorMessage mapErrorMessage = mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageExtensionContainer(
                    (Long) (long) MAPErrorCode.ussdBusy, null);
            curDialog.sendErrorComponent(invokeId, mapErrorMessage);

            curDialog.close(false);

            invokeId = null;

            currentRequestDef += "Sent ussdBusyResp;";
            this.countErrSent++;
            StringBuilder sb = new StringBuilder();
            sb.append("dialogId=");
            sb.append(curDialog.getLocalDialogId());
            sb.append(" unstrSsResp");
            String uData = sb.toString();
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: ussdBusyResp", uData, Level.DEBUG);

            return "UssdBusyResponse has been sent";
        } catch (MAPException ex) {
            return "Exception when sending UssdBusyResponse: " + ex.toString();
        }
    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse ind) {
        if (!isStarted)
            return;

        if (this.sender == null) {
            MAPDialogSupplementary curDialog = currentDialog;
            if (curDialog != ind.getMAPDialog())
                return;
            try {
                currentRequestDef += "procUnstrSsResp=\"" + ind.getUSSDString().getString(null) + "\";";
            } catch (MAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.countProcUnstResp++;
        if (!this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().isOneNotificationFor100Dialogs()) {
            String uData = this.createUssdMessageData(ind.getMAPDialog().getLocalDialogId(), ind.getDataCodingScheme()
                    .getCode(), null, null);
            try {
                this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: procUnstrSsResp: " + ind.getUSSDString().getString(null), uData,
                        Level.DEBUG);
            } catch (MAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.doRemoveDialog();
    }

    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest ind) {
        if (!isStarted)
            return;

        if (currentDialog == null) {
            currentDialog = ind.getMAPDialog();
        }
        MAPDialogSupplementary curDialog = currentDialog;
        if (curDialog != ind.getMAPDialog()) {
            return;
        }

        ussdEmptyDialogBeginFlag = 2;
        invokeId = ind.getInvokeId();

        try {
            currentRequestDef += "Rcvd: unstrSsReq=\"" + ind.getUSSDString().getString(null) + "\";";
        } catch (MAPException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        this.countUnstReq++;
        String uData = this
                .createUssdMessageData(curDialog.getLocalDialogId(), ind.getDataCodingScheme().getCode(), null, null);
        try {
            this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: unstrSsReq: " + ind.getUSSDString().getString(null), uData,
                    Level.DEBUG);
        } catch (MAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest ind) {
        if (!isStarted)
            return;

        if (currentDialog == null) {
            currentDialog = ind.getMAPDialog();
        }
        MAPDialogSupplementary curDialog = currentDialog;
        if (curDialog != ind.getMAPDialog()) {
            return;
        }

        ussdEmptyDialogBeginFlag = 2;
        invokeId = ind.getInvokeId();

        this.countUnstNotifReq++;
        String uData = this.createUssdMessageData(curDialog.getLocalDialogId(), ind.getDataCodingScheme().getCode(), null, null);
        try {
            this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: unstrSsNotify: " + ind.getUSSDString().getString(null), uData,
                    Level.DEBUG);
        } catch (MAPException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            curDialog.addUnstructuredSSNotifyResponse(invokeId);
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: unstrSsNotifyResp", "-", Level.DEBUG);
//            this.needSendClose = true;
            this.needSendSend = true;
        } catch (MAPException e) {
            this.testerHost.sendNotif(SOURCE_NAME,
                    "Exception when invoking addUnstructuredSSNotifyResponse() : " + e.getMessage(), e, Level.ERROR);
        }
    }

    @Override
    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {
        ussdEmptyDialogBeginFlag = 0;
        if (mapDialog.getApplicationContext().getApplicationContextName() == MAPApplicationContextName.networkUnstructuredSsContext)
            ussdEmptyDialogBeginFlag = 1;
    }

    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        if (ussdEmptyDialogBeginFlag == 1)
            // empty USSD begin received - sending back of dialog accept
            needSendSend = true;

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
    public void onDialogRelease(MAPDialog mapDialog) {
        if (this.currentDialog == mapDialog)
            this.doRemoveDialog();

        nbConcurrentDialogs.decrementAndGet();
        if (this.sender != null) {
            if (nbConcurrentDialogs.get() < this.testerHost.getConfigurationData().getTestUssdClientConfigurationData()
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

                if (nbConcurrentDialogs.get() < testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                        .getMaxConcurrentDialogs()) {
                    doPerformProcessUnstructuredRequest(testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                            .getAutoRequestString(), false);
                    nbConcurrentDialogs.incrementAndGet();
                }

                if (nbConcurrentDialogs.get() >= testerHost.getConfigurationData().getTestUssdClientConfigurationData()
                        .getMaxConcurrentDialogs()) {
                    try {
                        this.wait(100);
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }

    @Override
    public void onRegisterSSRequest(RegisterSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRegisterSSResponse(RegisterSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEraseSSRequest(EraseSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEraseSSResponse(EraseSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateSSRequest(ActivateSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateSSResponse(ActivateSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeactivateSSRequest(DeactivateSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeactivateSSResponse(DeactivateSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInterrogateSSRequest(InterrogateSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInterrogateSSResponse(InterrogateSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetPasswordRequest(GetPasswordRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetPasswordResponse(GetPasswordResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRegisterPasswordResponse(RegisterPasswordResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest ind) {
        if (!isStarted)
            return;

        this.countSriReq++;

//        try {
//            FileOutputStream fios = new FileOutputStream("aaa.txt");
//            PrintWriter pw = new PrintWriter(fios);
//            pw.write("");
//            fios.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        ((MAPDialogImpl)ind.getMAPDialog()).getTcapDialog().setRemoteAddress(remoteAddress);

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        MAPDialogSms curDialog = ind.getMAPDialog();
        long invokeId = ind.getInvokeId();

        String uData;
        if (!this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().isOneNotificationFor100Dialogs()) {
            uData = this.createSriData(ind);
            this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: sriReq", uData, Level.DEBUG);
        }

        IMSI imsi = mapProvider.getMAPParameterFactory().createIMSI(
                this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getSriResponseImsi());
        ISDNAddressString networkNodeNumber = mapProvider.getMAPParameterFactory().createISDNAddressString(
                this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getMsisdnAddressNature(),
                this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getMsisdnNumberingPlan(),
                this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getSriResponseVlr());
        LocationInfoWithLMSI li = null;

        try {
            SRIReaction sriReaction = this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().getSRIReaction();
            Random rnd = new Random();
            if (this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().isReturn20PersDeliveryErrors()) {
                int n = rnd.nextInt(5);
                if (n == 0) {
                    n = rnd.nextInt(4);
                    sriReaction = new SRIReaction(n + 2);
                } else {
                    sriReaction = new SRIReaction(SRIReaction.VAL_RETURN_SUCCESS);
                }
            }

            switch (sriReaction.intValue()) {
            case SRIReaction.VAL_RETURN_SUCCESS:
            case SRIReaction.VAL_RETURN_SUCCESS_WITH_LMSI:
                li = mapProvider.getMAPParameterFactory().createLocationInfoWithLMSI(networkNodeNumber, null, null, false, null);
                curDialog.addSendRoutingInfoForSMResponse(invokeId, imsi, li, null, null, null);

                this.countSriResp++;
                if (!this.testerHost.getConfigurationData().getTestUssdClientConfigurationData().isOneNotificationFor100Dialogs()) {
                    uData = this.createSriRespData(curDialog.getLocalDialogId(), imsi, li);
                    this.testerHost.sendNotif(SOURCE_NAME, "Sent: sriResp", uData, Level.DEBUG);
                }
                break;

            case SRIReaction.VAL_ERROR_ABSENT_SUBSCRIBER:
                MAPErrorMessage mapErrorMessage = null;
                switch (curDialog.getApplicationContext().getApplicationContextVersion()) {
                case version1:
                    Boolean mwdSet = null;
                    mapErrorMessage = mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageAbsentSubscriber(mwdSet);
                    break;
                case version2:
                    mapErrorMessage = mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageAbsentSubscriber(null, null);
                    break;
                default:
                    mapErrorMessage = mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageAbsentSubscriberSM(
                            AbsentSubscriberDiagnosticSM.IMSIDetached, null, null);
                    break;
                }

                curDialog.sendErrorComponent(invokeId, mapErrorMessage);

                this.countErrSent++;
                uData = this.createErrorData(curDialog.getLocalDialogId(), (int) invokeId, mapErrorMessage);
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: errAbsSubs", uData, Level.DEBUG);
                break;

            case SRIReaction.VAL_ERROR_CALL_BARRED:
                mapErrorMessage = mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageCallBarred(
                        (long) curDialog.getApplicationContext().getApplicationContextVersion().getVersion(), CallBarringCause.operatorBarring, null, null);
                curDialog.sendErrorComponent(invokeId, mapErrorMessage);

                this.countErrSent++;
                uData = this.createErrorData(curDialog.getLocalDialogId(), (int) invokeId, mapErrorMessage);
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: errCallBarr", uData, Level.DEBUG);
                break;

            case SRIReaction.VAL_ERROR_SYSTEM_FAILURE:
                mapErrorMessage = mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageSystemFailure(
                        (long) curDialog.getApplicationContext().getApplicationContextVersion().getVersion(), NetworkResource.hlr, null, null);
                curDialog.sendErrorComponent(invokeId, mapErrorMessage);

                this.countErrSent++;
                uData = this.createErrorData(curDialog.getLocalDialogId(), (int) invokeId, mapErrorMessage);
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: errSysFail", uData, Level.DEBUG);
                break;
            }

            this.needSendClose = true;

        } catch (MAPException e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking addSendRoutingInfoForSMResponse() : " + e.getMessage(), e, Level.ERROR);
        }
    }

    private String createSriData(SendRoutingInfoForSMRequest ind) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(ind.getMAPDialog().getLocalDialogId());
        sb.append(",\nsriReq=");
        sb.append(ind);

        sb.append(",\nRemoteAddress=");
        sb.append(ind.getMAPDialog().getRemoteAddress());
        sb.append(",\nLocalAddress=");
        sb.append(ind.getMAPDialog().getLocalAddress());

        return sb.toString();
    }

    private String createSriRespData(long dialogId, IMSI imsi, LocationInfoWithLMSI li) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(",\n imsi=");
        sb.append(imsi);
        sb.append(",\n locationInfo=");
        sb.append(li);
        sb.append(",\n");
        return sb.toString();
    }

    private String createErrorData(long dialogId, int invokeId, MAPErrorMessage mapErrorMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(",\n invokeId=");
        sb.append(invokeId);
        sb.append(",\n mapErrorMessage=");
        sb.append(mapErrorMessage);
        sb.append(",\n");
        return sb.toString();
    }

    @Override
    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReadyForSMRequest(ReadyForSMRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReadyForSMResponse(ReadyForSMResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request) {
        // TODO Auto-generated method stub

    }

}
