package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

public class UserInformationTest extends TestCase {

	@org.junit.Test
	public void testUserInformationDecode() throws IOException, AsnException {

		// This raw data is from wireshark trace of TCAP - MAP
		byte[] data = new byte[] { (byte) 0xbe, 0x25, 0x28, 0x23, 0x06, 0x07,
				0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01, (byte) 0xa0, 0x18,
				(byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02,
				0x24, (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2,
				(byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98,
				(byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 };

		AsnInputStream asin = new AsnInputStream(new ByteArrayInputStream(data));
		int tag = asin.readTag();
		assertEquals(UserInformation._TAG, tag);

		UserInformation userInformation = new UserInformationImpl();
		userInformation.decode(asin);
		
		assertTrue(userInformation.isOid());
		
		assertTrue(Arrays.equals(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 },
				userInformation.getOidValue()));

		assertFalse(userInformation.isInteger());

		assertTrue(userInformation.isAsn());
		assertTrue(Arrays.equals(new byte[] { (byte) 0xa0, (byte) 0x80,
				(byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03,
				0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07,
				(byte) 0x91, 0x13, 0x26, (byte) 0x98, (byte) 0x86, 0x03,
				(byte) 0xf0, 0x00, 0x00 }, userInformation.getEncodeType()));


	}
	

	//@org.junit.Test
	public void testUserInformationEncode() throws IOException, ParseException {
		
		byte[] encodedData = new byte[] { (byte) 0xbe, 0x25, 0x28, 0x23, 0x06, 0x07,
				0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01, (byte) 0xa0, 0x18,
				(byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02,
				0x24, (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2,
				(byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98,
				(byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 };		
		
		
		UserInformation userInformation = new UserInformationImpl();

		
		
		userInformation.setOid(true);
		userInformation.setOidValue(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 });

		userInformation.setAsn(true);
		userInformation.setEncodeType(new byte[] { (byte) 0xa0, (byte) 0x80,
				(byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03,
				0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07,
				(byte) 0x91, 0x13, 0x26, (byte) 0x98, (byte) 0x86, 0x03,
				(byte) 0xf0, 0x00, 0x00 });		
		
		
		AsnOutputStream asnos = new AsnOutputStream();
		userInformation.encode(asnos);
		
		byte[] userInfData = asnos.toByteArray();
		
		String s = TcBeginTest.dump(userInfData, userInfData.length, false);
		System.out.println(s);
		assertTrue(Arrays.equals(encodedData, userInfData));
		
		
		
	}

}
