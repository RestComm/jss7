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

package org.mobicents.protocols.ss7.m3ua;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * A selectable channel for M3UA listening sockets.
 * 
 * @author kulikov
 */
public interface M3UAServerChannel extends M3UASelectableChannel {
    /**
     * Accepts a connection made to this channel's socket.
     * 
     * The channel returned by this method, if any, will be in non-blocking mode.
     * 
     * @return The M3UA channel for the new connection, or null if no connection is available 
     * to be accepted
     * @throws java.io.IOException
     */
    public M3UAChannel accept() throws IOException;
    
    /**
     * Binds the channel to a local address.
     * 
     * @param address the SocketAddress to bind to
     * @throws java.io.IOException
     */
    public void bind(SocketAddress address) throws IOException;
    
    /**
     * Closes this channel.
     * 
     * If the channel has already been closed then this method returns immediately. 
     * @throws java.io.IOException
     */
    public void close() throws IOException;

}

