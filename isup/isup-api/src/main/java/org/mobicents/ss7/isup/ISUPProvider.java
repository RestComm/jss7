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
package org.mobicents.ss7.isup;

import java.io.IOException;

import org.mobicents.ss7.SS7Provider;
import org.mobicents.ss7.isup.message.ISUPMessage;

/**
 *
 * @author kulikov
 */
public interface ISUPProvider {
	/**
	 * Stateles messages send. No state is maintained.
	 * @param msg
	 * @throws ParameterRangeInvalidException
	 * @throws IOException
	 */
	public void sendMessage(ISUPMessage msg) throws ParameterRangeInvalidException, IOException;
//	/**
//	 * Send message with use of session, it will allow us to receive timeout in case of bad behaviour.
//	 * @param msg
//	 * @throws ParameterRangeInvalidException
//	 * @throws IOException
//	 */
//	public void sendMessage(ISUPTransaction msg) throws ParameterRangeInvalidException, IOException;
	//For mtp?
	public SS7Provider getTransportProvider();
	
	public void addListener(ISUPListener listener);
	public void removeListener(ISUPListener listener);
	
	public ISUPMessageFactory getMessageFactory();
	public ISUPClientTransaction createClientTransaction(ISUPMessage msg) throws   TransactionAlredyExistsException, IllegalArgumentException; 
	public ISUPServerTransaction createServerTransaction(ISUPMessage msg) throws   TransactionAlredyExistsException, IllegalArgumentException; 
    
}
