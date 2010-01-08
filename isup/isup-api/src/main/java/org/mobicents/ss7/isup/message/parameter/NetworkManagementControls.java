/**
 * Start time:13:32:08 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:32:08 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface NetworkManagementControls extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x5B;
	
	public boolean isTARControlEnabled(byte b) ;

	public byte createTAREnabledByte(boolean enabled) ;

	public byte[] getNetworkManagementControls() ;

	public void setNetworkManagementControls(byte[] networkManagementControls) throws IllegalArgumentException ;
	
}
