/**
 * Start time:08:56:08 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup;

import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * Start time:08:56:08 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface ISUPTransaction {
	/**
	 * Get unique transaction key associated with this transaction
	 * @return
	 */
	public TransactionKey getTransactionKey();
	/**
	 * Determine if this transaction is server.
	 * @return
	 */
	public boolean isServerTransaction();
	/**
	 * Get original message whcih started this transaction
	 * @return
	 */
	public ISUPMessage getOriginalMessage();
	/**
	 * Determine if transaction has terminated properly.
	 * @return
	 */
	public boolean isTerminated();
	/**
	 * Determine if transaction has terminated properly.
	 * @return
	 */
	public boolean isTimedout();
	
}
