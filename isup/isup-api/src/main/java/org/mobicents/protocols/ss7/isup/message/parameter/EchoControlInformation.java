/**
 * Start time:12:44:34 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:44:34 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface EchoControlInformation extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x37;

	
	/**
	 * See Q.763 3.19 Outgoing echo control device information indicator : no
	 * information
	 */
	public static final int _OUTGOING_ECHO_CDII_NOINFO = 0;

	/**
	 * See Q.763 3.19 Outgoing echo control device information indicator :
	 * outgoing echo control device not included and not available
	 */
	public static final int _OUTGOING_ECHO_CDII_NINA = 1;

	/**
	 * See Q.763 3.19 Outgoing echo control device information indicator :
	 * outgoing echo control device included
	 */
	public static final int _OUTGOING_ECHO_CDII_INCLUDED = 2;

	/**
	 * See Q.763 3.19 Outgoing echo control device information indicator :
	 * outgoing echo control device not included but available
	 */
	public static final int _OUTGOING_ECHO_CDII_NIA = 3;

	/**
	 * See Q.763 3.19 Incoming echo control device information indicator : no
	 * information
	 */
	public static final int _INCOMING_ECHO_CDII_NOINFO = 0;

	/**
	 * See Q.763 3.19 Incoming echo control device information indicator :
	 * incoming echo control device not included and not available
	 */
	public static final int _INCOMING_ECHO_CDII_NINA = 1;

	/**
	 * See Q.763 3.19 Incoming echo control device information indicator :
	 * incoming echo control device included
	 */
	public static final int _INCOMING_ECHO_CDII_INCLUDED = 2;

	/**
	 * See Q.763 3.19 Incoming echo control device information indicator :
	 * incoming echo control device not included but available
	 */
	public static final int _INCOMING_ECHO_CDII_NIA = 3;

	/**
	 * See Q.763 3.19 Incoming echo control device request indicator : no
	 * information
	 */
	public static final int _INCOMING_ECHO_CDRI_NOINFO = 0;

	/**
	 * See Q.763 3.19 Incoming echo control device request indicator : incoming
	 * echo control device activation request
	 */
	public static final int _INCOMING_ECHO_CDRI_AR = 1;

	/**
	 * See Q.763 3.19 Incoming echo control device request indicator : incoming
	 * echo control device deactivation request (Note 2)
	 */
	public static final int _INCOMING_ECHO_CDRI_DR = 2;

	/**
	 * See Q.763 3.19 Outgoing echo control device request indicator : no
	 * information
	 */
	public static final int _OUTGOING_ECHO_CDRI_NOINFO = 0;

	/**
	 * See Q.763 3.19 Outgoing echo control device request indicator : outgoing
	 * echo control device activation request
	 */
	public static final int _OUTGOING_ECHO_CDRI_AR = 1;

	/**
	 * See Q.763 3.19 Outgoing echo control device request indicator : outgoing
	 * echo control device deactivation request (Note 1)
	 */
	public static final int _OUTGOING_ECHO_CDRI_DR = 2;
	
	
	public int getOutgoingEchoControlDeviceInformationIndicator();

	public void setOutgoingEchoControlDeviceInformationIndicator(int outgoingEchoControlDeviceInformationIndicator);

	public int getIncomingEchoControlDeviceInformationIndicator();

	public void setIncomingEchoControlDeviceInformationIndicator(int incomingEchoControlDeviceInformationIndicator);

	public int getOutgoingEchoControlDeviceInformationRequestIndicator();

	public void setOutgoingEchoControlDeviceInformationRequestIndicator(int outgoingEchoControlDeviceInformationRequestIndicator);

	public int getIncomingEchoControlDeviceInformationRequestIndicator();

	public void setIncomingEchoControlDeviceInformationRequestIndicator(int incomingEchoControlDeviceInformationRequestIndicator);

}
