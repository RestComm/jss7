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
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class ForwardingDataTest {
    Logger logger = Logger.getLogger(ForwardingDataTest.class);
    
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
		byte[] data = new byte[] { 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36};
		
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		
		ForwardingDataImpl forwardingData = new ForwardingDataImpl();
		forwardingData.decodeAll(asn);
		
		ForwardingOptions forwardingOptions = forwardingData.getForwardingOptions();
		ISDNAddressString isdnAdd = forwardingData.getForwardedToNumber();
		
		assertNotNull(isdnAdd);
		assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
		assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(isdnAdd.getAddress(), "79273605819");	
		assertNotNull(forwardingOptions);
		assertTrue(!forwardingOptions.isNotificationToForwardingParty());
		assertTrue(!forwardingOptions.isRedirectingPresentation());
		assertTrue(forwardingOptions.isNotificationToCallingParty());
		assertTrue(forwardingOptions.getForwardingReason() == ForwardingReason.busy);
	} 
	
	@Test(groups = { "functional.encode", "service.callhandling" })
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36};
		
		ISDNAddressString isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, 
				   NumberingPlan.ISDN, "79273605819");
		ForwardingOptionsImpl forwardingOptions = new ForwardingOptionsImpl(false, false, true, ForwardingReason.busy);
		ForwardingDataImpl forwardingData = new ForwardingDataImpl(isdnAdd, null, forwardingOptions, null, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		forwardingData.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(data, encodedData));
	}
	
	@Test(groups = { "functional.serialize", "service.callhandling" })
	public void testSerialization() throws Exception {
		
	}
}

