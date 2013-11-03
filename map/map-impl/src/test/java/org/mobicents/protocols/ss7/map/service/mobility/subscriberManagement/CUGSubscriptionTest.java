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
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.IntraCUGOptions;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CUGSubscriptionTest {

    public byte[] getData() {
        return new byte[] { 48, 58, 2, 1, 1, 4, 4, 1, 2, 3, 4, 10, 1, 0, 48, 3, -126, 1, 22, -96, 39, -96, 32, 48, 10, 6, 3,
                42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33 };
    };

    private byte[] getGugData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        CUGSubscriptionImpl prim = new CUGSubscriptionImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertTrue(prim.getCUGIndex() == 1);
        assertTrue(Arrays.equals(prim.getCugInterlock().getData(), getGugData()));
        assertEquals(prim.getIntraCugOptions(), IntraCUGOptions.noCUGRestrictions);
        assertNotNull(prim.getBasicServiceGroupList());
        assertTrue(prim.getBasicServiceGroupList().size() == 1);
        ExtBasicServiceCode bsc = prim.getBasicServiceGroupList().get(0);
        assertNotNull(bsc);
        assertEquals(bsc.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        int cugIndex = 1;
        CUGInterlock cugInterlock = new CUGInterlockImpl(getGugData());
        IntraCUGOptions intraCugOptions = IntraCUGOptions.noCUGRestrictions;
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        ExtBasicServiceCodeImpl bs = new ExtBasicServiceCodeImpl(b);
        ArrayList<ExtBasicServiceCode> basicService = new ArrayList<ExtBasicServiceCode>();
        basicService.add(bs);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        CUGSubscriptionImpl prim = new CUGSubscriptionImpl(cugIndex, cugInterlock, intraCugOptions, basicService,
                extensionContainer);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
