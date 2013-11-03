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
import static org.testng.Assert.assertNull;
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
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
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
public class LCSLocationInfoTest {
    MAPParameterFactory MAPParameterFactory = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
        MAPParameterFactory = new MAPParameterFactoryImpl();
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        return new byte[] { 48, 96, 4, 5, -111, 85, 22, 9, 112, -128, 4, 11, 12, 13, 14, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3,
                4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
                33, -126, 0, -93, 8, -128, 6, -111, 34, 34, 17, 34, 34, -124, 2, 3, -64, -123, 2, 3, -32, -122, 9, 21, 22, 23,
                23, 25, 26, 27, 28, 29, -120, 9, 31, 32, 33, 33, 35, 36, 37, 38, 39 };
    }

    public byte[] getDataLmsi() {
        return new byte[] { 11, 12, 13, 14 };
    }

    public byte[] getDataMmeName() {
        return new byte[] { 21, 22, 23, 23, 25, 26, 27, 28, 29 };
    }

    public byte[] getDataAaaServerName() {
        return new byte[] { 31, 32, 33, 33, 35, 36, 37, 38, 39 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        LCSLocationInfoImpl imp = new LCSLocationInfoImpl();
        imp.decodeAll(asn);
        ISDNAddressString networkNodeNumber = imp.getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(networkNodeNumber.getAddress().equals("55619007"));

        assertTrue(Arrays.equals(imp.getLMSI().getData(), getDataLmsi()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(imp.getExtensionContainer()));
        assertTrue(imp.getGprsNodeIndicator());

        assertTrue(imp.getAdditionalNumber().getMSCNumber().getAddress().equals("2222112222"));
        assertNull(imp.getAdditionalNumber().getSGSNNumber());

        SupportedLCSCapabilitySets supportedLCSCapabilitySets = imp.getSupportedLCSCapabilitySets();
        assertTrue(supportedLCSCapabilitySets.getCapabilitySetRelease98_99());
        assertTrue(supportedLCSCapabilitySets.getCapabilitySetRelease4());
        assertFalse(supportedLCSCapabilitySets.getCapabilitySetRelease5());
        assertFalse(supportedLCSCapabilitySets.getCapabilitySetRelease6());

        SupportedLCSCapabilitySets additionalLCSCapabilitySets = imp.getAdditionalLCSCapabilitySets();
        assertTrue(additionalLCSCapabilitySets.getCapabilitySetRelease98_99());
        assertTrue(additionalLCSCapabilitySets.getCapabilitySetRelease4());
        assertTrue(additionalLCSCapabilitySets.getCapabilitySetRelease5());
        assertFalse(additionalLCSCapabilitySets.getCapabilitySetRelease6());

        assertTrue(Arrays.equals(imp.getMmeName().getData(), getDataMmeName()));
        assertTrue(Arrays.equals(imp.getAaaServerName().getData(), getDataAaaServerName()));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        byte[] data = getEncodedData();

        ISDNAddressString networkNodeNumber = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "55619007");
        LMSIImpl lmsi = new LMSIImpl(getDataLmsi());
        ISDNAddressString mscNumber = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "2222112222");
        AdditionalNumberImpl additionalNumber = new AdditionalNumberImpl(mscNumber, null);
        SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true, false,
                false, false);
        SupportedLCSCapabilitySetsImpl additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true, true,
                false, false);
        DiameterIdentityImpl mmeName = new DiameterIdentityImpl(getDataMmeName());
        DiameterIdentityImpl aaaServerName = new DiameterIdentityImpl(getDataAaaServerName());

        LCSLocationInfoImpl imp = new LCSLocationInfoImpl(networkNodeNumber, lmsi,
                MAPExtensionContainerTest.GetTestExtensionContainer(), true, additionalNumber, supportedLCSCapabilitySets,
                additionalLCSCapabilitySets, mmeName, aaaServerName);
        // ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, boolean gprsNodeIndicator,
        // AdditionalNumber additionalNumber, SupportedLCSCapabilitySets supportedLCSCapabilitySets, SupportedLCSCapabilitySets
        // additionalLCSCapabilitySets,
        // DiameterIdentity mmeName, DiameterIdentity aaaServerName
        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }
}
