package org.mobicents.protocols.ss7.tcapAnsi;

import org.mobicents.protocols.ss7.sccp.SccpProvider;

public class NonPersistentTCAPStackImpl extends TCAPStackImpl {

    public NonPersistentTCAPStackImpl(String name) {
        super(name);
    }

    public NonPersistentTCAPStackImpl(String name, SccpProvider sccpProvider, int ssn) {
        super(name, sccpProvider, ssn);
    }

    @Override
    public void load() {

    }

    @Override
    public void store() {

    }
}
