package org.mobicents.protocols.ss7.map.load.mapp;

import org.mobicents.protocols.ss7.map.load.mapp.MAPpContext;
import org.mobicents.protocols.ss7.map.load.mapp.StackInitializer;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;

public class MAPInitializer implements StackInitializer {

    static final String STACK_ID  = "mapStack";

    @Override
    public String getStackProtocol() {
        return STACK_ID;
    }

    @Override
    public void init(MAPpContext ctx) throws Exception {
        TCAPStack tcapStack = (TCAPStack) ctx.data.get("tcapStack");
        MAPStackImpl mapStack = new MAPStackImpl("TestClient", tcapStack.getProvider());
        MAPProvider mapProvider = mapStack.getMAPProvider();

        mapProvider.addMAPDialogListener(ctx.scenario);
        mapProvider.getMAPServiceSupplementary().addMAPServiceListener(ctx.scenario);

        mapProvider.getMAPServiceSupplementary().acivate();

        mapStack.start();
        
        ctx.data.put(STACK_ID, mapStack);
    }
}
