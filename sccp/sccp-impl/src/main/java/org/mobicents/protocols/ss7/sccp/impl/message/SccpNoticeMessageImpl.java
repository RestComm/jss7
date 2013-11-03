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

package org.mobicents.protocols.ss7.sccp.impl.message;

import java.io.IOException;

import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.AbstractParameter;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReturnCauseImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * This class represents SCCP a connectionless notice message (UDTS, XUDTS and LUDTS)
 *
 * @author Oleg Kulikov
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class SccpNoticeMessageImpl extends SccpDataNoticeTemplateMessageImpl implements SccpNoticeMessage {

    protected ReturnCause returnCause;

    /**
     * Create a SCCP-User originated message
     *
     * @param sccpStackImpl
     * @param type
     * @param returnCause
     * @param calledParty
     * @param callingParty
     * @param data
     * @param hopCounter
     * @param importance
     */
    protected SccpNoticeMessageImpl(SccpStackImpl sccpStackImpl, int type, ReturnCause returnCause, SccpAddress calledParty,
            SccpAddress callingParty, byte[] data, HopCounter hopCounter, Importance importance) {
        super(sccpStackImpl, type, 0, -1, calledParty, callingParty, data, hopCounter, importance);

        this.returnCause = returnCause;
    }

    /**
     * Create a MTP3 originated message
     *
     * @param sccpStackImpl
     * @param type
     * @param incomingOpc
     * @param incomingDpc
     * @param incomingSls
     */
    protected SccpNoticeMessageImpl(SccpStackImpl sccpStackImpl, int type, int incomingOpc, int incomingDpc, int incomingSls) {
        super(sccpStackImpl, type, incomingOpc, incomingDpc, incomingSls);
    }

    public ReturnCause getReturnCause() {
        return returnCause;
    }

    public void setReturnCause(ReturnCause rc) {
        this.returnCause = rc;
    }

    public boolean getReturnMessageOnError() {
        return false;
    }

    public void clearReturnMessageOnError() {
    }

    public boolean getSccpCreatesSls() {
        return true;
    }

    @Override
    protected boolean getSecondParamaterPresent() {
        return this.returnCause != null;
    }

    @Override
    protected byte[] getSecondParamaterData() throws IOException {
        return ((AbstractParameter) this.returnCause).encode();
    }

    @Override
    protected void setSecondParamaterData(int data) throws IOException {
        this.returnCause = new ReturnCauseImpl(ReturnCauseValue.getInstance(data));
    }

    @Override
    protected boolean getIsProtocolClass1() {
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Sccp Msg [Type=");
        switch (this.type) {
            case SccpMessage.MESSAGE_TYPE_UDTS:
                sb.append("UDTS");
                break;
            case SccpMessage.MESSAGE_TYPE_XUDTS:
                sb.append("XUDTS");
                break;
            case SccpMessage.MESSAGE_TYPE_LUDTS:
                sb.append("LUDTS");
                break;
        }
        sb.append(" sls=").append(this.sls).append(" returnCause=").append(this.returnCause).append(" incomingOpc=")
                .append(this.incomingOpc).append(" incomingDpc=").append(this.incomingDpc).append(" outgoingDpc=")
                .append(this.outgoingDpc).append(" CallingAddress(").append(this.callingParty).append(") CalledParty(")
                .append(this.calledParty).append(")");
        sb.append(" DataLen=");
        if (this.data != null)
            sb.append(this.data.length);
        return sb.toString();
    }
}
