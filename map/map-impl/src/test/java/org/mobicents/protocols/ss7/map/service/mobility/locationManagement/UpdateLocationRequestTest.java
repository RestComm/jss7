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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.LocationArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class UpdateLocationRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 25, 4, 5, 17, 17, 33, 34, 34, -127, 4, -111, 34, 34, -8, 4, 4, -111, 34, 34, -7, -90, 4, -123,
                2, 3, -128 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 80, 4, 5, 17, 17, 33, 34, 51, -127, 4, -111, 34, 34, -8, 4, 4, -111, 34, 34, -7, -118, 4, 1, 3,
                5, 8, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3,
                5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -90, 4, -123, 2, 3, -128, -117, 0, -116, 0, -113, 0, -112, 0 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 65, 4, 5, 17, 17, 33, 34, 51, -127, 4, -111, 34, 34, -8, 4, 4, -111, 34, 34, -7, -118, 4, 1, 3,
                5, 8, -90, 4, -123, 2, 3, -128, -117, 0, -116, 0, -126, 6, 1, 1, 1, 1, 1, 1, -83, 10, -128, 8, 33, 67, 101,
                -121, 9, -112, 120, -10, -82, 4, -127, 2, 0, 123, -113, 0, -112, 0 };
    }

    private byte[] getEncodedData_V1() {
        return new byte[] { 48, 19, 4, 5, 17, 17, 33, 34, 51, -128, 4, -111, 34, 34, -16, 4, 4, -111, 34, 34, -15 };
    }

    private byte[] getLmsiData() {
        return new byte[] { 1, 3, 5, 8 };
    }

    private byte[] getGSNAddressData() {
        return new byte[] { 1, 1, 1, 1, 1, 1 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        UpdateLocationRequestImpl asc = new UpdateLocationRequestImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 3);

        IMSI imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("1111122222"));

        assertNull(asc.getRoamingNumber());
        ISDNAddressString mscNumber = asc.getMscNumber();
        assertTrue(mscNumber.getAddress().equals("22228"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        ISDNAddressString vlrNumber = asc.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22229"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        VLRCapability vlrCap = asc.getVlrCapability();
        assertTrue(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertFalse(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());

        assertNull(asc.getLmsi());
        assertNull(asc.getExtensionContainer());

        assertFalse(asc.getInformPreviousNetworkEntity());
        assertFalse(asc.getCsLCSNotSupportedByUE());
        assertFalse(asc.getSkipSubscriberDataUpdate());
        assertFalse(asc.getRestorationIndicator());

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new UpdateLocationRequestImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 3);

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("1111122233"));

        assertNull(asc.getRoamingNumber());
        mscNumber = asc.getMscNumber();
        assertTrue(mscNumber.getAddress().equals("22228"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrNumber = asc.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22229"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrCap = asc.getVlrCapability();
        assertTrue(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertFalse(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());

        assertTrue(Arrays.equals(asc.getLmsi().getData(), getLmsiData()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

        assertTrue(asc.getInformPreviousNetworkEntity());
        assertTrue(asc.getCsLCSNotSupportedByUE());
        assertTrue(asc.getSkipSubscriberDataUpdate());
        assertTrue(asc.getRestorationIndicator());

        rawData = getEncodedData3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new UpdateLocationRequestImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 3);

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("1111122233"));

        assertNull(asc.getRoamingNumber());
        mscNumber = asc.getMscNumber();
        assertTrue(mscNumber.getAddress().equals("22228"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrNumber = asc.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22229"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrCap = asc.getVlrCapability();
        assertTrue(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertFalse(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());

        assertTrue(Arrays.equals(asc.getLmsi().getData(), getLmsiData()));
        assertNull(asc.getExtensionContainer());

        assertTrue(asc.getInformPreviousNetworkEntity());
        assertTrue(asc.getCsLCSNotSupportedByUE());
        assertTrue(asc.getSkipSubscriberDataUpdate());
        assertTrue(asc.getRestorationIndicator());

        assertTrue(Arrays.equals(asc.getVGmlcAddress().getData(), getGSNAddressData()));
        assertTrue(asc.getADDInfo().getImeisv().getIMEI().equals("123456789009876"));
        assertFalse(asc.getADDInfo().getSkipSubscriberDataUpdate());
        assertEquals(asc.getPagingArea().getLocationAreas().size(), 1);
        assertEquals(asc.getPagingArea().getLocationAreas().get(0).getLAC().getLac(), 123);

        rawData = getEncodedData_V1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new UpdateLocationRequestImpl(1);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 1);

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("1111122233"));

        assertNull(asc.getMscNumber());
        ISDNAddressString roamingNumber = asc.getRoamingNumber();
        assertTrue(roamingNumber.getAddress().equals("22220"));
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrNumber = asc.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22221"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertNull(asc.getVlrCapability());
        assertNull(asc.getLmsi());
        assertNull(asc.getExtensionContainer());
        assertFalse(asc.getInformPreviousNetworkEntity());
        assertFalse(asc.getCsLCSNotSupportedByUE());
        assertFalse(asc.getSkipSubscriberDataUpdate());
        assertFalse(asc.getRestorationIndicator());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        IMSIImpl imsi = new IMSIImpl("1111122222");
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNAddressStringImpl vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22229");
        SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, false, false, false,
                false);
        VLRCapability vlrCap = new VLRCapabilityImpl(null, null, false, null, null, false, supportedLCSCapabilitySets, null,
                null, false, false);
        UpdateLocationRequestImpl asc = new UpdateLocationRequestImpl(3, imsi, mscNumber, null, vlrNumber, null, null, vlrCap,
                false, false, null, null, null, false, false);
        // long mapProtocolVersion, IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString roamingNumber,
        // ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, VlrCapability vlrCapability,
        // boolean informPreviousNetworkEntity,
        // boolean csLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo, PagingArea pagingArea, boolean
        // skipSubscriberDataUpdate,
        // boolean restorationIndicator

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("1111122233");
        mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22229");
        supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, false, false, false, false);
        vlrCap = new VLRCapabilityImpl(null, null, false, null, null, false, supportedLCSCapabilitySets, null, null, false,
                false);
        LMSIImpl lmsi = new LMSIImpl(getLmsiData());
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        asc = new UpdateLocationRequestImpl(3, imsi, mscNumber, null, vlrNumber, lmsi, extensionContainer, vlrCap, true, true,
                null, null, null, true, true);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        GSNAddressImpl vGmlcAddress = new GSNAddressImpl(getGSNAddressData());
        IMEIImpl imeisv = new IMEIImpl("123456789009876");
        ADDInfoImpl addInfo = new ADDInfoImpl(imeisv, false);
        ArrayList<LocationArea> locationAreas = new ArrayList<LocationArea>();
        LACImpl lac = new LACImpl(123);
        LocationAreaImpl la = new LocationAreaImpl(lac);
        locationAreas.add(la);
        PagingAreaImpl pagingArea = new PagingAreaImpl(locationAreas);
        asc = new UpdateLocationRequestImpl(3, imsi, mscNumber, null, vlrNumber, lmsi, null, vlrCap, true, true, vGmlcAddress,
                addInfo, pagingArea, true, true);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("1111122233");
        ISDNAddressStringImpl roamingNumberNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22220");
        vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22221");
        asc = new UpdateLocationRequestImpl(1, imsi, null, roamingNumberNumber, vlrNumber, null, null, null, false, false,
                null, null, null, false, false);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_V1();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
