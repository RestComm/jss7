/**
 * Start time:10:27:43 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>

 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:10:27:43 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface NatureOfAddressIndicator {

	/**
	 * nature of address indicator value. See Q.763 - 3.9b
	 */
	public final static int _SPARE = 0;
	/**
	 * nature of address indicator value. See Q.763 - 3.9b
	 */
	public final static int _SUBSCRIBER = 1;
	/**
	 * nature of address indicator value. See Q.763 - 3.9b
	 */
	public final static int _UNKNOWN = 2;
	/**
	 * nature of address indicator value. See Q.763 - 3.9b
	 */
	public final static int _NATIONAL = 3;
	/**
	 * nature of address indicator value. See Q.763 - 3.9b
	 */
	public final static int _INTERNATIONAL = 4;
	/**
	 * nature of address indicator value. See Q.763 - 3.9b
	 */
	public final static int _NETWORK_SPECIFIC = 5;
}
