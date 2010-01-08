/**
 * Start time:12:11:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:12:11:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CauseIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x12;

	
	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_ITUT = 0;

	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_IOS_IEC = 1;

	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_NATIONAL = 2;
	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_SPECIFIC = 3;
	

	/**
	 * See Q.850
	 */
	public static final int _LOCATION_USER = 0;

	/**
	 * See Q.850 private network serving the local user (LPN)
	 */
	public static final int _LOCATION_PRIVATE_NSLU = 1;

	/**
	 * See Q.850 public network serving the local user (LN)
	 */
	public static final int _LOCATION_PUBLIC_NSLU = 2;

	/**
	 * See Q.850 transit network (TN)
	 */
	public static final int _LOCATION_TRANSIT_NETWORK = 3;

	/**
	 * See Q.850 private network serving the remote user (RPN)
	 */
	public static final int _LOCATION_PRIVATE_NSRU = 5;

	/**
	 * See Q.850 public network serving the remote user (RLN)
	 */
	public static final int _LOCATION_PUBLIC_NSRU = 4;
	/**
	 * See Q.850
	 */
	public static final int _LOCATION_INTERNATIONAL_NETWORK = 7;

	/**
	 * See Q.850 network beyond interworking point (BI)
	 */
	public static final int _LOCATION_NETWORK_BEYOND_IP = 10;

	
	
	public int getCodingStandard();

	public void setCodingStandard(int codingStandard);

	public int getLocation();

	public void setLocation(int location);

	public int getCauseValue();

	public void setCauseValue(int causeValue);

	public byte[] getDiagnostics();

	public void setDiagnostics(byte[] diagnostics);

}
