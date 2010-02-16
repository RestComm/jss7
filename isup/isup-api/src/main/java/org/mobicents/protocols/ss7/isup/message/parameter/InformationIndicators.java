/**
 * Start time:13:16:08 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:16:08 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface InformationIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x0F;

	/**
	 * See Q.763 3.28 Calling party address response indicator : calling party
	 * address not included
	 */
	public static final int _CPARI_ADDRESS_NOT_INCLUDED = 0;

	/**
	 * See Q.763 3.28 Calling party address response indicator : calling party
	 * address included
	 */
	public static final int _CPARI_ADDRESS_INCLUDED = 3;
	/**
	 * See Q.763 3.28 Calling party address response indicator : calling party
	 * address not available
	 */
	public static final int _CPARI_ADDRESS_NOT_AVAILABLE = 1;

	/**
	 * See Q.763 3.28 Hold provided indicator : hold not provided
	 */
	public static final boolean _HPI_NOT_PROVIDED = false;
	/**
	 * See Q.763 3.28 Hold provided indicator : hold provided
	 */
	public static final boolean _HPI_PROVIDED = true;

	/**
	 * See Q.763 3.28 Charge information response indicator : charge information
	 * not included
	 */
	public static final boolean _CIRI_NOT_INCLUDED = false;
	/**
	 * See Q.763 3.28 Charge information response indicator : charge information
	 * included
	 */
	public static final boolean _CIRI_INCLUDED = true;

	/**
	 * See Q.763 3.28 Solicited information indicator : solicited
	 */
	public static final boolean _SII_SOLICITED = false;
	/**
	 * See Q.763 3.28 Solicited information indicator : unsolicited
	 */
	public static final boolean _SII_UNSOLICITED = true;
	/**
	 * See Q.763 3.28 Calling party's category response indicator : calling
	 * party's category not included
	 */
	public static final boolean _CPCRI_CATEOGRY_NOT_INCLUDED = false;

	/**
	 * See Q.763 3.28 Calling party's category response indicator : calling
	 * party's category not included
	 */
	public static final boolean _CPCRI_CATEOGRY_INCLUDED = true;
	
	public int getCallingPartyAddressResponseIndicator();

	public void setCallingPartyAddressResponseIndicator(int callingPartyAddressResponseIndicator);

	public boolean isHoldProvidedIndicator();

	public void setHoldProvidedIndicator(boolean holdProvidedIndicator);

	public boolean isCallingPartysCategoryResponseIndicator();

	public void setCallingPartysCategoryResponseIndicator(boolean callingPartysCategoryResponseIndicator);

	public boolean isChargeInformationResponseIndicator();

	public void setChargeInformationResponseIndicator(boolean chargeInformationResponseIndicator);

	public boolean isSolicitedInformationIndicator();

	public void setSolicitedInformationIndicator(boolean solicitedInformationIndicator);

	public int getReserved();

	public void setReserved(int reserved);

}
