/**
 * Start time:13:27:33 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:27:33 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface MCIDResponseIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x3C;
	/**
	 * Flag that indicates that information is requested
	 */
	public static final boolean _INDICATOR_PROVIDED = true;

	/**
	 * Flag that indicates that information is not requested
	 */
	public static final boolean _INDICATOR_NOT_PROVIDED = false;

	public boolean isMcidIncludedIndicator();

	public void setMcidIncludedIndicator(boolean mcidIncludedIndicator);

	public boolean isHoldingProvidedIndicator();

	public void setHoldingProvidedIndicator(boolean holdingProvidedIndicator);
}
