/**
 * Start time:13:52:59 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:52:59 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface RangeAndStatus extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x16;
	public byte getRange();

	public void setRange(byte range);

	public byte[] getStatus() ;

	public void setStatus(byte[] status) ;
}
