/**
 * Start time:13:54:36 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:54:36 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface RedirectCapability extends ISUPParameter {

	public static final int _PARAMETER_CODE = 0x4E;

	/**
	 * See Q.763 3.96 Redirect possible indicator : not used
	 */
	public static final int _RPI_NOT_USED = 0;
	/**
	 * See Q.763 3.96 Redirect possible indicator : redirect possible before
	 * ACM use)
	 */
	public static final int _RPI_RPB_ACM = 1;
	/**
	 * See Q.763 3.96 Redirect possible indicator : redirect possible before
	 * ANM
	 */
	public static final int _RPI_RPB_ANM = 2;
	/**
	 * See Q.763 3.96 Redirect possible indicator : redirect possible at any
	 * time during the call
	 */
	public static final int _RPI_RPANTDC = 3;
	
	
	public byte[] getCapabilities();

	public void setCapabilities(byte[] capabilities);

	public int getCapability(byte b);
}
