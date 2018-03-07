package org.restcomm.protocols.ss7.m3ua.impl;

import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterface;

public class NonPersistentM3UAManagementImpl extends M3UAManagementImpl {

    public NonPersistentM3UAManagementImpl(String name, String productName, Ss7ExtInterface ss7ExtInterface) {
       super(name, productName, ss7ExtInterface);
    }

    @Override
    public void load() {

    }

    @Override
    public void store() {

    }

}
