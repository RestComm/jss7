package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * The trace is from nad1053.pcap wirehsark trace
 * 
 * @author amit bhayani
 *
 */
public class InvokeTest extends TestCase {
	@org.junit.Test
	public void testEncode() throws IOException, ParseException {
		
		byte[] expected = new byte[] { 
				(byte) 0xa1, //Invoke Tag
				
				0x1d, //Length Dec 29 
				
				0x02, 0x01, 0x0c, //Invoke ID TAG(2) Length(1) Value(12) 
				
				0x02, 0x01, 0x3b, //Operation Code TAG(2), Length(1), Value(59)
				
				//Parameter
				0x24, 0x15, 
				
				//Parameter 1
				0x04, 0x01, 0x0f, 
				
				//Parameter 2
				0x04, 0x10, (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 };		
		
		Invoke invoke = TcapFactory.createComponentInvoke();
		invoke.setInvokeId(12l);
		
		OperationCode oc = TcapFactory.createOperationCode(false, 59l);
		
		invoke.setOperationCode(oc);
		
		Parameter p1 = TcapFactory.createParameter();
		p1.setTagClass(Tag.CLASS_UNIVERSAL);
		p1.setTag(0x04);
		p1.setData(new byte[]{0x0F});
		
		
		
		Parameter p2 = TcapFactory.createParameter();
		p2.setTagClass(Tag.CLASS_UNIVERSAL);
		p2.setTag(0x04);
		p2.setData(new byte[]{(byte) 0xaa, (byte) 0x98, (byte) 0xac,
				(byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36,
				0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5,
				0x72, (byte) 0xb9, 0x11 });		
	
		Parameter pm = TcapFactory.createParameter();
		pm.setTagClass(Tag.CLASS_UNIVERSAL);
		pm.setTag(0x04);
		pm.setParameters(new Parameter[]{p1, p2});
		invoke.setParameter(pm);
		
		AsnOutputStream asnos = new AsnOutputStream();
		
		invoke.encode(asnos);
		
		byte[] encodedData = asnos.toByteArray();
		
		
		//assertTrue(Arrays.equals(expected, encodedData));
		
	}

	@org.junit.Test
	public void testDecodeWithParaSequ() throws IOException, ParseException {

		byte[] b = new byte[] { 
				(byte) 0xa1, //Invoke Tag
				
				0x1d, //Length Dec 29 
				
				0x02, 0x01, 0x0c, //Invoke ID TAG(2) Length(1) Value(12) 
				
				0x02, 0x01, 0x3b, //Operation Code TAG(2), Length(1), Value(59)
				
				//Sequence of parameter 
				0x30, 0x15, 
				
				//Parameter 1
				0x04, 0x01, 0x0f, 
				
				//Parameter 2
				0x04, 0x10, (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 };
		
		AsnInputStream asnIs = new AsnInputStream(new ByteArrayInputStream(b));

		Component comp = TcapFactory.createComponent(asnIs);

		assertEquals(ComponentType.Invoke, comp.getType());

		Invoke invokeComp = (Invoke) comp;

		assertTrue(12L == invokeComp.getInvokeId());

		OperationCode oc = invokeComp.getOperationCode();

		assertNotNull(oc);

		assertTrue(59 == oc.getCode());
		assertEquals(OperationCodeType.Local, oc.getOperationType());

		//FIXME:
		//Parameter[] params = invokeComp.getParameters();

//		assertNotNull(params);
//		assertTrue(2 == params.length);
//
//		// Test Param1. We dont care for what data means, its job of TCAP users
//		Parameter param1 = params[0];
//		assertTrue(0x04 == param1.getTag());
//		assertTrue(Arrays.equals(new byte[] { 0x0F }, param1.getData()));
//
//		Parameter param2 = params[1];
//		assertTrue(0x04 == param2.getTag());
//		assertTrue(Arrays
//				.equals(
//						new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac,
//								(byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36,
//								0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5,
//								0x72, (byte) 0xb9, 0x11 }, param2.getData()));
//		
		

	}

}
