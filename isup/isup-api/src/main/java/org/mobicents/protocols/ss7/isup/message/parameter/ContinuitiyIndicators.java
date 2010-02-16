/**
 * Start time:12:34:42 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:34:42 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ContinuitiyIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x10;

	/**
	 * See Q.763 3.18
	 */
	public static final boolean _CONTINUITY_CHECK_FAILED = false;

	/**
	 * See Q.763 3.18
	 */
	public static final boolean _CONTINUITY_CHECK_SUCCESSFUL = true;

	public boolean isContinuityCheck();

	public void setContinuityCheck(boolean continuityCheck);
}
