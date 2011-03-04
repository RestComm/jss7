/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.protocols.ss7.isup;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * @author baranowb
 * @author kulikov
 */
public interface ISUPProvider {
	/**
	 * Sends message statelesly.
	 * 
	 * @param msg
	 * @throws ParameterException
	 * @throws IOException
	 */
	public void sendMessage(ISUPMessage msg) throws ParameterException, IOException;

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
	 * @param timerId
	 *            - integer id of timer. See {@link ISUPTimeoutEvent} static values.
	 * @return <ul>
	 * 				<li><b>true</b> - if timer was removed</li>
	 * 				<li><b>false</b> - otherwise</li>
	 *         </ul>
	 */
	public boolean cancelTimer(int cic, int timerId);

}
