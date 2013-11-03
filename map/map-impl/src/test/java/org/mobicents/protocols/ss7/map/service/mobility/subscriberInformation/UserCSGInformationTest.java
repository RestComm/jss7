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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CSGIdImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class UserCSGInformationTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 54, -128, 5, 5, -128, 0, 0, 32, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -126, 1, 2, -125, 1,
                3 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        UserCSGInformationImpl impl = new UserCSGInformationImpl();
        impl.decodeAll(asn);
        assertEquals(tag, Tag.SEQUENCE);

        BitSetStrictLength bs = impl.getCSGId().getData();
        assertTrue(bs.get(0));
        assertFalse(bs.get(1));
        assertFalse(bs.get(25));
        assertTrue(bs.get(26));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));
        assertEquals((int) impl.getAccessMode(), 2);
        assertEquals((int) impl.getCmi(), 3);

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        BitSetStrictLength bs = new BitSetStrictLength(27);
        bs.set(0);
        bs.set(26);
        CSGIdImpl csgId = new CSGIdImpl(bs);
        UserCSGInformationImpl impl = new UserCSGInformationImpl(csgId, MAPExtensionContainerTest.GetTestExtensionContainer(),
                2, 3);
        // CSGId csgId, MAPExtensionContainer extensionContainer, Integer accessMode, Integer cmi
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "subscriberInformation" })
    public void testXMLSerialize() throws Exception {

        BitSetStrictLength bs = new BitSetStrictLength(27);
        bs.set(0);
        bs.set(26);
        CSGIdImpl csgId = new CSGIdImpl(bs);
        UserCSGInformationImpl original = new UserCSGInformationImpl(csgId,
                MAPExtensionContainerTest.GetTestExtensionContainer(), 2, 3);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "userCSGInformation", UserCSGInformationImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        UserCSGInformationImpl copy = reader.read("userCSGInformation", UserCSGInformationImpl.class);

        assertEquals(copy.getCSGId().getData().get(0), original.getCSGId().getData().get(0));
        assertEquals(copy.getCSGId().getData().get(1), original.getCSGId().getData().get(1));
        assertEquals(copy.getCSGId().getData().get(26), original.getCSGId().getData().get(26));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(copy.getExtensionContainer()));
        assertEquals(copy.getAccessMode(), original.getAccessMode());
        assertEquals(copy.getCmi(), original.getCmi());

    }

}
