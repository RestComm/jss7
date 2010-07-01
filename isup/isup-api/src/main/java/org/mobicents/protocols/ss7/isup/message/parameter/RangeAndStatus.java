/**
 * Start time:13:52:59 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:52:59 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * This RangeAndStatus indiactes whcih CICs, starting from one present in
 * message are affected. Range indicates how many CICs are potentially affected.
 * Status contains bits indicating CIC affected(1 - affected, 0 - not affected) <br>
 * For content interpretation refer to Q.763 3.43
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface RangeAndStatus extends ISUPParameter {
	
	public static final int _PARAMETER_CODE = 0x16;

	/**
	 * Fetches range.
	 * 
	 * @return
	 */
	public byte getRange();

	/**
	 * Sets range.
	 * 
	 * @param range
	 * @param addStatus
	 *            - flag indicates if implementation should create proper status
	 */
	public void setRange(byte range, boolean addStatus);

	/**
	 * Sets range.
	 * 
	 * @param range
	 */
	public void setRange(byte range);

	/**
	 * Gets raw status part.
	 * 
	 * @return
	 */
	public byte[] getStatus();

	/**
	 * Gets raw status part.
	 * 
	 * @return
	 */
	public void setStatus(byte[] status);

	public void setAffected(byte subrange, boolean v) throws IllegalArgumentException;

	public boolean isAffected(byte b) throws IllegalArgumentException;
}
