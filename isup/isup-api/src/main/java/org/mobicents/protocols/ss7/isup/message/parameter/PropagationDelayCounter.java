/**
 * Start time:13:50:26 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:50:26 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface PropagationDelayCounter extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x31;

	public int getPropagationDelay();

	public void setPropagationDelay(int propagationDelay);

}
