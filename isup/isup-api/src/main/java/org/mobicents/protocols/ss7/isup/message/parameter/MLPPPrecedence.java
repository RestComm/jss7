/**
 * Start time:13:29:53 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:29:53 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface MLPPPrecedence extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x3A;
	
	/**
	 * See Q.763 3.34 LFB (Look ahead for busy) : LFB allowed
	 */
	public static final int _LFB_INDICATOR_ALLOWED = 0;
	/**
	 * See Q.763 3.34 LFB (Look ahead for busy) : path reserved (national use)
	 */
	public static final int _LFB_INDICATOR_PATH_RESERVED = 1;
	/**
	 * See Q.763 3.34 LFB (Look ahead for busy) : LFB not allowed
	 */
	public static final int _LFB_INDICATOR_NOT_ALLOWED = 2;

	/**
	 * See Q.763 3.34 Precedence level : flash override
	 */
	public static final int _PLI_FLASH_OVERRIDE = 0;

	/**
	 * See Q.763 3.34 Precedence level : flash
	 */
	public static final int _PLI_FLASH = 1;
	/**
	 * See Q.763 3.34 Precedence level : immediate
	 */
	public static final int _PLI_IMMEDIATE = 2;
	/**
	 * See Q.763 3.34 Precedence level : priority
	 */
	public static final int _PLI_PRIORITY = 3;

	/**
	 * See Q.763 3.34 Precedence level : routine
	 */
	public static final int _PLI_ROUTINE = 4;
	
	public byte getLfb();

	public void setLfb(byte lfb);

	public byte getPrecedenceLevel() ;

	public void setPrecedenceLevel(byte precedenceLevel) ;

	public int getMllpServiceDomain() ;

	public void setMllpServiceDomain(int mllpServiceDomain) ;

	public byte[] getNiDigits();

	public void setNiDigits(byte[] niDigits) ;
}
