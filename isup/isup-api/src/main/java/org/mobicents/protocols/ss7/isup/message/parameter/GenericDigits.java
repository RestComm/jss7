/**
 * Start time:12:37:11 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:37:11 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface GenericDigits extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0xC1;
	
	/**
	 * See Q.763 3.24 Encoding scheme : BCD even: (even number of digits)
	 */
	public static final int _ENCODING_SCHEME_BCD_EVEN = 0;

	/**
	 * See Q.763 3.24 Encoding scheme : BCD odd: (odd number of digits)
	 */
	public static final int _ENCODING_SCHEME_BCD_ODD = 1;

	/**
	 * See Q.763 3.24 Encoding scheme : IA5 character
	 */
	public static final int _ENCODING_SCHEME_IA5 = 2;

	/**
	 * See Q.763 3.24 Encoding scheme : binary coded
	 */
	public static final int _ENCODING_SCHEME_BINARY = 3;

	/**
	 * See Q.763 3.24 Type of digits : reserved for account code
	 */
	public static final int _TOD_ACCOUNT_CODE = 0;

	/**
	 * See Q.763 3.24 Type of digits : reserved for authorisation code
	 */
	public static final int _TOD_AUTHORIZATION_CODE = 1;

	/**
	 * See Q.763 3.24 Type of digits : reserved for private networking
	 * travelling class mark
	 */
	public static final int _TOD_PNTCM = 2;

	/**
	 * See Q.763 3.24 Type of digits : reserved for business communication group
	 * identity
	 */
	public static final int _TOD_BGCI = 3;
	
	public int getEncodingScheme();

	public void setEncodingScheme(int encodingScheme) ;

	public int getTypeOfDigits() ;

	public void setTypeOfDigits(int typeOfDigits) ;

	public int[] getDigits() ;

	public void setDigits(int[] digits) ;

}
