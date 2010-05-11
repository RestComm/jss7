/**
 * Start time:08:53:28 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup;

/**
 * Start time:08:53:28 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class TransactionAlredyExistsException extends Exception {

	/**
	 * 	
	 * @param string
	 */
	public TransactionAlredyExistsException(String string) {
		
	}

	public TransactionAlredyExistsException() {
		super();
		
	}

	public TransactionAlredyExistsException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public TransactionAlredyExistsException(Throwable cause) {
		super(cause);
		
	}

}
