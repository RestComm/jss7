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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTDPDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CamelRoutingInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { -88, 64, 48, 7, -123, 5, -111, 17, 17, 33, 34, -96, 12, -96, 10, 48, 8, 48, 6, 10, 1, 12, 2, 1, 5,
                -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        CamelRoutingInfoImpl ind = new CamelRoutingInfoImpl();
        assertEquals(tag, 8);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        ind.decodeAll(asn);

        ForwardingData fd = ind.getForwardingData();
        assertTrue(fd.getForwardedToNumber().getAddress().equals("11111222"));
        assertNull(fd.getForwardedToSubaddress());

        GmscCamelSubscriptionInfo gcs = ind.getGmscCamelSubscriptionInfo();
        assertEquals(gcs.getTCsi().getTBcsmCamelTDPDataList().get(0).getTBcsmTriggerDetectionPoint(),
                TBcsmTriggerDetectionPoint.termAttemptAuthorized);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {

        ISDNAddressString forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "11111222");
        ForwardingData forwardingData = new ForwardingDataImpl(forwardedToNumber, null, null, null, null);
        ArrayList<TBcsmCamelTDPData> lst = new ArrayList<TBcsmCamelTDPData>();
        TBcsmCamelTDPData tb = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.termAttemptAuthorized, 5, null, null, null);
        lst.add(tb);
        TCSI tCsi = new TCSIImpl(lst, null, null, false, false);
        GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo = new GmscCamelSubscriptionInfoImpl(tCsi, null, null, null, null,
                null);
        CamelRoutingInfoImpl ind = new CamelRoutingInfoImpl(forwardingData, gmscCamelSubscriptionInfo,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        // ForwardingData forwardingData, GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo, MAPExtensionContainer
        // extensionContainer

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 8);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
