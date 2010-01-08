/**
 * Start time:13:26:06 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:26:06 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface MCIDRequestIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x3B;
	/**
	 * Flag that indicates that information is requested
	 */
	public static final boolean _INDICATOR_REQUESTED = true;

	/**
	 * Flag that indicates that information is not requested
	 */
	public static final boolean _INDICATOR_NOT_REQUESTED = false;

	public boolean isMcidRequestIndicator();

	public void setMcidRequestIndicator(boolean mcidRequestIndicator);

	public boolean isHoldingIndicator();

	public void setHoldingIndicator(boolean holdingIndicator);

}
