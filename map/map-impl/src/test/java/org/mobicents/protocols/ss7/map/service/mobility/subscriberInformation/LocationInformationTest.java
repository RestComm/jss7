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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CSGIdImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationInformationTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 22, 2, 1, 0, -127, 6, -111, 0, 32, 34, 17, -15, -93, 9, -128, 7, 82, -16, 16, 0, 111, 8, -92 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 32, 2, 1, 0, -127, 6, -111, 0, 32, 34, 17, -15, -126, 8, -124, -105, 8, 2, -105, 1, 32, -112,
                -93, 9, -128, 7, 82, -16, 16, 0, 111, 8, -92 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 87, 2, 1, 3, -128, 8, 11, 12, 13, 14, 15, 16, 17, 18, -127, 6, -111, 0, 32, 34, 17, -15, -126,
                8, -124, -105, 8, 2, -105, 1, 32, -112, -93, 9, -128, 7, 82, -16, 16, 0, 111, 8, -92, -123, 3, 21, 22, 23,
                -122, 6, -111, -120, 40, 34, 102, -10, -121, 10, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, -120, 0, -119, 0, -86,
                5, -123, 0, -122, 1, 7, -85, 7, -128, 5, 5, -128, 0, 0, 32 };
    }

    private byte[] getDataGeographicalInformation() {
        return new byte[] { 11, 12, 13, 14, 15, 16, 17, 18 };
    }

    private byte[] getDataLSAIdentity() {
        return new byte[] { 21, 22, 23 };
    }

    private byte[] getDataGeodeticInformation() {
        return new byte[] { 31, 32, 33, 34, 35, 36, 37, 38, 39, 40 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        LocationInformationImpl impl = new LocationInformationImpl();
        impl.decodeAll(asn);

        assertEquals((int) impl.getAgeOfLocationInformation(), 0);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 250);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellId(), 2212);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 111);
        assertEquals(impl.getVlrNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(impl.getVlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(impl.getVlrNumber().getAddress().equals("000222111"));

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        impl = new LocationInformationImpl();
        impl.decodeAll(asn);

        assertEquals((int) impl.getAgeOfLocationInformation(), 0);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 250);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellId(), 2212);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 111);
        assertEquals(impl.getVlrNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(impl.getVlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(impl.getVlrNumber().getAddress().equals("000222111"));
        LocationNumber ln = impl.getLocationNumber().getLocationNumber();
        assertEquals(ln.getNatureOfAddressIndicator(), 4);
        assertTrue(ln.getAddress().equals("80207910020"));
        assertEquals(ln.getNumberingPlanIndicator(), 1);
        assertEquals(ln.getInternalNetworkNumberIndicator(), 1);
        assertEquals(ln.getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(ln.getScreeningIndicator(), 3);
        assertFalse(impl.getCurrentLocationRetrieved());
        assertFalse(impl.getSaiPresent());

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        impl = new LocationInformationImpl();
        impl.decodeAll(asn);

        assertEquals((int) impl.getAgeOfLocationInformation(), 3);
        assertTrue(Arrays.equals(impl.getGeographicalInformation().getData(), getDataGeographicalInformation()));
        assertEquals(impl.getVlrNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(impl.getVlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(impl.getVlrNumber().getAddress().equals("000222111"));
        ln = impl.getLocationNumber().getLocationNumber();
        assertEquals(ln.getNatureOfAddressIndicator(), 4);
        assertTrue(ln.getAddress().equals("80207910020"));
        assertEquals(ln.getNumberingPlanIndicator(), 1);
        assertEquals(ln.getInternalNetworkNumberIndicator(), 1);
        assertEquals(ln.getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(ln.getScreeningIndicator(), 3);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 250);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellId(), 2212);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 111);
        assertTrue(Arrays.equals(impl.getSelectedLSAId().getData(), getDataLSAIdentity()));
        assertTrue(impl.getMscNumber().getAddress().equals("888222666"));
        assertTrue(Arrays.equals(impl.getGeodeticInformation().getData(), getDataGeodeticInformation()));
        assertTrue(impl.getCurrentLocationRetrieved());
        assertTrue(impl.getSaiPresent());
        assertEquals((int) impl.getLocationInformationEPS().getAgeOfLocationInformation(), 7);
        assertTrue(impl.getCurrentLocationRetrieved());
        BitSetStrictLength bs = impl.getUserCSGInformation().getCSGId().getData();
        assertTrue(bs.get(0));
        assertFalse(bs.get(1));
        assertFalse(bs.get(25));
        assertTrue(bs.get(26));
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        ISDNAddressString vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "000222111");
        CellGlobalIdOrServiceAreaIdFixedLengthImpl cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(
                250, 1, 111, 2212);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(
                cellGlobalIdOrServiceAreaIdFixedLength);
        LocationInformationImpl impl = new LocationInformationImpl(0, null, vlrNumber, null, cellGlobalIdOrServiceAreaIdOrLAI,
                null, null, null, null, false, false, null, null);
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000222111");
        cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(250, 1, 111, 2212);
        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
        LocationNumberMapImpl locationNumber = new LocationNumberMapImpl(new byte[] { (byte) 132, (byte) 151, 8, 2, (byte) 151,
                1, 32, (byte) 144 });
        impl = new LocationInformationImpl(0, null, vlrNumber, locationNumber, cellGlobalIdOrServiceAreaIdOrLAI, null, null,
                null, null, false, false, null, null);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        GeographicalInformationImpl ggi = new GeographicalInformationImpl(getDataGeographicalInformation());
        LSAIdentityImpl selectedLSAId = new LSAIdentityImpl(getDataLSAIdentity());
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "888222666");
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(getDataGeodeticInformation());
        LocationInformationEPSImpl liEps = new LocationInformationEPSImpl(null, null, null, null, null, true, 7, null);
        BitSetStrictLength bs = new BitSetStrictLength(27);
        bs.set(0);
        bs.set(26);
        CSGIdImpl csgId = new CSGIdImpl(bs);
        UserCSGInformationImpl uci = new UserCSGInformationImpl(csgId, null, null, null);
        impl = new LocationInformationImpl(3, ggi, vlrNumber, locationNumber, cellGlobalIdOrServiceAreaIdOrLAI, null,
                selectedLSAId, mscNumber, gdi, true, true, liEps, uci);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Integer ageOfLocationInformation, GeographicalInformation geographicalInformation, ISDNAddressString vlrNumber,
        // LocationNumber locationNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
        // MAPExtensionContainer extensionContainer,
        // LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation, boolean
        // currentLocationRetrieved,
        // boolean saiPresent, LocationInformationEPS locationInformationEPS, UserCSGInformation userCSGInformation
    }

}
