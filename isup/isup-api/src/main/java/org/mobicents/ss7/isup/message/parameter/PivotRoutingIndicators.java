/**
 * Start time:13:49:29 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:49:29 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface PivotRoutingIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x7C;
	
	//FIXME: add C defs
	
	/**
	 * See Q.763 3.85 Pivot routing indicators : no indication
	 */
	public static final int _PRI_NO_INDICATION = 0;
	/**
	 * See Q.763 3.85 Pivot routing indicators : pivote request
	 */
	public static final int _PRI_PIVOT_REQUEST = 1;
	/**
	 * See Q.763 3.85 Pivot routing indicators : cancel pivot request
	 */
	public static final int _PRI_C_PR = 2;
	/**
	 * See Q.763 3.85 Pivot routing indicators : pivot request failure
	 */
	public static final int _PRI_PRF = 3;
	/**
	 * See Q.763 3.85 Pivot routing indicators : interworking to redirection
	 * prohibited (backward) (national use)
	 */
	public static final int _PRI_ITRP = 4;
	
	
	public byte[] getPivotRoutingIndicators() ;

	public void setPivotRoutingIndicators(byte[] pivotRoutingIndicators) ;
}
