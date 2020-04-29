package org.restcomm.protocols.ss7.map.load;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.AsState;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingScheme;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

import java.util.concurrent.atomic.AtomicLong;

public class SriClient extends Client implements MAPServiceSmsListener {
    private static final int WAIT_TIME_PER_ATTEMP = 5000;
    private static final int MAX_ATTEMPS = 10;
    private static Logger logger = Logger.getLogger(SriClient.class);
    final RateLimiter rateLimiterObj;
    AtomicLong callsFinished = new AtomicLong(0);
    AtomicLong successSri = new AtomicLong(0);
    AtomicLong mapExceptions = new AtomicLong(0);
    String targetGT;
    String originGT = "12345";
    private volatile long startTime;
    private volatile long stopTime;
    private AtomicLong componentErrors = new AtomicLong(0);
    private AtomicLong timeoutErrors = new AtomicLong(0);
    private AtomicLong rejections = new AtomicLong(0);
    private AtomicLong dialogAbort = new AtomicLong(0);

    SriClient() {
        this.rateLimiterObj = RateLimiter.create(TestHarness.MAXCONCURRENTDIALOGS);
    }

    public static void main(String[] args) {
        try {
            IpChannelType ipChannelType = collectClientArgs(args);

            final SriClient client = new SriClient();
            client.initializeStackNoReport(ipChannelType);
            client.targetGT = "55";
            client.start();
            client.waitFinishAndPrintReport();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed: " + e.toString());
        }
    }

    @Override
    protected void initM3UA() throws Exception {
        this.clientM3UAMgmt = new M3UAManagementImpl("Client", null, null);
        clientM3UAMgmt.setTransportManagement(this.sctpManagement);
        clientM3UAMgmt.setDeliveryMessageThreadCount(TestHarness.MAXCONCURRENTDIALOGS);
        clientM3UAMgmt.start();
        clientM3UAMgmt.removeAllResourses();

        // m3ua as create rc <rc> <ras-name>
        RoutingContext rc = factory.createRoutingContext(new long[]{TestHarness.ROUTING_CONTEXT});
        int override = TrafficModeType.Override;
        TrafficModeType trafficModeType = factory.createTrafficModeType(override);
        clientM3UAMgmt.createAs("AS1", Functionality.IPSP, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, null);

        clientM3UAMgmt.createAspFactory(ASP_NAME, CLIENT_ASSOCIATION_NAME);
        clientM3UAMgmt.assignAspToAs("AS1", ASP_NAME);
        clientM3UAMgmt.addRoute(TestHarness.SERVET_SPC, TestHarness.CLIENT_SPC, TestHarness.SERVICE_INIDCATOR, "AS1", override);
    }

    private void waitFinishAndPrintReport() throws InterruptedException {
        while (true) {
            if (this.callsFinished.get() == TestHarness.NDIALOGS) {
                this.shutdown();
                break;
            }
            Thread.sleep(20000);
        }
        this.printReport();
        System.exit(0);
    }

    void printReport() {
        long totalErrors = this.mapExceptions.get() + this.timeoutErrors.get() + this.rejections.get() + this.componentErrors.get() + dialogAbort.get();
        String msg = String.format("Finished calls: %d;\nTotal successSri calls: %d;\nTotal errors: %d;\nTotal time seconds: %f",
                this.callsFinished.get(),
                this.successSri.get(),
                totalErrors,
                (stopTime - startTime) / 1000.0);
        logger.info(msg);
        if (totalErrors > 0) {
            String detailedErrors = String.format(
                    "Error details:\nMap exceptions: %d;\nTimeout errors: %d;\nRejections: %d;\nComponent errors: %d;\nDialog aborts: %d",
                    this.mapExceptions.get(),
                    this.timeoutErrors.get(),
                    this.rejections.get(),
                    this.componentErrors.get(),
                    this.dialogAbort.get());
            logger.info(detailedErrors);
        }
    }

    void start() {
        this.waitAppServersState(AsState.ACTIVE);

        logger.info("Sending sri req to: " + targetGT);
        logger.info("number of concurrent calls == " + TestHarness.MAXCONCURRENTDIALOGS + "; Number of total calls == " + TestHarness.NDIALOGS);
        startTime = System.currentTimeMillis();

        mapProvider.getMAPServiceSms().addMAPServiceListener(this);
        mapProvider.getMAPServiceSms().acivate();

        for (int i = 0; i < TestHarness.NDIALOGS; i++) {
            rateLimiterObj.acquire();

            createDialogAndSentSRIReq();
        }
    }

