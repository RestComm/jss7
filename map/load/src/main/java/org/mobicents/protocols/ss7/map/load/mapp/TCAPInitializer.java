
package org.mobicents.protocols.ss7.map.load.mapp;

import org.mobicents.protocols.ss7.map.load.mapp.MAPpContext;
import org.mobicents.protocols.ss7.map.load.mapp.StackInitializer;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;


public class TCAPInitializer implements StackInitializer{
    static final String STACK_ID = "sctpStack";
    
    //TCAP Details
    protected final int MAX_DIALOGS = 500000;
    
    // MTP Details
    protected static int CLIENT_SPC = 1;
    protected static int SERVET_SPC = 2;
    protected static int NETWORK_INDICATOR = 2;
    protected static int SERVICE_INIDCATOR = 3; // SCCP
    protected static int SSN = 8;    

    protected final SccpAddress SCCP_CLIENT_ADDRESS = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
            null, CLIENT_SPC, SSN);
    protected final SccpAddress SCCP_SERVER_ADDRESS = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
            null, SERVET_SPC, SSN);

    protected final org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl factory = new org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl();

    @Override
    public String getStackProtocol() {
        return STACK_ID;
    }

    @Override
    public void init(MAPpContext ctx) throws Exception {
        SccpStackImpl sccpStack = (SccpStackImpl) ctx.data.get(SCCPInitializer.STACK_ID);
        TCAPStackImpl tcapStack = new TCAPStackImpl("Test", sccpStack.getSccpProvider(), SSN);
        tcapStack.start();
        tcapStack.setDialogIdleTimeout(60000);
        tcapStack.setInvokeTimeout(30000);
        tcapStack.setMaxDialogs(MAX_DIALOGS);    
        ctx.data.put(STACK_ID, tcapStack);
    }
}
