package org.mobicents.protocols.ss7.map.dialog;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPOpenInfoTest extends TestCase {

	@org.junit.Test
	public void testDecode() throws Exception {

		byte[] data = new byte[] { (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09,
				(byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03, 0x00, (byte) 0x80,
				0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
				(byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 };

		ByteArrayInputStream baIs = new ByteArrayInputStream(data);
		AsnInputStream asnIs = new AsnInputStream(baIs);

		int tag = asnIs.readTag();

		MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
		mapOpenInfoImpl.decode(asnIs);

		assertTrue(Arrays.equals(new byte[] { (byte) 0x96, 0x02, 0x24,
				(byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 },
				mapOpenInfoImpl.getDestReference()));

		assertTrue(Arrays.equals(new byte[] { (byte) 0x91, 0x13, 0x26,
				(byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0 }, mapOpenInfoImpl
				.getOrigReference()));

	}

	// @org.junit.Test
	public void testEncode() throws Exception {
		MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
		mapOpenInfoImpl.setDestReference(new byte[] { (byte) 0x96, 0x02, 0x24,
				(byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 });
		mapOpenInfoImpl.setOrigReference(new byte[] { (byte) 0x91, 0x13, 0x26,
				(byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0 });
		
		AsnOutputStream asnOS = new AsnOutputStream();
		
		mapOpenInfoImpl.encode(asnOS);
		
		byte[] data = asnOS.toByteArray();
		
		//System.out.println(dump(data, data.length, false));
		
		assertTrue(Arrays.equals(new byte[]{(byte) 0xa0, (byte) 0x14, (byte) 0x80, 0x09,
				(byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03, 0x00, (byte) 0x80,
				0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
				(byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0}, data));
		
	}

}
