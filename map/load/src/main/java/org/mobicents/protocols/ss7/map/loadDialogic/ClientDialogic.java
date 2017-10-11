/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.map.loadDialogic;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
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
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_DAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_OAImpl;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.ss7.hardware.dialogic.DialogicMtp3UserPart;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ClientDialogic implements MAPDialogListener, MAPServiceSmsListener {

    private static Logger logger;

    // MAP
    private MAPStackImpl mapStack;
    private MAPProvider mapProvider;

    // SCCP
    private SccpStackImpl sccpStack;
    private SccpResource sccpResource;

    // Dialogic
    private DialogicMtp3UserPart dialogic;

    // MTP Details
    protected final int CLIENT_SPC = 2;
    protected final int SERVET_SPC = 1;
    protected final int NETWORK_INDICATOR = 2;
    protected final int SERVICE_INIDCATOR = 3; // SCCP
    protected final int SSN = 8;

    protected final SccpAddress SCCP_CLIENT_ADDRESS = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
            CLIENT_SPC, SSN);
    protected final SccpAddress SCCP_SERVER_ADDRESS = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
            SERVET_SPC, SSN);

    private static int endCount = 0;
    private static int nbConcurrentDialogs = 0;

    private static ClientDialogic client;

    private static int MAXCONCURRENTDIALOGS;

    protected void initializeStack() throws Exception {

        // Initialize Dialogic first
        this.initDialogic();

        // Initialize SCCP
        this.initSCCP();

        // Initialize MAP
        this.initMAP();
    }

    private void initDialogic() throws Exception {

        this.dialogic = new DialogicMtp3UserPart(null);
        this.dialogic.setSourceModuleId(0x3d);
        this.dialogic.setDestinationModuleId(0x22);

        this.dialogic.start();
    }

    private void initSCCP() throws Exception {
        this.sccpStack = new SccpStackImplWrapper("ClientDialogicSccpStack", logger);
        this.sccpStack.setMtp3UserPart(1, this.dialogic);

        this.sccpStack.start();
        this.sccpStack.removeAllResourses();

        this.sccpStack.getSccpResource().addRemoteSpc(0, SERVET_SPC, 0, 0);
        this.sccpStack.getSccpResource().addRemoteSsn(0, SERVET_SPC, SSN, 0, false);

        this.sccpStack.getRouter().addMtp3ServiceAccessPoint(1, 1, CLIENT_SPC, NETWORK_INDICATOR, 0, null);
        this.sccpStack.getRouter().addMtp3Destination(1, 1, SERVET_SPC, SERVET_SPC, 0, 255, 255);
    }

    private void initMAP() throws Exception {

        System.out.println("initMAP");

        this.mapStack = new MAPStackImpl("Test", this.sccpStack.getSccpProvider(), SSN);
        this.mapProvider = this.mapStack.getMAPProvider();

        System.out.println("this.mapProvider = " + this.mapProvider);

        this.mapProvider.addMAPDialogListener(this);
        this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceSms().acivate();

        this.mapStack.start();

        this.mapStack.getMAPProvider().getMAPServiceSupplementary().acivate();
    }

    private void initiateSms() throws MAPException {

        // System.out.println("initiateUSSD");

        // First create Dialog
        MAPDialogSms mapDialog = this.mapProvider.getMAPServiceSms().createNewDialog(
                MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMTRelayContext,
                        MAPApplicationContextVersion.version2), SCCP_CLIENT_ADDRESS, null, SCCP_SERVER_ADDRESS, null);

        IMSIImpl imsi = new IMSIImpl("987654321");
        AddressString serviceCentreAddressOA = new AddressStringImpl(AddressNature.national_significant_number,
                NumberingPlan.ISDN, "123456789");

        SM_RP_DA sm_RP_DA = new SM_RP_DAImpl(imsi);
        SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(serviceCentreAddressOA);
        byte[] sm_RP_UI = new byte[] { 0x2c, 0x09, 0x04, 0x21, 0x43, 0x65, (byte) 0x87, (byte) 0xf9, 0x04, 0x00, 0x11, 0x11,
                0x22, 0x71, 0x50, (byte) 0x93, 0x00, 0x0c, (byte) 0xe7, (byte) 0xf7, (byte) 0x9b, 0x0c, 0x6a, (byte) 0xbf,
                (byte) 0xe5, (byte) 0xee, (byte) 0xb4, (byte) 0xfb, 0x0c };
        // TODO sm_RP_UI is object niw
        mapDialog.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, null, false);

        // nbConcurrentDialogs.incrementAndGet();

        mapDialog.send();

        endCount++;
        incrementNbConcurrentDialogs();
        logger.error("-- smsSent");

    }

    private static int getNbConcurrentDialogs() {
        synchronized (client) {
            return nbConcurrentDialogs;

        }
    }

    private static void incrementNbConcurrentDialogs() {
        synchronized (client) {
            nbConcurrentDialogs++;
        }
    }

    private static void decrementNbConcurrentDialogs() {
        synchronized (client) {
            nbConcurrentDialogs--;
        }
    }

    public static void main(String[] args) {

        // int noOfCalls = Integer.parseInt(args[0]);
        // int noOfConcurrentCalls = Integer.parseInt(args[1]);
        //
        // NDIALOGS = noOfCalls;
        // MAXCONCURRENTDIALOGS = noOfConcurrentCalls;

        // InputStream inStreamLog4j = TestHarness.class.getResourceAsStream("/log4j.properties");
        //
        // System.out.println("Input Stream = " + inStreamLog4j);
        //
        // Properties propertiesLog4j = new Properties();
        // try {
        // propertiesLog4j.load(inStreamLog4j);
        // PropertyConfigurator.configure(propertiesLog4j);
        // } catch (IOException e) {
        // e.printStackTrace();
        // BasicConfigurator.configure();
        // }

        // BasicConfigurator.configure();

        logger = Logger.getLogger(ClientDialogic.class);

        logger.error("-- started");

        client = new ClientDialogic();

        int NDIALOGS = 10;
        MAXCONCURRENTDIALOGS = 5;

        try {
            logger.error("-- beforeInit");

            client.initializeStack();

            logger.error("-- afterInit");

            Thread.sleep(10000);

            while (endCount < NDIALOGS) {
                while (getNbConcurrentDialogs() >= MAXCONCURRENTDIALOGS) {

                    // logger.warn("nbConcurrentInvite = " + client.nbConcurrentDialogs.intValue()
                    // + " Waiting for max CRCX count to go down!");

                    synchronized (client) {
                        try {
                            client.wait();
                        } catch (Exception ex) {
                        }
                    }
                }

                // if (client.endCount == 0) {
                // client.start = System.currentTimeMillis();
                // }

                client.initiateSms();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(600000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRequestEricsson(MAPDialog arg0, AddressString arg1, AddressString arg2, AddressString arg3, AddressString arg4) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogClose(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
        // TODO Auto-generated method stub

        logger.error("onForwardShortMessageRespIndication: " + forwSmRespInd.getMAPDialog().getLocalDialogId());

        decrementNbConcurrentDialogs();
        synchronized (this) {
            if (nbConcurrentDialogs < MAXCONCURRENTDIALOGS / 2)
                this.notify();
        }

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
    public void onMAPMessage(MAPMessage mapMessage) {
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
    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
        // TODO Auto-generated method stub

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
