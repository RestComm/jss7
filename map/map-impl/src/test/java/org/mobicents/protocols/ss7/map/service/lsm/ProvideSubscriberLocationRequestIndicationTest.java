/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.BitSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class ProvideSubscriberLocationRequestIndicationTest {

	MapServiceFactory mapServiceFactory = new MapServiceFactoryImpl();

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
		// The trace is from Brazilian operator
		byte[] rawData = new byte[] { (byte) 0xa1, 0x49, 0x02, 0x01, 0x00, 0x02, 0x01, 0x53, 0x30, 0x41, 0x30, 0x03, (byte) 0x80, 0x01, 0x00, 0x04, 0x05,
				(byte) 0x91, 0x55, 0x16, 0x09, 0x70, (byte) 0xa0, 0x1b, (byte) 0x80, 0x01, 0x02, (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x13, (byte) 0x80, 0x01,
				0x0f, (byte) 0x82, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86,
				(byte) 0xc3, 0x65, (byte) 0x82, 0x08, 0x27, (byte) 0x94, (byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7, (byte) 0x86, 0x01, 0x01,
				(byte) 0xa7, 0x05, (byte) 0xa3, 0x03, 0x0a, 0x01, 0x00, (byte) 0x89, 0x02, 0x01, (byte) 0xfe };

		AsnInputStream asnIs = new AsnInputStream(new ByteArrayInputStream(rawData));

		Component comp = TcapFactory.createComponent(asnIs);

		assertEquals(ComponentType.Invoke, comp.getType());

		Invoke invokeComp = (Invoke) comp;

		assertTrue(0L == invokeComp.getInvokeId());

		OperationCode oc = invokeComp.getOperationCode();

		assertNotNull(oc);

		assertTrue(83 == oc.getLocalOperationCode());
		assertEquals(OperationCodeType.Local, oc.getOperationType());

		Parameter param = invokeComp.getParameter();

		ProvideSubscriberLocationRequestIndicationImpl reqInd = new ProvideSubscriberLocationRequestIndicationImpl();
		reqInd.decode(param);

		LocationType locationType = reqInd.getLocationType();
		assertNotNull(locationType);
		assertEquals(LocationEstimateType.currentLocation, locationType.getLocationEstimateType());

		ISDNAddressString mlcNumber = reqInd.getMlcNumber();
		assertNotNull(mlcNumber);
		assertEquals(AddressNature.international_number, mlcNumber.getAddressNature());
		assertEquals(NumberingPlan.ISDN, mlcNumber.getNumberingPlan());
		assertEquals("55619007", mlcNumber.getAddress());

		LCSClientID lcsClientId = reqInd.getLCSClientID();
		assertNotNull(lcsClientId);
		assertEquals(LCSClientType.plmnOperatorServices, lcsClientId.getLCSClientType());
		assertEquals(LCSClientInternalID.broadcastService, lcsClientId.getLCSClientInternalID());
		LCSClientName lcsClientName = lcsClientId.getLCSClientName();
		assertNotNull(lcsClientName);
		assertEquals((byte) 0x0f, lcsClientName.getDataCodingScheme());
		lcsClientName.getNameString().decode();
		assertEquals("ndmgapp2ndmgapp2", lcsClientName.getNameString().getString());

		IMSI imsi = reqInd.getIMSI();
		assertNotNull(imsi);
		assertEquals(724l, imsi.getMCC());
		assertEquals(99l, imsi.getMNC());
		assertEquals("9900000007", imsi.getMSIN());

		assertEquals(1, reqInd.getLCSPriority());

		LCSQoS lcsQoS = reqInd.getLCSQoS();
		assertNotNull(lcsQoS);
		ResponseTime respTime = lcsQoS.getResponseTime();
		assertNotNull(respTime);
		assertEquals(ResponseTimeCategory.lowdelay, respTime.getResponseTimeCategory());

		BitSet suppGadShapes = reqInd.getSupportedGADShapes();
		assertNotNull(suppGadShapes);
		assertTrue(suppGadShapes.get(0));
		assertTrue(suppGadShapes.get(1));
		assertTrue(suppGadShapes.get(2));
		assertTrue(suppGadShapes.get(3));

		assertTrue(suppGadShapes.get(4));
		assertTrue(suppGadShapes.get(5));
		assertTrue(suppGadShapes.get(6));
	}

	@Test
	public void testEncode() throws Exception {
		// The trace is from Brazilian operator
		byte[] data = new byte[] { 0x30, 0x03, (byte) 0x80, 0x01, 0x00, 0x04, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70, (byte) 0xa0, 0x1b, (byte) 0x80, 0x01,
				0x02, (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x13, (byte) 0x80, 0x01, 0x0f, (byte) 0x82, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86,
				(byte) 0xc3, 0x65, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, (byte) 0x82, 0x08, 0x27, (byte) 0x94, (byte) 0x99, 0x09,
				0x00, 0x00, 0x00, (byte) 0xf7, (byte) 0x86, 0x01, 0x01, (byte) 0xa7, 0x05, (byte) 0xa3, 0x03, 0x0a, 0x01, 0x00, (byte) 0x89, 0x02, 0x01,
				(byte) 0xfe };

		LocationType locationType = new LocationTypeImpl(LocationEstimateType.currentLocation, null);
		ISDNAddressString mlcNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "55619007");

		USSDString nameString = mapServiceFactory.createUSSDString("ndmgapp2ndmgapp2");
		LCSClientName lcsClientName = new LCSClientNameImpl((byte) 0x0f, nameString, null);

		LCSClientID lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null, LCSClientInternalID.broadcastService, lcsClientName, null,
				null, null);

		IMSI imsi = mapServiceFactory.createIMSI(724l, 99l, "9900000007");

		LCSQoS lcsQoS = new LCSQoSImpl(null, null, null, new ResponseTimeImpl(ResponseTimeCategory.lowdelay), null);

		BitSet supportedGADShapes = new BitSet(7);
		supportedGADShapes.set(0);
		supportedGADShapes.set(1);
		supportedGADShapes.set(2);
		supportedGADShapes.set(3);
		supportedGADShapes.set(4);
		supportedGADShapes.set(5);
		supportedGADShapes.set(6);

		ProvideSubscriberLocationRequestIndicationImpl reqInd = new ProvideSubscriberLocationRequestIndicationImpl(locationType, mlcNumber, lcsClientID, null,
				imsi, null, null, null, 1, lcsQoS, null, supportedGADShapes, null, null, null, null, null, null);

		AsnOutputStream asnOs = new AsnOutputStream();
		reqInd.encode(asnOs);

		assertTrue(Arrays.equals(data, asnOs.toByteArray()));
	}
}
