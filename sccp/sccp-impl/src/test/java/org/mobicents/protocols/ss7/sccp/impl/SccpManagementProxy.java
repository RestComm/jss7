/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.congestion.SccpCongestionControl;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.Mtp3PrimitiveMessage;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.SccpMgmtMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;

/**
 * @author baranowb
 *
 */
public class SccpManagementProxy extends SccpManagement {

    private int seq = 0; // seq, to mark messages, so we get them correctly
    // separate lists, thats better
    private List<SccpMgmtMessage> mgmtMessages = new ArrayList<SccpMgmtMessage>();
    private List<Mtp3PrimitiveMessage> mtp3Messages = new ArrayList<Mtp3PrimitiveMessage>();
    private boolean encounteredError = false;
    private SccpStackImpl sccpStackImpl;

    /**
     * @param sccpProviderImpl
     * @param sccpStackImpl
     */
    public SccpManagementProxy(String name, SccpProviderImpl sccpProviderImpl, SccpStackImpl sccpStackImpl) {
        super(name, sccpProviderImpl, sccpStackImpl);

        this.sccpStackImpl = sccpStackImpl;
    }

    // =----------------= some getters

    public int getSeq() {
        return seq;
    }

    public boolean isEncounteredError() {
        return encounteredError;
    }

    public List<SccpMgmtMessage> getMgmtMessages() {
        return mgmtMessages;
    }

    public List<Mtp3PrimitiveMessage> getMtp3Messages() {
        return mtp3Messages;
    }

    // =----------------= deletage to intercept.

    public SccpRoutingControl getSccpRoutingControl() {

        return super.getSccpRoutingControl();
    }

    public SccpCongestionControl getSccpCongestionControl() {

        return super.getSccpCongestionControl();
    }

    public void setSccpRoutingControl(SccpRoutingControl sccpRoutingControl) {

        super.setSccpRoutingControl(sccpRoutingControl);
    }

    public void setSccpCongestionControl(SccpCongestionControl sccpCongestionControl) {

        super.setSccpCongestionControl(sccpCongestionControl);
    }

    @Override
    public void onManagementMessage(SccpDataMessage message) {
        byte[] data = message.getData();
        int messgType = data[0];
        int affectedSsn = data[1];
        int affectedPc;
        int subsystemMultiplicity;
        if (sccpStackImpl.getSccpProtocolVersion() == SccpProtocolVersion.ANSI) {
            affectedPc = (data[2] & 0xff) | ((data[3] & 0xff) << 8) | ((data[4] & 0xff) << 16);
            subsystemMultiplicity = data[5] & 0xff;
        } else {
            affectedPc = (data[2] & 0xff) | ((data[3] & 0xff) << 8);
            subsystemMultiplicity = data[4] & 0xff;
        }
        SccpMgmtMessage mgmtMessage = new SccpMgmtMessage(seq++, messgType, affectedSsn, affectedPc, subsystemMultiplicity);
        mgmtMessages.add(mgmtMessage);

        super.onManagementMessage(message);
    }

    @Override
    protected void recdMsgForProhibitedSsn(SccpMessage msg, int ssn) {

        super.recdMsgForProhibitedSsn(msg, ssn);
    }

    public void start() {

        super.start();
    }

    public void stop() {

        super.stop();
    }

    @Override
    protected void handleMtp3Pause(int affectedPc) {
        super.handleMtp3Pause(affectedPc);

        Mtp3PrimitiveMessage prim = new Mtp3PrimitiveMessage(seq++, MTP3_PAUSE, affectedPc);
        mtp3Messages.add(prim);
    }

    @Override
    protected void handleMtp3Resume(int affectedPc) {
        super.handleMtp3Resume(affectedPc);

        Mtp3PrimitiveMessage prim = new Mtp3PrimitiveMessage(seq++, MTP3_RESUME, affectedPc);
        mtp3Messages.add(prim);
    }

    @Override
    protected void handleMtp3Status(Mtp3StatusCause cause, int affectedPc, int congStatus) {
        super.handleMtp3Status(cause, affectedPc, congStatus);

        int status = 0;
        int unavailabiltyCause = 0;
        switch (cause) {
            case SignallingNetworkCongested:
                status = 2;
                break;
            case UserPartUnavailability_Unknown:
                unavailabiltyCause = 0;
                status = 1;
                break;
            case UserPartUnavailability_UnequippedRemoteUser:
                unavailabiltyCause = 1;
                status = 1;
                break;
            case UserPartUnavailability_InaccessibleRemoteUser:
                unavailabiltyCause = 2;
                status = 1;
                break;
        }
        Mtp3PrimitiveMessage prim = new Mtp3PrimitiveMessage(seq++, MTP3_STATUS, affectedPc, status, congStatus,
                unavailabiltyCause);
        mtp3Messages.add(prim);
    }
}
