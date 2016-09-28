package org.mobicents.protocols.ss7.tools.simulator.tests.lcs;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;

import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;

import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;


/**
 * @author falonso@csc.com
 *
 */
public class TestMapLcsServerMan extends TesterBase implements TestMapLcsServerManMBean, Stoppable, MAPServiceLsmListener {

    private static Logger logger = Logger.getLogger(TestMapLcsServerMan.class);

    public static String SOURCE_NAME = "TestMapLcsServerMan";
    private final String name;
    private MapMan mapMan;
    private boolean isStarted = false;
    private MAPProvider mapProvider;
    private MAPServiceLsm mapServiceLsm;
    private MAPParameterFactory mapParameterFactory;
    private int countMapLcsReq = 0;
    private int countMapLcsResp = 0;
    private String currentRequestDef = "";

    public TestMapLcsServerMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
    }

    public boolean start() {

        this.mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        mapServiceLsm = mapProvider.getMAPServiceLsm();
        this.mapParameterFactory = mapProvider.getMAPParameterFactory();
        mapServiceLsm.acivate();
        mapServiceLsm.addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);

        isStarted = true;

        return true;
    }

    public void setTesterHost(TesterHost testerHost) {
        this.testerHost = testerHost;
    }

    public void setMapMan(MapMan val) {
        this.mapMan = val;
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(SOURCE_NAME);
        sb.append(": ");
        sb.append("<br>Count: countMapLcsReq-");
        sb.append(countMapLcsReq);
        sb.append(", countMapLcsResp-");
        sb.append(countMapLcsResp);
        sb.append("</html>");
        return sb.toString();
    }

    @Override
    public void execute() {
    }

    @Override
    public void stop() {
    }

    @Override
    public String performSendRoutingInfoForLCSResponse() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return sendRoutingInfoForLCSResponse();
    }

    @Override
    public String sendRoutingInfoForLCSResponse() {

        return "sendRoutingInfoForLCSResponse called automatically";
    }

    @Override
    public String performSubscriberLocationReportResponse() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return subscriberLocationReportResponse();
    }

        @Override
    public String performSubscriberLocationReportRequest(){
        if (!isStarted) {
            return "The tester is not started";
        }

        return subscriberLocationReportRequest();
    }

    private String subscriberLocationReportRequest(){
        if (mapProvider== null) {
            return "mapProvider is null";
        }


        try {

            MAPApplicationContext appCnt = null;

            appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);

            MAPDialogLsm clientDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), null,
                   this.mapMan.createDestAddress(), null);

            logger.debug("MAPDialogLsm Created");

            // Mandatory parameters
            LCSClientID lcsClientID = mapParameterFactory.createLCSClientID(LCSClientType.plmnOperatorServices, null, null,
                    null, null, null, null);
            LCSEvent lcsEvent = this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getLCSEvent();
            ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getNumberingPlanType(),
                    getNetworkNodeNumberAddress());
            LCSLocationInfo lcsLocationInfo = mapParameterFactory.createLCSLocationInfo(networkNodeNumber, null, null, false,
                    null, null, null, null, null);

            // Conditional parameters
            IMSI imsi = mapParameterFactory.createIMSI(getIMSI());
            ISDNAddressString msisdn = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getNumberingPlanType(),
                    getMSISDN());

            IMEI imei = mapParameterFactory.createIMEI(getIMEI());

            GSNAddress hgmlcAddress = createGSNAddress(getHGMLCAddress());

            CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = mapParameterFactory.createCellGlobalIdOrServiceAreaIdFixedLength(this.getMCC(), this.getMNC(), this.getLAC(), this.getCellId());
            CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai = mapParameterFactory.createCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdFixedLength);
            clientDialogLsm.addSubscriberLocationReportRequest(lcsEvent, lcsClientID, lcsLocationInfo,
                    msisdn, imsi, imei,  null, null, null, getAgeOfLocationEstimate(), null, null,null, getLCSReferenceNumber(), null,
                    null, cellIdOrSai, hgmlcAddress,  null, false, false, null, null, null, null, false, null, null, null);
            logger.debug("Added SubscriberLocationReportRequest");

            clientDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportRequest", createSLRReqData(clientDialogLsm.getLocalDialogId(),lcsEvent,
                    this.getNetworkNodeNumberAddress(), lcsClientID, msisdn,imsi,imei, getAgeOfLocationEstimate(),getLCSReferenceNumber(),cellIdOrSai,hgmlcAddress), Level.INFO);

            currentRequestDef += "Sent SLR Request;";

        }
        catch(MAPException e) {
            return "Exception "+e.toString();
        }

        return "subscriberLocationReportRequest sent";
    }


    @Override
    public void putAddressNature(String val) {
        AddressNatureType x = AddressNatureType.createInstance(val);
        if (x != null)
            this.setAddressNature(x);
    }

    @Override
    public void putNumberingPlanType(String val) {
        NumberingPlanMapType x = NumberingPlanMapType.createInstance(val);
        if (x != null)
            this.setNumberingPlanType(x);
    }

    @Override
    public void putLCSEventType(String val) {
        LCSEventType x = LCSEventType.createInstance(val);
        if (x != null)
            this.setLCSEventType(x);
    }

    @Override
    public String subscriberLocationReportResponse() {

        return "subscriberLocationReportResponse called automatically";
    }

        private String createPSLRequest(
        long dialogId,
        LocationType locationType,
        ISDNAddressString mlcNumber,
        LCSClientID lcsClientID,
        IMSI imsi,
        ISDNAddressString msisdn,
        IMEI imei,
        Integer lcsReferenceNumber,
        Integer lcsServiceTypeID,
        LCSCodeword lcsCodeword,
        LCSPrivacyCheck lcsPrivacyCheck,
        AreaEventInfo areaEventInfo,
        GSNAddress hgmlcAddress,
        boolean moLrShortCircuitIndicator,
        PeriodicLDRInfo periodicLDRInfo){
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId).append("\",\n ");
        sb.append("locationType=\"");
        sb.append(locationType).append("\",\n ");
        sb.append("mlcNumber=\"");
        sb.append(mlcNumber).append("\",\n ");
        sb.append("lcsClientID=\"");
        sb.append(lcsClientID).append("\",\n ");
        sb.append("imsi=\"");
        sb.append(imsi).append("\",\n ");
        sb.append("msisdn=\"");
        sb.append(msisdn).append("\",\n ");
        sb.append("imei=\"");
        sb.append(imei).append("\",\n ");
        sb.append("lcsReferenceNumber=\"");
        sb.append(lcsReferenceNumber).append("\",\n ");
        sb.append("lcsServiceTypeID=\"");
        sb.append(lcsServiceTypeID).append("\",\n ");
        sb.append("lcsCodeword=\"");
        sb.append(lcsCodeword).append("\",\n ");
        sb.append("lcsPrivacyCheck=\"");
        sb.append(lcsPrivacyCheck).append("\",\n ");
        sb.append("areaEventInfo=\"");
        sb.append(areaEventInfo).append("\",\n ");
        sb.append("hgmlcAddress=\"");
        sb.append(hgmlcAddress).append("\",\n ");
        sb.append("moLrShortCircuitIndicator=\"");
        sb.append(moLrShortCircuitIndicator).append("\",\n ");
        sb.append("periodicLDRInfo=\"");
        sb.append(periodicLDRInfo);
        return sb.toString();
    }

    private String createPSLResponse(
        long dialogId,
        ExtGeographicalInformation locationEstimate){

        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId).append("\",\n ");
        sb.append("locationEstimate=\"");
        sb.append(locationEstimate).append("\"");

        return sb.toString();
    }



    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequestIndication) {

        logger.debug("onProvideSubscriberLocationRequest");
        if (!isStarted)
            return;

        this.countMapLcsReq++;

        MAPDialogLsm curDialog = provideSubscriberLocationRequestIndication.getMAPDialog();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: ProvideSubscriberLocationRequest",
                   createPSLRequest(curDialog.getLocalDialogId(),
                    provideSubscriberLocationRequestIndication.getLocationType(),
                    provideSubscriberLocationRequestIndication.getMlcNumber(),
                    provideSubscriberLocationRequestIndication.getLCSClientID(),
                    provideSubscriberLocationRequestIndication.getIMSI(),
                    provideSubscriberLocationRequestIndication.getMSISDN(),
                    provideSubscriberLocationRequestIndication.getIMEI(),
                    provideSubscriberLocationRequestIndication.getLCSReferenceNumber(),
                    provideSubscriberLocationRequestIndication.getLCSServiceTypeID(),
                    provideSubscriberLocationRequestIndication.getLCSCodeword(),
                    provideSubscriberLocationRequestIndication.getLCSPrivacyCheck(),
                    provideSubscriberLocationRequestIndication.getAreaEventInfo(),
                    provideSubscriberLocationRequestIndication.getHGMLCAddress(),
                    provideSubscriberLocationRequestIndication.getMoLrShortCircuitIndicator(),
                    provideSubscriberLocationRequestIndication.getPeriodicLDRInfo()
                    ), Level.INFO);

            PositioningDataInformation geranPositioningData = null;
            UtranPositioningDataInfo utranPositioningData = null;
            Integer ageOfLocationEstimate = 0;
            AddGeographicalInformation additionalLocationEstimate = null;
            MAPExtensionContainer extensionContainer = null;
            boolean deferredMTLRResponseIndicator = false;
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = null;
            boolean saiPresent = false;
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = null;
            VelocityEstimate velocityEstimate = null;
            boolean moLrShortCircuitIndicator = false;
            GeranGANSSpositioningData geranGANSSpositioningData = null;
            UtranGANSSpositioningData utranGANSSpositioningData = null;
            ServingNodeAddress targetServingNodeForHandover = null;

        try {
            ExtGeographicalInformation locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPoint(40.416775,-3.703790);

            curDialog.addProvideSubscriberLocationResponse(
                provideSubscriberLocationRequestIndication.getInvokeId(),
                locationEstimate,
                geranPositioningData,
                utranPositioningData,
                ageOfLocationEstimate,
                additionalLocationEstimate,
                extensionContainer,
                deferredMTLRResponseIndicator,
                cellGlobalIdOrServiceAreaIdOrLAI,
                saiPresent,
                accuracyFulfilmentIndicator,
                velocityEstimate,
                moLrShortCircuitIndicator,
                geranGANSSpositioningData,
                utranGANSSpositioningData,
                targetServingNodeForHandover);

            logger.debug("set addProvideSubscriberLocationResponse");
            curDialog.send();
            logger.debug("addProvideSubscriberLocationResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SendRoutingInfoForLCSResponse",
                createPSLResponse(curDialog.getLocalDialogId(),
                    locationEstimate), Level.INFO);

         } catch (MAPException e) {
            logger.debug("Failed building  SendRoutingInfoForLCS response "+e.toString());
        }
    }

    public void onProvideSubscriberLocationResponse(
            ProvideSubscriberLocationResponse provideSubscriberLocationResponseIndication) {


    }

    public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest subscriberLocationReportRequestIndication) {
        logger.debug("onSubscriberLocationReportRequest");
        if (!isStarted)
            return;

        MAPDialogLsm curDialog = subscriberLocationReportRequestIndication.getMAPDialog();
        String networkNodeNumberAddress = subscriberLocationReportRequestIndication.getLCSLocationInfo().getNetworkNodeNumber().getAddress();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: SubscriberLocationReportRequest",
                   createSLRReqData(curDialog.getLocalDialogId(),networkNodeNumberAddress), Level.INFO);

        MAPParameterFactory mapParameterFactory = this.mapProvider.getMAPParameterFactory();
        ISDNAddressString naEsrd = mapParameterFactory.createISDNAddressString(
                AddressNature.getInstance(getAddressNature().intValue()),
                NumberingPlan.getInstance(getNumberingPlanType().intValue()),
                getNaESRDAddress());

        try {
            curDialog.addSubscriberLocationReportResponse(subscriberLocationReportRequestIndication.getInvokeId(), naEsrd, null, null);
            logger.debug("set addSubscriberLocationReportResponse");
            curDialog.send();
            logger.debug("addSubscriberLocationReportResponse sent");

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportResponse",
                   createSLRResData(curDialog.getLocalDialogId(),getNaESRDAddress() ), Level.INFO);

         } catch (MAPException e) {
            logger.debug("Failed building response "+e.toString());
        }
    }

    @Override
    public String getNaESRDAddress(){
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getNaESRDAddress();
    }

    @Override
    public void setNaESRDAddress(String address) {
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setNaESRDAddress(address);
        this.testerHost.markStore();
    }

    public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse subscriberLocationReportResponseIndication) {
        logger.debug("onSubscriberLocationReportResponse");
        this.countMapLcsResp++;
        MAPDialogLsm curDialog = subscriberLocationReportResponseIndication.getMAPDialog();
        this.testerHost.sendNotif(SOURCE_NAME,
                "Rcvd: SubscriberLocationReportResponse", this
                        .createSLRResData(
                                curDialog.getLocalDialogId(),
                                subscriberLocationReportResponseIndication
                                        .getNaESRD().getAddress()), Level.INFO);

    }

    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest sendRoutingInforForLCSRequestIndication) {
        logger.debug("onSendRoutingInfoForLCSRequest");

        this.countMapLcsReq++;

        if (!isStarted)
            return;

        MAPParameterFactory mapParameterFactory = this.mapProvider.getMAPParameterFactory();

        ISDNAddressString mlc = sendRoutingInforForLCSRequestIndication.getMLCNumber();
        SubscriberIdentity targetMS = sendRoutingInforForLCSRequestIndication.getTargetMS();


        MAPDialogLsm curDialog = sendRoutingInforForLCSRequestIndication.getMAPDialog();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: SendRoutingInfoForLCSRequest",
                   createSRIforLCSReqData(curDialog.getLocalDialogId(),
                   mlc.getAddress(),
                   targetMS.getIMSI().getData()), Level.INFO);


        ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                   AddressNature.getInstance(getAddressNature().intValue()),
                   NumberingPlan.getInstance(getNumberingPlanType().intValue()),
                   getNetworkNodeNumberAddress());

        LCSLocationInfo lcsLocationInfo = mapParameterFactory.createLCSLocationInfo(
                networkNodeNumber, null, null,
                false, null, null, null, null, null);

        try {

            curDialog.addSendRoutingInfoForLCSResponse(
                sendRoutingInforForLCSRequestIndication.getInvokeId(),
                targetMS,
                lcsLocationInfo,
                null,
                null,
                null,
                null,
                null);
            logger.debug("set addSendRoutingInfoForLCSResponse");
            curDialog.send();
            logger.debug("addSendRoutingInfoForLCSResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SendRoutingInfoForLCSResponse",
                   createSRIforLCSResData(curDialog.getLocalDialogId(),
                   networkNodeNumber.getAddress() ), Level.INFO);

         } catch (MAPException e) {
            logger.debug("Failed building response "+e.toString());
        }

    }

    private String createSRIforLCSReqData(long dialogId, String address,String imsi) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", address=\"");
        sb.append(address);
        sb.append("\"");
        sb.append(", imsi=\"");
        sb.append(imsi);
        sb.append("\"");
        return sb.toString();
    }

    private String createSRIforLCSResData(long dialogId, String address) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", networkNodeNumber=\"");
        sb.append(address);
        sb.append("\"");
        return sb.toString();
    }
    private String createSLRReqData(Long dialogId, String networkNodeNumberAddress) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", networkNodeNumber=\"");
        sb.append(networkNodeNumberAddress);
        sb.append("\"");
        return sb.toString();
    }

    private String createSLRResData(long dialogId, String address) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", naESRD=\"");
        sb.append(address);
        sb.append("\"");
        return sb.toString();
    }

    private String createSLRReqData(long dialogId, LCSEvent lcsEvent, String address, LCSClientID lcsClientID, ISDNAddressString msisdn, IMSI imsi, IMEI imei, Integer ageOfLocationEstimate, Integer lcsReferenceNumber, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai, GSNAddress hgmlcAddress) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", lcsEvent=\"");
        sb.append(lcsEvent);
        sb.append("\", networkNodeNumber=\"");
        sb.append(address).append(", ");
        sb.append(lcsClientID).append(", ");
        sb.append(msisdn).append(", ");
        sb.append(imsi).append(", ");
        sb.append("\", ageOfLocationEstimate=\"");
        sb.append(ageOfLocationEstimate);
        sb.append("\", lcsReferenceNumber=\"");
        sb.append(lcsReferenceNumber).append("\", ");
        sb.append(cellIdOrSai).append(", ");
        sb.append(hgmlcAddress);
        return sb.toString();
    }

    public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse sendRoutingInforForLCSResponseIndication){
        logger.debug("onSendRoutingInfoForLCSResponse");

    }

    @Override
    public String getNetworkNodeNumberAddress(){
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getNetworkNodeNumberAddress();
    }

    @Override
    public void setNetworkNodeNumberAddress(String address) {
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setNetworkNodeNumberAddress(address);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getAddressNature().getIndicator());
    }

    @Override
    public NumberingPlanMapType getNumberingPlanType(){
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getNumberingPlanType().getIndicator());
    }

    @Override
    public void setAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void setNumberingPlanType(NumberingPlanMapType val){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setNumberingPlanType(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public String getIMSI() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getIMSI();
    }
    @Override
    public void setIMSI(String imsi){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setIMSI(imsi);
        this.testerHost.markStore();
    }
    @Override
    public String getIMEI() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getIMEI();
    }
    @Override
    public void setIMEI(String imei){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setIMEI(imei);
        this.testerHost.markStore();
    }
    @Override
    public String getMSISDN() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getMSISDN();
    }
    @Override
    public void setMSISDN(String msisdn){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setMSISDN(msisdn);
        this.testerHost.markStore();
    }
    @Override
    public String getHGMLCAddress() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getHGMLCAddress();
    }
    @Override
    public void setHGMLCAddress(String hgmlcAddress){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setHGMLCAddress(hgmlcAddress);
        this.testerHost.markStore();
    }
    @Override
    public Integer getMNC() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getMNC();
    }
    @Override
    public void setMNC(Integer mnc){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setMNC(mnc);
        this.testerHost.markStore();
    }
    @Override
    public Integer getMCC() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getMCC();
    }
    @Override
    public void setMCC(Integer mcc){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setMCC(mcc);
        this.testerHost.markStore();
    }
    @Override
    public Integer getAgeOfLocationEstimate() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getAgeOfLocationEstimate();
    }
    @Override
    public void setAgeOfLocationEstimate(Integer ageLocationEstimate){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setAgeOfLocationEstimate(ageLocationEstimate);
        this.testerHost.markStore();
    }
    @Override
    public Integer getLCSReferenceNumber() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getLCSReferenceNumber();
    }
    @Override
    public void setLCSReferenceNumber(Integer lcsReferenceNumber){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setLCSReferenceNumber(lcsReferenceNumber);
        this.testerHost.markStore();
    }
    @Override
    public LCSEventType getLCSEventType() {
        return new LCSEventType(this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getLCSEvent().getEvent());
    }
    @Override
    public void setLCSEventType(LCSEventType val){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setLCSEvent(LCSEvent.getLCSEvent(val.intValue()));
        this.testerHost.markStore();
    }
    @Override
    public Integer getLAC() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getLAC();
    }
    @Override
    public void setLAC(Integer lac){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setLAC(lac);
        this.testerHost.markStore();
    }
     //TODO move this helper method to constructor type...
    private GSNAddress createGSNAddress(String gsnAddress) throws MAPException {
        try {
            //From InetAddress javadoc "the host name can either be a machine name, such as "java.sun.com", or a textual representation of its IP address.
            //If a literal IP address is supplied, only the validity of the address format is checked".
            InetAddress address = InetAddress.getByName(gsnAddress);
            GSNAddressAddressType addressType = null;
            if (address instanceof Inet4Address) {
                addressType = GSNAddressAddressType.IPv4;
            } else if (address instanceof Inet6Address) {
                addressType = GSNAddressAddressType.IPv6;
            }
            byte[] addressData = address.getAddress();
            return this.mapParameterFactory.createGSNAddress(addressType, addressData);

        } catch (UnknownHostException e) {
            throw new MAPException("Invalid GSNAddress",e);
        }
    }
    @Override
    public Integer getCellId() {
        return this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().getCellId();
    }
    @Override
    public void setCellId(Integer cellId){
        this.testerHost.getConfigurationData().getTestMapLcsServerConfigurationData().setCellId(cellId);
        this.testerHost.markStore();
    }
    @Override
    public String getCurrentRequestDef() {
        return "LastDialog: " + currentRequestDef;
    }


}
