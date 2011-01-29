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

package org.mobicents.protocols.ss7.m3ua.impl;

import java.io.IOException;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;

/**
 * The base implementation for the M3UAChannel based on NIO.
 * 
 * @author kulikov
 */
public abstract class M3UAChannelImpl extends M3UASelectableChannelImpl implements M3UAChannel {
    
    //Queue for incoming messages
    protected ConcurrentLinkedQueue<M3UAMessageImpl> rxQueue = new ConcurrentLinkedQueue();
    //Queue for outgoing messages
    protected ConcurrentLinkedQueue<M3UAMessageImpl> txQueue = new ConcurrentLinkedQueue();
    
    
    /**
     * Constructs new M3UA channel.
     * 
     * @param channel the underlying network channel.
     * @throws java.io.IOException
     */
    protected M3UAChannelImpl(AbstractSelectableChannel channel) throws IOException {
        this.channel = channel;
        this.channel.configureBlocking(false);
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#receive() 
     */
    public M3UAMessage receive() throws IOException {
        //extracts next message from queue
        return rxQueue.poll();
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#send(org.mobicents.protocols.ss7.m3ua.message.M3UAMessage)  
     */
    public void send(M3UAMessage message) throws IOException {
        //queue next message for sending
        txQueue.offer((M3UAMessageImpl) message);
        System.out.println("M3UAChannelImpl has txQueue size is " + txQueue.size());
    
    }
    
    /**
     * Implements undelying network read operation.
     * 
     * @throws java.io.IOException
     */
    protected abstract void doRead() throws IOException;
    
    /**
     * Implements undelying network write operation.
     * 
     * @throws java.io.IOException
     */
    protected abstract void doWrite() throws IOException;
    
    /**
     * Tests if this channel contains data available for reading
     * 
     * @return true if this channel contains data which can be read by user.
     */
    protected boolean isReadable() {
        return !rxQueue.isEmpty();
    }
    
    /**
     * Tests if write operation available for this channel
     * 
     * @return true if this channel allows to user to use write operation.
     */
    protected boolean isWritable() {
        return txQueue.isEmpty();
    }
    
    
}
