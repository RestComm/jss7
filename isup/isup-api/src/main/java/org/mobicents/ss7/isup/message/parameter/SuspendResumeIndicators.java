/**
 * Start time:14:09:47 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:14:09:47 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface SuspendResumeIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x22;

	// FIXME: add C defs

	/**
	 * See Q.763 3.52 Suspend/resume indicator : ISDN subscriber initiated
	 */
	public static final boolean _SRI_ISDN_SI = false;

	/**
	 * See Q.763 3.52 Suspend/resume indicator : network initiated
	 */
	public static final boolean _SRI_NI = true;

	public boolean isSuspendResumeIndicator();

	public void setSuspendResumeIndicator(boolean suspendResumeIndicator);

}
