package org.mobicents.protocols.ss7.map.dialog;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPRefuseInfoTest extends TestCase {

	@org.junit.Test
	public void testDecode() throws Exception {
		// The raw data is from last packet of long ussd-abort from msc2.txt
		byte[] data = new byte[] { (byte) 0xA3, 0x03, (byte) 0x0A, 0x01, 0x00 };

		ByteArrayInputStream baIs = new ByteArrayInputStream(data);
		AsnInputStream asnIs = new AsnInputStream(baIs);

		int tag = asnIs.readTag();

		MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
		mapRefuseInfoImpl.decode(asnIs);

		Reason reason = mapRefuseInfoImpl.getReason();

		assertNotNull(reason);

		assertEquals(Reason.noReasonGiven, reason);
	}

	@org.junit.Test
	public void testEncode() throws Exception {

		MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
		mapRefuseInfoImpl.setReason(Reason.noReasonGiven);

		AsnOutputStream asnOS = new AsnOutputStream();

		mapRefuseInfoImpl.encode(asnOS);

		byte[] data = asnOS.toByteArray();

		// System.out.println(dump(data, data.length, false));

		assertTrue(Arrays.equals(new byte[] { (byte) 0xA3, 0x03, (byte) 0x0A,
				0x01, 0x00 }, data));

	}

}
