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
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;
import org.mobicents.protocols.ss7.m3ua.M3UAServerChannel;
import org.mobicents.protocols.ss7.m3ua.impl.M3UASelectorImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.parm.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.impl.message.parms.ParameterFactoryImpl;

/**
 * Implements M3UAProvider for TCP/IP undelying network.
 * 
 * @author kulikov
 */
public class TcpProvider implements M3UAProvider {

    public static TcpProvider open() {
        return new TcpProvider();
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#openChannel() 
     */
    public M3UAChannel openChannel() throws IOException {
        return TcpChannel.open(this);
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#openServerChannel() 
     */
    public M3UAServerChannel openServerChannel() throws IOException {
        return TcpServerChannel.open(this);
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#openServerChannel() 
     */
    public M3UASelector openSelector() throws IOException {
        return M3UASelectorImpl.open();
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#getMessageFactory() 
     */
    public MessageFactory getMessageFactory() {
        return new MessageFactoryImpl();
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#getParameterFactory() 
     */
    public ParameterFactory getParameterFactory() {
        return new ParameterFactoryImpl();
    }
    
}
