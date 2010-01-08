/**
 * Start time:13:31:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:31:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface NatureOfConnectionIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x06;

	/**
	 * See Q.763 3.35 Echo control device indicator : outgoing echo control
	 * device included
	 */
	public static final boolean _ECDI_INCLUDED = true;

	/**
	 * See Q.763 3.35 Echo control device indicator : outgoing echo control
	 * device not included
	 */
	public static final boolean _ECDI_NOT_INCLUDED = false;

	/**
	 * See Q.763 3.35 Satellite indicator : no satellite circuit in the
	 * connection
	 */
	public static final int _SI_NO_SATELLITE = 0;

	/**
	 * See Q.763 3.35 Satellite indicator : one satellite circuit in the
	 * connection
	 */
	public static final int _SI_ONE_SATELLITE = 1;

	/**
	 * See Q.763 3.35 Satellite indicator : two satellite circuits in the
	 * connection
	 */
	public static final int _SI_TWO_SATELLITE = 2;

	/**
	 * See Q.763 3.35 Continuity check indicator
	 */
	public static final int _CCI_NOT_REQUIRED = 0;
	/**
	 * See Q.763 3.35 Continuity check indicator
	 */
	public static final int _CCI_REQUIRED_ON_THIS_CIRCUIT = 1;

	/**
	 * See Q.763 3.35 Continuity check indicator
	 */
	public static final int _CCI_PERFORMED_ON_PREVIOUS_CIRCUIT = 2;

	public int getSatelliteIndicator();

	public void setSatelliteIndicator(int satelliteIndicator);

	public int getContinuityCheckIndicator();

	public void setContinuityCheckIndicator(int continuityCheckIndicator);

	public boolean isEchoControlDeviceIndicator();

	public void setEchoControlDeviceIndicator(boolean echoControlDeviceIndicator);
}
