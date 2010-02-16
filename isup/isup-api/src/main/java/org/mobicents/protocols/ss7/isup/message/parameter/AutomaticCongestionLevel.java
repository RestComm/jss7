/**
 * Start time:10:58:00 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:10:58:00 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface AutomaticCongestionLevel extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x27;
	public static final int _CONGESTION_LEVE_1_EXCEEDED = 1;
	public static final int _CONGESTION_LEVE_2_EXCEEDED = 2;

	public int getAutomaticCongestionLevel();

	public void setAutomaticCongestionLevel(int automaticCongestionLevel);
}
