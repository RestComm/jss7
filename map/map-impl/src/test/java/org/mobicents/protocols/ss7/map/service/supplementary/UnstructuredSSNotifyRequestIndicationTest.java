package org.mobicents.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Real Trace
 * @author amit bhayani
 * 
 */
public class UnstructuredSSNotifyRequestIndicationTest {
	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeTest
	public void setUp() {
	}

	@AfterTest
	public void tearDown() {
	}

	@Test(groups = { "functional.decode", "service.ussd" })
	public void testDecode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x53, 0x04, 0x01, 0x0f, 0x04, 0x4e, (byte) 0xd9, 0x77, 0x5d, 0x0e, 0x72,
				(byte) 0x97, (byte) 0xef, 0x20, 0x71, (byte) 0x98, 0x1d, 0x76, (byte) 0x8f, (byte) 0xcb, (byte) 0xa0,
				(byte) 0xf4, 0x1c, 0x34, (byte) 0xa3, (byte) 0xb9, 0x66, 0x38, 0x50, (byte) 0xd0, (byte) 0xe8, 0x04,
				(byte) 0x85, (byte) 0xdd, 0x64, 0x50, 0x19, 0x0f, 0x4f, (byte) 0xcb, (byte) 0xcb, 0x73, (byte) 0xd0,
				(byte) 0xdb, 0x0d, (byte) 0x9a, (byte) 0xc1, 0x5c, (byte) 0xb0, (byte) 0x9b, 0x4b, 0x06, (byte) 0x8b,
				(byte) 0xc9, 0x5c, (byte) 0xa0, (byte) 0xe1, 0x7b, 0x4e, 0x07, (byte) 0xbd, (byte) 0xcd, 0x20, 0x76,
				0x78, 0x4e, 0x07, (byte) 0x95, (byte) 0xed, 0x65, 0x37, 0x1d, 0x74, 0x0f, (byte) 0xcf, 0x41, 0x30,
				0x57, 0x0d, 0x06, 0x0a, 0x1a, (byte) 0x9d, 0x2e };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		UnstructuredSSNotifyRequestIndicationImpl addNum = new UnstructuredSSNotifyRequestIndicationImpl();
		addNum.decodeAll(asn);
		byte dataCodingScheme = addNum.getUSSDDataCodingScheme();
		assertEquals(dataCodingScheme, (byte) 0x0f);

		USSDString ussdString = addNum.getUSSDString();
		assertNotNull(ussdString);

		assertEquals(ussdString.getString(),
				"Your new balance is 34.38 AFN and expires on 30.07.2012. Cost of last event was 0.50 AFN.");

	}

	@Test(groups = { "functional.encode", "service.ussd" })
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x53, 0x04, 0x01, 0x0f, 0x04, 0x4e, (byte) 0xd9, 0x77, 0x5d, 0x0e, 0x72,
				(byte) 0x97, (byte) 0xef, 0x20, 0x71, (byte) 0x98, 0x1d, 0x76, (byte) 0x8f, (byte) 0xcb, (byte) 0xa0,
				(byte) 0xf4, 0x1c, 0x34, (byte) 0xa3, (byte) 0xb9, 0x66, 0x38, 0x50, (byte) 0xd0, (byte) 0xe8, 0x04,
				(byte) 0x85, (byte) 0xdd, 0x64, 0x50, 0x19, 0x0f, 0x4f, (byte) 0xcb, (byte) 0xcb, 0x73, (byte) 0xd0,
				(byte) 0xdb, 0x0d, (byte) 0x9a, (byte) 0xc1, 0x5c, (byte) 0xb0, (byte) 0x9b, 0x4b, 0x06, (byte) 0x8b,
				(byte) 0xc9, 0x5c, (byte) 0xa0, (byte) 0xe1, 0x7b, 0x4e, 0x07, (byte) 0xbd, (byte) 0xcd, 0x20, 0x76,
				0x78, 0x4e, 0x07, (byte) 0x95, (byte) 0xed, 0x65, 0x37, 0x1d, 0x74, 0x0f, (byte) 0xcf, 0x41, 0x30,
				0x57, 0x0d, 0x06, 0x0a, 0x1a, (byte) 0x9d, 0x2e };

		USSDString ussdStr = new USSDStringImpl(
				"Your new balance is 34.38 AFN and expires on 30.07.2012. Cost of last event was 0.50 AFN.", null);
		UnstructuredSSNotifyRequestIndicationImpl addNum = new UnstructuredSSNotifyRequestIndicationImpl((byte) 0x0f,
				ussdStr, null, null);

		AsnOutputStream asnOS = new AsnOutputStream();
		addNum.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();

		assertTrue(Arrays.equals(data, encodedData));
	}

}
