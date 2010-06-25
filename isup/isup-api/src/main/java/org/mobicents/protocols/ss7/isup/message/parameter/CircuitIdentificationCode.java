/**
 * Start time:14:43:22 2009-09-18<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:43:22 2009-09-18<br>
 * Project: mobicents-isup-stack<br>
 * This is not real parameter, its present to follow same way of defining message, null not present, != null present.
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CircuitIdentificationCode extends ISUPParameter {

	public int getCIC();
	public void setCIC(int cic);
	
}
