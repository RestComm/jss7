/**
 * Start time:14:37:23 2009-04-20<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup;

/**
 * Start time:14:37:23 2009-04-20<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class ParameterException extends Exception {
	
	//TODO: add more info here.
	//private int paramCode;
	//private int msgCode;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8747311560315513861L;

	public ParameterException() {
		super();
		
	}

	public ParameterException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ParameterException(String message) {
		super(message);
		
	}

	public ParameterException(Throwable cause) {
		super(cause);
		
	}

}