    private void createDialogAndSentSRIReq() {
        try {
            MAPParameterFactory mapParameterFactory = mapProvider.getMAPParameterFactory();
            AddressString destRef = mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, targetGT);
            AddressString origRef = mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, originGT);
            MAPDialogSms mapDialog = getMapDialogSms(origRef, destRef, MAPApplicationContextName.shortMsgGatewayContext);

            ISDNAddressString msisdn = mapProvider.getMAPParameterFactory().createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, targetGT);

            boolean sm_RP_PRI = true;
            mapDialog.addSendRoutingInfoForSMRequest(msisdn, sm_RP_PRI, origRef, null, false, null, null, null, false, null, false, false, null, null);

            mapDialog.send();
        } catch (MAPException e) {
            e.printStackTrace();
            this.callsFinished.incrementAndGet();
            this.mapExceptions.incrementAndGet();
        }
    }

    protected MAPDialogSms getMapDialogSms(AddressString origRef, AddressString destRef, MAPApplicationContextName context) throws MAPException {
        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);

        SccpAddressImpl sccp_client_address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                gt1, TestHarness.CLIENT_SPC, TestHarness.SSN);
        SccpAddressImpl sccp_server_address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                gt2, TestHarness.SERVET_SPC, TestHarness.SSN);

        MAPApplicationContext appCntx = MAPApplicationContext.getInstance(context, MAPApplicationContextVersion.version3);
        return mapProvider.getMAPServiceSms().createNewDialog(appCntx, sccp_client_address, origRef, sccp_server_address, destRef);
    }

    private void waitAppServersState(AsState asState) {
        try {
            int attempt = MAX_ATTEMPS;
            while (!allAppServersState(asState)) {
                if (attempt < 0) {
                    logger.warn(String.format("Not all AS started after %d seconds", MAX_ATTEMPS * WAIT_TIME_PER_ATTEMP / 1000));
                    this.shutdown();
                    return;
                }

                attempt--;
                logger.warn("Not all ASPs started, continue waiting.");
                Thread.sleep(WAIT_TIME_PER_ATTEMP);
            }
            logger.debug("All asps has started");
        } catch (InterruptedException e) {
            logger.error("Interrupted");
            this.shutdown();
        }
    }

    private boolean allAppServersState(AsState asState) {
        for (As as : this.clientM3UAMgmt.getAppServers()) {
            if (as.getState() != asState) {
                return false;
            }
        }
        return true;
    }

    void shutdown() {
        try {
            logger.info("Shutdown");
            this.clientM3UAMgmt.stopAsp(ASP_NAME);
            waitAppServersState(AsState.DOWN);

            this.mapStack.stop();
            this.sccpStack.stop();
            this.clientM3UAMgmt.stop();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Oh no :(, Error: " + e.toString());
        }
    }

    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        long i = this.callsFinished.incrementAndGet();

        if (i % 2000 == 0) {
            logger.warn("Finished " + i + " calls");
        }

        if (i == TestHarness.NDIALOGS) {
            stopTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {

    }

    @Override
    public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {

    }

    @Override
    public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {

    }

    @Override
    public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {

    }

    @Override
    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {

    }

    @Override
    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {

    }

    @Override
    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {

    }

    @Override
    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
        this.successSri.incrementAndGet();
    }

    @Override
    public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {

    }

    @Override
    public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {

    }

    @Override
    public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd) {

    }

    @Override
    public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {

    }

    @Override
    public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd) {

    }

    @Override
    public void onReadyForSMRequest(ReadyForSMRequest request) {

    }

    @Override
    public void onReadyForSMResponse(ReadyForSMResponse response) {

    }

    @Override
    public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request) {

    }

    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
        this.rejections.incrementAndGet();
    }

    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        dialogAbort.incrementAndGet();
    }

    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        MAPErrorMessageFactory mapErrorMessageFactory = new MAPErrorMessageFactoryImpl();
        MAPErrorMessage messageFromErrorCode = mapErrorMessageFactory.createMessageFromErrorCode(mapErrorMessage.getErrorCode());
        logger.error(messageFromErrorCode.toString());
        this.componentErrors.incrementAndGet();
    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        this.rejections.incrementAndGet();
    }

    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        this.timeoutErrors.incrementAndGet();
    }

    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        this.timeoutErrors.incrementAndGet();
    }

    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        logger.error(String.format("onDialogNotice for DialogId=%d MAPNoticeProblemDiagnostic=%s ",
                mapDialog.getLocalDialogId(), noticeProblemDiagnostic));
    }

    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        logger.error(String.format("onDialogUserAbort for DialogId=%d MAPUserAbortChoice=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), userReason, extensionContainer));
    }
}