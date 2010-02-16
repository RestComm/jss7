/**
 * Start time:13:44:52 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:44:52 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface PivotCapability extends ISUPParameter {

	public static final int _PARAMETER_CODE = 0x7B;

	//FIXME: add C defs
	/**
	 * See Q.763 3.84 Pivot possible indicator : no indication
	 */
	public static final int _PPI_NO_INDICATION = 0;

	/**
	 * See Q.763 3.84 Pivot possible indicator : pivot routing possible before
	 * ACM
	 */
	public static final int _PPI_PRPB_ACM = 1;

	/**
	 * See Q.763 3.84 Pivot possible indicator : pivot routing possible before
	 * ANM
	 */
	public static final int _PPI_PRPB_ANM = 2;

	/**
	 * See Q.763 3.84 Pivot possible indicator : pivot routing possible any time
	 * during the call
	 */
	public static final int _PPI_PRPB_ANY = 3;

	/**
	 * See Q.763 3.84 Interworking to redirection indicator (national use)
	 */
	public static final boolean _ITRI_ALLOWED = false;

	/**
	 * See Q.763 3.84 Interworking to redirection indicator (national use)
	 */
	public static final boolean _ITRI_NOT_ALLOWED = true;
	
	
	public byte[] getPivotCapabilities();

	public void setPivotCapabilities(byte[] pivotCapabilities);

	public byte createPivotCapabilityByte(boolean itriNotAllowed, int pivotPossibility);

	public boolean getITRINotAllowed(byte b);

	public int getPivotCapability(byte b);
}
