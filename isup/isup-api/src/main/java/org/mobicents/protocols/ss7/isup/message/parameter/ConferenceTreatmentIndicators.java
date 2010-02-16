/**
 * Start time:12:28:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:28:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ConferenceTreatmentIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x72;

	
	/**
	 * See Q.763 3.76 Conference acceptance indicator (Note) : no indication
	 */
	public static final int _CAI_NO_INDICATION = 0;

	/**
	 * See Q.763 3.76 Conference acceptance indicator (Note) : accept conference
	 * request
	 */
	public static final int _CAI_ACR = 1;

	/**
	 * See Q.763 3.76 Conference acceptance indicator (Note) : reject conference
	 * request
	 */
	public static final int _CAI_RCR = 2;
	
	public byte[] getConferenceAcceptance();

	public void setConferenceAcceptance(byte[] conferenceAcceptance);

	public int getConferenceTreatmentIndicator(byte b);
}
