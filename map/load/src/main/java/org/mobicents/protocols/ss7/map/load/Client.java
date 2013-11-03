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
package org.mobicents.protocols.ss7.map.load;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.ManagementImpl;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * @author amit bhayani
 *
 */
public class Client extends TestHarness {

    private static Logger logger = Logger.getLogger(Client.class);

    // TCAP
    private TCAPStack tcapStack;

    // MAP
    private MAPStackImpl mapStack;
    private MAPProvider mapProvider;

    // SCCP
    private SccpStackImpl sccpStack;
    private SccpResource sccpResource;

    // M3UA
    private M3UAManagementImpl clientM3UAMgmt;

    // SCTP
    private ManagementImpl sctpManagement;

    // a ramp-up period is required for performance testing.
    int endCount = -100;

    AtomicInteger nbConcurrentDialogs = new AtomicInteger(0);

    volatile long start = 0L;

    protected void initializeStack(IpChannelType ipChannelType) throws Exception {

        this.initSCTP(ipChannelType);

        // Initialize M3UA first
        this.initM3UA();

        // Initialize SCCP
        this.initSCCP();

        // Initialize TCAP
        this.initTCAP();

        // Initialize MAP
        this.initMAP();

        // FInally start ASP
        // Set 5: Finally start ASP
        this.clientM3UAMgmt.startAsp("ASP1");
    }

    private void initSCTP(IpChannelType ipChannelType) throws Exception {
        this.sctpManagement = new ManagementImpl("Client");
        this.sctpManagement.setSingleThread(true);
        this.sctpManagement.setConnectDelay(10000);
        this.sctpManagement.start();
        this.sctpManagement.removeAllResourses();

        // 1. Create SCTP Association
        sctpManagement.addAssociation(CLIENT_IP, CLIENT_PORT, SERVER_IP, SERVER_PORT, CLIENT_ASSOCIATION_NAME, ipChannelType,
                null);
    }

    private void initM3UA() throws Exception {
        this.clientM3UAMgmt = new M3UAManagementImpl("Client");
        this.clientM3UAMgmt.setTransportManagement(this.sctpManagement);
        this.clientM3UAMgmt.start();
        this.clientM3UAMgmt.removeAllResourses();

        // m3ua as create rc <rc> <ras-name>
        RoutingContext rc = factory.createRoutingContext(new long[] { 100L });
        TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
        this.clientM3UAMgmt.createAs("AS1", Functionality.AS, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, null);

        // Step 2 : Create ASP
        this.clientM3UAMgmt.createAspFactory("ASP1", CLIENT_ASSOCIATION_NAME);

        // Step3 : Assign ASP to AS
        Asp asp = this.clientM3UAMgmt.assignAspToAs("AS1", "ASP1");

        // Step 4: Add Route. Remote point code is 2
        clientM3UAMgmt.addRoute(SERVET_SPC, -1, -1, "AS1");

    }

    private void initSCCP() throws Exception {
        this.sccpStack = new SccpStackImpl("MapLoadClientSccpStack");
        this.sccpStack.setMtp3UserPart(1, this.clientM3UAMgmt);

        this.sccpStack.start();
        this.sccpStack.removeAllResourses();

        this.sccpStack.getSccpResource().addRemoteSpc(0, SERVET_SPC, 0, 0);
        this.sccpStack.getSccpResource().addRemoteSsn(0, SERVET_SPC, SSN, 0, false);

        this.sccpStack.getRouter().addMtp3ServiceAccessPoint(1, 1, CLIENT_SPC, NETWORK_INDICATOR);
        this.sccpStack.getRouter().addMtp3Destination(1, 1, SERVET_SPC, SERVET_SPC, 0, 255, 255);
    }

    private void initTCAP() throws Exception {
        this.tcapStack = new TCAPStackImpl("Test", this.sccpStack.getSccpProvider(), SSN);
        this.tcapStack.setDialogIdleTimeout(60000);
        this.tcapStack.setInvokeTimeout(30000);
        this.tcapStack.setMaxDialogs(2000);
        this.tcapStack.start();
    }

