/**
 * Start time:12:39:58 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:39:58 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface DisplayInformation extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x73;
	
	public byte[] getInfo() ;

	public void setInfo(byte[] info) throws IllegalArgumentException ;
}
