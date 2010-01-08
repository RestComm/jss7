/**
 * Start time:14:18:57 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:14:18:57 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface UIDCapabilityIndicators extends ISUPParameter {

	public static final int _PARAMETER_CODE = 0x75;

	//FIXME: add C defs
	/**
	 * See Q.763 3.79 Through-connection instruction indicator : no indication
	 */
	public static final boolean _TCI_NO_INDICATION = false;

	/**
	 * See Q.763 3.79 Through-connection instruction indicator :
	 * through-connection modification possible
	 */
	public static final boolean _TCI_TCMP = true;

	/**
	 * See Q.763 3.79 T9 timer indicator : no indication
	 */
	public static final boolean _T9_TII_NO_INDICATION = false;

	/**
	 * See Q.763 3.79 T9 timer indicator : stopping of T9 timer possible
	 */
	public static final boolean _T9_TI_SOT9P = false;

	
	public byte[] getUIDCapabilityIndicators();

	public void setUIDCapabilityIndicators(byte[] uidCapabilityIndicators);

	public byte createUIDAction(boolean TCI, boolean T9);

	public boolean getT9Indicator(byte b);

	public boolean getTCIndicator(byte b);
}
