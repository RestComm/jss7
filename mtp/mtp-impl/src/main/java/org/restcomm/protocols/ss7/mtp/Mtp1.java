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

package org.restcomm.protocols.ss7.mtp;

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
