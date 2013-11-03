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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTDPDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/*
 *
 * @author cristian veliscu
 *
 */
public class ExtendedRoutingInfoTest {
    Logger logger = Logger.getLogger(ExtendedRoutingInfoTest.class);

    private byte[] getData1() {
        return new byte[] { 4, 7, -111, -105, 114, 99, 80, 24, -7 };
    }

    private byte[] getData2() {
        return new byte[] { -88, 64, 48, 7, -123, 5, -111, 17, 17, 33, 34, -96, 12, -96, 10, 48, 8, 48, 6, 10, 1, 12, 2, 1, 5,
                -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

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

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {
        // 4 = 00|0|00100, 7 = length
        // Option 1
        AsnInputStream asn = new AsnInputStream(this.getData1());
        int tag = asn.readTag();

        ExtendedRoutingInfoImpl extRouteInfo = new ExtendedRoutingInfoImpl();
        extRouteInfo.decodeAll(asn);

        RoutingInfo routeInfo = extRouteInfo.getRoutingInfo();
        ISDNAddressString isdnAdd = routeInfo.getRoamingNumber();

        assertNotNull(isdnAdd);
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(isdnAdd.getAddress(), "79273605819");

        // Option 2
        asn = new AsnInputStream(this.getData2());
        tag = asn.readTag();

        extRouteInfo = new ExtendedRoutingInfoImpl();
        extRouteInfo.decodeAll(asn);

        ForwardingData fd = extRouteInfo.getCamelRoutingInfo().getForwardingData();
        assertTrue(fd.getForwardedToNumber().getAddress().equals("11111222"));
        assertNull(fd.getForwardedToSubaddress());
        GmscCamelSubscriptionInfo gcs = extRouteInfo.getCamelRoutingInfo().getGmscCamelSubscriptionInfo();
        assertEquals(gcs.getTCsi().getTBcsmCamelTDPDataList().get(0).getTBcsmTriggerDetectionPoint(),
                TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extRouteInfo.getCamelRoutingInfo()
                .getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
        // 4 = 00|0|00100, 7 = length
        // Option 1
        ISDNAddressString isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        RoutingInfoImpl routeInfo = new RoutingInfoImpl(isdnAdd);
        ExtendedRoutingInfoImpl extRouteInfo = new ExtendedRoutingInfoImpl(routeInfo);

        AsnOutputStream asnOS = new AsnOutputStream();
        extRouteInfo.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(this.getData1(), encodedData));

        // Option 2
        ISDNAddressString forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "11111222");
        ForwardingData forwardingData = new ForwardingDataImpl(forwardedToNumber, null, null, null, null);
        ArrayList<TBcsmCamelTDPData> lst = new ArrayList<TBcsmCamelTDPData>();
        TBcsmCamelTDPData tb = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.termAttemptAuthorized, 5, null, null, null);
        lst.add(tb);
        TCSI tCsi = new TCSIImpl(lst, null, null, false, false);
        GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo = new GmscCamelSubscriptionInfoImpl(tCsi, null, null, null, null,
                null);
        CamelRoutingInfoImpl camelRoutingInfo = new CamelRoutingInfoImpl(forwardingData, gmscCamelSubscriptionInfo,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        extRouteInfo = new ExtendedRoutingInfoImpl(camelRoutingInfo);
        asnOS = new AsnOutputStream();
        extRouteInfo.encodeAll(asnOS);

        assertTrue(Arrays.equals(this.getData2(), asnOS.toByteArray()));
    }

    @Test(groups = { "functional.serialize", "service.callhandling" })
    public void testSerialization() throws Exception {

    }
}
