/**
 * 
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.TransactionAlredyExistsException;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * Provider for sccp
 * @author baranowb
 *
 */
public class ISUPSccpProviderImpl implements ISUPProvider {

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPProvider#addListener(org.mobicents.protocols.ss7.isup.ISUPListener)
	 */
	public void addListener(ISUPListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPProvider#createClientTransaction(org.mobicents.protocols.ss7.isup.message.ISUPMessage)
	 */
	public ISUPClientTransaction createClientTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPProvider#createServerTransaction(org.mobicents.protocols.ss7.isup.message.ISUPMessage)
	 */
	public ISUPServerTransaction createServerTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPProvider#getMessageFactory()
	 */
	public ISUPMessageFactory getMessageFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPProvider#removeListener(org.mobicents.protocols.ss7.isup.ISUPListener)
	 */
	public void removeListener(ISUPListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPProvider#sendMessage(org.mobicents.protocols.ss7.isup.message.ISUPMessage)
	 */
	public void sendMessage(ISUPMessage msg) throws ParameterRangeInvalidException, IOException {
		// TODO Auto-generated method stub

	}

}
