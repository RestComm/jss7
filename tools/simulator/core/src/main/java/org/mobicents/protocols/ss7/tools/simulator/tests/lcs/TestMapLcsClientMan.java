package org.mobicents.protocols.ss7.tools.simulator.tests.lcs;

import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

import org.apache.log4j.Logger;


/**
 * @author falonso@csc.com
 *
 */
public class TestMapLcsClientMan extends TesterBase implements TestMapLcsClientManMBean, Stoppable {

    private static Logger logger = Logger.getLogger(TestMapLcsClientMan.class);

    public static String SOURCE_NAME = "TestMapLcsClientMan";
    private final String name;
    private MapMan mapMan;
    private boolean isStarted = false;

    public TestMapLcsClientMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
        this.isStarted = false;
    }

    public boolean start() {
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
    public String performSendRoutingInfoForLCSRequest() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return sendRoutingInfoForLCSRequest();
    }

    @Override
    public String sendRoutingInfoForLCSRequest() {

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

        if (mapProvider== null) {

            return "mapProvider is null";

        }


        try {

            MAPServiceLsm service = mapProvider.getMAPServiceLsm();

            // falonso acivate should be activate, never used before?
            service.acivate();

            MAPApplicationContext appCnt = null;

            appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcGatewayContext,
                MAPApplicationContextVersion.version3);

            MAPParameterFactory mapParameterFactory = mapProvider.getMAPParameterFactory();


            MAPDialogLsm clientDialogLsm = service.createNewDialog(appCnt, this.mapMan.createOrigAddress(), null,
                   this.mapMan.createDestAddress(), null);

            logger.debug("MAPDialogLsm Created");

            ISDNAddressString mlcNumber = mapParameterFactory.createISDNAddressString(
                AddressNature.getInstance(getAddressNature().intValue()),       // e.g. international_number
                NumberingPlan.getInstance(getNumberingPlanType().intValue()),   // e.g  ISDN
                getNumberingPlan());      // e.g. 11112222

            IMSI imsi = mapParameterFactory.createIMSI(
                getIMSI());               // e.g. 55555
            SubscriberIdentity targetMS = mapParameterFactory.createSubscriberIdentity(imsi);

            clientDialogLsm.addSendRoutingInfoForLCSRequest(mlcNumber, targetMS, null);

            logger.debug("Added SendRoutingInfoForLCSRequest");

            clientDialogLsm.send();

        }
        catch(MAPException e) {
            return "Exception "+e.toString();
        }

        return "sendRoutingInfoForLCSRequest sent";
    }

    public String subscriberLocationReportRequest(String address){
        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        if (mapProvider== null) {
            return "mapProvider is null";
        }


        try {

            MAPServiceLsm service = mapProvider.getMAPServiceLsm();

            // falonso acivate should be activate, never used before?
            service.acivate();

            MAPApplicationContext appCnt = null;

            appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);

            MAPParameterFactory mapParameterFactory = mapProvider.getMAPParameterFactory();


            MAPDialogLsm clientDialogLsm = service.createNewDialog(appCnt, this.mapMan.createOrigAddress(), null,
                   this.mapMan.createDestAddress(), null);

            logger.debug("MAPDialogLsm Created");

            LCSClientID lcsClientID = mapParameterFactory.createLCSClientID(LCSClientType.plmnOperatorServices, null, null,
                    null, null, null, null);
            ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getNumberingPlanType(), address);
            LCSLocationInfo lcsLocationInfo = mapParameterFactory.createLCSLocationInfo(networkNodeNumber, null, null, false,
                    null, null, null, null, null);

            clientDialogLsm.addSubscriberLocationReportRequest(LCSEvent.emergencyCallOrigination, lcsClientID, lcsLocationInfo,
                    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false,
                    null, null, null, null, false, null, null, null);
            logger.debug("Added SubscriberLocationReportRequest");

            clientDialogLsm.send();

        }
        catch(MAPException e) {
            return "Exception "+e.toString();
        }

        return "subscriberLocationReportRequest sent";
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
    public String getIMSI() {
        return this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().getIMSI();
    }

    @Override
    public void setIMSI(String data) {
        this.testerHost.getConfigurationData().getTestMapLcsClientConfigurationData().setIMSI(data);
        this.testerHost.markStore();
    }

}
