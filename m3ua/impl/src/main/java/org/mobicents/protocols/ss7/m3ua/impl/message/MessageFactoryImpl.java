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

package org.mobicents.protocols.ss7.m3ua.impl.message;

import io.netty.buffer.ByteBuf;

import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.ASPDownAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.ASPDownImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.ASPUpAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.ASPUpImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.HeartbeatAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.HeartbeatImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPActiveAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPActiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPInactiveAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPInactiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.mgmt.ErrorImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.mgmt.NotifyImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.rkm.DeregistrationRequestImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.rkm.DeregistrationResponseImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.rkm.RegistrationRequestImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.rkm.RegistrationResponseImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationAvailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationRestrictedImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationStateAuditImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationUPUnavailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationUnavailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.SignallingCongestionImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;

/**
 * @author amit bhayani
 * @author kulikov
 * @author sergey vetyutnev
 */
public class MessageFactoryImpl implements MessageFactory {

public M3UAMessageImpl createMessage(int messageClass, int messageType) {
        switch (messageClass) {
            case MessageClass.TRANSFER_MESSAGES:
                switch (messageType) {
                    case MessageType.PAYLOAD:
                        return new PayloadDataImpl();
                }
                break;
            case MessageClass.SIGNALING_NETWORK_MANAGEMENT:
                switch (messageType) {
                    case MessageType.DESTINATION_UNAVAILABLE:
                        return new DestinationUnavailableImpl();
                    case MessageType.DESTINATION_AVAILABLE:
                        return new DestinationAvailableImpl();
                    case MessageType.DESTINATION_STATE_AUDIT:
                        return new DestinationStateAuditImpl();
                    case MessageType.SIGNALING_CONGESTION:
                        return new SignallingCongestionImpl();
                    case MessageType.DESTINATION_USER_PART_UNAVAILABLE:
                        return new DestinationUPUnavailableImpl();
                    case MessageType.DESTINATION_RESTRICTED:
                        return new DestinationRestrictedImpl();
                }
                break;
            case MessageClass.ASP_STATE_MAINTENANCE:
                switch (messageType) {
                    case MessageType.ASP_UP:
                        return new ASPUpImpl();
                    case MessageType.ASP_UP_ACK:
                        return new ASPUpAckImpl();
                    case MessageType.ASP_DOWN:
                        return new ASPDownImpl();
                    case MessageType.ASP_DOWN_ACK:
                        return new ASPDownAckImpl();
                    case MessageType.HEARTBEAT:
                        return new HeartbeatImpl();
                    case MessageType.HEARTBEAT_ACK:
                        return new HeartbeatAckImpl();
                }
                break;
            case MessageClass.ASP_TRAFFIC_MAINTENANCE:
                switch (messageType) {
                    case MessageType.ASP_ACTIVE:
                        return new ASPActiveImpl();
                    case MessageType.ASP_ACTIVE_ACK:
                        return new ASPActiveAckImpl();
                    case MessageType.ASP_INACTIVE:
                        return new ASPInactiveImpl();
                    case MessageType.ASP_INACTIVE_ACK:
                        return new ASPInactiveAckImpl();

                }
                break;
            case MessageClass.ROUTING_KEY_MANAGEMENT:
                switch (messageType) {
                    case MessageType.REG_REQUEST:
                        return new RegistrationRequestImpl();
                    case MessageType.REG_RESPONSE:
                        return new RegistrationResponseImpl();
                    case MessageType.DEREG_REQUEST:
                        return new DeregistrationRequestImpl();
                    case MessageType.DEREG_RESPONSE:
                        return new DeregistrationResponseImpl();
                }

            case MessageClass.MANAGEMENT:
                switch (messageType) {
                    case MessageType.ERROR:
                        return new ErrorImpl();
                    case MessageType.NOTIFY:
                        return new NotifyImpl();
                }

        }
        return null;
    }

    public M3UAMessageImpl createMessage(ByteBuf message) {
        int dataLen;
        if (message.readableBytes() < 8) {
            return null;
        }

        // obtain message class and type from header
        message.markReaderIndex();
        message.skipBytes(2);
        int messageClass = message.readUnsignedByte();
        int messageType = message.readUnsignedByte();

        // obtain remaining length of the message and prepare buffer
        dataLen = message.readInt() - 8;
        if (message.readableBytes() < dataLen) {
            message.resetReaderIndex();
            return null;
        }

        // construct new message instance
        M3UAMessageImpl messageTemp = this.createMessage(messageClass, messageType);

        // parsing params of this message
        message.markWriterIndex();
        message.writerIndex(message.readerIndex() + dataLen);
        messageTemp.decode(message);
        message.resetWriterIndex();

        return messageTemp;
    }
}
