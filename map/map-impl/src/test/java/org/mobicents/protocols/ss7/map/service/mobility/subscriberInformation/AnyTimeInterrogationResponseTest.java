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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 * @author abhayani
 * @author sergey vetyutnev
 *
 */
public class AnyTimeInterrogationResponseTest {

    // Real Trace
    byte[] data = new byte[] { 0x30, 0x34, (byte) 0x30, 0x32, (byte) 0xa0, 0x2c, 0x02, 0x01, 0x01, (byte) 0x80, 0x08, 0x10,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x43, 0x69, 0x26, (byte) 0x99,
            0x01, (byte) 0xa3, 0x09, (byte) 0x80, 0x07, 0x27, (byte) 0xf4, 0x43, 0x79, (byte) 0x9e, 0x29, (byte) 0xa0,
            (byte) 0x86, 0x07, (byte) 0x91, 0x55, 0x43, 0x69, 0x26, (byte) 0x99, 0x01, (byte) 0x89, 0x00, (byte) 0xa1, 0x02,
            (byte) 0x80, 0x00 };

    byte[] dataFull = new byte[] { 48, 47, 48, 4, -95, 2, -128, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
            48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };

    byte[] dataGeoInfo = new byte[] { 16, 0, 0, 0, 0, 0, 0, 0 };

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        AnyTimeInterrogationResponseImpl atiResponse = new AnyTimeInterrogationResponseImpl();
        atiResponse.decodeAll(asn);

        SubscriberInfo subscriberInfo = atiResponse.getSubscriberInfo();

        LocationInformation locInfo = subscriberInfo.getLocationInformation();
        assertNotNull(locInfo);
        assertEquals((int) locInfo.getAgeOfLocationInformation(), 1);
        assertTrue(Arrays.equals(locInfo.getGeographicalInformation().getData(), dataGeoInfo));
        assertTrue(locInfo.getVlrNumber().getAddress().equals("553496629910"));
        assertEquals(locInfo.getVlrNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(locInfo.getVlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 724);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 34);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 31134);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength()
                .getCellIdOrServiceAreaCode(), 10656);
        assertTrue(locInfo.getMscNumber().getAddress().equals("553496629910"));
        assertEquals(locInfo.getMscNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(locInfo.getMscNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(locInfo.getSaiPresent());

        SubscriberState subState = subscriberInfo.getSubscriberState();
        assertEquals(subState.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);
        assertNull(atiResponse.getExtensionContainer());

        asn = new AsnInputStream(dataFull);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        atiResponse = new AnyTimeInterrogationResponseImpl();
        atiResponse.decodeAll(asn);

        subscriberInfo = atiResponse.getSubscriberInfo();
        locInfo = subscriberInfo.getLocationInformation();
        assertNull(locInfo);
        subState = subscriberInfo.getSubscriberState();
        assertEquals(subState.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(atiResponse.getExtensionContainer()));

    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testEncode() throws Exception {

        ISDNAddressStringImpl vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629910");
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629910");
        GeographicalInformationImpl gi = new GeographicalInformationImpl(dataGeoInfo);
        CellGlobalIdOrServiceAreaIdFixedLengthImpl c2 = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(724, 34, 31134, 10656);
        CellGlobalIdOrServiceAreaIdOrLAIImpl c1 = new CellGlobalIdOrServiceAreaIdOrLAIImpl(c2);
        LocationInformationImpl li = new LocationInformationImpl(1, gi, vlrNumber, null, c1, null, null, mscNumber, null,
                false, true, null, null);
        // Integer ageOfLocationInformation, GeographicalInformation geographicalInformation, ISDNAddressString vlrNumber,
        // LocationNumberMap locationNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
        // MAPExtensionContainer extensionContainer,
        // LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation, boolean
        // currentLocationRetrieved,
        // boolean saiPresent, LocationInformationEPS locationInformationEPS, UserCSGInformation userCSGInformation
        SubscriberStateImpl ss = new SubscriberStateImpl(SubscriberStateChoice.assumedIdle, null);
        SubscriberInfoImpl si = new SubscriberInfoImpl(li, ss, null, null, null, null, null, null, null);
        // LocationInformation locationInformation, SubscriberState subscriberState, MAPExtensionContainer extensionContainer,
        // LocationInformationGPRS locationInformationGPRS, PSSubscriberState psSubscriberState, IMEI imei, MSClassmark2
        // msClassmark2,
        // GPRSMSClass gprsMSClass, MNPInfoRes mnpInfoRes

        AnyTimeInterrogationResponseImpl anyTimeInt = new AnyTimeInterrogationResponseImpl(si, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        si = new SubscriberInfoImpl(null, ss, null, null, null, null, null, null, null);
        anyTimeInt = new AnyTimeInterrogationResponseImpl(si, MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(dataFull, encodedData));
    }
}
