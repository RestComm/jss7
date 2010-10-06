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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ParseException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author baranowb
 * 
 */
public abstract class AbstractSccpProvider implements SccpProvider {
	// Oleg dont remove this class!

	//TODO: check again if we can make it generic as it was before...
	private static final Logger logger = Logger.getLogger(AbstractSccpProvider.class);
	protected MessageFactoryImpl messageFactory = new MessageFactoryImpl();
	protected ParameterFactoryImpl parameterFactory = new ParameterFactoryImpl();
    
	/**
	 * @param sccpStack
	 */
	public AbstractSccpProvider(SccpStackImpl sccpStack) {
		// TODO Auto-generated constructor stub
	}
	public AbstractSccpProvider() {
		// TODO Auto-generated constructor stub
	}
	// ---------------------------------------
	// Implemented methods
	// ---------------------------------------


	@Override
	public MessageFactory getMessageFactory() {
		
		return this.messageFactory;
	}

	@Override
	public ParameterFactory getParameterFactory() {
		
		return this.parameterFactory;
	}

	// ---------------------------------------
	// Not API methods,
	// ---------------------------------------
	/**
	 * Starts SCCP layer.
	 * 
	 * @throws IllegalStateException
	 *             if the SCCP has no assigned linksets.
	 */
	public abstract void start() throws StartFailedException;

	/**
	 * Stops SCCP layer.
	 * 
	 */
	public abstract void stop();

	/**
	 * @param properties
	 * @throws ConfigurationException 
	 */
	abstract void configure(Properties properties) throws ConfigurationException;
	
	protected SccpMessageImpl parse(ByteArrayInputStream data) throws ParseException {
		//baranowb: if I ever again see "catch&eat exception code" I will pay mafia to bring me your b....
		
		SccpMessageImpl message = null;

		try {
			// wrap stream with DataInputStream
			DataInputStream in = new DataInputStream(data);
			int sio = 0;
			sio = in.read() & 0xff;

			// getting service indicator
			int si = sio & 0x0f;

			// ignore message if this is not sccp service.
			if (si != 3) {
				return null;
			}

			// determine message type
			int mt = 0;

			mt = in.readUnsignedByte();
	

			message = messageFactory.createMessage(mt, in);

		} catch (IOException e) {
		
			throw new ParseException("Failed to parse buffer into valid SCCP message.",e);
		}
		return message;
	}
}
