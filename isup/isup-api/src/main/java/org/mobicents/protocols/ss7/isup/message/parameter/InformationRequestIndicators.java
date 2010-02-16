/**
 * Start time:13:17:28 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:17:28 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface InformationRequestIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x0E;

	/**
	 * Flag that indicates that information is requested
	 */
	public static final boolean _INDICATOR_REQUESTED = true;

	/**
	 * Flag that indicates that information is not requested
	 */
	public static final boolean _INDICATOR_NOT_REQUESTED = false;
	
	public boolean isCallingPartAddressRequestIndicator();

	public void setCallingPartAddressRequestIndicator(boolean callingPartAddressRequestIndicator);

	public boolean isHoldingIndicator();

	public void setHoldingIndicator(boolean holdingIndicator);

	public boolean isCallingpartysCategoryRequestIndicator();

	public void setCallingpartysCategoryRequestIndicator(boolean callingpartysCategoryRequestIndicator);

	public boolean isChargeInformationRequestIndicator();

	public void setChargeInformationRequestIndicator(boolean chargeInformationRequestIndicator);

	public boolean isMaliciousCallIdentificationRequestIndicator();

	public void setMaliciousCallIdentificationRequestIndicator(boolean maliciousCallIdentificationRequestIndicator);

	public int getReserved();

	public void setReserved(int reserved);

}
