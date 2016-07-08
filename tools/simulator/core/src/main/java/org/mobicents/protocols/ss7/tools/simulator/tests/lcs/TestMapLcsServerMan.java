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
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;



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

    public TestMapLcsServerMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
    }

    public boolean start() {

        this.mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        mapServiceLsm = mapProvider.getMAPServiceLsm();
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
        sb.append(": CurDialog=");
        sb.append("No");
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
    public String subscriberLocationReportResponse() {

        return "subscriberLocationReportResponse called automatically";
    }

    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequestIndication) {

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
                        AddressNature.international_number,
                        NumberingPlan.ISDN,
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

    }

    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest sendRoutingInforForLCSRequestIndication) {
        logger.debug("onSendRoutingInfoForLCSRequest");

        if (!isStarted)
            return;

        MAPParameterFactory mapParameterFactory = this.mapProvider.getMAPParameterFactory();

        ISDNAddressString mlc = sendRoutingInforForLCSRequestIndication.getMLCNumber();
        SubscriberIdentity targetMS = sendRoutingInforForLCSRequestIndication.getTargetMS();


        MAPDialogLsm curDialog = sendRoutingInforForLCSRequestIndication.getMAPDialog();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: SendRoutingInfoForLCSRequest",
                   createSRIforLCSReqData(curDialog.getLocalDialogId(),
                   mlc.getAddress() ), Level.INFO);


        ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                        AddressNature.international_number,
                        NumberingPlan.ISDN,
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

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SendRoutingInfoForLCSResponse",
                   createSRIforLCSResData(curDialog.getLocalDialogId(),
                   networkNodeNumber.getAddress() ), Level.INFO);

         } catch (MAPException e) {
            logger.debug("Failed building response "+e.toString());
        }

    }

    private String createSRIforLCSReqData(long dialogId, String address) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", address=\"");
        sb.append(address);
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
}
