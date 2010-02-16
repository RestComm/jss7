/**
 * Start time:13:34:38 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:34:38 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface NetworkSpecificFacility extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x2F;
	
	/**
	 * See Q.763 Type of network identification : national network
	 * identification
	 */
	public static final int _TNI_NNI = 0x02;

	/**
	 * See Q.763 Type of network identification : reserved for international
	 * network identification
	 */
	public static final int _TNI_RESERVED_INI = 0x03;
	
	
	public boolean isIncludeNetworkIdentification();

	public int getLengthOfNetworkIdentification() ;

	public int getTypeOfNetworkIdentification() ;

	public void setTypeOfNetworkIdentification(byte typeOfNetworkIdentification);

	public int getNetworkIdentificationPlan() ;

	public void setNetworkIdentificationPlan(byte networkdIdentificationPlan) ;

	public byte[] getNetworkIdentification() ;

	public void setNetworkIdentification(byte[] networkdIdentification) ;

	public byte[] getNetworkSpecificaFacilityIndicator() ;

	public void setNetworkSpecificaFacilityIndicator(byte[] networkSpecificaFacilityIndicator) ;
	
}
