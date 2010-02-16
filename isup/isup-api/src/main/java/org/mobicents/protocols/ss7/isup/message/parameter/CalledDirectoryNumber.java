/**
 * Start time:11:12:56 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:12:56 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CalledDirectoryNumber extends ISUPParameter, AbstractNAINumberInterface {
	// FIXME: whats the code ?
	public static final int _PARAMETER_CODE = 0x7D;

	
	/**
	 * See Q.763 Numbering plan indicator : ISDN (Telephony) numbering plan
	 * (ITU-T Recommendation E.164)
	 */
	public static final int _NPI_ISDN_NP = 1;

	/**
	 * See Q.763 Internal network number indicator (INN) : reserved
	 */
	public static final int _INNI_RESERVED = 0;

	/**
	 * See Q.763 Internal network number indicator (INN) : routing to internal
	 * network number not allowed
	 */
	public static final int _INNI_RTINNNA = 1;
	
	public int getNumberingPlanIndicator();

	public void setNumberingPlanIndicator(int numberingPlanIndicator);

	public int getInternalNetworkNumberIndicator();

	public void setInternalNetworkNumberIndicator(int internalNetworkNumberIndicator);
}
