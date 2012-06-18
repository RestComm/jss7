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
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingOptions;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class SendRoutingInformationRequestTest {
    Logger logger = Logger.getLogger(SendRoutingInformationRequestTest.class);
    
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
	
	private byte[] getMSISDNData() {
		return new byte[] { (byte) 128, 7, -111, -110, 17, 19, 50, 19, -15 };
	}
	
	private byte[] getGMSCAddressData() {
		return new byte[] { (byte) 134, 7, -111, -108, -120, 115, 0, -110, -14 };
	}
	
	@Test(groups = { "functional.decode", "service.callhandling" })
	public void testDecode() throws Exception {
		byte[] data = { 48, 21, (byte) 128, 7, -111, -110, 17, 19, 50, 19, -15, (byte) 131, 1, 1, (byte) 134, 7, -111, -108, -120, 115, 0, -110, -14 };
	
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		
		SendRoutingInformationRequestImpl sri = new SendRoutingInformationRequestImpl();
		sri.decodeAll(asn);
		
		ISDNAddressString msisdn = sri.getMsisdn();
		InterrogationType type = sri.getInterogationType();
		ISDNAddressString gmsc = sri.getGatewayMSCAddress();

		assertNotNull(msisdn);
		assertNotNull(type);
		assertNotNull(gmsc);
		assertTrue(msisdn.getAddressNature() == AddressNature.international_number);
		assertTrue(msisdn.getNumberingPlan() == NumberingPlan.ISDN);
		assertEquals(msisdn.getAddress(), "29113123311");
		assertTrue(gmsc.getAddressNature() == AddressNature.international_number);
		assertTrue(gmsc.getNumberingPlan() == NumberingPlan.ISDN);
		assertEquals(gmsc.getAddress(), "49883700292");
		assertEquals(type, InterrogationType.forwarding);
	}
	
	@Test(groups = { "functional.encode", "service.callhandling" })
	public void testEncode() throws Exception {
		byte[] data = { 48, 21, (byte) 128, 7, -111, -110, 17, 19, 50, 19, -15, (byte) 131, 1, 1, (byte) 134, 7, -111, -108, -120, 115, 0, -110, -14 };
		
		InterrogationType type = InterrogationType.forwarding;
		ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, 
															 NumberingPlan.ISDN, "29113123311");
		ISDNAddressString gmsc = new ISDNAddressStringImpl(AddressNature.international_number, 
				 										   NumberingPlan.ISDN, "49883700292");
		
		SendRoutingInformationRequestImpl sri = new SendRoutingInformationRequestImpl(msisdn, gmsc, type, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
	    sri.encodeAll(asnOS);
	    
	    byte[] encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(data, encodedData));

	}
	
	@Test(groups = { "functional.serialize", "service.callhandling" })
	public void testSerialization() throws Exception {
		
	}
}

