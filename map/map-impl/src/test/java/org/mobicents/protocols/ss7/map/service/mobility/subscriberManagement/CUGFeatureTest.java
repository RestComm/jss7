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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictions;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictionsValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CUGFeatureTest {

    public byte[] getData() {
        return new byte[] { 48, 50, -126, 1, 38, 2, 1, 1, 4, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 50, -125, 1, 17, 2, 1, 1, 4, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        {
            byte[] data = this.getData();
            AsnInputStream asn = new AsnInputStream(data);
            int tag = asn.readTag();
            CUGFeatureImpl prim = new CUGFeatureImpl();
            prim.decodeAll(asn);

            assertEquals(tag, Tag.SEQUENCE);
            assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
            MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
            // assertTrue(Arrays.equals(prim.getBasicService().getExtBearerService().getData(),
            // this.getData2()));
            assertEquals(prim.getBasicService().getExtBearerService().getBearerServiceCodeValue(),
                    BearerServiceCodeValue.padAccessCA_9600bps);
            assertNull(prim.getBasicService().getExtTeleservice());
            // assertTrue(prim.getPreferentialCugIndicator().equals(new Integer(1)));
            assertEquals((int) prim.getPreferentialCugIndicator(), 1);
            // assertTrue(prim.getInterCugRestrictions().getInterCUGRestrictionsValue().equals(
            // InterCUGRestrictionsValue.CUGOnlyFacilities));
            assertEquals(prim.getInterCugRestrictions().getInterCUGRestrictionsValue(),
                    InterCUGRestrictionsValue.CUGOnlyFacilities);
            assertNotNull(extensionContainer);
            assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        }

        {
            byte[] data = this.getData1();
            AsnInputStream asn = new AsnInputStream(data);
            int tag = asn.readTag();
            CUGFeatureImpl prim = new CUGFeatureImpl();
            prim.decodeAll(asn);

            assertEquals(tag, Tag.SEQUENCE);
            assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
            MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
            // assertTrue(Arrays.equals(prim.getBasicService().getExtTeleservice().getData(),
            // this.getData3()));
            assertEquals(prim.getBasicService().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.telephony);
            assertNull(prim.getBasicService().getExtBearerService());
            // assertTrue(prim.getPreferentialCugIndicator().equals(new Integer(1)));
            assertEquals((int) prim.getPreferentialCugIndicator(), 1);
            assertTrue(prim.getInterCugRestrictions().getInterCUGRestrictionsValue()
                    .equals(InterCUGRestrictionsValue.CUGOnlyFacilities));
            assertNotNull(extensionContainer);
        }

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        {
            // ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(this.getData2());
            ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
            ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
            // Integer preferentialCugIndicator = new Integer(1);
            Integer preferentialCugIndicator = 1;
            // InterCUGRestrictions interCugRestrictions = new InterCUGRestrictionsImpl(0);
            InterCUGRestrictions interCugRestrictions = new InterCUGRestrictionsImpl(
                    InterCUGRestrictionsValue.CUGOnlyFacilities);
            // MAPExtensionContainer extensionContainer = getMapExtensionContainer();
            MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
            CUGFeatureImpl prim = new CUGFeatureImpl(basicService, preferentialCugIndicator, interCugRestrictions,
                    extensionContainer);

            AsnOutputStream asn = new AsnOutputStream();
            prim.encodeAll(asn);

            assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
        }
        {
            // ExtTeleserviceCodeImpl b = new ExtTeleserviceCodeImpl(this.getData3());
            ExtTeleserviceCodeImpl b = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.telephony);
            ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);

            Integer preferentialCugIndicator = new Integer(1);
            InterCUGRestrictions interCugRestrictions = new InterCUGRestrictionsImpl(0);
            // MAPExtensionContainer extensionContainer = getMapExtensionContainer();
            MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
            CUGFeatureImpl prim = new CUGFeatureImpl(basicService, preferentialCugIndicator, interCugRestrictions,
                    extensionContainer);

            AsnOutputStream asn = new AsnOutputStream();
            prim.encodeAll(asn);

            assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));
        }

    }

}
