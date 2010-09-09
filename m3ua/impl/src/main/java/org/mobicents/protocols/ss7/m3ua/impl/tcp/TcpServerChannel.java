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
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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
