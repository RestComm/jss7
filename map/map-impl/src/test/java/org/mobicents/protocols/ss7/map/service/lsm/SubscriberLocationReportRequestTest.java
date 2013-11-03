/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SubscriberLocationReportRequestTest {

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
        return new byte[] { 48, -127, -92, 10, 1, 0, 48, 3, -128, 1, 2, 48, 8, 4, 6, -111, 68, 68, 84, 85, 85, -128, 6, -111,
                102, 102, 118, 119, 119, -127, 5, 33, 67, 21, 50, 84, -126, 8, 33, 67, 101, -121, 9, 33, 67, 101, -125, 6,
                -111, -120, -120, -104, -103, -103, -124, 6, -111, -120, -120, 8, 0, 0, -123, 1, 11, -122, 1, 5, -89, 4, -95,
                2, -128, 0, -120, 1, 12, -87, 14, 3, 2, 4, -128, -95, 8, 4, 6, -111, 68, 68, 84, 85, 85, -118, 1, 6, -117, 2,
                13, 14, -116, 3, 15, 16, 17, -83, 7, -127, 5, 34, -16, 33, 16, -31, -114, 5, 21, 22, 23, 24, 25, -113, 1, 7,
                -111, 0, -110, 0, -109, 1, 0, -108, 4, 26, 27, 28, 29, -107, 1, 9, -74, 6, 2, 1, 10, 2, 1, 11, -105, 0, -104,
                2, 31, 32, -103, 1, 33, -70, 8, -128, 6, -111, -111, -126, 115, 100, -11 };
    }

    public byte[] getDataExtGeographicalInformation() {
        return new byte[] { 11 };
    }

    public byte[] getDataAddGeographicalInformation() {
        return new byte[] { 12 };
    }

    public byte[] getPositioningDataInformation() {
        return new byte[] { 13, 14 };
    }

    public byte[] getUtranPositioningDataInfo() {
        return new byte[] { 15, 16, 17 };
    }

    public byte[] getGSNAddress() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    public byte[] getVelocityEstimate() {
        return new byte[] { 26, 27, 28, 29 };
    }

    public byte[] getGeranGANSSpositioningData() {
        return new byte[] { 31, 32 };
    }

    public byte[] getUtranGANSSpositioningData() {
        return new byte[] { 33 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportRequestImpl imp = new SubscriberLocationReportRequestImpl();
        imp.decodeAll(asn);

        assertEquals(imp.getLCSEvent(), LCSEvent.emergencyCallOrigination);
        assertEquals(imp.getLCSClientID().getLCSClientType(), LCSClientType.plmnOperatorServices);
        assertTrue(imp.getLCSLocationInfo().getNetworkNodeNumber().getAddress().equals("4444455555"));
        assertTrue(imp.getMSISDN().getAddress().equals("6666677777"));
        assertTrue(imp.getIMSI().getData().equals("1234512345"));
        assertTrue(imp.getIMEI().getIMEI().equals("1234567890123456"));
        assertTrue(imp.getNaESRD().getAddress().equals("8888899999"));
        assertTrue(imp.getNaESRK().getAddress().equals("8888800000"));
        assertTrue(Arrays.equals(imp.getLocationEstimate().getData(), getDataExtGeographicalInformation()));
        assertEquals((int) imp.getAgeOfLocationEstimate(), 5);
        assertTrue(imp.getSLRArgExtensionContainer().getSlrArgPcsExtensions().getNaEsrkRequest());
        assertTrue(Arrays.equals(imp.getAdditionalLocationEstimate().getData(), getDataAddGeographicalInformation()));
        assertTrue(imp.getDeferredmtlrData().getDeferredLocationEventType().getMsAvailable());
        assertFalse(imp.getDeferredmtlrData().getDeferredLocationEventType().getEnteringIntoArea());
        assertFalse(imp.getDeferredmtlrData().getDeferredLocationEventType().getLeavingFromArea());
        assertFalse(imp.getDeferredmtlrData().getDeferredLocationEventType().getBeingInsideArea());
        assertEquals((int) imp.getLCSReferenceNumber(), 6);
        assertTrue(Arrays.equals(imp.getGeranPositioningData().getData(), getPositioningDataInformation()));
        assertTrue(Arrays.equals(imp.getUtranPositioningData().getData(), getUtranPositioningDataInfo()));
        assertEquals(imp.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 220);
        assertEquals(imp.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 12);
        assertEquals(imp.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4321);
        assertTrue(Arrays.equals(imp.getHGMLCAddress().getData(), getGSNAddress()));
        assertEquals((int) imp.getLCSServiceTypeID(), 7);
        assertTrue(imp.getSaiPresent());
        assertTrue(imp.getPseudonymIndicator());
        assertEquals(imp.getAccuracyFulfilmentIndicator(), AccuracyFulfilmentIndicator.requestedAccuracyFulfilled);
        assertTrue(Arrays.equals(imp.getVelocityEstimate().getData(), getVelocityEstimate()));
        assertEquals((int) imp.getSequenceNumber(), 9);
        assertEquals((int) imp.getPeriodicLDRInfo().getReportingAmount(), 10);
        assertEquals((int) imp.getPeriodicLDRInfo().getReportingInterval(), 11);
        assertTrue(imp.getMoLrShortCircuitIndicator());
        assertTrue(Arrays.equals(imp.getGeranGANSSpositioningData().getData(), getGeranGANSSpositioningData()));
        assertTrue(Arrays.equals(imp.getUtranGANSSpositioningData().getData(), getUtranGANSSpositioningData()));
        assertTrue(imp.getTargetServingNodeForHandover().getMscNumber().getAddress().equals("192837465"));

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        byte[] data = getEncodedData();

        LCSClientIDImpl lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null, null, null, null, null,
                null);
        ISDNAddressStringImpl networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "4444455555");
        LCSLocationInfoImpl lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, false, null, null, null,
                null, null);
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "6666677777");
        ;
        IMSIImpl imsi = new IMSIImpl("1234512345");
        IMEIImpl imei = new IMEIImpl("1234567890123456");
        ISDNAddressString naEsrd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "8888899999");
        ISDNAddressString naEsrk = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "8888800000");
        ExtGeographicalInformationImpl locationEstimate = new ExtGeographicalInformationImpl(
                getDataExtGeographicalInformation());
        SLRArgPCSExtensionsImpl slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(true);
        SLRArgExtensionContainerImpl slrArgExtensionContainer = new SLRArgExtensionContainerImpl(null, slrArgPcsExtensions);
        AddGeographicalInformationImpl addLocationEstimate = new AddGeographicalInformationImpl(
                getDataAddGeographicalInformation());
        DeferredLocationEventTypeImpl deferredLocationEventType = new DeferredLocationEventTypeImpl(true, false, false, false);
        // boolean msAvailable, boolean enteringIntoArea, boolean leavingFromArea, boolean beingInsideArea
        DeferredmtlrDataImpl deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, null, lcsLocationInfo);
        PositioningDataInformationImpl geranPositioningData = new PositioningDataInformationImpl(
                getPositioningDataInformation());
        UtranPositioningDataInfoImpl utranPositioningData = new UtranPositioningDataInfoImpl(getUtranPositioningDataInfo());
        LAIFixedLengthImpl laiFixedLength = new LAIFixedLengthImpl(220, 12, 4321);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(laiFixedLength);
        GSNAddressImpl hgmlcAddress = new GSNAddressImpl(getGSNAddress());
        VelocityEstimateImpl velocityEstimate = new VelocityEstimateImpl(getVelocityEstimate());
        PeriodicLDRInfoImpl periodicLDRInfo = new PeriodicLDRInfoImpl(10, 11);
        GeranGANSSpositioningDataImpl geranGANSSpositioningData = new GeranGANSSpositioningDataImpl(
                getGeranGANSSpositioningData());
        UtranGANSSpositioningDataImpl utranGANSSpositioningData = new UtranGANSSpositioningDataImpl(
                getUtranGANSSpositioningData());
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "192837465");
        ServingNodeAddressImpl targetServingNodeForHandover = new ServingNodeAddressImpl(mscNumber, true);

        SubscriberLocationReportRequestImpl imp = new SubscriberLocationReportRequestImpl(LCSEvent.emergencyCallOrigination,
                lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, 5,
                slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, 6, geranPositioningData, utranPositioningData,
                cellIdOrSai, hgmlcAddress, 7, true, true, AccuracyFulfilmentIndicator.requestedAccuracyFulfilled,
                velocityEstimate, 9, periodicLDRInfo, true, geranGANSSpositioningData, utranGANSSpositioningData,
                targetServingNodeForHandover);
        // LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo, ISDNAddressString msisdn,
        // IMSI imsi, IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk, ExtGeographicalInformation
        // locationEstimate,
        // Integer ageOfLocationEstimate, SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation
        // addLocationEstimate,
        // DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
        // UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai, GSNAddress hgmlcAddress,
        // Integer lcsServiceTypeID,
        // boolean saiPresent, boolean pseudonymIndicator, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator,
        // VelocityEstimate velocityEstimate,
        // Integer sequenceNumber, PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator, GeranGANSSpositioningData
        // geranGANSSpositioningData,
        // UtranGANSSpositioningData utranGANSSpositioningData, ServingNodeAddress targetServingNodeForHandover

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

    }
}
