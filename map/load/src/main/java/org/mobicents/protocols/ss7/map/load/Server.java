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

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.ManagementImpl;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
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
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * @author amit bhayani
 *
 */
public class Server extends TestHarness {

    private static Logger logger = Logger.getLogger(Server.class);

    // MAP
    private MAPStackImpl mapStack;
    private MAPProvider mapProvider;

    // SCCP
    private SccpStackImpl sccpStack;
    private SccpResource sccpResource;

    // M3UA
    private M3UAManagementImpl serverM3UAMgmt;

    // SCTP
    private ManagementImpl sctpManagement;

    protected void initializeStack(IpChannelType ipChannelType) throws Exception {

        this.initSCTP(ipChannelType);

        // Initialize M3UA first
        this.initM3UA();

        // Initialize SCCP
        this.initSCCP();

        // Initialize MAP
        this.initMAP();

        // 7. Start ASP
        serverM3UAMgmt.startAsp("RASP1");
    }

    private void initSCTP(IpChannelType ipChannelType) throws Exception {
        this.sctpManagement = new ManagementImpl("Server");
        this.sctpManagement.setSingleThread(true);
        this.sctpManagement.setConnectDelay(10000);
        this.sctpManagement.start();
        this.sctpManagement.removeAllResourses();

        // 1. Create SCTP Server
        sctpManagement.addServer(SERVER_NAME, SERVER_IP, SERVER_PORT, ipChannelType, null);

        // 2. Create SCTP Server Association
        sctpManagement.addServerAssociation(CLIENT_IP, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);

        // 3. Start Server
        sctpManagement.startServer(SERVER_NAME);
    }

    private void initM3UA() throws Exception {
        this.serverM3UAMgmt = new M3UAManagementImpl("Server");
        this.serverM3UAMgmt.setTransportManagement(this.sctpManagement);
        this.serverM3UAMgmt.start();
        this.serverM3UAMgmt.removeAllResourses();

        // Step 1 : Create App Server

        RoutingContext rc = factory.createRoutingContext(new long[] { 100L });
        TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
        As as = this.serverM3UAMgmt.createAs("RAS1", Functionality.SGW, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType,
                1, null);

        // Step 2 : Create ASP
        AspFactory aspFactor = this.serverM3UAMgmt.createAspFactory("RASP1", SERVER_ASSOCIATION_NAME);

        // Step3 : Assign ASP to AS
        Asp asp = this.serverM3UAMgmt.assignAspToAs("RAS1", "RASP1");

        // Step 4: Add Route. Remote point code is 2
        this.serverM3UAMgmt.addRoute(CLIENT_SPC, -1, -1, "RAS1");
    }

    private void initSCCP() throws Exception {
        this.sccpStack = new SccpStackImpl("MapLoadServerSccpStack");
        this.sccpStack.setMtp3UserPart(1, this.serverM3UAMgmt);

        this.sccpStack.start();
        this.sccpStack.removeAllResourses();

        this.sccpStack.getSccpResource().addRemoteSpc(0, CLIENT_SPC, 0, 0);
        this.sccpStack.getSccpResource().addRemoteSsn(0, CLIENT_SPC, SSN, 0, false);

        this.sccpStack.getRouter().addMtp3ServiceAccessPoint(1, 1, SERVET_SPC, NETWORK_INDICATOR);
        this.sccpStack.getRouter().addMtp3Destination(1, 1, CLIENT_SPC, CLIENT_SPC, 0, 255, 255);
    }

    private void initMAP() throws Exception {
        this.mapStack = new MAPStackImpl("Test", this.sccpStack.getSccpProvider(), SSN);
        this.mapProvider = this.mapStack.getMAPProvider();

        this.mapProvider.addMAPDialogListener(this);
        this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceSupplementary().acivate();

        this.mapStack.start();
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

    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            IMSI imsi, AddressString vlr) {
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onProcessUnstructuredSSRequestIndication(org .mobicents.protocols.ss7.map.
     * api.service.supplementary.ProcessUnstructuredSSRequestIndication)
     */
    @Override
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onProcessUnstructuredSSRequestIndication for DialogId=%d", procUnstrReqInd
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            long invokeId = procUnstrReqInd.getInvokeId();

            USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString(
                    "USSD String : Hello World <CR> 1. Balance <CR> 2. Texts Remaining");
            CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0F);
            MAPDialogSupplementary dialog = procUnstrReqInd.getMAPDialog();

            dialog.setUserObject(invokeId);

            ISDNAddressString msisdn = this.mapProvider.getMAPParameterFactory().createISDNAddressString(
                    AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

            dialog.addUnstructuredSSRequest(ussdDataCodingScheme, ussdStrObj, null, msisdn);
            dialog.send();
        } catch (MAPException e) {
            logger.error("Error while sending UnstructuredSSRequest ", e);
        }
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
        // Server shouldn't be getting ProcessUnstructuredSSResponseIndication
        logger.error(String.format("onProcessUnstructuredSSResponseIndication for Dialog=%d and invokeId=%d", procUnstrResInd
                .getMAPDialog().getLocalDialogId(), procUnstrResInd.getInvokeId()));
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
        // Server shouldn't be getting UnstructuredSSRequestIndication
        logger.error(String.format("onUnstructuredSSRequestIndication for Dialog=%d and invokeId=%d", unstrReqInd
                .getMAPDialog().getLocalDialogId(), unstrReqInd.getInvokeId()));
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
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onUnstructuredSSResponseIndication for DialogId=%d", unstrResInd.getMAPDialog()
                    .getLocalDialogId()));
        }
        try {
            USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString("Your balance is 500");
            CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0F);
            MAPDialogSupplementary dialog = unstrResInd.getMAPDialog();

            AddressString msisdn = this.mapProvider.getMAPParameterFactory().createAddressString(
                    AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

            dialog.addProcessUnstructuredSSResponse(((Long) dialog.getUserObject()).longValue(), ussdDataCodingScheme,
                    ussdStrObj);
            dialog.close(false);
        } catch (MAPException e) {
            logger.error("Error while sending UnstructuredSSRequest ", e);
        }
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

    public static void main(String[] args) {
        IpChannelType ipChannelType = IpChannelType.SCTP;
        if (args.length >= 1 && args[0].toLowerCase().equals("tcp")) {
            ipChannelType = IpChannelType.TCP;
        } else {
            ipChannelType = IpChannelType.SCTP;
        }

        if (args.length >= 2) {
            TestHarness.CLIENT_IP = args[1];
        }

        if (args.length >= 3) {
            TestHarness.CLIENT_PORT = Integer.parseInt(args[2]);
        }

        if (args.length >= 4) {
            TestHarness.SERVER_IP = args[3];
        }

        if (args.length >= 5) {
            TestHarness.SERVER_PORT = Integer.parseInt(args[4]);
        }

        if (args.length >= 6) {
            TestHarness.CLIENT_SPC = Integer.parseInt(args[5]);
        }

        if (args.length >= 7) {
            TestHarness.SERVET_SPC = Integer.parseInt(args[6]);
        }

        if (args.length >= 8) {
            TestHarness.NETWORK_INDICATOR = Integer.parseInt(args[7]);
        }

        if (args.length >= 9) {
            TestHarness.SERVICE_INIDCATOR = Integer.parseInt(args[8]);
        }

        if (args.length >= 10) {
            TestHarness.SSN = Integer.parseInt(args[9]);
        }

        if (args.length >= 11) {
            TestHarness.ROUTING_CONTEXT = Integer.parseInt(args[10]);
        }

        final Server server = new Server();
        try {
            server.initializeStack(ipChannelType);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
