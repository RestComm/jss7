/**
 * Start time:12:16:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:12:16:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CCSS extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x4B;

	/**
	 * See Q.763 3.83 CCNR possible indicator : not possible
	 */
	public static final boolean _NOT_CCSS_CALL = false;
	/**
	 * See Q.763 3.83 CCNR possible indicator : possible
	 */
	public static final boolean _CCSS_CALL = true;
	
	public boolean isCcssCall();

	public void setCcssCall(boolean ccssCall);
}
