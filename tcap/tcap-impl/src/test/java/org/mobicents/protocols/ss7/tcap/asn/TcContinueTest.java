package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortionImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;

import junit.framework.TestCase;

public class TcContinueTest extends TestCase {

	private void compareArrays(byte[] expected, byte[] encoded) {
		boolean same = Arrays.equals(expected, encoded);
		assertTrue("byte[] dont match, expected|encoded \n"
				+ Arrays.toString(expected) + "\n" + Arrays.toString(encoded),
				same);
	}
	
	@org.junit.Test
	public void testBasicTCContinue() throws IOException, ParseException {

		
		//OrigTran ID (full)............ 145031169
		//DestTran ID (full)............ 144965633
		
		
		//no idea how to check rest...?
		
		
		byte[] b = new byte[]{
		0x65,
		0x16,
		//org txid
		0x48,
		0x04,
		0x08,
		(byte) 0xA5,
		0,
		0x01,
		//dtx
		0x49,
		0x04,
		8,
		(byte) 0xA4,
		0,
		1,
		//comp portion
		0x6C,
		8,
		//invoke
		(byte) 0xA1,
		6,
		//invoke ID
		0x02,
		0x01,
		0x01,
		//op code
		0x02,
		0x01,
		0x37};
		
		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCInvoke",TCContinueMessage._TAG,tag);
		TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
		
		assertNull("Dialog portion should not be present",tcm.getDialogPortion());
		assertEquals("Originating transaction id does not match",new Long(145031169L), tcm.getOriginatingTransactionId());
		assertEquals("Desination transaction id does not match",new Long(144965633L), tcm.getDestinationTransactionId());
		
		assertNotNull("Component portion should be present",tcm.getComponent());
		assertEquals("Component count is wrong",1,tcm.getComponent().length);
		Component c = tcm.getComponent()[0];
		assertEquals("Wrong component type",ComponentType.Invoke, c.getType());
		Invoke i = (Invoke) c;
		assertEquals("Wrong invoke ID",new Long(1), i.getInvokeId());
		assertNull("Linked ID is not null", i.getLinkedId());
		
		assertNotNull("Operation code is null",i.getOperationCode());
		OperationCode oc = i.getOperationCode();
		assertEquals("Wrong operation type",OperationCodeType.Local, oc.getOperationType());
		assertEquals("Wrong operation code", new Long(0x37),oc.getCode());
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}
	
	
	
}
