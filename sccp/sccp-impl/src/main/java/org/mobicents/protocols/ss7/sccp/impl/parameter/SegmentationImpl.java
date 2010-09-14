/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.util.Arrays;

/**
 * See Q.713 3.17
 * 
 * @author baranowb
 * 
 */
public class SegmentationImpl extends OptionalParameter  {

	
	private static final int _TRUE = 1;
	private static final int _FALSE = 0;
	private boolean firstSegIndication = false;
	private boolean class1Selected = false;
	private byte remainingSegments = 0x0F;
	private byte[] segmentationLocalRef = null;

	/**
	 * 
	 */
	public SegmentationImpl() {
		// TODO Auto-generated constructor stub
	}

	public SegmentationImpl(boolean firstSegIndication, boolean class1Selected, byte remainingSegments, byte[] segmentationLocalRef) {
		super();
		this.firstSegIndication = firstSegIndication;
		this.class1Selected = class1Selected;
		this.remainingSegments = remainingSegments;
		this.segmentationLocalRef = segmentationLocalRef;
	}

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
	public boolean isFirstSegIndication() {
		return firstSegIndication;
	}

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
	public void setFirstSegIndication(boolean firstSegIndication) {
		this.firstSegIndication = firstSegIndication;
	}

	/**
	 * Bit 7 of octet 1 is used to keep in the message in sequence delivery
	 * option required by the SCCP user and is coded as follows:
	 * 
	 * @return <li><b>true</b></li> - class 1 selected <li><b>false</b> - class
	 *         0 selected</li> </ul>
	 */
	public boolean isClass1Selected() {
		return class1Selected;
	}

	/**
	 * Bit 7 of octet 1 is used to keep in the message in sequence delivery
	 * option required by the SCCP user and is coded as follows:
	 * 
	 * @return <li><b>true</b></li> - class 1 selected <li><b>false</b> - class
	 *         0 selected</li> </ul>
	 */
	public void setClass1Selected(boolean class1Selected) {
		this.class1Selected = class1Selected;
	}

	/**
	 * Bits 4-1 of octet 1 are used to indicate the number of remaining
	 * segments. The values 0000 to 1111 are possible; the value 0000 indicates
	 * the last segment.
	 * 
	 * @return
	 */
	public byte getRemainingSegments() {
		return remainingSegments;
	}

	/**
	 * Bits 4-1 of octet 1 are used to indicate the number of remaining
	 * segments. The values 0000 to 1111 are possible; the value 0000 indicates
	 * the last segment.
	 * 
	 * @param remainingSegments
	 */
	public void setRemainingSegments(byte remainingSegments) {
		if (remainingSegments < 0 || remainingSegments > 0x0F) {
			throw new IllegalArgumentException("Wrong value of remaining segments: " + remainingSegments);
		}
		this.remainingSegments = remainingSegments;
	}

	public byte[] getSegmentationLocalRef() {
		return segmentationLocalRef;
	}

	public void setSegmentationLocalRef(byte[] segmentationLocalRef) {
		if (segmentationLocalRef != null && segmentationLocalRef.length != 4) {
			throw new IllegalArgumentException("Segmentation reference wrong size: " + segmentationLocalRef.length);
		}
		this.segmentationLocalRef = segmentationLocalRef;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.OptionalParameter#decode(byte[])
	 */
	@Override
	public void decode(byte[] buffer) throws IOException {
		int v = buffer[0];
		this.firstSegIndication = ((v >> 7) & 0x01) == _TRUE;
		this.class1Selected = ((v >> 6) & 0x01) == _TRUE;
		this.remainingSegments = (byte) (v & 0x0F);
		if (remainingSegments < 0 || remainingSegments > 0x0F) {
			throw new IOException("Wrong value of remaining segments: " + remainingSegments);
		}
		this.segmentationLocalRef = new byte[buffer.length-1];
		System.arraycopy(buffer, 1, this.segmentationLocalRef, 0, this.segmentationLocalRef.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.OptionalParameter#encode()
	 */
	@Override
	public byte[] encode() throws IOException {
		if(this.segmentationLocalRef == null)
		{
			throw new IOException("No segmentation reference.");
		}
		byte[] buffer = new byte[1+this.segmentationLocalRef.length];
		System.arraycopy(this.segmentationLocalRef, 0,buffer, 1,  this.segmentationLocalRef.length);
		int v = this.remainingSegments & 0x0F;
		v |= ( (this.class1Selected?_TRUE:_FALSE) << 6);
		v |= ( (this.firstSegIndication?_TRUE:_FALSE) << 7);
		buffer[0] = (byte) v;
		return buffer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (class1Selected ? 1231 : 1237);
		result = prime * result + (firstSegIndication ? 1231 : 1237);
		result = prime * result + remainingSegments;
		result = prime * result + Arrays.hashCode(segmentationLocalRef);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SegmentationImpl other = (SegmentationImpl) obj;
		if (class1Selected != other.class1Selected)
			return false;
		if (firstSegIndication != other.firstSegIndication)
			return false;
		if (remainingSegments != other.remainingSegments)
			return false;
		if (!Arrays.equals(segmentationLocalRef, other.segmentationLocalRef))
			return false;
		return true;
	}

	
}
