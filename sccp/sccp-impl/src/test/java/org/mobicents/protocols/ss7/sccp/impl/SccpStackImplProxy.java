/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Executors;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.sccp.impl.congestion.SccpCongestionControl;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;

/**
 * @author baranowb
 *
 */
public class SccpStackImplProxy extends SccpStackImpl {

    /**
	 *
	 */
    public SccpStackImplProxy(String name) {
        super(name);
    }

    public SccpManagementProxy getManagementProxy() {
        return (SccpManagementProxy) super.sccpManagement;
    }

    @Override
    public void start() {
        this.persistFile.clear();

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(SCCP_MANAGEMENT_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
                    .append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        }

        logger.info(String.format("SCCP Management configuration file path %s", persistFile.toString()));

        try {
            this.load();
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Failed to load the Sccp Management configuration file. \n%s", e.getMessage()));
        }

        this.messageFactory = new MessageFactoryImpl(this);

        this.sccpProvider = new SccpProviderImpl(this);

        super.sccpManagement = new SccpManagementProxy(this.getName(), sccpProvider, this);
        super.sccpRoutingControl = new SccpRoutingControl(sccpProvider, this);
        super.sccpCongestionControl = new SccpCongestionControl(sccpManagement, this);

        super.sccpManagement.setSccpRoutingControl(sccpRoutingControl);
        super.sccpRoutingControl.setSccpManagement(sccpManagement);
        this.sccpManagement.setSccpCongestionControl(sccpCongestionControl);

        this.router = new RouterImpl(this.getName(), this);
        this.router.setPersistDir(this.getPersistDir());
        this.router.start();

        this.sccpResource = new SccpResourceImpl(this.getName());
        this.sccpResource.setPersistDir(this.getPersistDir());
        this.sccpResource.start();

        this.sccpRoutingControl.start();
        this.sccpManagement.start();
        // layer3exec.execute(new MtpStreamHandler());

        this.timerExecutors = Executors.newScheduledThreadPool(1);

        for (FastMap.Entry<Integer, Mtp3UserPart> e = this.mtp3UserParts.head(), end = this.mtp3UserParts.tail(); (e = e
                .getNext()) != end;) {
            Mtp3UserPart mup = e.getValue();
            mup.addMtp3UserPartListener(this);
        }
        // this.mtp3UserPart.addMtp3UserPartListener(this);

        this.state = State.RUNNING;
    }

    public int getReassemplyCacheSize() {
        return reassemplyCache.size();
    }

    @Override
    public void setReassemblyTimerDelay(int reassemblyTimerDelay) {
        this.reassemblyTimerDelay = reassemblyTimerDelay;
    }

}
