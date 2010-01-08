/**
 * Start time:12:32:51 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:12:32:51 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ConnectionRequest extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x0D;

	public int getLocalReference();

	public void setLocalReference(int localReference);

	public int getSignalingPointCode();

	public void setSignalingPointCode(int signalingPointCode);

	public int getProtocolClass();

	public void setProtocolClass(int protocolClass);

	public int getCredit();

	public void setCredit(int credit);
}
