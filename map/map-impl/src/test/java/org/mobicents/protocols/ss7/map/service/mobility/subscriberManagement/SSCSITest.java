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
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCamelData;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SSCSITest {

    public byte[] getData() {
        return new byte[] { 48, 97, 48, 52, 48, 3, 4, 1, 96, 4, 4, -111, 34, 50, -11, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
                22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 0 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        SSCSIImpl prim = new SSCSIImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        SSCamelData ssCamelData = prim.getSsCamelData();

        ArrayList<SSCode> ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 1);
        SSCode one = ssEventList.get(0);
        assertNotNull(one);
        assertEquals(one.getSupplementaryCodeValue(), SupplementaryCodeValue.allFacsimileTransmissionServices);

        ISDNAddressString gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(ssCamelData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ssCamelData.getExtensionContainer()));
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getCsiActive());
        assertTrue(!prim.getNotificationToCSE());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        ArrayList<SSCode> ssEventList = new ArrayList<SSCode>();
        ssEventList.add(new SSCodeImpl(SupplementaryCodeValue.allFacsimileTransmissionServices.getCode()));
        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SSCamelData ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddress, extensionContainer);
        boolean notificationToCSE = false;
        boolean csiActive = true;

        SSCSIImpl prim = new SSCSIImpl(ssCamelData, extensionContainer, notificationToCSE, csiActive);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
