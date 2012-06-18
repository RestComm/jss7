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
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProtocolId;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class ExternalSignalInfoTest {
    Logger logger = Logger.getLogger(ExternalSignalInfoTest.class);
    
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
		byte[] data = new byte[] { 48, 9, 10, 1,  2, 4, 4, 10, 20, 30, 40 };
		byte[] data_ =  new byte[] { 10, 20, 30, 40 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		ExternalSignalInfoImpl extSignalInfo = new ExternalSignalInfoImpl();
		extSignalInfo.decodeAll(asn);
		
		ProtocolId protocolId = extSignalInfo.getProtocolId();
		byte[] signalInfo = extSignalInfo.getSignalInfo();

		assertTrue(Arrays.equals(data_, signalInfo));
		assertNotNull(protocolId);
		assertTrue(protocolId == ProtocolId.gsm_0806);
	} 
	
	@Test(groups = { "functional.encode", "service.callhandling" })
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 48, 9, 10, 1,  2, 4, 4, 10, 20, 30, 40 };
		byte[] data_ =  new byte[] { 10, 20, 30, 40 };
		
		byte[] signalInfo = data_;
		ProtocolId protocolId = ProtocolId.gsm_0806;
		ExternalSignalInfoImpl extSignalInfo = new ExternalSignalInfoImpl(signalInfo, protocolId, null);

		AsnOutputStream asnOS = new AsnOutputStream();
		extSignalInfo.encodeAll(asnOS);

	    byte[] encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(data, encodedData));
	}
	
	@Test(groups = { "functional.serialize", "service.callhandling" })
	public void testSerialization() throws Exception {
		
	}
}

