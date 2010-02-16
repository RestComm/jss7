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
	public TransactionID getTransactionID();
	//public TransactionKey getTransactionKey();
	public boolean isServerTransaction();
	public ISUPMessage getOriginalMessage();
	public boolean isTerminated();
	public boolean isTimedout();
	
}
