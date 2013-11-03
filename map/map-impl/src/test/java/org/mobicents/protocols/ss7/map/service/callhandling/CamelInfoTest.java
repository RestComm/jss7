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

package org.mobicents.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
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
public class CamelInfoTest {
    Logger logger = Logger.getLogger(ExtendedRoutingInfoTest.class);

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

    public static byte[] getData() {
        return new byte[] { (byte) 171, 4, 3, 2, 4, (byte) 224 };
    }

    public static byte[] getDataFull() {
        return new byte[] { -85, 51, 3, 2, 4, -64, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6,
                3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 2, 1, -86 };
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {

        AsnInputStream asn = new AsnInputStream(getData());
        int tag = asn.readTag();
        assertEquals(tag, 11);

        CamelInfoImpl impl = new CamelInfoImpl();
        impl.decodeAll(asn);

        SupportedCamelPhases scf = impl.getSupportedCamelPhases();
        assertTrue(scf.getPhase1Supported());
        assertTrue(scf.getPhase2Supported());
        assertTrue(scf.getPhase3Supported());
        assertFalse(scf.getPhase4Supported());

        assertFalse(impl.getSuppressTCSI());
        assertNull(impl.getExtensionContainer());
        assertNull(impl.getOfferedCamel4CSIs());

        asn = new AsnInputStream(getDataFull());
        tag = asn.readTag();
        assertEquals(tag, 11);

        impl = new CamelInfoImpl();
        impl.decodeAll(asn);

        scf = impl.getSupportedCamelPhases();
        assertTrue(scf.getPhase1Supported());
        assertTrue(scf.getPhase2Supported());
        assertFalse(scf.getPhase3Supported());
        assertFalse(scf.getPhase4Supported());

        assertTrue(impl.getSuppressTCSI());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));
        OfferedCamel4CSIs ofc = impl.getOfferedCamel4CSIs();
        assertTrue(ofc.getOCsi());
        assertFalse(ofc.getDCsi());
        assertTrue(ofc.getVtCsi());
        assertFalse(ofc.getTCsi());
        assertTrue(ofc.getMtSmsCsi());
        assertFalse(ofc.getMgCsi());
        assertTrue(ofc.getPsiEnhancements());
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {

        SupportedCamelPhases scf = new SupportedCamelPhasesImpl(true, true, true, false);
        CamelInfoImpl impl = new CamelInfoImpl(scf, false, null, null);
        // SupportedCamelPhases supportedCamelPhases, boolean suppressTCSI, MAPExtensionContainer extensionContainer,
        // OfferedCamel4CSIs offeredCamel4CSIs

        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 11);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(getData(), encodedData));

        scf = new SupportedCamelPhasesImpl(true, true, false, false);
        OfferedCamel4CSIs ofc = new OfferedCamel4CSIsImpl(true, false, true, false, true, false, true);
        // boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi, boolean mgCsi, boolean psiEnhancements
        impl = new CamelInfoImpl(scf, true, MAPExtensionContainerTest.GetTestExtensionContainer(), ofc);

        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 11);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(getDataFull(), encodedData));
    }

    @Test(groups = { "functional.serialize", "service.callhandling" })
    public void testSerialization() throws Exception {

    }
}
