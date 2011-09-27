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

package org.mobicents.protocols.ss7.m3ua.impl.tcp;

import java.io.IOException;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;
import org.mobicents.protocols.ss7.m3ua.M3UAServerChannel;
import org.mobicents.protocols.ss7.m3ua.impl.M3UASelectorImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;

/**
 * Implements M3UAProvider for TCP/IP undelying network.
 * 
 * @author amit bhayani
 * @author kulikov
 */
public class TcpProvider implements M3UAProvider {

    static private TcpProvider provider = new TcpProvider();

    private MessageFactoryImpl msgFactory = new MessageFactoryImpl();
    private ParameterFactoryImpl paramFactory = new ParameterFactoryImpl();

    private TcpProvider() {

    }

    /**
     * Returns the system-wide default selector provider for this invocation of
     * the Java virtual machine.
     * 
     * @return
     */
    public static M3UAProvider provider() {
        return provider;
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
        return msgFactory;
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#getParameterFactory()
     */
    public ParameterFactory getParameterFactory() {
        return paramFactory;
    }

}
