/**
 * Start time:11:06:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:11:06:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallDiversionInformation extends ISUPParameter {

	public static final int _PARAMETER_CODE = 0x36;
	
	/**
	 * see Q.763 3.6 Notification subscription options Unknown
	 */
	public static final int _NSO_UNKNOWN = 0;

	/**
	 * see Q.763 3.6 Notification subscription options presentation not allowed
	 */
	public static final int _NSO_P_NOT_ALLOWED = 1;

	/**
	 * see Q.763 3.6 Notification subscription options presentation allowed with
	 * redirection number
	 */
	public static final int _NSO_P_A_WITH_RN = 2;

	/**
	 * see Q.763 3.6 Notification subscription options presentation allowed
	 * without redirection number
	 */
	public static final int _NSO_P_A_WITHOUT_RN = 3;


	/**
	 * see Q.763 3.6 Notification subscription options Unknown
	 */
	public static final int _REDIRECTING_REASON_UNKNOWN = 0;

	/**
	 * see Q.763 3.6 Redirecting reason User busy
	 */
	public static final int _REDIRECTING_REASON_USER_BUSY = 1;

	/**
	 * see Q.763 3.6 Redirecting reason no reply
	 */
	public static final int _REDIRECTING_REASON_NO_REPLY = 2;

	/**
	 * see Q.763 3.6 Redirecting reason unconditional
	 */
	public static final int _REDIRECTING_REASON_UNCONDITIONAL = 3;

	/**
	 * see Q.763 3.6 Redirecting reason deflection during alerting
	 */
	public static final int _REDIRECTING_REASON_DDA = 4;
	/**
	 * see Q.763 3.6 Redirecting reason deflection immediate response
	 */
	public static final int _REDIRECTING_REASON_DIR = 5;

	/**
	 * see Q.763 3.6 Redirecting reason mobile subscriber not reachable
	 */
	public static final int _REDIRECTING_REASON_MSNR = 6;
	

	public int getNotificationSubscriptionOptions();

	public void setNotificationSubscriptionOptions(int notificationSubscriptionOptions);

	public int getRedirectingReason();

	public void setRedirectingReason(int redirectingReason);
}
