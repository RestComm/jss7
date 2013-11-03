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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ForwardingOptionsImpl;
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
public class RoutingInfoTest {
    Logger logger = Logger.getLogger(RoutingInfoTest.class);

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
        byte[] data = new byte[] { 4, 7, -111, -105, 114, 99, 80, 24, -7 };
        byte[] _data = new byte[] { 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36 };
        // 4 = 00|0|00100, 7 = length

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        RoutingInfoImpl routeInfo = new RoutingInfoImpl();
        routeInfo.decodeAll(asn);

        ISDNAddressString isdnAdd = routeInfo.getRoamingNumber();

        assertNotNull(isdnAdd);
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(isdnAdd.getAddress(), "79273605819");

        // :::::::::::::::::::::::::::::
        AsnInputStream _asn = new AsnInputStream(_data);
        int _tag = _asn.readTag();

        RoutingInfoImpl _routeInfo = new RoutingInfoImpl();
        _routeInfo.decodeAll(_asn);

        ForwardingData _forwardingData = _routeInfo.getForwardingData();
        ForwardingOptions _forwardingOptions = _forwardingData.getForwardingOptions();
        ISDNAddressString _isdnAdd = _forwardingData.getForwardedToNumber();

        assertNotNull(_isdnAdd);
        assertEquals(_isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(_isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(_isdnAdd.getAddress(), "79273605819");
        assertNotNull(_forwardingOptions);
        assertTrue(!_forwardingOptions.isNotificationToForwardingParty());
        assertTrue(!_forwardingOptions.isRedirectingPresentation());
        assertTrue(_forwardingOptions.isNotificationToCallingParty());
        assertTrue(_forwardingOptions.getForwardingReason() == ForwardingReason.busy);
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { 4, 7, -111, -105, 114, 99, 80, 24, -7 };
        byte[] _data = new byte[] { 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36 };
        // 4 = 00|0|00100, 7 = length

        ISDNAddressString isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        RoutingInfoImpl routeInfo = new RoutingInfoImpl(isdnAdd);

        AsnOutputStream asnOS = new AsnOutputStream();
        routeInfo.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        // :::::::::::::::::::::::
        ISDNAddressString _isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        ForwardingData _forwardingData = null;
        ForwardingOptions _forwardingOptions = null;
        _forwardingOptions = new ForwardingOptionsImpl(false, false, true, ForwardingReason.busy);
        _forwardingData = new ForwardingDataImpl(_isdnAdd, null, _forwardingOptions, null, null);

        RoutingInfoImpl _routeInfo = new RoutingInfoImpl(_forwardingData);

        AsnOutputStream _asnOS = new AsnOutputStream();
        _routeInfo.encodeAll(_asnOS);

        byte[] _encodedData = _asnOS.toByteArray();
        assertTrue(Arrays.equals(_data, _encodedData));
    }

    @Test(groups = { "functional.serialize", "service.callhandling" })
    public void testSerialization() throws Exception {

    }
}
