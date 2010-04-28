package org.mobicents.protocols.ss7.map;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.AddressNature;
import org.mobicents.protocols.ss7.map.api.NumberingPlan;

/**
 * 
 * @author amit bhayani
 *
 */
public class AddressStringTest extends TestCase {

	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = new byte[] { (byte) 0x96, 0x02, 0x24, (byte) 0x80,
				0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 };
		
		AsnInputStream asn = new AsnInputStream(new ByteArrayInputStream(rawData));
		

		AddressStringImpl addStr = new AddressStringImpl();
		addStr.decode(asn);

		assertFalse(addStr.isExtension());

		assertEquals(AddressNature.international_number, addStr
				.getAddressNature());
		
		assertEquals(NumberingPlan.land_mobile, addStr.getNumberingPlan());
		
		assertEquals("204208300008002", addStr.getAddress());
	}
	
	@org.junit.Test
	public void testEncode() throws Exception {
		AddressStringImpl addStr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.land_mobile, "204208300008002");
		AsnOutputStream asnOS = new AsnOutputStream();
		
		addStr.encode(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		
		//System.out.println(Utils.dump(rawData, rawData.length, false));
		
		byte[] rawData = new byte[] { (byte) 0x96, 0x02, 0x24, (byte) 0x80,
				0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 };		
		
		assertTrue(Arrays.equals(rawData, encodedData));
		
	}

}
