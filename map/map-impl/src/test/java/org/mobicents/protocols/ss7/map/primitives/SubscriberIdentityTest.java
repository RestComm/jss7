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

package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Trace are from Brazil Operator
 *
 * @author amit bhayani
 *
 */
public class SubscriberIdentityTest {
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

    byte[] data = new byte[] { (byte) 0x80, 0x08, 0x27, (byte) 0x94, (byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7 };
    byte[] dataMsIsdn = new byte[] { -127, 7, -111, 34, 34, 34, 51, 51, -13 };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        SubscriberIdentityImpl subsIdent = new SubscriberIdentityImpl();
        subsIdent.decodeAll(asn);
        IMSI imsi = subsIdent.getIMSI();
        ISDNAddressString msisdn = subsIdent.getMSISDN();
        assertNotNull(imsi);
        assertNull(msisdn);
        assertTrue(imsi.getData().equals("724999900000007"));

        asn = new AsnInputStream(dataMsIsdn);
        tag = asn.readTag();
        subsIdent = new SubscriberIdentityImpl();
        subsIdent.decodeAll(asn);
        imsi = subsIdent.getIMSI();
        msisdn = subsIdent.getMSISDN();
        assertNull(imsi);
        assertNotNull(msisdn);
        assertTrue(msisdn.getAddress().equals("22222233333"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        IMSI imsi = this.MAPParameterFactory.createIMSI("724999900000007");
        SubscriberIdentityImpl subsIdent = new SubscriberIdentityImpl(imsi);
        AsnOutputStream asnOS = new AsnOutputStream();
        subsIdent.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22222233333");
        subsIdent = new SubscriberIdentityImpl(msisdn);
        asnOS = new AsnOutputStream();
        subsIdent.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(dataMsIsdn, encodedData));
    }

    @Test(groups = { "functional.serialize", "primitives" })
    public void testSerialization() throws Exception {
        IMSI imsi = this.MAPParameterFactory.createIMSI("724999900000007");
        SubscriberIdentityImpl original = new SubscriberIdentityImpl(imsi);

        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        // deserialize
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        SubscriberIdentityImpl copy = (SubscriberIdentityImpl) o;

        // test result
        assertEquals(copy, original);
    }
}
