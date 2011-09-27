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

package org.mobicents.protocols.ss7.m3ua.impl.sctp;

import java.io.IOException;

import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;
import org.mobicents.protocols.ss7.m3ua.M3UAServerChannel;
import org.mobicents.protocols.ss7.m3ua.impl.M3UASelectorImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;

/**
 * @author amit bhayani
 *
 */
public class SctpProvider implements M3UAProvider {
	
	private static final SctpProvider provider = new SctpProvider();  
	
    private MessageFactoryImpl msgFactory = new MessageFactoryImpl();
    private ParameterFactoryImpl paramFactory = new ParameterFactoryImpl();
    
    private SctpProvider(){
    	
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

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#openChannel()
	 */
	@Override
	public M3UAChannel openChannel() throws IOException {
		return SctpChannel.open(this);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#openServerChannel()
	 */
	@Override
	public M3UAServerChannel openServerChannel() throws IOException {
		return SctpServerChannel.open(this);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#openSelector()
	 */
	@Override
	public M3UASelector openSelector() throws IOException {
		return M3UASelectorImpl.open();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#getMessageFactory()
	 */
	@Override
	public MessageFactory getMessageFactory() {
		return msgFactory;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAProvider#getParameterFactory()
	 */
	@Override
	public ParameterFactory getParameterFactory() {
		return paramFactory;
	}

}
