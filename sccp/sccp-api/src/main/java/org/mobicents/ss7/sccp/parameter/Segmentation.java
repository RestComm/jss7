/**
 * 
 */
package org.mobicents.ss7.sccp.parameter;

import java.io.IOException;

/**
 * @author baranowb
 * 
 */
public interface Segmentation {
	
	public static final byte _PARAMETER_CODE = 0x10;

	byte[] encode() throws IOException;

	void decode(byte[] buffer) throws IOException;

	/**
	 * Bit 8 of octet 1 is used for First segment indication and is coded as
	 * follows:
	 * <ul>
	 * <li>0: in all segments but the first;</li>
	 * <li>1: first segment.</li>
	 * </ul>
	 * 
	 * @return <ul>
	 *         <li><b>true</b></li> - in case first segment indication bit is
	 *         equal to 1(first segment)
	 *         <li><b>false</b> - in case segment indication is equal 0(in all
	 *         segments but the first)</li>
	 *         </ul>
	 */
	public boolean isFirstSegIndication();

	/**
	 * Bit 8 of octet 1 is used for First segment indication and is coded as
	 * follows:
	 * <ul>
	 * <li>0: in all segments but the first;</li>
	 * <li>1: first segment.</li>
	 * </ul>
	 * <ul>
	 * <li><b>true</b></li> - in case first segment indication bit is equal to
	 * 1(first segment)
	 * <li><b>false</b> - in case segment indication is equal 0(in all segments
	 * but the first)</li>
	 * </ul>
	 * 
	 * @param firstSegIndication
	 */
	public void setFirstSegIndication(boolean firstSegIndication);

	/**
	 * Bit 7 of octet 1 is used to keep in the message in sequence delivery
	 * option required by the SCCP user and is coded as follows:
	 * 
	 * @return <li><b>true</b></li> - class 1 selected <li><b>false</b> - class
	 *         0 selected</li> </ul>
	 */
	public boolean isClass1Selected();

	/**
	 * Bit 7 of octet 1 is used to keep in the message in sequence delivery
	 * option required by the SCCP user and is coded as follows:
	 * 
	 * @return <li><b>true</b></li> - class 1 selected <li><b>false</b> - class
	 *         0 selected</li> </ul>
	 */
	public void setClass1Selected(boolean class1Selected);

	/**
	 * Bits 4-1 of octet 1 are used to indicate the number of remaining
	 * segments. The values 0000 to 1111 are possible; the value 0000 indicates
	 * the last segment.
	 * 
	 * @return
	 */
	public byte getRemainingSegments();

	/**
	 * Bits 4-1 of octet 1 are used to indicate the number of remaining
	 * segments. The values 0000 to 1111 are possible; the value 0000 indicates
	 * the last segment.
	 * 
	 * @param remainingSegments
	 */
	public void setRemainingSegments(byte remainingSegments);

	public byte[] getSegmentationLocalRef();

	public void setSegmentationLocalRef(byte[] segmentationLocalRef);

}
