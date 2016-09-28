package org.mobicents.protocols.ss7.tools.simulator.tests.lcs;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
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

import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;

import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import java.util.ArrayList;
import org.mobicents.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;


/**
 * @author falonso@csc.com
 *
 */
public class TestMapLcsClientMan extends TesterBase implements TestMapLcsClientManMBean, Stoppable, MAPServiceLsmListener {

    private static Logger logger = Logger.getLogger(TestMapLcsClientMan.class);

    public static String SOURCE_NAME = "TestMapLcsClientMan";
    private final String name;
    private MapMan mapMan;
    private boolean isStarted = false;
    private int countMapLcsReq = 0;
    private int countMapLcsResp = 0;
    private String currentRequestDef = "";
    private MAPProvider mapProvider;
    private MAPServiceLsm mapServiceLsm;
    private MAPParameterFactory mapParameterFactory;

    public TestMapLcsClientMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
        this.isStarted = false;
    }

    public boolean start() {

        this.mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        this.mapServiceLsm = mapProvider.getMAPServiceLsm();
        this.mapParameterFactory = mapProvider.getMAPParameterFactory();

        mapServiceLsm.acivate();
        mapServiceLsm.addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);

        isStarted = true;
        this.countMapLcsReq = 0;
        this.countMapLcsResp = 0;
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
        isStarted = false;
        mapProvider.getMAPServiceLsm().deactivate();
        mapProvider.getMAPServiceLsm().removeMAPServiceListener(this);
        mapProvider.removeMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "LCS Client has been stopped", "", Level.INFO);
    }

    @Override
    public String performSendRoutingInfoForLCSRequest(String addressIMSI) {
        if (!isStarted) {
            return "The tester is not started";
        }

        return sendRoutingInfoForLCSRequest(addressIMSI);
    }

    private String sendRoutingInfoForLCSRequest(String addressIMSI) {

        if (mapProvider== null) {

            return "mapProvider is null";

        }


        try {

            MAPApplicationContext appCnt = null;

            appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcGatewayContext,
                MAPApplicationContextVersion.version3);

            MAPDialogLsm clientDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), null,
                   this.mapMan.createDestAddress(), null);

            logger.debug("MAPDialogLsm Created");

            ISDNAddressString mlcNumber = mapParameterFactory.createISDNAddressString(
                AddressNature.getInstance(getAddressNature().intValue()),       // e.g. international_number
                NumberingPlan.getInstance(getNumberingPlanType().intValue()),   // e.g  ISDN
                getNumberingPlan());      // e.g. 11112222

            IMSI imsi = mapParameterFactory.createIMSI(
                addressIMSI);               // e.g. 55555
            SubscriberIdentity targetMS = mapParameterFactory.createSubscriberIdentity(imsi);

            clientDialogLsm.addSendRoutingInfoForLCSRequest(mlcNumber, targetMS, null);

            logger.debug("Added SendRoutingInfoForLCSRequest");

            clientDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SendRoutingInfoForLCSRequest",
                        createSRIforLCSReqData(clientDialogLsm.getLocalDialogId(),getNumberingPlan(),addressIMSI), Level.INFO);

            currentRequestDef += "Sent SRIforLCS Request;";

        }
        catch(MAPException e) {
            return "Exception "+e.toString();
        }

        return "sendRoutingInfoForLCSRequest sent";
    }


    private String createSRIforLCSReqData(long dialogId, String address, String imsi) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", address=\"");
        sb.append(address);
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

    @Override
    public String performSubscriberLocationReportRequest(){
        if (!isStarted) {
            return "The tester is not started";
        }

        return subscriberLocationReportRequest();
    }

    @Override
    public String performProvideSubscriberLocationRequest(){
        if (!isStarted) {
            return "The tester is not started";
        }

        return provideSubscriberLocationRequest();
    }

    private String provideSubscriberLocationRequest()  {
        if (mapProvider== null) {
            return "mapProvider is null";
        }

        try {

            MAPApplicationContext appCnt = null;

            appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);

            TestMapLcsClientConfigurationData configData=this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData();

            MAPDialogLsm clientDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), null,
                   this.mapMan.createDestAddress(), null);

            // Mandatory parameters
            LocationEstimateType locationEstimateType = configData.getLocationEstimateType();
            LocationType locationType = mapParameterFactory.createLocationType(
                   locationEstimateType, // final LocationEstimateType locationEstimateType,
                   null // final DeferredLocationEventType deferredLocationEventType
                   );
            ISDNAddressString mlcNumber = mapParameterFactory.createISDNAddressString(
                AddressNature.getInstance(getAddressNature().intValue()),       // e.g. international_number
                NumberingPlan.getInstance(getNumberingPlanType().intValue()),   // e.g  ISDN
                getNumberingPlan());

            LCSClientID lcsClientID = mapParameterFactory.createLCSClientID(
                    configData.getLcsClientType(),
                    null, null, null, null, null, null);

            // Conditional parameters
            IMSI imsi = mapParameterFactory.createIMSI(getIMSI());
            ISDNAddressString msisdn = mapParameterFactory.createISDNAddressString(
                    configData.getAddressNature(),
                    configData.getNumberingPlanType(),
                    getNetworkNodeNumberAddress());
            GSNAddress hgmlcAddress = createGSNAddress(getHGMLCAddress());

            LCSCodeword lcsCodeword = mapParameterFactory.createLCSCodeword(
                new CBSDataCodingSchemeImpl(getDataCodingScheme()),
                mapParameterFactory.createUSSDString(getCodeWordUSSDString()));

            IMEI imei = mapParameterFactory.createIMEI(getIMEI());

            LCSPrivacyCheck lcsPrivacyCheck = mapParameterFactory.createLCSPrivacyCheck(
                PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(getCallSessionUnrelated().intValue()),
                PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(getCallSessionRelated().intValue())
                );

            ArrayList<Area> areaList = new ArrayList<Area>();

            AreaType areaType = AreaType.getAreaType(getAreaType().intValue());

            AreaIdentification areaIdentification = mapParameterFactory.createAreaIdentification(
                areaType,
                getMCC(),
                getMNC(),
                getLAC(),
                getCellId());

            Area area = mapParameterFactory.createArea(areaType,areaIdentification);

            areaList.add(area);

            AreaDefinition areaDef = mapParameterFactory.createAreaDefinition(areaList);

            AreaEventInfo areaEventInfo = mapParameterFactory.createAreaEventInfo(
                areaDef,
                OccurrenceInfo.oneTimeEvent,
                10
                );

            PeriodicLDRInfo periodicLDRInfo = mapParameterFactory.createPeriodicLDRInfo(getReportingAmmount(),getReportingInterval());

            logger.debug("MAPDialogLsm Created");
            clientDialogLsm.addProvideSubscriberLocationRequest(
            locationType, // Mand round 1 - LocationType locationType,
            mlcNumber, // Mand round 1 - ISDNAddressString mlcNumber,
            lcsClientID, // Mand round 1 - LCSClientID lcsClientID,
            false, // UDefined round 2 - boolean privacyOverride,
            imsi, // Cond round 1 - IMSI imsi,
            msisdn, // Cond round 1 - ISDNAddressString msisdn,
            null, // Cond round 2 - LMSI lmsi,
            imei, // UDefined round 1 - IMEI imei
            null, // Cond round 2 - LCSPriority lcsPriority,
            null,  // Cond round 2 - LCSQoS lcsQoS,
            null, // Cond round 2 - MAPExtensionContainer extensionContainer,
            null, // Cond round 2 -SupportedGADShapes supportedGADShapes,
            getLCSReferenceNumber(), // Cond round 1 - Integer lcsReferenceNumber,
            configData.getLcsServiceTypeID(), // Cond round 1 - Integer lcsServiceTypeID,
            lcsCodeword, // Cond round 1 - LCSCodeword lcsCodeword,
            lcsPrivacyCheck, // Cond round 1 - LCSPrivacyCheck lcsPrivacyCheck,
            areaEventInfo, // Cond round 1 - AreaEventInfo areaEventInfo,
            hgmlcAddress, // Cond round 1 - GSNAddress hgmlcAddress,
            getMoLrShortCircuitIndicator(), // Cond round 1 - boolean moLrShortCircuitIndicator,
            periodicLDRInfo, // Cond round 1 - PeriodicLDRInfo periodicLDRInfo,
            null // Cond round 2 - ReportingPLMNList reportingPLMNList
            );

            logger.debug("Added ProviderSubscriberLocationRequest");

            clientDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: ProviderSubscriberLocationRequest",
                createPSLRequest(
            clientDialogLsm.getLocalDialogId(),
            locationType, // Mand round 1 - LocationType locationType,
            mlcNumber, // Mand round 1 - ISDNAddressString mlcNumber,
            lcsClientID, // Mand round 1 - LCSClientID lcsClientID,
            imsi, // Cond round 1 - IMSI imsi,
            msisdn, // Cond round 1 - ISDNAddressString msisdn,
            imei, // UDefined round 1 - IMEI imei
            getLCSReferenceNumber(), // Cond round 1 - Integer lcsReferenceNumber,
            configData.getLcsServiceTypeID(), // Cond round 1 - Integer lcsServiceTypeID,
            lcsCodeword, // Cond round 1 - LCSCodeword lcsCodeword,
            lcsPrivacyCheck, // Cond round 1 - LCSPrivacyCheck lcsPrivacyCheck,
            areaEventInfo, // Cond round 1 - AreaEventInfo areaEventInfo,
            hgmlcAddress, // Cond round 1 - GSNAddress hgmlcAddress,
            getMoLrShortCircuitIndicator(), // Cond round 1 - boolean moLrShortCircuitIndicator,
            periodicLDRInfo), // Cond round 1 - PeriodicLDRInfo periodicLDRInfo,
            Level.INFO);

            currentRequestDef += "Sent PSL Request;";
        }
        catch(MAPException e) {
            return "Exception "+e.toString();
        }

        return "ProviderSubscriberLocationRequest sent";

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
            LCSEvent lcsEvent = this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getLCSEvent();
            ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getNumberingPlanType(),
                    getNetworkNodeNumberAddress());
            LCSLocationInfo lcsLocationInfo = mapParameterFactory.createLCSLocationInfo(networkNodeNumber, null, null, false,
                    null, null, null, null, null);

            // Conditional parameters
            IMSI imsi = mapParameterFactory.createIMSI(getIMSI());
            ISDNAddressString msisdn = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getNumberingPlanType(),
                    getMSISDN());
            LMSI lmsi = mapParameterFactory.createLMSI(new byte[] { 49, 48, 47, 46 });//TODO make this configurable
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

    // PSL Request
    @Override
    public LocationEstimateTypeEnumerated getLocEstimateType(){
        return new LocationEstimateTypeEnumerated(this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getLocationEstimateType().getType());
    }
    @Override
    public void setLocEstimateType(LocationEstimateTypeEnumerated locEstimate){
        this.testerHost.getConfigurationData().
           getTestMapLcsClientConfigurationData().setLocationEstimateType(LocationEstimateType.getLocationEstimateType(locEstimate.intValue()));
        this.testerHost.markStore();
    }
    @Override
    public Integer getLcsServiceTypeID(){
        return this.testerHost.getConfigurationData().
           getTestMapLcsClientConfigurationData().getLcsServiceTypeID();
    }
    @Override
    public void setLcsServiceTypeID(Integer lcsServiceTypeID){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setLcsServiceTypeID(lcsServiceTypeID);
        this.testerHost.markStore();
    }
    @Override
    public LCSClientTypeEnumerated getLcsClientType() {
        return new LCSClientTypeEnumerated(this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getLcsClientType().getType());
    }
    @Override
    public void setLcsClientType(LCSClientTypeEnumerated val){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setLcsClientType(LCSClientType.getLCSClientType(val.intValue()));
        this.testerHost.markStore();
    }
    @Override
    public void setCodeWordUSSDString(String codeWordUSSDString){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setCodeWordUSSDString(codeWordUSSDString);
    }
    @Override
    public String getCodeWordUSSDString(){
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getCodeWordUSSDString();
    }
    @Override
    public void setCallSessionUnrelated(PrivacyCheckRelatedActionEnumerated val){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setCallSessionUnrelated(PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(val.intValue()));
        this.testerHost.markStore();
    }
    @Override
    public PrivacyCheckRelatedActionEnumerated getCallSessionUnrelated(){
        return new PrivacyCheckRelatedActionEnumerated(this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getCallSessionUnrelated().getAction());
    }
    @Override
    public void setCallSessionRelated(PrivacyCheckRelatedActionEnumerated val){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setCallSessionRelated(PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(val.intValue()));
        this.testerHost.markStore();
    }
    @Override
    public PrivacyCheckRelatedActionEnumerated getCallSessionRelated(){
        return new PrivacyCheckRelatedActionEnumerated(this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getCallSessionRelated().getAction());
    }

    @Override
    public boolean getMoLrShortCircuitIndicator(){
        return this.testerHost.getConfigurationData().
           getTestMapLcsClientConfigurationData().getMoLrShortCircuitIndicator();
    }
    @Override
    public void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setMoLrShortCircuitIndicator(moLrShortCircuitIndicator);
        this.testerHost.markStore();
    }

    @Override
    public LCSEventType getLCSEventType() {
        return new LCSEventType(this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getLCSEvent().getEvent());
    }
    @Override
    public void setLCSEventType(LCSEventType val){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setLCSEvent(LCSEvent.getLCSEvent(val.intValue()));
        this.testerHost.markStore();
    }
    @Override
    public Integer getCellId() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getCellId();
    }
    @Override
    public void setCellId(Integer cellId){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setCellId(cellId);
        this.testerHost.markStore();
    }
    @Override
    public Integer getLAC() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getLAC();
    }
    @Override
    public void setLAC(Integer lac){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setLAC(lac);
        this.testerHost.markStore();
    }

    @Override
    public Integer getMNC() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getMNC();
    }
    @Override
    public void setMNC(Integer mnc){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setMNC(mnc);
        this.testerHost.markStore();
    }
    @Override
    public Integer getMCC() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getMCC();
    }
    @Override
    public void setMCC(Integer mcc){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setMCC(mcc);
        this.testerHost.markStore();
    }
    @Override
    public Integer getAgeOfLocationEstimate() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getAgeOfLocationEstimate();
    }
    @Override
    public void setAgeOfLocationEstimate(Integer ageLocationEstimate){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setAgeOfLocationEstimate(ageLocationEstimate);
        this.testerHost.markStore();
    }
    @Override
    public String getHGMLCAddress() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getHGMLCAddress();
    }
    @Override
    public void setHGMLCAddress(String hgmlcAddress){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setHGMLCAddress(hgmlcAddress);
        this.testerHost.markStore();
    }

    @Override
    public Integer getLCSReferenceNumber() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getLCSReferenceNumber();
    }
    @Override
    public void setLCSReferenceNumber(Integer lcsReferenceNumber){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setLCSReferenceNumber(lcsReferenceNumber);
        this.testerHost.markStore();
    }
    @Override
    public String getMSISDN() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getMSISDN();
    }
    @Override
    public void setMSISDN(String msisdn){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setMSISDN(msisdn);
        this.testerHost.markStore();
    }
    @Override
    public String getIMEI() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getIMEI();
    }
    @Override
    public void setIMEI(String imei){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setIMEI(imei);
        this.testerHost.markStore();
    }
    @Override
    public String getIMSI() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getIMSI();
    }
    @Override
    public void setIMSI(String imsi){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setIMSI(imsi);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getAddressNature().getIndicator());
    }

    @Override
    public void setAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public String getNumberingPlan(){
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getNumberingPlan();
    }

    @Override
    public void setNumberingPlan(String numPlan){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setNumberingPlan(numPlan);
        this.testerHost.markStore();
    }

    @Override
    public NumberingPlanMapType getNumberingPlanType(){
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getNumberingPlanType().getIndicator());
    }

    @Override
    public void setNumberingPlanType(NumberingPlanMapType val){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setNumberingPlanType(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void setAreaType(AreaTypeEnumerated val){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setAreaType(AreaType.getAreaType(val.intValue()));
        this.testerHost.markStore();
    }
    @Override
    public AreaTypeEnumerated getAreaType(){
        return new AreaTypeEnumerated(this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getAreaType().getType());
    }
    @Override
    public void setReportingAmmount(Integer val){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setReportingAmmount(val);
        this.testerHost.markStore();
    }
    @Override
    public Integer getReportingAmmount(){
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getReportingAmmount();
    }
    @Override
    public void setReportingInterval(Integer val){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setReportingInterval(val);
        this.testerHost.markStore();
    }
    @Override
    public Integer getReportingInterval(){
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getReportingInterval();
    }
    @Override
    public void setDataCodingScheme(Integer val){
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setDataCodingScheme(val);
        this.testerHost.markStore();
    }
    @Override
    public Integer getDataCodingScheme(){
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getDataCodingScheme();
    }

    @Override
    public String getCurrentRequestDef() {
        return "LastDialog: " + currentRequestDef;
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


    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequestIndication) {

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

    public void onProvideSubscriberLocationResponse(
            ProvideSubscriberLocationResponse provideSubscriberLocationResponseIndication) {

        logger.debug("onProvideSubscriberLocationResponse");

        MAPDialogLsm curDialog = provideSubscriberLocationResponseIndication.getMAPDialog();

        this.countMapLcsResp++;
        this.testerHost.sendNotif(SOURCE_NAME,
                "Rcvd: ProvideSubscriberLocationResponse", this
                        .createPSLResponse(
                                curDialog.getLocalDialogId(),
                                provideSubscriberLocationResponseIndication
                                        .getLocationEstimate()), Level.INFO);
    }

    public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest subscriberLocationReportRequestIndication) {
        logger.debug("onSubscriberLocationReportRequest");
        this.countMapLcsReq++;
        if (!isStarted)
            return;

        MAPDialogLsm curDialog = subscriberLocationReportRequestIndication.getMAPDialog();
        String networkNodeNumberAddress = subscriberLocationReportRequestIndication.getLCSLocationInfo().getNetworkNodeNumber().getAddress();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: SubscriberLocationReportRequest",
                    createSLRReqData(curDialog.getLocalDialogId(),  subscriberLocationReportRequestIndication.getLCSEvent(),
                    networkNodeNumberAddress,
                    subscriberLocationReportRequestIndication.getLCSClientID(),
                    subscriberLocationReportRequestIndication.getMSISDN(),
                    subscriberLocationReportRequestIndication.getIMSI(),
                    subscriberLocationReportRequestIndication.getIMEI(),
                    subscriberLocationReportRequestIndication.getAgeOfLocationEstimate(),
                    subscriberLocationReportRequestIndication.getLCSReferenceNumber(),
                    subscriberLocationReportRequestIndication.getCellGlobalIdOrServiceAreaIdOrLAI(), //cellIdOrSai,
                    subscriberLocationReportRequestIndication.getHGMLCAddress()), Level.INFO);

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
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportResponse",
                   createSLRResData(curDialog.getLocalDialogId(),getNaESRDAddress() ), Level.INFO);

         } catch (MAPException e) {
            logger.debug("Failed building response "+e.toString());
        }
    }

    public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse subscriberLocationReportResponseIndication) {
        logger.debug("onSubscriberLocationReportResponse");
        this.countMapLcsResp++;
        this.testerHost.sendNotif(SOURCE_NAME,
                "Rcvd: SubscriberLocationReportResponse", this
                        .createSLRResData(
                                subscriberLocationReportResponseIndication
                                        .getInvokeId(),
                                subscriberLocationReportResponseIndication
                                        .getNaESRD().getAddress()), Level.INFO);
    }

    @Override
    public String getNetworkNodeNumberAddress() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getNetworkNodeNumberAddress();
    }

    @Override
    public void setNetworkNodeNumberAddress(String data) {
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setNetworkNodeNumberAddress(data);
        this.testerHost.markStore();
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

    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest sendRoutingInforForLCSRequestIndication) {
    }

    public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse sendRoutingInforForLCSResponseIndication){
        logger.debug("onSendRoutingInfoForLCSResponse");
        this.countMapLcsResp++;
        MAPDialogLsm curDialog = sendRoutingInforForLCSResponseIndication.getMAPDialog();
        this.testerHost.sendNotif(SOURCE_NAME,
                "Rcvd: SendRoutingInfoForLCSResponse", this
                        .createSRIforLCSResData(
                                curDialog.getLocalDialogId(),
                                sendRoutingInforForLCSResponseIndication
                                        .getLCSLocationInfo()
                                        .getNetworkNodeNumber().getAddress()),
                Level.INFO);
    }




}
