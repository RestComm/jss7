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
import java.io.InputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author kulikov
 * @author sergey vetyutnev
 *
 */
public class MessageFactoryImpl implements MessageFactory {
    private static final Logger logger = Logger.getLogger(MessageFactoryImpl.class);

    private transient SccpStackImpl sccpStackImpl;

    public MessageFactoryImpl(SccpStackImpl sccpStackImpl) {
        this.sccpStackImpl = sccpStackImpl;
    }

    public SccpDataMessage createDataMessageClass0(SccpAddress calledParty, SccpAddress callingParty, byte[] data,
            int localSsn, boolean returnMessageOnError, HopCounter hopCounter, Importance importance) {
        return new SccpDataMessageImpl(this.sccpStackImpl, new ProtocolClassImpl(0, returnMessageOnError),
                sccpStackImpl.newSls(), localSsn, calledParty, callingParty, data, hopCounter, importance);
    }

    public SccpDataMessage createDataMessageClass1(SccpAddress calledParty, SccpAddress callingParty, byte[] data, int sls,
            int localSsn, boolean returnMessageOnError, HopCounter hopCounter, Importance importance) {
        return new SccpDataMessageImpl(this.sccpStackImpl, new ProtocolClassImpl(1, returnMessageOnError), sls, localSsn,
                calledParty, callingParty, data, hopCounter, importance);
    }

    public SccpNoticeMessage createNoticeMessage(int origMsgType, ReturnCause returnCause, SccpAddress calledParty,
            SccpAddress callingParty, byte[] data, HopCounter hopCounter, Importance importance) {
        int type = SccpMessage.MESSAGE_TYPE_UNDEFINED;
        switch (origMsgType) {
            case SccpMessage.MESSAGE_TYPE_UDT:
                type = SccpMessage.MESSAGE_TYPE_UDTS;
                break;
            case SccpMessage.MESSAGE_TYPE_XUDT:
                type = SccpMessage.MESSAGE_TYPE_XUDTS;
                break;
            case SccpMessage.MESSAGE_TYPE_LUDT:
                type = SccpMessage.MESSAGE_TYPE_LUDTS;
                break;
        }

        return new SccpNoticeMessageImpl(this.sccpStackImpl, type, returnCause, calledParty, callingParty, data, hopCounter,
                importance);
    }

    public SccpMessageImpl createMessage(int type, int opc, int dpc, int sls, InputStream in) throws IOException {
        SccpMessageImpl msg = null;
        switch (type) {
            case SccpMessage.MESSAGE_TYPE_UDT:
            case SccpMessage.MESSAGE_TYPE_XUDT:
            case SccpMessage.MESSAGE_TYPE_LUDT:
                msg = new SccpDataMessageImpl(this.sccpStackImpl, type, opc, dpc, sls);
                break;

            case SccpMessage.MESSAGE_TYPE_UDTS:
            case SccpMessage.MESSAGE_TYPE_XUDTS:
            case SccpMessage.MESSAGE_TYPE_LUDTS:
                msg = new SccpNoticeMessageImpl(this.sccpStackImpl, type, opc, dpc, sls);
                break;
        }

        if (msg != null) {
            msg.decode(in);
        } else if (logger.isEnabledFor(Level.WARN)) {
            logger.warn("No message implementation for MT: " + type);
        }
        return msg;
    }
}
