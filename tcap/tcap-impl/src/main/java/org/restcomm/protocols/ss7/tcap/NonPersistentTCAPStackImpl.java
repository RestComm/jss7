package org.restcomm.protocols.ss7.tcap;

import org.restcomm.protocols.ss7.sccp.SccpProvider;


//TODO: is this needed? there is no usages of this class
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
