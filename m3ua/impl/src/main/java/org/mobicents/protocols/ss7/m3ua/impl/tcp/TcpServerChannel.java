/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.m3ua.impl.tcp;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAChannelImpl;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAServerChannelImpl;

/**
 * Implements M3UA server channel over TCP/IP
 * @author kulikov
 */
public class TcpServerChannel extends M3UAServerChannelImpl {
    private TcpProvider provider;
    /**
     * Creates new server channel.
     * 
     * @param channel the undelying TCP server socket channel
     * @throws java.io.IOException
     */
    protected TcpServerChannel(TcpProvider provider, AbstractSelectableChannel channel) throws IOException {        
        super(channel);
        this.provider = provider;
    }
    
    /**
     * Opens new M3UA channel.
     * 
     * @return the new M3UA channel
     * @throws java.io.IOException
     */
    public static TcpServerChannel open(TcpProvider provider) throws IOException {
        return new TcpServerChannel(provider, ServerSocketChannel.open());
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAServerChannel#accept() 
     */
    public M3UAChannelImpl accept() throws IOException {
        return new TcpChannel(provider,((ServerSocketChannel)channel).accept());
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAServerChannel#bind(java.net.SocketAddress) 
     */
    public void bind(SocketAddress address) throws IOException {
        ((ServerSocketChannel)channel).socket().bind(address);
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAServerChannel#close() 
     */
    public void close() throws IOException {
        ((ServerSocketChannel)channel).close();
        ((ServerSocketChannel)channel).socket().close();
    }

}
