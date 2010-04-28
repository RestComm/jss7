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
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
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
				65,
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
				57,
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
						47,
						//inoke id
						0x02,
						0x01,
						0x02,
						//sequence start
						0x30,
						42,
	//					 	global operation
							0x06,
							0x02,
							0x00,
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
		assertEquals("Expected TCEnd",TCEndMessage._TAG,tag);
		TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);
		
		assertNull("Dialog portion should not be present",tcm.getDialogPortion());
		assertEquals("Destination transaction id does not match",new Long(144965633L), tcm.getDestinationTransactionId());
	
		//comp portion
		assertNotNull("Component portion should be present",tcm.getComponent());
		assertEquals("Component count is wrong",2,tcm.getComponent().length);
		Component c = tcm.getComponent()[0];
		assertEquals("Wrong component type",ComponentType.Invoke, c.getType());
		Invoke i = (Invoke) c;
		assertEquals("Wrong invoke ID",new Long(1), i.getInvokeId());
		assertNull("Linked ID is not null", i.getLinkedId());
		
		c = tcm.getComponent()[1];
		assertEquals("Wrong component type",ComponentType.ReturnResultLast, c.getType());
		ReturnResultLast rrl = (ReturnResultLast) c;
		assertEquals("Wrong invoke ID",new Long(2), rrl.getInvokeId());
		assertNotNull("Operation code should not be null", rrl.getOperationCode());

		
		OperationCode ocs = rrl.getOperationCode();
		

		assertEquals("Wrong Operation Code type",OperationCodeType.Global, ocs.getOperationType());
		assertEquals("Wrong Operation Code",new Long(0x00FF), ocs.getCode());
		
		assertNotNull("Parameter should not be null",rrl.getParameter());
		
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}

	@org.junit.Test
	public void testTCEndMessage_No_Component() throws IOException, ParseException {

		
		
		//created by hand
		byte[] b = new byte[]{
				//TCEnd
				0x64,
					50,
					//oidTx
					//OrigTran ID (full)............ 145031169
					0x49,
					4,
					8,
					(byte) 0xA5,
					0,
					1,
					//dialog portion
					0x6B,
						42,
						//extrnal tag
						0x28,
						40,
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
							(byte)160, //asn
							
								29,
								0x61,	//dialog response
									27,
									//protocol version
									(byte)0x80, //protocol version
									
										2,
										7,
									(byte) 0x80,
									(byte) 161,//acn 
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
										0,
									//result
								(byte)0xA2,
										0x03,
										0x2, 
											0x1, 
											(byte) 0x0,
									//result source diagnostic
									(byte)0xA3,
										5,
								  (byte)0x0A2, //provider
											3,
											0x02,//int 2
											0x01,
									  (byte)0x2
									//no user info?
		};
		
		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCEnd",TCEndMessage._TAG,tag);
		TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);
		assertNull("Component portion should not be present",tcm.getComponent());
		assertNotNull("Dialog portion should not be null",tcm.getDialogPortion());
		assertEquals("Destination transaction id does not match",new Long(145031169L), tcm.getDestinationTransactionId());
		
		assertFalse("Dialog should not be Uni", tcm.getDialogPortion().isUnidirectional());
		DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
		assertEquals("Wrong dialog APDU type!",DialogAPDUType.Response, _dapd.getType());
		
		DialogResponseAPDU dapd = (DialogResponseAPDU) _dapd;
		
		//check nulls first
		assertNull("UserInformation should not be present",dapd.getUserInformation());
		
		//not nulls
		assertNotNull("Result should not be null", dapd.getResult());
		Result r = dapd.getResult();
		assertEquals("Wrong result",ResultType.Accepted ,r.getResultType());
		
		
		assertNotNull("Result Source Diagnostic should not be null",dapd.getResultSourceDiagnostic());
		
		ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
		assertNull("User diagnostic should not be present",rsd.getDialogServiceUserType());
		assertEquals("Wrong provider diagnostic type",DialogServiceProviderType.NoCommonDialogPortion,rsd.getDialogServiceProviderType());
		
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}
	@org.junit.Test
	public void testTCEndMessage_No_Nothing() throws IOException, ParseException {

		
		
		
		
		//no idea how to check rest...?
		
		//created by hand
		byte[] b = new byte[]{
				//TCEnd
				0x64,
					6,
					//oidTx
					//OrigTran ID (full)............ 145031169
					0x49,
					4,
					8,
					(byte) 0xA5,
					0,
					1,
				
		
		};
		
		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCEnd",TCEndMessage._TAG,tag);
		TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);
		
		assertNull("Dialog portion should be null",tcm.getDialogPortion());
		assertNull("Component portion should not be present",tcm.getComponent());
		assertEquals("Destination transaction id does not match",new Long(145031169L), tcm.getDestinationTransactionId());
		
	
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}
	
	@org.junit.Test
	public void testTCEndMessage_All() throws IOException, ParseException {

		
		
		
		
		//no idea how to check rest...?
		
		//created by hand
		byte[] b = new byte[]{
				//TCEnd
				0x64,
					68+41,
					//dtx
					//DestTran ID (full)............ 144965633
					0x49,
						4,
						8,
						(byte) 0xA4,
						0,
						1,
					//dialog portion
						0x6B,
						42,
						//extrnal tag
						0x28,
						40,
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
							(byte)160, //asn
							
								29,
								0x61,	//dialog response
									27,
									//protocol version
									(byte)0x80, //protocol version
									
										2,
										7,
									(byte) 0x80,
									(byte) 161,//acn 
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
										0,
									//result
								(byte)0xA2,
										0x03,
										0x2, 
											0x1, 
											(byte) 0x01,
									//result source diagnostic
									(byte)0xA3,
										5,
								  (byte)0x0A2, //provider
											3,
											0x02,//int 2
											0x01,
									  (byte)0x00,
									//no user info?
					//comp portion
					0x6C,
					57,
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
							47,
							//inoke id
							0x02,
							0x01,
							0x02,
							//sequence start
							0x30,
							42,
								//	local operation
								0x02,
								0x02,
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
		assertEquals("Expected TCEnd",TCEndMessage._TAG,tag);
		TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);
		
		//universal
		assertEquals("Destination transaction id does not match",new Long(144965633L), tcm.getDestinationTransactionId());
		
		//dialog portion
		assertNotNull("Dialog portion should not be null",tcm.getDialogPortion());
		assertEquals("Destination transaction id does not match",new Long(144965633L), tcm.getDestinationTransactionId());
		
		assertFalse("Dialog should not be Uni", tcm.getDialogPortion().isUnidirectional());
		DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
		assertEquals("Wrong dialog APDU type!",DialogAPDUType.Response, _dapd.getType());
		
		DialogResponseAPDU dapd = (DialogResponseAPDU) _dapd;
		
		//check nulls first
		assertNull("UserInformation should not be present",dapd.getUserInformation());
		
		//not nulls
		assertNotNull("Result should not be null", dapd.getResult());
		Result r = dapd.getResult();
		assertEquals("Wrong result",ResultType.RejectedPermanent ,r.getResultType());
		
		
		assertNotNull("Result Source Diagnostic should not be null",dapd.getResultSourceDiagnostic());
		
		ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
		assertNull("User diagnostic should not be present",rsd.getDialogServiceUserType());
		assertEquals("Wrong provider diagnostic type",DialogServiceProviderType.Null,rsd.getDialogServiceProviderType());
		
		//comp portion
		assertNotNull("Component portion should be present",tcm.getComponent());
		assertEquals("Component count is wrong",2,tcm.getComponent().length);
		Component c = tcm.getComponent()[0];
		assertEquals("Wrong component type",ComponentType.Invoke, c.getType());
		Invoke i = (Invoke) c;
		assertEquals("Wrong invoke ID",new Long(1), i.getInvokeId());
		assertNull("Linked ID is not null", i.getLinkedId());
		
		c = tcm.getComponent()[1];
		assertEquals("Wrong component type",ComponentType.ReturnResultLast, c.getType());
		ReturnResultLast rrl = (ReturnResultLast) c;
		assertEquals("Wrong invoke ID",new Long(2), rrl.getInvokeId());
		assertNotNull("Operation code should not be null", rrl.getOperationCode());

		
		OperationCode ocs = rrl.getOperationCode();
		
		assertEquals("Wrong Operation Code type",OperationCodeType.Local, ocs.getOperationType());
		assertEquals("Wrong Operation Code",new Long(511), ocs.getCode());

		
		assertNotNull("Parameter should not be null",rrl.getParameter());
		
		
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);
	


	}
}
