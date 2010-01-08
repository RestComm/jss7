/**
 * Start time:13:13:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:13:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface HopCounter extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x3D;
	
	public int getHopCounter();

	public void setHopCounter(int hopCounter);
	
}
