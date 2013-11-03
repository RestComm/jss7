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

package org.mobicents.protocols.ss7.mtp;

import java.io.IOException;

import org.mobicents.protocols.stream.api.Stream;

/**
 * @author baranowb
 * @author kulikov
 */
public interface Mtp1 extends Stream {
    // FIXME: Oleg what's that?
    /**
     * Gets the code of this channel.
     *
     * @return the code of this channel.
     */
    int getCode();

    /**
     * Set MTP2 layer serving this MTP1
     *
     * @param link
     */
    void setLink(Object link);

    /**
     * Get MTP2 layer serving this MTP1
     *
     * @return
     */
    Object getLink();

    /**
     * Fetches implementation dependent IO Buffer size which should be used
     *
     * @return integer number, Mtp2 implementation should assign buffers of this size to interact with Mtp1
     */
    int getIOBufferSize();

    /**
     * Reads up to buffer.length bytes from layer 1.
     *
     * @param buffer reader buffer
     * @return the number of actually read bytes.
     */
    int read(byte[] buffer) throws IOException;

    /**
     * Writes data to layer 1.
     *
     * @param buffer the buffer containing data to write.
     * @param bytesToWrite
     */
    void write(byte[] buffer, int bytesToWrite) throws IOException;

    /**
     * Open message transfer part layer 1.
     */
    void open() throws IOException;

    /**
     * Close message transfer part layer 1.
     */
    void close();

}
