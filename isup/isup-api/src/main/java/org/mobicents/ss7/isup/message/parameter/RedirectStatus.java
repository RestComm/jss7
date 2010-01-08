/**
 * Start time:13:59:51 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:59:51 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface RedirectStatus extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x8A;

	/**
	 * See Q.763 3.98 Redirect status indicator : not used
	 */
	public static int _RSI_NOT_USED = 0;
	/**
	 * See Q.763 3.98 Redirect status indicator : ack of redirection
	 */
	public static int _RSI_AOR = 1;
	/**
	 * See Q.763 3.98 Redirect status indicator : redirection will not be
	 * invoked
	 */
	public static int _RSI_RWNBI = 2;

	public byte[] getStatus();

	public void setStatus(byte[] status);

	public int getStatusIndicator(byte b);
}
