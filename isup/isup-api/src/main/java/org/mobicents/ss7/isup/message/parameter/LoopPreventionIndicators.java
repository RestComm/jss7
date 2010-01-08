/**
 * Start time:13:24:32 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:24:32 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface LoopPreventionIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x44;

	public static final boolean _TYPE_REQUEST = false;
	public static final boolean _TYPE_RESPONSE = true;

	/**
	 * See Q.763 3.67 Response indicator : insufficient information (note)
	 */
	public static final int _RI_INS_INFO = 0;

	/**
	 * See Q.763 3.67 Response indicator : no loop exists
	 */
	public static final int _RI_NO_LOOP_E = 1;

	/**
	 * See Q.763 3.67 Response indicator : simultaneous transfer
	 */
	public static final int _RI_SIM_TRANS = 2;
	
	public boolean isResponse();

	public void setResponse(boolean response);

	public int getResponseIndicator();

	public void setResponseIndicator(int responseIndicator);

}
