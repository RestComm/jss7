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

package org.mobicents.ss7.management.transceiver;

import java.nio.ByteBuffer;

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
     * Encodes this message. The protocol is first 4 bytes are length of this
     * command followed by the byte stream of command
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

}
