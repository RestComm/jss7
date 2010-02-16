/**
 * Start time:12:48:26 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:48:26 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface FacilityIndicator extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x18;

	/**
	 * See Q.763 3.22 facility indicator user-to-user service
	 */
	public static final int _FACILITY_INDICATOR_UTUS = 2;
	
	public int getFacilityIndicator();

	public void setFacilityIndicator(int facilityIndicator);
}
