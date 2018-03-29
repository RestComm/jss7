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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoice;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberStateImpl;
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

    byte[] dataMSNetworkCapability = new byte[] { 12 };
    byte[] dataMsClassMark2 = new byte[] { 11, 12, 13 };

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

    @Test(groups = { "functional.xml.serialize", "subscriberInformation" })
    public void testXMLSerialize() throws Exception {

        ISDNAddressStringImpl vlrN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "554433221100");
        LocationInformationImpl li = new LocationInformationImpl(null, null, vlrN, null, null, null, null, null, null, false, false, null, null);
        SubscriberStateImpl ss = new SubscriberStateImpl(SubscriberStateChoice.camelBusy, null);
        LAIFixedLengthImpl laiFixedLength = new LAIFixedLengthImpl(260, 11, 144);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cg = new CellGlobalIdOrServiceAreaIdOrLAIImpl(laiFixedLength);
        LocationInformationGPRSImpl liGprs = new LocationInformationGPRSImpl(cg, null, null, null, null, null, false, null, false, null);
        GPRSMSClassImpl gprsMSClass = new GPRSMSClassImpl(new MSNetworkCapabilityImpl(dataMSNetworkCapability), null);
        MNPInfoResImpl mnpInfoRes = new MNPInfoResImpl(null, new IMSIImpl("456787654"), null, null, null);
        SubscriberInfoImpl si = new SubscriberInfoImpl(li, ss, null, liGprs, new PSSubscriberStateImpl(PSSubscriberStateChoice.notProvidedFromSGSNorMME,
                null, null), new IMEIImpl("1122334455667788"), new MSClassmark2Impl(dataMsClassMark2), gprsMSClass, mnpInfoRes);
        AnyTimeInterrogationResponseImpl original = new AnyTimeInterrogationResponseImpl(si, MAPExtensionContainerTest.GetTestExtensionContainer());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "anyTimeInterrogationResponse", AnyTimeInterrogationResponseImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        AnyTimeInterrogationResponseImpl copy = reader.read("anyTimeInterrogationResponse", AnyTimeInterrogationResponseImpl.class);

        assertEquals(copy.getExtensionContainer(), original.getExtensionContainer());

        assertEquals(copy.getSubscriberInfo().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI(), original.getSubscriberInfo().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI());
        assertEquals(copy.getSubscriberInfo().getLocationInformationGPRS().isCurrentLocationRetrieved(), original.getSubscriberInfo().getLocationInformationGPRS().isCurrentLocationRetrieved());
        assertEquals(copy.getSubscriberInfo().getLocationInformationGPRS().isSaiPresent(), original.getSubscriberInfo().getLocationInformationGPRS().isSaiPresent());
        assertEquals(copy.getSubscriberInfo().getPSSubscriberState().getChoice(), original.getSubscriberInfo().getPSSubscriberState().getChoice());
        assertEquals(copy.getSubscriberInfo().getGPRSMSClass().getMSNetworkCapability(), original.getSubscriberInfo().getGPRSMSClass().getMSNetworkCapability());
        assertEquals(copy.getSubscriberInfo().getMNPInfoRes().getIMSI(), original.getSubscriberInfo().getMNPInfoRes().getIMSI());

    }
}
