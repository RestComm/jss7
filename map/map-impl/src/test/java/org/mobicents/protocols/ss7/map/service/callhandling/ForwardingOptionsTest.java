package org.mobicents.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingOptions;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingReason;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class ForwardingOptionsTest {
    Logger logger = Logger.getLogger(ForwardingOptionsTest.class);
    
	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeTest
	public void setUp() {
	}

	@AfterTest
	public void tearDown() {
	}
	
	@Test(groups = { "functional.decode", "service.callhandling" })
	public void testDecode() throws Exception {
		byte[] data1 = new byte[] { 0x4, 0x1, (byte) 0xE4 };
		byte[] data2 = new byte[] { 0x4, 0x1, (byte) 0x00 };
		byte[] data3 = new byte[] { 0x4, 0x1, (byte) 0xA8 };
		byte[] data4 = new byte[] { 0x4, 0x1, (byte) 0xC8 };
		byte[] data5 = new byte[] { 0x4, 0x1, (byte) 0x24 };
		
		AsnInputStream asn = new AsnInputStream(data1);
		int tag = asn.readTag();
		ForwardingOptionsImpl fo = new ForwardingOptionsImpl();
		fo.decodeAll(asn);
		
		//logger.info(":::::" + fo.getEncodedDataString());
		assertTrue(fo.isNotificationToForwardingParty());
		assertTrue(fo.isRedirectingPresentation());
		assertTrue(fo.isNotificationToCallingParty());
		assertTrue(fo.getForwardingReason() == ForwardingReason.busy);
		
		
		asn = new AsnInputStream(data2);
		tag = asn.readTag();
		fo = new ForwardingOptionsImpl();
		fo.decodeAll(asn);
		
		//logger.info(":::::" + fo.getEncodedDataString());
		assertTrue(!fo.isNotificationToForwardingParty());
		assertTrue(!fo.isRedirectingPresentation());
		assertTrue(!fo.isNotificationToCallingParty());
		assertTrue(fo.getForwardingReason() == ForwardingReason.notReachable);
		
		
		asn = new AsnInputStream(data3);
		tag = asn.readTag();
		fo = new ForwardingOptionsImpl();
		fo.decodeAll(asn);
		
		//logger.info(":::::" + fo.getEncodedDataString());
		assertTrue(fo.isNotificationToForwardingParty());
		assertTrue(!fo.isRedirectingPresentation());
		assertTrue(fo.isNotificationToCallingParty());
		assertTrue(fo.getForwardingReason() == ForwardingReason.noReply);
		
		
		asn = new AsnInputStream(data4);
		tag = asn.readTag();
		fo = new ForwardingOptionsImpl();
		fo.decodeAll(asn);
		
		//logger.info(":::::" + fo.getEncodedDataString());
		assertTrue(fo.isNotificationToForwardingParty());
		assertTrue(fo.isRedirectingPresentation());
		assertTrue(!fo.isNotificationToCallingParty());
		assertTrue(fo.getForwardingReason() == ForwardingReason.noReply);
		
		
		asn = new AsnInputStream(data5);
		tag = asn.readTag();
		fo = new ForwardingOptionsImpl();
		fo.decodeAll(asn);
		
		//logger.info(":::::" + fo.getEncodedDataString());
		assertTrue(!fo.isNotificationToForwardingParty());
		assertTrue(!fo.isRedirectingPresentation());
		assertTrue(fo.isNotificationToCallingParty());
		assertTrue(fo.getForwardingReason() == ForwardingReason.busy);
	} 
	
	@Test(groups = { "functional.encode", "service.callhandling" })
	public void testEncode() throws Exception {
		byte[] data1 = new byte[] { 0x4, 0x1, (byte) 0xE4 };
		byte[] data2 = new byte[] { 0x4, 0x1, (byte) 0x00 };
		byte[] data3 = new byte[] { 0x4, 0x1, (byte) 0xA8 };
		byte[] data4 = new byte[] { 0x4, 0x1, (byte) 0xC8 };
		byte[] data5 = new byte[] { 0x4, 0x1, (byte) 0x24 };
		
		ForwardingOptionsImpl fo = new ForwardingOptionsImpl(true, true, true, ForwardingReason.busy);
		AsnOutputStream asnOS = new AsnOutputStream();
	    fo.encodeAll(asnOS);
	    
	    //logger.info("1:::::" + fo.getEncodedDataString());
	    byte[] encodedData = asnOS.toByteArray();
	    assertTrue(Arrays.equals(data1, encodedData));
	    
	    
		fo = new ForwardingOptionsImpl(false, false, false, ForwardingReason.notReachable);
		asnOS = new AsnOutputStream();
		fo.encodeAll(asnOS);

		//logger.info("2:::::" + fo.getEncodedDataString());
		encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(data2, encodedData));
		
		
		fo = new ForwardingOptionsImpl(true, false, true, ForwardingReason.noReply);
		asnOS = new AsnOutputStream();
		fo.encodeAll(asnOS);

		//logger.info("3:::::" + fo.getEncodedDataString());
		encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(data3, encodedData));
		
		
		fo = new ForwardingOptionsImpl(true, true, false, ForwardingReason.noReply);
		asnOS = new AsnOutputStream();
		fo.encodeAll(asnOS);

		//logger.info("4:::::" + fo.getEncodedDataString());
		encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(data4, encodedData));
		
		
		fo = new ForwardingOptionsImpl(false, false, true, ForwardingReason.busy);
		asnOS = new AsnOutputStream();
		fo.encodeAll(asnOS);

		//logger.info("5:::::" + fo.getEncodedDataString());
		encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(data5, encodedData));
	}
	
	@Test(groups = { "functional.serialize", "service.callhandling" })
	public void testSerialization() throws Exception {
		
	}
}

