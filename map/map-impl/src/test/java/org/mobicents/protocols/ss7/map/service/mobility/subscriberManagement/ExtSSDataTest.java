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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ExtSSDataTest {

    public byte[] getData() {
        return new byte[] { 48, 55, 4, 1, 0, -124, 1, 5, -126, 1, 0, 48, 3, -126, 1, 22, -91, 39, -96, 32, 48, 10, 6, 3, 42, 3,
                4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
                33 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 55, 4, 1, 0, -124, 1, 5, -127, 1, 1, 48, 3, -126, 1, 22, -91, 39, -96, 32, 48, 10, 6, 3, 42, 3,
                4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
                33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        {
            byte[] data = this.getData();
            AsnInputStream asn = new AsnInputStream(data);
            int tag = asn.readTag();
            ExtSSDataImpl prim = new ExtSSDataImpl();
            prim.decodeAll(asn);

            assertEquals(tag, Tag.SEQUENCE);
            assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

            MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
            assertEquals(prim.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);
            assertNotNull(prim.getSsStatus());
            assertTrue(prim.getSsStatus().getBitA());
            assertTrue(prim.getSsStatus().getBitP());
            assertTrue(!prim.getSsStatus().getBitQ());
            assertTrue(!prim.getSsStatus().getBitR());
            assertNotNull(prim.getSSSubscriptionOption());
            assertNotNull(prim.getSSSubscriptionOption().getCliRestrictionOption());
            assertEquals(prim.getSSSubscriptionOption().getCliRestrictionOption(), CliRestrictionOption.permanent);
            assertNull(prim.getSSSubscriptionOption().getOverrideCategory());

            assertEquals(prim.getBasicServiceGroupList().size(), 1);
            ExtBasicServiceCode ebsc = prim.getBasicServiceGroupList().get(0);
            assertEquals(ebsc.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);

            assertNotNull(extensionContainer);
            assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        }

        {
            byte[] data = this.getData1();
            AsnInputStream asn = new AsnInputStream(data);
            int tag = asn.readTag();
            ExtSSDataImpl prim = new ExtSSDataImpl();
            prim.decodeAll(asn);

            assertEquals(tag, Tag.SEQUENCE);
            assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

            MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
            assertEquals(prim.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);
            assertNotNull(prim.getSsStatus());
            assertTrue(prim.getSsStatus().getBitA());
            assertTrue(prim.getSsStatus().getBitP());
            assertTrue(!prim.getSsStatus().getBitQ());
            assertTrue(!prim.getSsStatus().getBitR());
            assertNotNull(prim.getSSSubscriptionOption());
            assertNotNull(prim.getSSSubscriptionOption().getOverrideCategory());
            assertEquals(prim.getSSSubscriptionOption().getOverrideCategory(), OverrideCategory.overrideDisabled);
            assertNull(prim.getSSSubscriptionOption().getCliRestrictionOption());

            assertEquals(prim.getBasicServiceGroupList().size(), 1);
            ExtBasicServiceCode ebsc = prim.getBasicServiceGroupList().get(0);
            assertEquals(ebsc.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);

            assertNotNull(extensionContainer);
            assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        }

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allServices);
        ExtSSStatus ssStatus = new ExtSSStatusImpl(false, true, false, true);

        SSSubscriptionOption ssSubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.permanent);

        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        ArrayList<ExtBasicServiceCode> basicServiceGroupList = new ArrayList<ExtBasicServiceCode>();
        basicServiceGroupList.add(basicService);

        ExtSSDataImpl prim = new ExtSSDataImpl(ssCode, ssStatus, ssSubscriptionOption, basicServiceGroupList,
                extensionContainer);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        ssSubscriptionOption = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
        prim = new ExtSSDataImpl(ssCode, ssStatus, ssSubscriptionOption, basicServiceGroupList, extensionContainer);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));
    }
}
