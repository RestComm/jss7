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
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;

import junit.framework.TestCase;

public class TcEndTest extends TestCase {

	private void compareArrays(byte[] expected, byte[] encoded) {
		boolean same = Arrays.equals(expected, encoded);
		assertTrue("byte[] dont match, expected|encoded \n"
				+ Arrays.toString(expected) + "\n" + Arrays.toString(encoded),
				same);
	}
	
	@org.junit.Test
	public void testTCEndMessage_No_Dialog() throws IOException, ParseException {

		
		
		
		
		//no idea how to check rest...?
		
		//created by hand
		byte[] b = new byte[]{
			//TCEnd
			0x64,
				67,
				//dialog portion
				//empty
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
				59,
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
					0x37,
				//return result last
				(byte) 0xA2,
					49,
					//inoke id
					0x02,
					0x01,
					0x02,
					//sequence start
					0x30,
					6,
					//	local operation
						0x02,
						0x01,
						0x01,
					// 	global operation
						0x06,
						0x01,
						(byte) 0xFF,
				//	parameter
					0x30,
					36,
					(byte) 0xA0,//some tag.1
					17,
					(byte) 0x80,//some tag.1.1
					2,
					0x11,
					0x11,
					(byte) 0xA1,//some tag.1.2
					04,
					(byte)0x82, //some tag.1.3 ?
					2,
					0x00,
					0x00,
					(byte) 0x82,
					//some tag.1.4
					1,
					12,
					(byte)0x83, //some tag.1.5
					2,
					0x33,
					0x33,
					(byte) 0xA1,//some trash here
					//tension indicator 2........ ???
					//use value.................. ???
					//some tag.2
					3,
					(byte) 0x80,//some tag.2.1
					1,
					-1,
					(byte)0xA2, //some tag.3
					3,
					(byte) 0x80,//some tag.3.1
					1,
					-1,
					(byte) 0xA3,//some tag.4
					5,
					(byte) 0x82,//some tag.4.1
					3,
					(byte) 0xAB,// - 85 serviceKey................... 123456 // dont care about this content, lets just make len correct
					(byte) 0xCD,
					(byte) 0xEF
				};
		
		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCInvoke",TCEndMessage._TAG,tag);
		TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);
		
	
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}

//	@org.junit.Test
//	public void testTCEndMessage_No_Component() throws IOException, ParseException {
//
//		
//		
//		
//		
//		//no idea how to check rest...?
//		
//		//created by hand
//		byte[] b = new byte[]{
//				
//		
//		};
//		
//		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
//		int tag = ais.readTag();
//		assertEquals("Expected TCInvoke",TCEndMessage._TAG,tag);
//		TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);
//		
//	
//		AsnOutputStream aos = new AsnOutputStream();
//		tcm.encode(aos);
//		byte[] encoded = aos.toByteArray();
//		
//		compareArrays(b,encoded);
//
//	}
//	@org.junit.Test
//	public void testTCEndMessage_No_Nothing() throws IOException, ParseException {
//
//		
//		
//		
//		
//		//no idea how to check rest...?
//		
//		//created by hand
//		byte[] b = new byte[]{
//				
//		
//		};
//		
//		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
//		int tag = ais.readTag();
//		assertEquals("Expected TCInvoke",TCEndMessage._TAG,tag);
//		TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);
//		
//	
//		AsnOutputStream aos = new AsnOutputStream();
//		tcm.encode(aos);
//		byte[] encoded = aos.toByteArray();
//		
//		compareArrays(b,encoded);
//
//	}
//	
//	@org.junit.Test
//	public void testTCEndMessage_All() throws IOException, ParseException {
//
//		
//		
//		
//		
//		//no idea how to check rest...?
//		
//		//created by hand
//		byte[] b = new byte[]{
//				
//		
//		};
//		
//		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
//		int tag = ais.readTag();
//		assertEquals("Expected TCInvoke",TCEndMessage._TAG,tag);
//		TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);
//		
//	
//		AsnOutputStream aos = new AsnOutputStream();
//		tcm.encode(aos);
//		byte[] encoded = aos.toByteArray();
//		
//		compareArrays(b,encoded);
//
//	}
}
