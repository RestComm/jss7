/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.*;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public class ProvideSubscriberLocationResponseTest {

	MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

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

	public byte[] getEncodedData() {
		return new byte[] { 48, 6, 4, 1, 99, -128, 1, 15 };
	}

	public byte[] getEncodedDataFull() {
		return new byte[] { 0 };
	}

	public byte[] getExtGeographicalInformation() {
		return new byte[] { 99 };
	}

	@Test(groups = { "functional.decode","service.lsm"})
	public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
		byte[] rawData = getEncodedData();

		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		assertEquals(tag, Tag.SEQUENCE);

		ProvideSubscriberLocationResponseImpl impl = new ProvideSubscriberLocationResponseImpl();
		impl.decodeAll(asn);

		assertTrue(Arrays.equals(impl.getLocationEstimate().getData(), getExtGeographicalInformation()));
		assertEquals((int)impl.getAgeOfLocationEstimate(), 15);

		assertNull(impl.getExtensionContainer());
		assertNull(impl.getAdditionalLocationEstimate());
		assertFalse(impl.getDeferredMTLRResponseIndicator());
		assertNull(impl.getGeranPositioningData());
		assertNull(impl.getUtranPositioningData());
		assertNull(impl.getCellIdOrSai());
		assertFalse(impl.getSaiPresent());
		assertNull(impl.getAccuracyFulfilmentIndicator());
		assertNull(impl.getVelocityEstimate());
		assertFalse(impl.getMoLrShortCircuitIndicator());
		assertNull(impl.getGeranGANSSpositioningData());
		assertNull(impl.getUtranGANSSpositioningData());
		assertNull(impl.getTargetServingNodeForHandover());
	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {
		byte[] rawData = getEncodedData();

//		LocationType locationType = new LocationTypeImpl(LocationEstimateType.currentLocation, null);
//		ISDNAddressString mlcNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "55619007");
//
//		USSDString nameString = MAPParameterFactory.createUSSDString("ndmgapp2ndmgapp2");
//		LCSClientName lcsClientName = new LCSClientNameImpl((byte) 0x0f, nameString, null);
//
//		LCSClientID lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null, LCSClientInternalID.broadcastService, lcsClientName, null,
//				null, null);
//
//		IMSI imsi = MAPParameterFactory.createIMSI("724999900000007");
//
//		LCSQoS lcsQoS = new LCSQoSImpl(null, null, false, new ResponseTimeImpl(ResponseTimeCategory.lowdelay), null);
//
//		SupportedGADShapes supportedGADShapes = new SupportedGADShapesImpl(true, true, true, true, true, true, true);

		ExtGeographicalInformationImpl egeo = new ExtGeographicalInformationImpl(getExtGeographicalInformation());

		ProvideSubscriberLocationResponseImpl reqInd = new ProvideSubscriberLocationResponseImpl(egeo, null, null, 15, null, null, false, null, false, null,
				null, false, null, null, null);
//		ExtGeographicalInformation locationEstimate, PositioningDataInformation geranPositioningData,
//		UtranPositioningDataInfo utranPositioningData, Integer ageOfLocationEstimate, AddGeographicalInformation additionalLocationEstimate,
//		MAPExtensionContainer extensionContainer, Boolean deferredMTLRResponseIndicator, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
//		Boolean saiPresent, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate, boolean moLrShortCircuitIndicator,
//		GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
//		ServingNodeAddress targetServingNodeForHandover

		AsnOutputStream asnOS = new AsnOutputStream();
		reqInd.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(rawData, encodedData));

	
		rawData = getEncodedDataFull();

//		ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "765432100");
//		LMSIImpl lmsi = new LMSIImpl(getDataLmsi());
//		IMEIImpl imei = new IMEIImpl("1234567890123456");
//		USSDString lcsCodewordString = MAPParameterFactory.createUSSDString("xxyyyzz");
//		LCSCodewordImpl lcsCodeword = new LCSCodewordImpl((byte) 0x0f, lcsCodewordString);
//		LCSPrivacyCheckImpl lcsPrivacyCheck = new LCSPrivacyCheckImpl(PrivacyCheckRelatedAction.allowedWithNotification, PrivacyCheckRelatedAction.allowedWithoutNotification);
//		ArrayList<Area> areaList = new ArrayList<Area>();
//		AreaIdentification areaIdentification = new AreaIdentificationImpl(AreaType.countryCode, 250, 0, 0, 0);
//		AreaImpl area = new AreaImpl(AreaType.countryCode, areaIdentification);
//		areaList.add(area);
//		AreaDefinition areaDefinition = new AreaDefinitionImpl(areaList);
//		AreaEventInfoImpl areaEventInfo = new AreaEventInfoImpl(areaDefinition, null, null);
//		GSNAddress hgmlcAddress = new GSNAddressImpl(getDataHgmlcAddress());
//		PeriodicLDRInfo periodicLDRInfo = new PeriodicLDRInfoImpl(200, 100);
//		ArrayList<ReportingPLMN> lstRplmn = new ArrayList<ReportingPLMN>();
//		PlmnId plmnId = new PlmnIdImpl(getPlmnId());
//		ReportingPLMN rplmn = new ReportingPLMNImpl(plmnId, null, false);
//		lstRplmn.add(rplmn);
//		ReportingPLMNList reportingPLMNList = new ReportingPLMNListImpl(false, lstRplmn);
//
//		reqInd = new ProvideSubscriberLocationRequestImpl(locationType, mlcNumber, lcsClientID, true, imsi, msisdn, lmsi, imei, LCSPriority.normalPriority,
//				lcsQoS, null, supportedGADShapes, 5, 6, lcsCodeword, lcsPrivacyCheck, areaEventInfo, hgmlcAddress, true, periodicLDRInfo, reportingPLMNList);
//// LocationType locationType, ISDNAddressString mlcNumber, LCSClientID lcsClientID, boolean privacyOverride,
////		IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei, LCSPriority lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
////		SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID, LCSCodeword lcsCodeword,
////		LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress, boolean moLrShortCircuitIndicator,
////		PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList
//
//		asnOS = new AsnOutputStream();
//		reqInd.encodeAll(asnOS);
//
//		encodedData = asnOS.toByteArray();
//		assertTrue(Arrays.equals(rawData, encodedData));
	}

}
