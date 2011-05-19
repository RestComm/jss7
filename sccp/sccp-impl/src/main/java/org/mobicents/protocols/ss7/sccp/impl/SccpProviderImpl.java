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

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;
import java.io.Serializable;

import javolution.util.FastMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public class SccpProviderImpl implements SccpProvider, Serializable {
	private static final Logger logger = Logger.getLogger(SccpProviderImpl.class);

	private transient SccpStackImpl stack;
	protected FastMap<Integer, SccpListener> ssnToListener = new FastMap<Integer, SccpListener>();

	private MessageFactoryImpl messageFactory;
	private ParameterFactoryImpl parameterFactory;

	SccpProviderImpl(SccpStackImpl stack) {
		this.stack = stack;
		this.messageFactory = stack.messageFactory;
		this.parameterFactory = new ParameterFactoryImpl();
	}

	public MessageFactory getMessageFactory() {
		return messageFactory;
	}

	public ParameterFactory getParameterFactory() {
		return parameterFactory;
	}

	public void registerSccpListener(int ssn, SccpListener listener) {
		SccpListener existingListener = ssnToListener.get(ssn);
		if (existingListener != null) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn(String.format("Registering SccpListener=%s for already existing SccpListnere=% for SSN=%d",
						listener, existingListener, ssn));
			}
		}

		ssnToListener.put(ssn, listener);

	}

	public void deregisterSccpListener(int ssn) {
		SccpListener existingListener = ssnToListener.remove(ssn);
		if (existingListener == null) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn(String.format("No existing SccpListnere=% for SSN=%d", existingListener, ssn));
			}
		}
	}

	public void send(SccpMessage message, int seqControl) throws IOException {
		SccpMessageImpl msg = ((SccpMessageImpl) message);
		msg.setSls(seqControl);
		stack.send(msg);
	}

}
