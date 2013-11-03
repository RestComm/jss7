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
import java.util.Arrays;

/**
 * Represents the Shell Command protocol data unit
 *
 * @author amit bhayani
 *
 */
public class Message {
    protected byte[] data = null;

    protected Message() {

    }

    protected Message(byte[] data) {
        this.data = data;
    }

    /**
     * Decodes the received byte[]
     *
     * @param data
     */
    protected void decode(byte[] data) {
        this.data = data;
    }

    /**
     * Encodes this message. The protocol is first 4 bytes are length of this command followed by the byte stream of command
     *
     * @param txBuffer
     */
    protected void encode(ByteBuffer txBuffer) {

        txBuffer.position(4); // Length int
        txBuffer.put(data);

        int length = txBuffer.position();

        txBuffer.rewind();

        txBuffer.putInt(length);

        txBuffer.position(length);
    }

    @Override
    public String toString() {
        return new String(data);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(data);
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (!Arrays.equals(data, other.data))
            return false;
        return true;
    }

}
