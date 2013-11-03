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
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * //TODO add ExtensionContainer and test
 *
 * @author amit bhayani
 *
 */
public class LCSClientExternalIDTest {

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

    public byte[] getData() {
        return new byte[] { 48, 0x07, (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70 };
    }

    public byte[] getDataFull() {
        return new byte[] { 48, 48, -128, 5, -111, 85, 22, 9, 112, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        LCSClientExternalIDImpl lcsClientExterId = new LCSClientExternalIDImpl();
        lcsClientExterId.decodeAll(asn);

        assertNotNull(lcsClientExterId.getExternalAddress());
        assertEquals(lcsClientExterId.getExternalAddress().getAddress(), "55619007");
        assertNull(lcsClientExterId.getExtensionContainer());

        data = getDataFull();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        lcsClientExterId = new LCSClientExternalIDImpl();
        lcsClientExterId.decodeAll(asn);

        assertNotNull(lcsClientExterId.getExternalAddress());
        assertEquals(lcsClientExterId.getExternalAddress().getAddress(), "55619007");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsClientExterId.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        byte[] data = getData();

        ISDNAddressString externalAddress = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "55619007");
        LCSClientExternalIDImpl lcsClientExterId = new LCSClientExternalIDImpl(externalAddress, null);
        AsnOutputStream asnOS = new AsnOutputStream();
        lcsClientExterId.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        data = getDataFull();

        lcsClientExterId = new LCSClientExternalIDImpl(externalAddress, MAPExtensionContainerTest.GetTestExtensionContainer());
        asnOS = new AsnOutputStream();
        lcsClientExterId.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.lsm" })
    public void testSerialization() throws Exception {
        ISDNAddressString externalAddress = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "55619007");
        LCSClientExternalIDImpl original = new LCSClientExternalIDImpl(externalAddress, null);

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
        LCSClientExternalIDImpl copy = (LCSClientExternalIDImpl) o;

        // test result
        assertEquals(copy.getExternalAddress(), original.getExternalAddress());
        assertEquals(copy.getExtensionContainer(), original.getExtensionContainer());

    }
}
