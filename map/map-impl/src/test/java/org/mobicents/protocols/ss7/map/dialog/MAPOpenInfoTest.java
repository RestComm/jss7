package org.mobicents.protocols.ss7.map.dialog;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPOpenInfoTest extends TestCase {

	@org.junit.Test
	public void testDecode() throws Exception {

		// The raw data is from packet 2 of nad1053.pcap
		byte[] data = new byte[] { (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09,
				(byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03, 0x00, (byte) 0x80,
				0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
				(byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 };

		ByteArrayInputStream baIs = new ByteArrayInputStream(data);
		AsnInputStream asnIs = new AsnInputStream(baIs);

		int tag = asnIs.readTag();

		MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
		mapOpenInfoImpl.decode(asnIs);

		AddressString destRef = mapOpenInfoImpl.getDestReference();
		AddressString origRef = mapOpenInfoImpl.getOrigReference();

		assertNotNull(destRef);

		assertEquals(AddressNature.international_number, destRef
				.getAddressNature());
		assertEquals(NumberingPlan.land_mobile, destRef.getNumberingPlan());
		assertEquals("204208300008002", destRef.getAddress());

		assertNotNull(origRef);

		assertEquals(AddressNature.international_number, origRef
				.getAddressNature());
		assertEquals(NumberingPlan.ISDN, origRef.getNumberingPlan());
		assertEquals("31628968300", origRef.getAddress());

	}

	@org.junit.Test
	public void testEncode() throws Exception {

		MapServiceFactory servFact = new MapServiceFactoryImpl();

		MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
		AddressString destReference = servFact.createAddressString(
				AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");
		
		mapOpenInfoImpl.setDestReference(destReference);


		AddressString origReference = servFact.createAddressString(
				AddressNature.international_number, NumberingPlan.ISDN,
				"31628968300");
		
		mapOpenInfoImpl.setOrigReference(origReference);
		
		AsnOutputStream asnOS = new AsnOutputStream();

		mapOpenInfoImpl.encode(asnOS);

		byte[] data = asnOS.toByteArray();

		// System.out.println(dump(data, data.length, false));

		assertTrue(Arrays.equals(new byte[] { (byte) 0xa0, (byte) 0x14,
				(byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03,
				0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07,
				(byte) 0x91, 0x13, 0x26, (byte) 0x98, (byte) 0x86, 0x03,
				(byte) 0xf0 }, data));

	}

}
