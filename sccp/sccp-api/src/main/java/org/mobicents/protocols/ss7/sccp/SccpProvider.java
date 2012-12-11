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

package org.mobicents.protocols.ss7.sccp;

import java.io.IOException;
import java.io.Serializable;

import javolution.util.FastList;

import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public interface SccpProvider extends Serializable {

	/**
	 * Gets the access to message factory.
	 * 
	 * @return message factory.
	 */
	public MessageFactory getMessageFactory();

	/**
	 * Gets the access to parameter factory.
	 * 
	 * @return parameter factory
	 */
	public ParameterFactory getParameterFactory();

	/**
	 * Register listener for some adddress.
	 * 
	 * @param listener
	 */
	public void registerSccpListener(int ssn, SccpListener listener);

	/**
	 * Removes listener
	 */
	public void deregisterSccpListener(int ssn);

	public void registerManagementEventListener(SccpManagementEventListener listener);

	public void deregisterManagementEventListener(SccpManagementEventListener listener);

	/**
	 * Sends message.
	 * 
	 * @param message
	 *            Message to be sent
	 * @throws IOException
	 */
	public void send(SccpDataMessage message) throws IOException;

	/**
	 * Return the maximum length (in bytes) of the sccp message data
	 * 
	 * @param calledPartyAddress
	 * @param callingPartyAddress
	 * @return
	 */
	public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress);

}
