package org.restcomm.protocols.ss7.sccp.impl;

import java.util.concurrent.Executors;

import javolution.util.FastMap;

import org.restcomm.protocols.ss7.mtp.Mtp3UserPart;
import org.restcomm.protocols.ss7.sccp.SccpManagementEventListener;
import org.restcomm.protocols.ss7.sccp.impl.router.NonPersistentRouterImpl;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterface;

public class NonPersistentSccpStackImpl extends SccpStackImpl {

    public NonPersistentSccpStackImpl(String name, Ss7ExtInterface ss7ExtInterface) {
        super(name, ss7ExtInterface);
    }

    @Override
    public void start() throws IllegalStateException {
        logger.info("Starting ...");

        ss7ExtSccpDetailedInterface.startExtBefore(persistDir, this.name);

        this.load();

        this.sccpManagement = new SccpManagement(name, sccpProvider, this);
        this.sccpRoutingControl = new SccpRoutingControl(sccpProvider, this);

        this.sccpManagement.setSccpRoutingControl(sccpRoutingControl);
        this.sccpRoutingControl.setSccpManagement(sccpManagement);

        this.router = new NonPersistentRouterImpl(this.name, this);
        this.router.start();

        this.sccpResource = new NonPersistentSccpResourceImpl(this.name, ss7ExtSccpDetailedInterface);
        this.sccpResource.start();

        logger.info("Starting routing engine...");
        this.sccpRoutingControl.start();
        logger.info("Starting management ...");
        this.sccpManagement.start();
        logger.info("Starting MSU handler...");

        this.timerExecutors = Executors.newScheduledThreadPool(1);

        for (FastMap.Entry<Integer, Mtp3UserPart> e = this.mtp3UserParts.head(), end = this.mtp3UserParts.tail(); (e = e
                .getNext()) != end;) {
            Mtp3UserPart mup = e.getValue();
            mup.addMtp3UserPartListener(this);
        }

        for (SccpManagementEventListener lstr : this.sccpProvider.managementEventListeners) {
            try {
                lstr.onServiceStarted();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServiceStarted", ee);
            }
        }

        ss7ExtSccpDetailedInterface.startExtAfter(this.router, this.sccpManagement);

        this.state = State.RUNNING;
    }

    @Override
    public void load() {

    }

    @Override
    public void store() {

    }

}
