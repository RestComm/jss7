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

package org.mobicents.protocols.ss7.isup;

import java.io.IOException;
import java.io.Serializable;

import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * @author baranowb
 * @author kulikov
 */
public interface ISUPProvider extends Serializable {
	
	/**
	 * Returns localy configured network indicator.
	 * @return
	 */
	public int getNi();
	/**
	 * Returns local PC (OPC for outgoing messages)
	 * @return
	 */
	public int getLocalSpc();
	
	/**
	 * Sends message.
	 * 
	 * @param msg
	 * @throws ParameterException
	 * @throws IOException
	 */
	public void sendMessage(ISUPMessage msg,int dpc) throws ParameterException, IOException;

	/**
	 * Adds default listener.
	 * 
	 * @param listener
	 */
	public void addListener(ISUPListener listener);

	/**
	 * Removes listener.
	 * 
	 * @param listener
	 */
	public void removeListener(ISUPListener listener);

	/**
	 * Get factory for ISUP parameters.
	 * 
	 * @return
	 */
	public ISUPParameterFactory getParameterFactory();

	/**
	 * Get factory for ISUP messages.
	 * 
	 * @return
	 */
	public ISUPMessageFactory getMessageFactory();

	/**
	 * cancel timer. It is required for instance in case of T17 to allow it be
	 * explicitly canceled
	 * 
	 * @param cic
	 *            - circuit identification code
	 * @param dpc
	 *            - destination point code
	 * @param timerId
	 *            - integer id of timer. See {@link ISUPTimeoutEvent} static values.
	 * @return <ul>
	 * 				<li><b>true</b> - if timer was removed</li>
	 * 				<li><b>false</b> - otherwise</li>
	 *         </ul>
	 */
	public boolean cancelTimer(int cic, int dcp, int timerId);

}
