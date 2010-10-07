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
package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public class SccpProviderImpl implements SccpProvider, Serializable {
    private SccpStackImpl stack;
    private HashMap<SccpAddress, SccpListener> listeners = new HashMap();
    
    private MessageFactoryImpl messageFactory;
    private ParameterFactoryImpl parameterFactory;
    
    SccpProviderImpl(SccpStackImpl stack) {
        this.stack = stack;
        this.messageFactory = new MessageFactoryImpl();
        this.parameterFactory = new ParameterFactoryImpl();
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public ParameterFactory getParameterFactory() {
        return parameterFactory;
    }

    public void registerSccpListener(SccpAddress localAddress, SccpListener listener) {
        listeners.put(localAddress, listener);
    }

    public void deregisterSccpListener(SccpAddress localAddress) {
        listeners.remove(localAddress);
    }

    /**
     * Sends notification to listeners.
     * 
     * @param address the address associated with listener.
     * @param message the message to deliver to listener
     */
    protected void notify(SccpAddress address, SccpMessage message) {
        SccpListener listener = listeners.get(address);
        listener.onMessage(message);
    }

    public void send(SccpMessage message) throws IOException {
        stack.send(message);
    }
}
