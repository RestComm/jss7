/**
 * 
 */
package org.mobicents.ss7.sccp.ud;

/**
 * @author baranowb
 * @author kulikov
 */
public interface XUnitData extends UDBase {
	/**
	 * Msg Type for XUDT is 17, binary 0001 0001
	 */
	public static final byte _MT = 17;
	
	public static final int HOP_COUNT_NOT_SET = 16;
	public static final int HOP_COUNT_LOW_ = 0;
	public static final int HOP_COUNT_HIGH_ = 16;
}
