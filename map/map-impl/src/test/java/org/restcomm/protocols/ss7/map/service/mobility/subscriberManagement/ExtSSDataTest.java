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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ExtSSDataTest {

    public byte[] getData() {
        return new byte[] { 48, 55, 4, 1, 0, -124, 1, 5, -126, 1, 0, 48, 3, -126, 1, 38, -91, 39, -96, 32, 48, 10, 6, 3, 42, 3,
                4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
                33 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 55, 4, 1, 0, -124, 1, 5, -127, 1, 1, 48, 3, -126, 1, 38, -91, 39, -96, 32, 48, 10, 6, 3, 42, 3,
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
            assertEquals(prim.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
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
            assertEquals(ebsc.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

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
            assertEquals(prim.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
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
            assertEquals(ebsc.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

            assertNotNull(extensionContainer);
            assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        }

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allSS);
        ExtSSStatus ssStatus = new ExtSSStatusImpl(false, true, false, true);

        SSSubscriptionOption ssSubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.permanent);

        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
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
