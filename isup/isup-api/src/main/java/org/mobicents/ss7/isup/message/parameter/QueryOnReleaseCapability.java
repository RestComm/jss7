/**
 * Start time:13:51:18 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:51:18 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface QueryOnReleaseCapability extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x85;
	
	
	//FIXME: add C defs
	/**
	 * See Q.763 QoR capability indicator : no indication
	 */
	public static final boolean _QoRI_NO_INDICATION = false;

	/**
	 * See Q.763 QoR capability indicator : QoR support
	 */
	public static final boolean _QoRI_SUPPORT = true;
	
	public byte[] getCapabilities();

	public void setCapabilities(byte[] capabilities);

	public boolean isQoRSupport(byte b);

	public byte createQoRSupport(boolean enabled);
}
