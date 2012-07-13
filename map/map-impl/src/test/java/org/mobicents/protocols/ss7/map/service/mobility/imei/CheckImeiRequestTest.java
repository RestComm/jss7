package org.mobicents.protocols.ss7.map.service.mobility.imei;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * 
 * @author normandes
 *
 */
public class CheckImeiRequestTest {

	// Real Trace
	private byte[] getEncodedDataV2() {
		return new byte[] { 0x04, 0x0A, 0x04, 0x08, 0x53, (byte) 0x97, 0x75, 0x40, 0x31, (byte) 0x93, (byte) 0x95, (byte) 0xF0 };
	}
	
	@Test(groups = { "functional.decode", "imei" })
	public void testDecode() throws Exception {
		byte[] rawData = getEncodedDataV2();
		AsnInputStream asnIS = new AsnInputStream(rawData);
		
		int tag = asnIS.readTag();
		CheckImeiRequestImpl checkImeiImpl = new CheckImeiRequestImpl(2);
		checkImeiImpl.decodeAll(asnIS);
		
		assertEquals(checkImeiImpl.getIMEI().getIMEI(), "357957041339590");
	}
	
	@Test(groups = { "functional.encode", "imei" })
	public void testEncode() throws Exception {
		IMEIImpl imei = new IMEIImpl("357957041339590");
		CheckImeiRequestImpl checkImei = new CheckImeiRequestImpl(2, imei, null, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		checkImei.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedDataV2();
		assertTrue(Arrays.equals(rawData, encodedData));
		
	}
	
}
