/**
 * Start time:16:05:47 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:16:05:47 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface RedirectionInformation extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x13;
	/**
	 * See Q.763 3.45 Redirecting indicator no redirection (national use)
	 */
	public static final int _RI_NO_REDIRECTION = 0;

	/**
	 * See Q.763 3.45 Redirecting indicator no redirection (national use)
	 */
	public static final int _RI_CALL_REROUTED = 1;

	/**
	 * See Q.763 3.45 Redirecting indicator call rerouted, all redirection
	 * information presentation restricted (national use)
	 */
	public static final int _RI_CALL_R_RID = 2;

	/**
	 * See Q.763 3.45 Redirecting indicator call diverted
	 */
	public static final int _RI_CALL_D = 3;

	/**
	 * See Q.763 3.45 Redirecting indicator call diverted, all redirection
	 * information presentation restricted
	 */
	public static final int _RI_CALL_D_RIR = 4;

	/**
	 * See Q.763 3.45 Redirecting indicator call rerouted, redirection number
	 * presentation restricted (national use)
	 */
	public static final int _RI_CALL_R_RNPR = 5;

	/**
	 * See Q.763 3.45 Redirecting indicator call diversion, redirection number
	 * presentation restricted (national use)
	 */
	public static final int _RI_CALL_D_RNPR = 6;

	/**
	 * See Q.763 3.45 Original redirection reason unknown/not available
	 */
	public static final int _ORR_UNA = 0;
	/**
	 * See Q.763 3.45 Original redirection reason user busy
	 */
	public static final int _ORR_USER_BUSY = 1;

	/**
	 * See Q.763 3.45 Original redirection reason no reply
	 */
	public static final int _ORR_NO_REPLY = 2;
	/**
	 * See Q.763 3.45 Original redirection reason unconditional
	 */
	public static final int _ORR_UNCONDITIONAL = 3;

	/**
	 * See Q.763 3.45 Redirecting reason unknown/not available
	 */
	public static final int _RR_UNA = 0;
	/**
	 * See Q.763 3.45 Redirecting reason user busy
	 */
	public static final int _RR_USER_BUSY = 1;

	/**
	 * See Q.763 3.45 Redirecting reason no reply
	 */
	public static final int _RR_NO_REPLY = 2;
	/**
	 * See Q.763 3.45 Redirecting reason unconditional
	 */
	public static final int _RR_UNCONDITIONAL = 3;

	/**
	 * See Q.763 3.45 Redirecting reason deflection during alerting
	 */
	public static final int _RR_DEFLECTION_DA = 4;

	/**
	 * See Q.763 3.45 Redirecting reason deflection immediate response
	 */
	public static final int _RR_DEFLECTION_IE = 5;

	/**
	 * See Q.763 3.45 Redirecting reason mobile subscriber not reachable
	 */
	public static final int _RR_MOBILE_SNR = 6;
	
	
	public int getRedirectingIndicator() ;

	public void setRedirectingIndicator(int redirectingIndicator) ;

	public int getOriginalRedirectionReason() ;

	public void setOriginalRedirectionReason(int originalRedirectionReason) ;

	public int getRedirectionCounter() ;

	public void setRedirectionCounter(int redirectionCounter) throws IllegalArgumentException ;

	public int getRedirectionReason() ;

	public void setRedirectionReason(int redirectionReason) ;
	
}
