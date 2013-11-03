/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.sccp.impl;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
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

    /**
     * @param sccpProviderImpl
     * @param sccpStackImpl
     */
    public SccpManagementProxy(String name, SccpProviderImpl sccpProviderImpl, SccpStackImpl sccpStackImpl) {
        super(name, sccpProviderImpl, sccpStackImpl);
        // TODO Auto-generated constructor stub
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

    public void setSccpRoutingControl(SccpRoutingControl sccpRoutingControl) {

        super.setSccpRoutingControl(sccpRoutingControl);
    }

    @Override
    public void onManagementMessage(SccpDataMessage message) {
        byte[] data = message.getData();
        int messgType = data[0];
        int affectedSsn = data[1];
        int affectedPc = (data[2] & 0x00FF) | (data[3] & 0x00FF << 8);
        int subsystemMultiplicity = data[3];
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
