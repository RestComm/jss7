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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class DCSITest {

    public byte[] getData() {
        return new byte[] { 48, 111, -96, 61, 48, 59, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48,
                39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 127, -96, 122, 48, 59, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48,
                39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, 48, 59, 4, 4, -111, 34, 50, -12, 2, 1, 8, 4, 4, -111, 34, 50, -11, 2, 1, 1,
                48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
                22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 2 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        {
            byte[] data = this.getData();
            AsnInputStream asn = new AsnInputStream(data);
            int tag = asn.readTag();
            DCSIImpl prim = new DCSIImpl();
            prim.decodeAll(asn);

            assertEquals(tag, Tag.SEQUENCE);
            assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

            MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
            ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = prim.getDPAnalysedInfoCriteriaList();
            assertNotNull(dpAnalysedInfoCriteriaList);
            assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
            DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
            assertNotNull(dpAnalysedInfoCriterium);
            ISDNAddressString dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
            assertTrue(dialledNumber.getAddress().equals("22234"));
            assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
            assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
            assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
            ISDNAddressString gsmSCFAddress = dpAnalysedInfoCriterium.getGsmSCFAddress();
            assertTrue(gsmSCFAddress.getAddress().equals("22235"));
            assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
            assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
            assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
            assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
            assertEquals(prim.getCamelCapabilityHandling().intValue(), 2);
            assertTrue(prim.getCsiActive());
            assertTrue(prim.getNotificationToCSE());
        }

        {
            byte[] data = this.getData2();
            AsnInputStream asn = new AsnInputStream(data);
            int tag = asn.readTag();
            DCSIImpl prim = new DCSIImpl();
            prim.decodeAll(asn);

            assertEquals(tag, Tag.SEQUENCE);
            assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

            MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
            ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = prim.getDPAnalysedInfoCriteriaList();
            assertNotNull(dpAnalysedInfoCriteriaList);
            assertEquals(dpAnalysedInfoCriteriaList.size(), 2);
            DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
            assertNotNull(dpAnalysedInfoCriterium);
            ISDNAddressString dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
            assertTrue(dialledNumber.getAddress().equals("22234"));
            assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
            assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
            assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
            ISDNAddressString gsmSCFAddress = dpAnalysedInfoCriterium.getGsmSCFAddress();
            assertTrue(gsmSCFAddress.getAddress().equals("22235"));
            assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
            assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
            assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
            assertNull(extensionContainer);
            assertEquals(prim.getCamelCapabilityHandling().intValue(), 2);
            assertTrue(!prim.getCsiActive());
            assertTrue(!prim.getNotificationToCSE());
        }

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        ISDNAddressStringImpl dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22234");
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");

        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, 7, gsmSCFAddress,
                DefaultCallHandling.continueCall, extensionContainer);

        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<DPAnalysedInfoCriterium>();
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);

        DCSIImpl prim = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, extensionContainer, true, true);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium2 = new DPAnalysedInfoCriteriumImpl(dialledNumber, 8, gsmSCFAddress,
                DefaultCallHandling.releaseCall, extensionContainer);
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium2);
        prim = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, null, false, false);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
    }
}
