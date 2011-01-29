/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, encode to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.m3ua.impl.message;

import java.nio.ByteBuffer;

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
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationStateAuditImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationUPUnavailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationUnavailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.SignallingCongestionImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;

/**
 * 
 * @author kulikov
 */
public class MessageFactoryImpl implements MessageFactory {
    // header binary view
    private byte[] header = new byte[8];
    // variable length parameters
    private byte[] params;
    // current position either in header or in params
    private int pos = 0;
    // flag indicating that header is completely read
    private boolean isHeaderReady = false;

    // the length of message's parameters
    private int length;
    // message instance
    private M3UAMessageImpl message;

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

    public M3UAMessageImpl createMessage(ByteBuffer buffer) {
        System.out.println("MessageFactory got buffer with size "
                + buffer.remaining());
        // fill header buffer completely before start parsing header
        if (!isHeaderReady) {
            // the amount of data possible to read is determined as
            // minimal remainder either of header buffer or of the input buffer
            int len = Math.min(header.length - pos, buffer.remaining());
            buffer.get(header, pos, len);

            // update cursor postion in the header's buffer
            pos += len;

            // header completed?
            isHeaderReady = pos == header.length;

            if (!isHeaderReady) {
                // no more data available
                return null;
            }

            // obtain message class and type from header
            int messageClass = header[2] & 0xff;
            int messageType = header[3] & 0xff;

            // construct new message instance
            message = this.createMessage(messageClass, messageType);

            // obtain remaining length of the message and prepare buffer
            length = ((header[4] & 0xff << 24) | (header[5] & 0xff << 16)
                    | (header[6] & 0xff << 8) | (header[7] & 0xff)) - 8;
            params = new byte[length];

            // finally switch cursor position
            pos = 0;
        }

        // at this point we must recheck remainder of the input buffer
        // because possible case when input buffer fits exactly to the header
        if (!buffer.hasRemaining()) {
            return null;
        }

        // again, reading all parameters before parsing

        // compute available or required data
        int len = Math.min(params.length, buffer.remaining());
        buffer.get(params, pos, len);

        // update cursor position
        pos += len;

        // end of message not reached
        if (pos < params.length) {
            return null;
        }

        // end of message reached and most probably some data remains in buffer
        // do not touch remainder of the input buffer, next call to this method
        // will proceed remainder

        // parsing params of this message
        message.decode(params);

        // switch factory for receiving new message
        this.isHeaderReady = false;
        this.pos = 0;

        // return
        return message;
    }

    public M3UAMessageImpl createSctpMessage(ByteBuffer buffer) {
        int dataLen;
        // fill header buffer completely before start parsing header
        if (buffer.remaining() < 8) {
            System.out
                    .println("M3UAMessageFactory can't create message from data < 8 byte. Double check!");
            return null;
        }
        pos = 0;
        buffer.get(header, pos, 8);

        // update cursor postion in the header's buffer
        pos += 8;

        // obtain message class and type from header
        int messageClass = header[2] & 0xff;
        int messageType = header[3] & 0xff;

        // construct new message instance
        message = this.createMessage(messageClass, messageType);

        // obtain remaining length of the message and prepare buffer
        length = ((header[4] & 0xff << 24) | (header[5] & 0xff << 16)
                | (header[6] & 0xff << 8) | (header[7] & 0xff)) - 8;

        if ((length > 8) && (buffer.remaining() >= length - 8)) {
            dataLen = length - 8;
            params = new byte[dataLen];
            buffer.get(params, pos, dataLen);

            // parsing params of this message
            message.decode(params);
        }
        return message;
    }
}
