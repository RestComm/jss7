/**
 * Start time:14:13:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:14:13:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface TransitNetworkSelection extends ISUPParameter {

	public static final int _PARAMETER_CODE = 0x23;

	//FIXME: add C defs
	/**
	 * See Q.763 3.53 Type of network identification : CCITT/ITU-T-standardized
	 * identification
	 */
	public static final int _TONI_ITU_T = 0;
	/**
	 * See Q.763 3.53 Type of network identification : national network
	 * identification
	 */
	public static final int _TONI_NNI = 2;

	/**
	 * See Q.763 3.53 Network identification plan : For CCITT/ITU-T-standardized
	 * identification : unknown
	 */
	public static final int _NIP_UNKNOWN = 0;

	/**
	 * See Q.763 3.53 Network identification plan : For CCITT/ITU-T-standardized
	 * identification : public data network identification code (DNIC), ITU-T
	 * Recommendation X.121
	 */
	public static final int _NIP_PDNIC = 3;

	/**
	 * See Q.763 3.53 Network identification plan : For CCITT/ITU-T-standardized
	 * identification : public land Mobile Network Identification Code (MNIC),
	 * ITU-T Recommendation E.212
	 */
	public static final int _NIP_PLMNIC = 6;
	
	public int getTypeOfNetworkIdentification();

	public void setTypeOfNetworkIdentification(int typeOfNetworkIdentification);

	public int getNetworkIdentificationPlan();

	public void setNetworkIdentificationPlan(int networkIdentificationPlan);

	public String getAddress();

	public void setAddress(String address);

	public boolean isOddFlag();

}
