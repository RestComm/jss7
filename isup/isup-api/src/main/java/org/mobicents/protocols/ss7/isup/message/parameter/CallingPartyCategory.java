/**
 * Start time:11:56:46 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:56:46 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallingPartyCategory extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x09;

	
	/**
	 * See Q.763 3.11
	 */
	public static final byte _CATEGORY_UNKNOWN = 0;

	/**
	 * See Q.763 3.11 operator, language French
	 */
	public static final byte _CATEGORY_OL_FRENCH = 1;

	/**
	 * See Q.763 3.11 operator, language English
	 */
	public static final byte _CATEGORY_OL_ENGLISH = 2;

	/**
	 * See Q.763 3.11 operator, language German
	 */
	public static final byte _CATEGORY_OL_GERMAN = 3;

	/**
	 * See Q.763 3.11 operator, language Russian
	 */
	public static final byte _CATEGORY_OL_RUSSIAN = 4;

	/**
	 * See Q.763 3.11 operator, language Spanish
	 */
	public static final byte _CATEGORY_OL_SPANISH = 5;
	
	public byte getCallingPartyCategory();

	public void setCallingPartyCategory(byte callingPartyCategory);
}
