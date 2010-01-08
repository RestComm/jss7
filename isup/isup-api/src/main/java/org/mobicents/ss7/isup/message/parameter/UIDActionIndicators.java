/**
 * Start time:14:17:18 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:14:17:18 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface UIDActionIndicators extends ISUPParameter {

	public static final int _PARAMETER_CODE = 0x74;
	//FIXME: add C defs
	/**
	 * See Q.763 3.78 Through-connection instruction indicator : no indication
	 */
	public static final boolean _TCII_NO_INDICATION = false;

	/**
	 * See Q.763 3.78 Through-connection instruction indicator : through-connect
	 * in both directions
	 */
	public static final boolean _TCII_TCIBD = true;

	/**
	 * See Q.763 3.78 T9 timer instruction indicator : no indication
	 */
	public static final boolean _T9_TII_NO_INDICATION = false;

	/**
	 * See Q.763 3.78 T9 timer instruction indicator : stop or do not start T9
	 * timer
	 */
	public static final boolean _T9_TII_SDNST9T = false;
	
	public byte[] getUdiActionIndicators() ;

	public void setUdiActionIndicators(byte[] udiActionIndicators) ;

	public byte createUIDAction(boolean TCII, boolean T9);

	public boolean getT9Indicator(byte b) ;

	public boolean getTCIIndicator(byte b) ;
}
