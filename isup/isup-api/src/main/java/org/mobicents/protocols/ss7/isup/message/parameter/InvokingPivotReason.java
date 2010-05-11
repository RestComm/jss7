/**
 * Start time:13:20:48 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:20:48 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface InvokingPivotReason extends ISUPParameter {
	//FIXME: fill this!
	public static final int _PARAMETER_CODE = 0;
	//FIXME: add C defs
	/**
	 * See Q.763 3.94.4 Pivot Reason : unknown/ not available
	 */
	public static final int _PR_UNKNOWN = 0;
	/**
	 * See Q.763 3.94.4 Pivot Reason : service provider portability (national
	 * use)
	 */
	public static final int _PR_SPP = 1;
	/**
	 * See Q.763 3.94.4 Pivot Reason : reserved for location portability
	 */
	public static final int _PR_RFLP = 2;
	/**
	 * See Q.763 3.94.4 Pivot Reason : reserved for service portability
	 */
	public static final int _PR_RFSP = 3;
	
	
	public byte[] getReasons();
	public void setReasons(byte[] reasons) throws IllegalArgumentException ;

	public int getReason(byte b) ;
}
