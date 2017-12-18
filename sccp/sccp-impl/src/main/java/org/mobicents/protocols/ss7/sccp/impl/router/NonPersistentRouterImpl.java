package org.mobicents.protocols.ss7.sccp.impl.router;

import org.mobicents.protocols.ss7.sccp.SccpStack;

public class NonPersistentRouterImpl extends RouterImpl {

    public NonPersistentRouterImpl(String name, SccpStack sccpStack) {
        super(name, sccpStack);
    }

    @Override
    protected void load() {

    }

    @Override
    public void store() {

   }
}
