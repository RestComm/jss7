package org.mobicents.protocols.ss7.map.dialog;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.Utils;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.dialog.ResourceUnavailableReason;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPUserAbortInfoTest extends TestCase {

	@org.junit.Test
	public void testUserSpecificReasonDecode() throws Exception {

		// The raw data is hand made
		byte[] data = new byte[] { (byte) 0xA4, 0x02, (byte) 0x00, 0x00 };

		ByteArrayInputStream baIs = new ByteArrayInputStream(data);
		AsnInputStream asnIs = new AsnInputStream(baIs);

		int tag = asnIs.readTag();

		MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
		mapUserAbortInfo.decode(asnIs);

		MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo
				.getMAPUserAbortChoice();

		assertNotNull(mapUserAbortChoice);

		assertTrue(mapUserAbortChoice.isUserSpecificReason());
		assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
		assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
		assertFalse(mapUserAbortChoice.isUserResourceLimitation());

	}

	@org.junit.Test
	public void testProcedureCancellationReasonDecode() throws Exception {

		// The raw data is hand made
		byte[] data = new byte[] { (byte) 0xa4, 0x05, 0x03, 0x03, 0x0a, 0x01,
				0x04 };

		ByteArrayInputStream baIs = new ByteArrayInputStream(data);
		AsnInputStream asnIs = new AsnInputStream(baIs);

		int tag = asnIs.readTag();

		MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
		mapUserAbortInfo.decode(asnIs);

		MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo
				.getMAPUserAbortChoice();

		assertNotNull(mapUserAbortChoice);

		assertFalse(mapUserAbortChoice.isUserSpecificReason());
		assertTrue(mapUserAbortChoice.isProcedureCancellationReason());
		assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
		assertFalse(mapUserAbortChoice.isUserResourceLimitation());

		ProcedureCancellationReason procdCancellReasn = mapUserAbortChoice
				.getProcedureCancellationReason();
		
		assertNotNull(procdCancellReasn);
		
		assertEquals(procdCancellReasn, ProcedureCancellationReason.associatedProcedureFailure);

	}

	@org.junit.Test
	public void testUserSpecificReasonEncode() throws Exception {
		MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
		MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
		mapUserAbortChoice.setUserSpecificReason();

		mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);

		AsnOutputStream asnOS = new AsnOutputStream();

		mapUserAbortInfo.encode(asnOS);

		byte[] data = asnOS.toByteArray();

		System.out.println(Utils.dump(data, data.length, false));

		assertTrue(Arrays.equals(new byte[] { (byte) 0xA4, 0x02, (byte) 0x00,
				0x00 }, data));
	}

	@org.junit.Test
	public void testUserResourceLimitationEncode() throws Exception {
		MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
		MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
		mapUserAbortChoice.setUserResourceLimitation();

		mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);

		AsnOutputStream asnOS = new AsnOutputStream();

		mapUserAbortInfo.encode(asnOS);

		byte[] data = asnOS.toByteArray();

		System.out.println(Utils.dump(data, data.length, false));

		assertTrue(Arrays.equals(new byte[] { (byte) 0xA4, 0x02, (byte) 0x01,
				0x00 }, data));
	}

	@org.junit.Test
	public void testResourceUnavailableEncode() throws Exception {
		MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();

		MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
		mapUserAbortChoice
				.setResourceUnavailableReason(ResourceUnavailableReason.longTermResourceLimitation);

		mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);

		AsnOutputStream asnOS = new AsnOutputStream();

		mapUserAbortInfo.encode(asnOS);

		byte[] data = asnOS.toByteArray();

		System.out.println(Utils.dump(data, data.length, false));

		assertTrue(Arrays.equals(new byte[] { (byte) 0xA4, 0x05, (byte) 0x02,
				0x03, 0x0A, 0x01, 0x01 }, data));
	}

	@org.junit.Test
	public void testProcedureCancellationReasonEncode() throws Exception {
		MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();

		MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
		mapUserAbortChoice
				.setProcedureCancellationReason(ProcedureCancellationReason.associatedProcedureFailure);

		mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);

		AsnOutputStream asnOS = new AsnOutputStream();

		mapUserAbortInfo.encode(asnOS);

		byte[] data = asnOS.toByteArray();

		System.out.println(Utils.dump(data, data.length, false));

		assertTrue(Arrays.equals(new byte[] { (byte) 0xA4, 0x05, (byte) 0x03,
				0x03, 0x0A, 0x01, 0x04 }, data));
	}

}
