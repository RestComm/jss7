package org.mobicents.protocols.ss7.sccp.impl;

public class NonPersistentSccpResourceImpl extends SccpResourceImpl {

    public NonPersistentSccpResourceImpl(String name) {
        super(name);
    }

    public NonPersistentSccpResourceImpl(String name, boolean rspProhibitedByDefault) {
        super(name, rspProhibitedByDefault);
    }

    @Override
    protected void load() {

    }

    @Override
    protected synchronized void store() {

    }

}
