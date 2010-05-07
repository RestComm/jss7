/**
 * 
 */
package org.mobicents.protocols.ss7.stream.tlv;

import java.util.Arrays;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author baranowb
 *
 */
public class TLVOutputStreamTest extends TestCase {

	private TLVOutputStream tlv;
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		tlv = new TLVOutputStream();
	
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void testLongLenData() throws Exception
	{
		byte[] data = new byte[]{-61, 53, -73, -47, -51, 9, 1, 3, 14, 25, 11, 18, -110, 0, 17, 4, -105, 32, 115, 0, 114, 1, 11, 18, -110, 0, 17, 4, -105, 32, 115, 0, 2, 2, -110, 98, -127, -113, 72, 4, 123, 0, 1, -10, 107, 30, 40, 28, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 17, 96, 15, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 50, 1, 108, -128, -95, 99, 2, 1, 0, 2, 1, 0, 48, 91, -128, 1, 8, -126, 8, -124, 16, -105, 32, -125, 32, 104, 6, -125, 7, 3, 19, 9, 50, 38, 89, 24, -123, 1, 10, -118, 8, -124, -109, -105, 32, 115, 0, 2, 2, -6, 9, 5, -128, 3, -128, -112, -93, -100, 1, 12, -97, 50, 8, 82, 0, 7, 50, 1, 86, 4, -14, -65, 53, 3, -125, 1, 17, -97, 54, 5, -13, 122, 36, 0, 1, -97, 55, 7, -111, -105, 32, 115, 0, 2, -14, -97, 57, 8, 2, 1, 80, 112, 81, 48, 112, 35, 0, 0};
		byte[] expected=new byte[]{74, -127, -74, -61, 53, -73, -47, -51, 9, 1, 3, 14, 25, 11, 18, -110, 0, 17, 4, -105, 32, 115, 0, 114, 1, 11, 18, -110, 0, 17, 4, -105, 32, 115, 0, 2, 2, -110, 98, -127, -113, 72, 4, 123, 0, 1, -10, 107, 30, 40, 28, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 17, 96, 15, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 50, 1, 108, -128, -95, 99, 2, 1, 0, 2, 1, 0, 48, 91, -128, 1, 8, -126, 8, -124, 16, -105, 32, -125, 32, 104, 6, -125, 7, 3, 19, 9, 50, 38, 89, 24, -123, 1, 10, -118, 8, -124, -109, -105, 32, 115, 0, 2, 2, -6, 9, 5, -128, 3, -128, -112, -93, -100, 1, 12, -97, 50, 8, 82, 0, 7, 50, 1, 86, 4, -14, -65, 53, 3, -125, 1, 17, -97, 54, 5, -13, 122, 36, 0, 1, -97, 55, 7, -111, -105, 32, 115, 0, 2, -14, -97, 57, 8, 2, 1, 80, 112, 81, 48, 112, 35, 0, 0};
		tlv.writeData(data);
		assertTrue("Expected value does not match encoded! \n"+Arrays.toString(expected)+"\n"+Arrays.toString(data), Arrays.equals(expected, tlv.toByteArray()));
	}
	
}