    private void initMAP() throws Exception {

        System.out.println("initMAP");

        // this.mapStack = new MAPStackImpl(this.sccpStack.getSccpProvider(), SSN);
        this.mapStack = new MAPStackImpl("Test", this.tcapStack.getProvider());
        this.mapProvider = this.mapStack.getMAPProvider();

        System.out.println("this.mapProvider = " + this.mapProvider);

        this.mapProvider.addMAPDialogListener(this);
        this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceSupplementary().acivate();

        this.mapStack.start();
    }

    private void initiateUSSD() throws MAPException {

        // System.out.println("initiateUSSD");

        // First create Dialog
        AddressString origRef = this.mapProvider.getMAPParameterFactory().createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "12345");
        AddressString destRef = this.mapProvider.getMAPParameterFactory().createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "67890");
        MAPDialogSupplementary mapDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(
                MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2),
                SCCP_CLIENT_ADDRESS, origRef, SCCP_SERVER_ADDRESS, destRef);

        CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);

        // USSD String: *125*+31628839999#
        // The Charset is null, here we let system use default Charset (UTF-7 as
        // explained in GSM 03.38. However if MAP User wants, it can set its own
        // impl of Charset
        USSDString ussdString = this.mapProvider.getMAPParameterFactory().createUSSDString("*125*+31628839999#", null, null);

        ISDNAddressString msisdn = this.mapProvider.getMAPParameterFactory().createISDNAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

        mapDialog.addProcessUnstructuredSSRequest(ussdDataCodingScheme, ussdString, null, msisdn);

        nbConcurrentDialogs.incrementAndGet();

        // This will initiate the TC-BEGIN with INVOKE component
        mapDialog.send();
    }

    public static void main(String[] args) {

        int noOfCalls = Integer.parseInt(args[0]);
        int noOfConcurrentCalls = Integer.parseInt(args[1]);
        IpChannelType ipChannelType = IpChannelType.SCTP;
        if (args.length >= 3 && args[2].toLowerCase().equals("tcp")) {
            ipChannelType = IpChannelType.TCP;
        } else {
            ipChannelType = IpChannelType.SCTP;
        }

        if (args.length >= 4) {
            TestHarness.CLIENT_IP = args[3];
        }

        if (args.length >= 5) {
            TestHarness.CLIENT_PORT = Integer.parseInt(args[4]);
        }

        if (args.length >= 6) {
            TestHarness.SERVER_IP = args[5];
        }

        if (args.length >= 7) {
            TestHarness.SERVER_PORT = Integer.parseInt(args[6]);
        }

        if (args.length >= 8) {
            TestHarness.CLIENT_SPC = Integer.parseInt(args[7]);
        }

        if (args.length >= 9) {
            TestHarness.SERVET_SPC = Integer.parseInt(args[8]);
        }

        if (args.length >= 10) {
            TestHarness.NETWORK_INDICATOR = Integer.parseInt(args[9]);
        }

        if (args.length >= 11) {
            TestHarness.SERVICE_INIDCATOR = Integer.parseInt(args[10]);
        }

        if (args.length >= 12) {
            TestHarness.SSN = Integer.parseInt(args[11]);
        }

        if (args.length >= 13) {
            TestHarness.ROUTING_CONTEXT = Integer.parseInt(args[12]);
        }

        // logger.info("Number of calls to be completed = " + noOfCalls +
        // " Number of concurrent calls to be maintained = " +
        // noOfConcurrentCalls);

        NDIALOGS = noOfCalls;
        MAXCONCURRENTDIALOGS = noOfConcurrentCalls;

        final Client client = new Client();

        try {
            client.initializeStack(ipChannelType);

            Thread.sleep(20000);

            while (client.endCount < NDIALOGS) {
                while (client.nbConcurrentDialogs.intValue() >= MAXCONCURRENTDIALOGS) {

                    // logger.warn("Number of concurrent MAP dialog's = " +
                    // client.nbConcurrentDialogs.intValue()
                    // + " Waiting for max dialog count to go down!");

                    synchronized (client) {
                        try {
                            client.wait();
                        } catch (Exception ex) {
                        }
                    }
                }// end of while (client.nbConcurrentDialogs.intValue() >=
                 // MAXCONCURRENTDIALOGS)

                if (client.endCount == 0) {
                    client.start = System.currentTimeMillis();
                    logger.warn("StartTime = " + client.start);
                }

                client.initiateUSSD();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPServiceListener#onErrorComponent
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long,
     * org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage)
     */
    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        logger.error(String.format("onErrorComponent for Dialog=%d and invokeId=%d MAPErrorMessage=%s",
                mapDialog.getLocalDialogId(), invokeId, mapErrorMessage));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPServiceListener#onRejectComponent
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long, org.mobicents.protocols.ss7.tcap.asn.comp.Problem)
     */
    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        logger.error(String.format("onRejectComponent for Dialog=%d and invokeId=%d Problem=%s isLocalOriginated=%s",
                mapDialog.getLocalDialogId(), invokeId, problem, isLocalOriginated));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPServiceListener#onInvokeTimeout
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long)
     */
    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        logger.error(String.format("onInvokeTimeout for Dialog=%d and invokeId=%d", mapDialog.getLocalDialogId(), invokeId));

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onProcessUnstructuredSSRequestIndication(org .mobicents.protocols.ss7.map.
     * api.service.supplementary.ProcessUnstructuredSSRequestIndication)
     */
    @Override
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
        // This error condition. Client should never receive the
        // ProcessUnstructuredSSRequestIndication
        logger.error(String.format("onProcessUnstructuredSSRequestIndication for Dialog=%d and invokeId=%d", procUnstrReqInd
                .getMAPDialog().getLocalDialogId(), procUnstrReqInd.getInvokeId()));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onProcessUnstructuredSSResponseIndication( org.mobicents.protocols.ss7.map
     * .api.service.supplementary.ProcessUnstructuredSSResponseIndication)
     */
    @Override
    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Rx ProcessUnstructuredSSResponseIndication.  USSD String=%s",
                    procUnstrResInd.getUSSDString()));
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onUnstructuredSSRequestIndication(org.mobicents .protocols.ss7.map.api.service
     * .supplementary.UnstructuredSSRequestIndication)
     */
    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Rx UnstructuredSSRequestIndication. USSD String=%s ", unstrReqInd.getUSSDString()));
        }
        MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();

        try {
            CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);

            USSDString ussdString = this.mapProvider.getMAPParameterFactory().createUSSDString("1", null, null);

            AddressString msisdn = this.mapProvider.getMAPParameterFactory().createAddressString(
                    AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

            mapDialog.addUnstructuredSSResponse(unstrReqInd.getInvokeId(), ussdDataCodingScheme, ussdString);
            mapDialog.send();

        } catch (MAPException e) {
            logger.error(String.format("Error while sending UnstructuredSSResponse for Dialog=%d", mapDialog.getLocalDialogId()));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onUnstructuredSSResponseIndication(org.mobicents .protocols.ss7.map.api.service
     * .supplementary.UnstructuredSSResponseIndication)
     */
    @Override
    public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
        // This error condition. Client should never receive the
        // UnstructuredSSResponseIndication
        logger.error(String.format("onUnstructuredSSResponseIndication for Dialog=%d and invokeId=%d", unstrResInd
                .getMAPDialog().getLocalDialogId(), unstrResInd.getInvokeId()));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onUnstructuredSSNotifyRequestIndication(org .mobicents.protocols.ss7.map.api
     * .service.supplementary.UnstructuredSSNotifyRequestIndication)
     */
    @Override
    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
        // This error condition. Client should never receive the
        // UnstructuredSSNotifyRequestIndication
        logger.error(String.format("onUnstructuredSSNotifyRequestIndication for Dialog=%d and invokeId=%d", unstrNotifyInd
                .getMAPDialog().getLocalDialogId(), unstrNotifyInd.getInvokeId()));
    }

    public void onUnstructuredSSNotifyResponseIndication(UnstructuredSSNotifyResponse unstrNotifyInd) {
        // This error condition. Client should never receive the
        // UnstructuredSSNotifyRequestIndication
        logger.error(String.format("onUnstructuredSSNotifyResponseIndication for Dialog=%d and invokeId=%d", unstrNotifyInd
                .getMAPDialog().getLocalDialogId(), unstrNotifyInd.getInvokeId()));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogDelimiter
     * (org.mobicents.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogDelimiter for DialogId=%d", mapDialog.getLocalDialogId()));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogRequest
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, org.mobicents.protocols.ss7.map.api.primitives.AddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.AddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s MAPExtensionContainer=%s",
                    mapDialog.getLocalDialogId(), destReference, origReference, extensionContainer));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogRequestEricsson
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, org.mobicents.protocols.ss7.map.api.primitives.AddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.AddressString, org.mobicents.protocols.ss7.map.api.primitives.IMSI,
     * org.mobicents.protocols.ss7.map.api.primitives.AddressString)
     */
    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            IMSI arg3, AddressString arg4) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s ",
                    mapDialog.getLocalDialogId(), destReference, origReference));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogAccept( org.mobicents.protocols.ss7.map.api.MAPDialog,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogAccept for DialogId=%d MAPExtensionContainer=%s", mapDialog.getLocalDialogId(),
                    extensionContainer));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogReject( org.mobicents.protocols.ss7.map.api.MAPDialog,
     * org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason, org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError,
     * org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
        logger.error(String.format(
                "onDialogReject for DialogId=%d MAPRefuseReason=%s ApplicationContextName=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), refuseReason, alternativeApplicationContext, extensionContainer));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogUserAbort
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        logger.error(String.format("onDialogUserAbort for DialogId=%d MAPUserAbortChoice=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), userReason, extensionContainer));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogProviderAbort
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason,
     * org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        logger.error(String.format(
                "onDialogProviderAbort for DialogId=%d MAPAbortProviderReason=%s MAPAbortSource=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), abortProviderReason, abortSource, extensionContainer));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogClose(org .mobicents.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogClose(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("DialogClose for Dialog=%d", mapDialog.getLocalDialogId()));
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogNotice( org.mobicents.protocols.ss7.map.api.MAPDialog,
     * org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic)
     */
    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        logger.error(String.format("onDialogNotice for DialogId=%d MAPNoticeProblemDiagnostic=%s ",
                mapDialog.getLocalDialogId(), noticeProblemDiagnostic));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogResease
     * (org.mobicents.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogResease for DialogId=%d", mapDialog.getLocalDialogId()));
        }

        int ndialogs = nbConcurrentDialogs.decrementAndGet();

        if (ndialogs > MAXCONCURRENTDIALOGS) {
            logger.warn("Concurrent Dialogs active = " + ndialogs);
        }
        synchronized (this) {
            if (ndialogs < MAXCONCURRENTDIALOGS / 2)
                this.notify();
        }

        this.endCount++;

        if ((this.endCount % 100) == 0) {
            logger.warn("Completed 100 Dialogs");
        }
        if (this.endCount == NDIALOGS) {
            long current = System.currentTimeMillis();
            logger.warn("Current Time = " + current);
            float sec = (float) (current - start) / 1000f;

            logger.warn("Total time in sec = " + sec);
            logger.warn("Thrupt = " + (float) (NDIALOGS / sec));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogTimeout
     * (org.mobicents.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        logger.error(String.format("onDialogTimeout for DialogId=%d", mapDialog.getLocalDialogId()));
    }

    @Override
    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
        // TODO Auto-generated method stub

    }

}
