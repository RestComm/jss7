/**
 * Start time:11:54:44 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:54:44 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallHistoryInformation extends ISUPParameter {
	// FIXME: add code?
	public static final int _PARAMETER_CODE = 0x2D;

	public int getCallHistory();

	public void setCallHistory(int callHistory);

}
