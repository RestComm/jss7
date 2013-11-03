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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DeferredmtlrDataTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

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

    public byte[] getEncodedData() {
        return new byte[] { 48, 18, 3, 2, 4, 80, -128, 1, 4, -95, 9, 4, 7, -111, 51, 0, 68, 0, 85, 0 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        DeferredmtlrDataImpl imp = new DeferredmtlrDataImpl();
        imp.decodeAll(asn);

        assertFalse(imp.getDeferredLocationEventType().getMsAvailable());
        assertTrue(imp.getDeferredLocationEventType().getEnteringIntoArea());
        assertFalse(imp.getDeferredLocationEventType().getLeavingFromArea());
        assertTrue(imp.getDeferredLocationEventType().getBeingInsideArea());

        assertEquals(imp.getTerminationCause(), TerminationCause.mtlrRestart);

        assertTrue(imp.getLCSLocationInfo().getNetworkNodeNumber().getAddress().equals("330044005500"));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        byte[] data = getEncodedData();

        DeferredLocationEventTypeImpl deferredLocationEventType = new DeferredLocationEventTypeImpl(false, true, false, true);
        // boolean msAvailable, boolean enteringIntoArea, boolean leavingFromArea, boolean beingInsideArea
        ISDNAddressStringImpl networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "330044005500");
        LCSLocationInfoImpl lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, false, null, null, null,
                null, null);

        DeferredmtlrDataImpl imp = new DeferredmtlrDataImpl(deferredLocationEventType, TerminationCause.mtlrRestart,
                lcsLocationInfo);
        // DeferredLocationEventType deferredLocationEventType, TerminationCause terminationCause, LCSLocationInfo
        // lcsLocationInfo

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

    }
}
