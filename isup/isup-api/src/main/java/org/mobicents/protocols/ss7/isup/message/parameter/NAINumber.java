/**
 * Start time:11:15:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:15:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface NAINumber extends Number {
	
	/**
	 * 0 0 0 0 0 0 1 subscriber number (national use)
	 */
	public static final int _NAI_SUBSCRIBER_NUMBER = 1;
	/**
	 * 0 0 0 0 0 1 0 unknown (national use)
	 */
	public static final int _NAI_UNKNOWN = 2;
	/**
	 * 0 0 0 0 0 1 1 national (significant) number
	 */
	public static final int _NAI_NATIONAL_SN = 3;
	
	/**
	 * 0 0 0 0 1 0 0 international number
	 */
	public static final int _NAI_INTERNATIONAL_NUMBER = 4;
	/**
	 * 0 0 0 0 1 0 1 network-specific number (national use) ITU-T Q.763 (12/1999) 23
	 */
	public static final int _NAI_NETWORK_SPECIFIC_NUMBER_NU = 5;
	
	/**
	 * 0 0 0 0 1 1 0 network routing number in national (significant) number format (national use)
	 */
	public static final int _NAI_NRNINSNF = 6;
	/**
	 * 0 0 0 0 1 1 1 network routing number in network-specific number format (national use)
	 */
	public static final int _NAI_NRNIN_SNF = 7;
	/**
	 * 0 0 0 1 0 0 0 network routing number concatenated with Called Directory Number (national use)
	 */
	public static final int _NAI_NRNCWCDN = 8;
	
	public int getNatureOfAddressIndicator();

	public void setNatureOfAddresIndicator(int natureOfAddresIndicator);
}
