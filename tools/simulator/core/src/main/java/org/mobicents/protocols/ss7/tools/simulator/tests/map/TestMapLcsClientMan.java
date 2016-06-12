package org.mobicents.protocols.ss7.tools.simulator.tests.map;

import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
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

import org.apache.log4j.Logger;



public class TestMapLcsClientMan extends TesterBase implements TestMapLcsClientManMBean, Stoppable {

    private static Logger logger = Logger.getLogger(TestMapLcsClientMan.class);

    public static String SOURCE_NAME = "TestMapLcsClientMan";
    private final String name;
    private MapMan mapMan;

    public TestMapLcsClientMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
    }

    public boolean start() {
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
        // MAPDialogMobility curDialog = currentDialog;
        //if (curDialog != null)
        //    sb.append(curDialog.getLocalDialogId());
        //else
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
    public String sendRoutingInfoForLCSRequest() {

        // Map Dialog Listener, MapDialog, MapMan and add as listener(this)

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

        if (mapProvider== null) {

            return "mapProvider is null";

        }


        try {

            MAPServiceLsm service = mapProvider.getMAPServiceLsm();
            

            // falonso acivate should be activate, never used before?
            service.acivate();

            // MAPDialogLsm dialog = service.createNewDialog(null,null,null,null,null,null);

            MAPApplicationContext appCnt = null;

            appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcGatewayContext,
                MAPApplicationContextVersion.version3);

            MAPParameterFactory mapParameterFactory = mapProvider.getMAPParameterFactory();


            MAPDialogLsm clientDialogLsm = service.createNewDialog(appCnt, this.mapMan.createOrigAddress(), null,
                   this.mapMan.createDestAddress(), null);

            logger.debug("MAPDialogLsm Created");

            ISDNAddressString mlcNumber = mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");
            IMSI imsi = mapParameterFactory.createIMSI("5555544444");
            SubscriberIdentity targetMS = mapParameterFactory.createSubscriberIdentity(imsi);

            clientDialogLsm.addSendRoutingInfoForLCSRequest(mlcNumber, targetMS, null);

            logger.debug("Added SendRoutingInfoForLCSRequest");

            clientDialogLsm.send();

        }
        catch(MAPException e) {
            return "Exception "+e.toString();
        }

        // mapProvider.getMAPServiceLsm().createNewDialog().addSendRoutingInfoForLCSRequest()

        // createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        /*  MAPDialogLsm createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference,
            SccpAddress destAddress, AddressString destReference, Long localTrId) throws MAPException;

    MAPDialogLsm createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference,
            SccpAddress destAddress, AddressString destReference) throws MAPException;

            */

        return "sendRoutingInfoForLCSRequest sent";
    }

}
