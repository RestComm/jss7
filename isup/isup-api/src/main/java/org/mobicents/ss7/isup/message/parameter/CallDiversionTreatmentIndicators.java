/**
 * Start time:11:09:03 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:11:09:03 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallDiversionTreatmentIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x6E;
	/**
	 * See Q.763 3.72 Call to be diverted indicator : no indication
	 */
	public static final int _NO_INDICATION = 0;

	/**
	 * See Q.763 3.72 Call to be diverted indicator : call diversion allowed
	 */
	public static final int _CD_ALLOWED = 1;

	/**
	 * See Q.763 3.72 Call to be diverted indicator : call diversion not allowed
	 */
	public static final int _CD_NOT_ALLOWED = 2;

	public byte[] getCallDivertedIndicators();

	public void setCallDivertedIndicators(byte[] callDivertedIndicators);

	public int getDiversionIndicator(byte b);
}
