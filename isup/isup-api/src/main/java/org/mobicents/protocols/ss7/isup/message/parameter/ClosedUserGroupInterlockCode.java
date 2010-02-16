/**
 * Start time:12:25:27 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:25:27 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ClosedUserGroupInterlockCode extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x1A;

	public byte[] getNiDigits();

	public void setNiDigits(byte[] niDigits);

	public int getBinaryCode();

	public void setBinaryCode(int binaryCode);
}
