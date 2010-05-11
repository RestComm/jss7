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
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author baranowb
 * @author kulikov
 */
public interface ISUPProvider {
	/**
	 * Stateles message send over MTP. No state is maintained.
	 * @param msg
	 * @throws ParameterRangeInvalidException
	 * @throws IOException
	 */
	public void sendMessage(ISUPMessage msg) throws ParameterRangeInvalidException, IOException;
//	/**
//	 * Stateles message send over SCCP. No state is maintained.
//	 * @param called
//	 * @param calling
//	 * @param msg
//	 * @throws ParameterRangeInvalidException
//	 * @throws IOException
//	 */
//	public void sendMessage(SccpAddress called,SccpAddress calling,ISUPMessage msg) throws ParameterRangeInvalidException, IOException;
//	/**
//	 * Send message with use of session, it will allow us to receive timeout in case of bad behaviour.
//	 * @param msg
//	 * @throws ParameterRangeInvalidException
//	 * @throws IOException
//	 */
//	public void sendMessage(ISUPTransaction msg) throws ParameterRangeInvalidException, IOException;
	
	public void addListener(ISUPListener listener);
	public void removeListener(ISUPListener listener);
	
	public ISUPParameterFactory getParameterFactory(); 
	public ISUPMessageFactory getMessageFactory();
	public ISUPClientTransaction createClientTransaction(ISUPMessage msg) throws   TransactionAlredyExistsException, IllegalArgumentException; 
	public ISUPServerTransaction createServerTransaction(ISUPMessage msg) throws   TransactionAlredyExistsException, IllegalArgumentException;


    
	
	//FIXME: add sccp methods for connection creation
	
}
