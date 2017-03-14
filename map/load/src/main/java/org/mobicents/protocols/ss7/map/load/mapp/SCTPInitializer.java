package org.mobicents.protocols.ss7.map.load.mapp;

import org.mobicents.protocols.ss7.map.load.mapp.MAPpContext;
import org.mobicents.protocols.ss7.map.load.mapp.StackInitializer;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;

public class SCTPInitializer implements StackInitializer {

    
    static final String STACK_ID = "sctpStack";

    // M3UA details
    // protected final String CLIENT_IP = "172.31.96.40";
    protected static String CLIENT_IP = "127.0.0.1";
    protected static int CLIENT_PORT = 2345;

    // protected final String SERVER_IP = "172.31.96.41";
    protected static String SERVER_IP = "127.0.0.1";
    protected static int SERVER_PORT = 3434;

    protected static int ROUTING_CONTEXT = 100;  
        protected final String CLIENT_ASSOCIATION_NAME = "clientAsscoiation";
    
    @Override
    public String getStackProtocol() {
        return STACK_ID;
    }

    @Override
    public void init(MAPpContext ctx) throws Exception {
    
        
        NettySctpManagementImpl sctpManagement = new NettySctpManagementImpl("Client");
//        this.sctpManagement.setSingleThread(false);
        sctpManagement.start();
        sctpManagement.setConnectDelay(10000);
        sctpManagement.removeAllResourses();

        IpChannelType ipChannelType = IpChannelType.SCTP;
        String channelType = ctx.props.getProperty(STACK_ID + ".channelType");
        ipChannelType = IpChannelType.valueOf(channelType);    
        
        // 1. Create SCTP Association
        sctpManagement.addAssociation(CLIENT_IP, CLIENT_PORT, SERVER_IP, SERVER_PORT, CLIENT_ASSOCIATION_NAME, ipChannelType,
                null);
        ctx.data.put(STACK_ID, sctpManagement);
    }
}
