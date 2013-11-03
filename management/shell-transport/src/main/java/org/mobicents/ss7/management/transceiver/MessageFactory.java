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

package org.mobicents.ss7.management.transceiver;

import java.nio.ByteBuffer;

/**
 * <p>
 * Construct the Shell Command Message
 * </p>
 *
 * <p>
 * A given invocation of the Java virtual machine maintains a single system-wide default message factory instance, which is
 * returned by the {@link ChannelProvider#getMessageFactory() getMessageFactory} method.
 * </p>
 *
 * @author amit bhayani
 *
 */
public class MessageFactory {

    private byte[] header = new byte[4];
    private boolean isHeaderReady = false;

    private int pos = 0;
    private int length;

    private byte[] params;

    private Message message;

    protected MessageFactory() {

    }

    /**
     * Crate new instance of message object
     *
     * @param message
     * @return
     */
    public Message createMessage(String message) {
        return new Message(message.getBytes());
    }

    /**
     * The received buffer may not have necessary bytes to decode a message. Instance of this factory keeps data locally till
     * next set of data is received and a message can be successfully decoded
     *
     * @param buffer
     * @return
     */
    public Message createMessage(ByteBuffer buffer) {

        if (!isHeaderReady) {
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

            // obtain remaining length of the message and prepare buffer
            length = ((header[0] & 0xff) << 24);
            length += ((header[1] & 0xff) << 16);
            length += ((header[2] & 0xff) << 8);
            length += (header[3] & 0xff);

            length -= 4;

            params = new byte[length];

            // finally switch cursor position
            pos = 0;

            message = new Message();
        }

        // at this point we must recheck remainder of the input buffer
        // because possible case when input buffer fits exactly to the header
        if (length > 0 && !buffer.hasRemaining()) {
            return null;
        }

        // again, reading all parameters before parsing

        // compute available or required data
        int len = Math.min((params.length - pos), buffer.remaining());
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

}
