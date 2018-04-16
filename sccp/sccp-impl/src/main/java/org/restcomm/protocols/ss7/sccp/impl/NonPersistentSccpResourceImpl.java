package org.restcomm.protocols.ss7.sccp.impl;

public class NonPersistentSccpResourceImpl extends SccpResourceImpl {

    public NonPersistentSccpResourceImpl(String name, Ss7ExtSccpDetailedInterface ss7ExtSccpDetailedInterface) {
        super(name, ss7ExtSccpDetailedInterface);
    }

    public NonPersistentSccpResourceImpl(String name, boolean rspProhibitedByDefault,
            Ss7ExtSccpDetailedInterface ss7ExtSccpDetailedInterface) {
        super(name, rspProhibitedByDefault, ss7ExtSccpDetailedInterface);
    }

    @Override
    public void load() {

    }

    @Override
    public synchronized void store() {

    }

}
