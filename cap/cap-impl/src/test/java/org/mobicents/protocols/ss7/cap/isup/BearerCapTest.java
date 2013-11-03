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

package org.mobicents.protocols.ss7.cap.isup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class BearerCapTest {

    public byte[] getData() {
        return new byte[] { (byte) 128, 3, (byte) 128, (byte) 144, (byte) 163 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 128, (byte) 144, (byte) 163 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {

        byte[] data = this.getData();
        AsnInputStream ais = new AsnInputStream(data);
        BearerCapImpl elem = new BearerCapImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        UserServiceInformation usi = elem.getUserServiceInformation();

        // TODO: implement UserServiceInformation (ISUP) and then implement CAP unit tests for UserServiceInformation usi

        // assertEquals(ci.getCodingStandard(), 0);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {

        BearerCapImpl elem = new BearerCapImpl(this.getIntData());
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

        // TODO: implement UserServiceInformation (ISUP) and then implement CAP unit tests for UserServiceInformation usi

        // UserServiceInformation usi = new UserServiceInformationImpl(cdata);
        // elem = new BearerCapImpl(usi);
        // aos = new AsnOutputStream();
        // elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        // assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
    }

    @Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        UserServiceInformationImpl original0 = new UserServiceInformationImpl();
        original0.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        original0.setInformationTransferCapability(UserServiceInformation._ITS_VIDEO);
        original0.setTransferMode(UserServiceInformation._TM_PACKET);
        original0.setInformationTransferRate(UserServiceInformation._ITR_64x2);

        BearerCapImpl original = new BearerCapImpl(original0);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "bearerCap", BearerCapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        BearerCapImpl copy = reader.read("bearerCap", BearerCapImpl.class);

        assertEquals(copy.getUserServiceInformation().getCodingStandart(), original.getUserServiceInformation()
                .getCodingStandart());
        assertEquals(copy.getUserServiceInformation().getInformationTransferCapability(), original.getUserServiceInformation()
                .getInformationTransferCapability());
        assertEquals(copy.getUserServiceInformation().getTransferMode(), original.getUserServiceInformation().getTransferMode());
        assertEquals(copy.getUserServiceInformation().getInformationTransferRate(), original.getUserServiceInformation()
                .getInformationTransferRate());

    }
}
