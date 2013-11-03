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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
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
public class SubscriberLocationReportResponseTest {

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
        return new byte[] { 48, 51, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
                6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 3, -111, 34, 34, -127, 3, -111, 17, 17 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportResponseImpl imp = new SubscriberLocationReportResponseImpl();
        imp.decodeAll(asn);

        assertTrue(imp.getNaESRD().getAddress().equals("1111"));
        assertTrue(imp.getNaESRK().getAddress().equals("2222"));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(imp.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        byte[] data = getEncodedData();

        ISDNAddressStringImpl naEsrd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1111");
        ISDNAddressStringImpl naEsrk = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "2222");

        SubscriberLocationReportResponseImpl imp = new SubscriberLocationReportResponseImpl(naEsrd, naEsrk,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        // ISDNAddressString naEsrd, ISDNAddressString naEsrk, MAPExtensionContainer extensionContainer

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

    }

}
