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

package org.mobicents.protocols.ss7.map.service.callhandling;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/*
 *
 * @author eva ogallar
 *
 */
public class IstCommandResponseTest {
    Logger logger = Logger.getLogger(IstCommandResponseTest.class);

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

    private byte[] getMAPParameterTestData() {
        return new byte[] { 48, 41, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33};
    }


    public byte[] getData1() {
        return new byte[] { 48, 0 };
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        IstCommandResponseImpl prim = new IstCommandResponseImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertNull(prim.getExtensionContainer());


        data = this.getMAPParameterTestData();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new IstCommandResponseImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
        byte[] data = getData1();

        IstCommandResponseImpl sri = new IstCommandResponseImpl(null);

        AsnOutputStream asnOS = new AsnOutputStream();
        sri.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertEquals(data, encodedData);

        // extensionContainer
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        IstCommandResponseImpl prim = new IstCommandResponseImpl(extensionContainer);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        assertEquals(getMAPParameterTestData(), asnOS.toByteArray());
    }

    @Test(groups = { "functional.serialize", "service.callhandling" })
    public void testSerialization() throws Exception {

    }
}
