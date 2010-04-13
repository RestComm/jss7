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
		assertNull("Parameter not null",i.getParameter());
		OperationCode oc = i.getOperationCode();
		assertEquals("Wrong operation type",OperationCodeType.Local, oc.getOperationType());
		assertEquals("Wrong operation code", new Long(0x37),oc.getCode());
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}
	
	
	@org.junit.Test
	public void testBasicTCContinue_Long() throws IOException, ParseException {

		
		byte[] b = new byte[]{
				//TCContinue
				0x65,
				//len
				60,
				//oid
				//OrigTran ID (full)............ 145031169 
				0x48,
				0x04,
				0x08,
				(byte) 0xA5,
				0,
				1,
				//dtx
				//DestTran ID (full)............ 144965633
				0x49,
				4,
				8,
				(byte) 0xA4,
				0,
				1,
				//comp portion
				0x6C,
				46,
				(byte) //invoke
				0xA1,
				44,
				//invokeId
				0x02,
				1,
				0x02,
				//op code
				0x02,
				0x01,
				42,
				//Parameter
				0x30,
				36,
				(byte) //some tag.1
				0xA0,
				17,
				(byte) //some tag.1.1
				0x80,
				2,
				0x11,
				0x11,
				(byte) //some tag.1.2
				0xA1,
				04,
				(byte) //some tag.1.3 ?
				0x82,
				2,
				0x00,
				0x00,
				(byte) //7
				//some tag.1.4
				0x82,
				1,
				12,
				(byte) //some tag.1.5
				0x83,
				2,
				0x33,
				0x33,
				(byte) //some trash here
				//tension indicator 2........ ???
				//use value.................. ???
				//some tag.2
				0xA1,
				3,
				(byte) //some tag.2.1
				0x80,
				1,
				-1,
				(byte) //some tag.3
				0xA2,
				3,
				(byte) //some tag.3.1
				0x80,
				1,
				 -1,
				 (byte) //some tag.4
				0xA3,
				5,
				(byte) //some tag.4.1
				0x82,
				3,
				(byte) // - 85 serviceKey................... 123456 // dont care about this content, lets just make len correct
				0xAB,
				(byte) 0xCD,
				(byte) 0xEF

		};
		
		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCInvoke",TCContinueMessage._TAG,tag);
		TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
		
		assertNull("Dialog portion should not be present",tcm.getDialogPortion());
		assertEquals("Originating transaction id does not match",new Long(145031169L), tcm.getOriginatingTransactionId());
		assertEquals("Desination transaction id does not match",new Long(144965633L),tcm.getDestinationTransactionId());
		
		assertNotNull("Component portion should be present",tcm.getComponent());
		assertEquals("Component count is wrong",1,tcm.getComponent().length);
		Component c = tcm.getComponent()[0];
		assertEquals("Wrong component type",ComponentType.Invoke, c.getType());
		Invoke i = (Invoke) c;
		assertEquals("Wrong invoke ID",new Long(2), i.getInvokeId());
		assertNull("Linked ID is not null", i.getLinkedId());
		
		assertNotNull("Operation code is null",i.getOperationCode());
		assertNotNull("Parameter null",i.getParameter());
		OperationCode oc = i.getOperationCode();
		assertEquals("Wrong operation type",OperationCodeType.Local, oc.getOperationType());
		assertEquals("Wrong operation code", new Long(42),oc.getCode());
		
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}
	
	
}
