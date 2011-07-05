/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;

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
		
		//trace
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
		0x37 };
		
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
		assertEquals("Wrong operation code", new Long(0x37),oc.getLocalOperationCode());
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}
	
	
	@org.junit.Test
	public void testBasicTCContinue_Long() throws IOException, ParseException {

		//trace
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
				0x24,
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
		assertEquals("Wrong operation code", new Long(42),oc.getLocalOperationCode());
		
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}
	
	
	
	
	@org.junit.Test
	public void testTCContinueMessage_No_Dialog() throws IOException, ParseException {

		
		
		
		
		//no idea how to check rest...?
		
		//created by hand
		byte[] b = new byte[]{
			//TCContinue
			0x65,
				71,
				//org txid
				//OrigTran ID (full)............ 145031169 
				0x48,
					0x04,
					0x08,
					(byte) 0xA5,
					0,
					0x01,
				//dtx
				//DestTran ID (full)............ 144965633
				0x49,
					4,
					8,
					(byte) 0xA4,
					0,
					1,
				//dialog portion
				//empty
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
					// 	local operation 
						0x02,
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
		assertEquals("Expected TCContinue",TCContinueMessage._TAG,tag);
		TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
		
		assertNull("Dialog portion should not be present",tcm.getDialogPortion());
		assertEquals("Destination transaction id does not match",new Long(144965633L), tcm.getDestinationTransactionId());
		assertEquals("Originating transaction id does not match",new Long(145031169L), tcm.getOriginatingTransactionId());
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
		assertEquals("Wrong Operation Code",new Long(0x00FF), ocs.getLocalOperationCode());
		
		assertNotNull("Parameter should not be null",rrl.getParameter());
		
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}

	@org.junit.Test
	public void testTCContinueMessage_No_Component() throws IOException, ParseException {

		
		
		//created by hand
		byte[] b = new byte[]{
				//TCContinue
				0x65,
					56,
					//org txid
					//OrigTran ID (full)............ 145031169 
					0x48,
						0x04,
						0x08,
						(byte) 0xA5,
						0,
						0x01,
					//didTx
					//DstTran ID (full)............ 145031169
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
		assertEquals("Expected TCContinue",TCContinueMessage._TAG,tag);
		TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
		assertNull("Component portion should not be present",tcm.getComponent());
		assertNotNull("Dialog portion should not be null",tcm.getDialogPortion());
		assertEquals("Destination transaction id does not match",new Long(145031169L), tcm.getDestinationTransactionId());
		assertEquals("Originating transaction id does not match",new Long(145031169L), tcm.getOriginatingTransactionId());
		
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
	public void testTCContinueMessage_No_Nothing() throws IOException, ParseException {

		
		
		
		
		//no idea how to check rest...?
		
		//created by hand
		byte[] b = new byte[]{
				//TCContinue
				0x65,
					12,
					//org txid
					//OrigTran ID (full)............ 145031169 
					0x48,
						0x04,
						0x08,
						(byte) 0xA5,
						0,
						0x01,
					//didTx
					//DEstTran ID (full)............ 145031169
					0x49,
					4,
					8,
					(byte) 0xA5,
					0,
					1,
				
		
		};
		
		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCContinue",TCContinueMessage._TAG,tag);
		TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
		
		assertNull("Dialog portion should be null",tcm.getDialogPortion());
		assertNull("Component portion should not be present",tcm.getComponent());
		assertEquals("Destination transaction id does not match",new Long(145031169L), tcm.getDestinationTransactionId());
		assertEquals("Originating transaction id does not match",new Long(145031169L), tcm.getOriginatingTransactionId());
	
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}
	
	@org.junit.Test
	public void testTCContinueMessage_All() throws IOException, ParseException {

		
		
		
		
		//no idea how to check rest...?
		
		//created by hand
		byte[] b = new byte[]{
				//TCContinue
				0x65,
					114,
					//org txid
					//OrigTran ID (full)............ 145031169 
					0x48,
						0x04,
						0x08,
						(byte) 0xA5,
						0,
						0x01,
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
					56,
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
						46,
						//inoke id
						0x02,
						0x01,
						0x02,
						//sequence start
						0x30,
						41,
						//	local operation
							0x02,
							0x01,
							0x01,
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
		assertEquals("Expected TCContinue",TCContinueMessage._TAG,tag);
		TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
		
		//universal
		assertEquals("Destination transaction id does not match",new Long(144965633L), tcm.getDestinationTransactionId());
		assertEquals("Originating transaction id does not match",new Long(145031169L), tcm.getOriginatingTransactionId());
		
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
		assertEquals("Wrong Operation Code",new Long(1), ocs.getLocalOperationCode());

		
		assertNotNull("Parameter should not be null",rrl.getParameter());
		
		
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();
		
		compareArrays(b,encoded);

	}
	
	
}
