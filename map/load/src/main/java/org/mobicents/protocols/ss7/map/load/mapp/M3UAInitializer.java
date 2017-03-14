package org.mobicents.protocols.ss7.map.load.mapp;

import org.mobicents.protocols.ss7.map.load.mapp.MAPpContext;
import org.mobicents.protocols.ss7.map.load.mapp.StackInitializer;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

public class M3UAInitializer implements StackInitializer {

    static final String STACK_ID = "m3uaStack";
    
    protected final ParameterFactoryImpl factory = new ParameterFactoryImpl();
    
    // M3UA details
    protected String CLIENT_IP = "127.0.0.1";
    protected int CLIENT_PORT = 2345;
    protected String SERVER_IP = "127.0.0.1";
    protected int SERVER_PORT = 3434;
    protected int ROUTING_CONTEXT = 100;
    protected final String SERVER_ASSOCIATION_NAME = "serverAsscoiation";
    protected final String CLIENT_ASSOCIATION_NAME = "clientAsscoiation";
    protected final String SERVER_NAME = "testserver";
    

        // MTP Details
    protected static int CLIENT_SPC = 1;
    protected static int SERVET_SPC = 2;
    protected static int NETWORK_INDICATOR = 2;
    protected static int SERVICE_INIDCATOR = 3; // SCCP
    protected static int SSN = 8;
    
    protected static int DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;    
    @Override
    public String getStackProtocol() {
        return STACK_ID;
    }

    @Override
    public void init(MAPpContext ctx) throws Exception {
        NettySctpManagementImpl sctpManagement = (NettySctpManagementImpl) ctx.data.get(SCTPInitializer.STACK_ID);
        M3UAManagementImpl clientM3UAMgmt = new M3UAManagementImpl("Client", null);
        clientM3UAMgmt.setTransportManagement(sctpManagement);
        clientM3UAMgmt.setDeliveryMessageThreadCount(DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);
        clientM3UAMgmt.start();
        clientM3UAMgmt.removeAllResourses();

        // m3ua as create rc <rc> <ras-name>
        RoutingContext rc = factory.createRoutingContext(new long[]{100L});
        TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
        clientM3UAMgmt.createAs("AS1", Functionality.AS, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, null);

        // Step 2 : Create ASP
        clientM3UAMgmt.createAspFactory("ASP1", CLIENT_ASSOCIATION_NAME);

        // Step3 : Assign ASP to AS
        Asp asp = clientM3UAMgmt.assignAspToAs("AS1", "ASP1");

        // Step 4: Add Route. Remote point code is 2
        clientM3UAMgmt.addRoute(SERVET_SPC, -1, -1, "AS1");
        
        ctx.data.put(STACK_ID, clientM3UAMgmt);
    }
}
