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
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;

import junit.framework.TestCase;

public class TcBeginTest extends TestCase {

	private void compareArrays(byte[] expected, byte[] encoded) {
		boolean same = Arrays.equals(expected, encoded);
		assertTrue("byte[] dont match, expected|encoded \n"
				+ Arrays.toString(expected) + "\n" + Arrays.toString(encoded),
				same);
	}
	
	@org.junit.Test
	public void testBasicTCBegin() throws IOException, ParseException {

		
		
		
		
		//no idea how to check rest...?
		
		//trace
		byte[] b = new byte[]{
				//TCBegin
				0x62,
				38,
				//oidTx
				//OrigTran ID (full)............ 145031169
				0x48,
				4,
				8,
				(byte) 0xA5,
				0,
				1,
				//dialog portion
				0x6B,
				30,
				//extrnal tag
				0x28,
				28,
				//oid
				0x06,
				7,
				0,
				17,
				(byte) 134,
				5,
				1,
				1,
				1,
				(byte) //asn
				160,
				17,
				//DialogPDU - Request
				0x60,
				15,
				(byte) //protocol version
				0x80,
				2,
				7,
				(byte) 0x80,
				(byte) //acn 
				161,
				9,
				//oid
				6,
				7,
				4,
				0,
				1,
				1,
				1,
				3,
				0
		
		};
		
		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCInvoke",TCBeginMessage._TAG,tag);
		TCBeginMessage tcm = TcapFactory.createTCBeginMessage(ais);
		
		assertEquals("Originating transaction id does not match",new Long(145031169L), tcm.getOriginatingTransactionId());
		assertNotNull("Dialog portion should not be null",tcm.getDialogPortion());
		
		assertNull("Component portion should not be present",tcm.getComponent());
		
		
		DialogPortion dp = tcm.getDialogPortion();
		
		assertFalse("Dialog should not be Uni",dp.isUnidirectional());
		
		DialogAPDU dapdu = dp.getDialogAPDU();
		assertNotNull("APDU should not be null",dapdu);
		assertEquals("Wrong APDU type",DialogAPDUType.Request, dapdu.getType());
		DialogRequestAPDU dr = (DialogRequestAPDU) dapdu;
		//rest is checked in dialog portion type!
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}

	
	
}
