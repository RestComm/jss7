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

package org.mobicents.protocols.ss7.inap.isup;

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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.RedirectionInformationImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class RedirectionInformationInapTest {

    public byte[] getData() {
        return new byte[] { (byte) 158, 2, 3, 97 };
    }

    public byte[] getIntData() {
        return new byte[] { 3, 97 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {

        byte[] data = this.getData();
        AsnInputStream ais = new AsnInputStream(data);
        RedirectionInformationInapImpl elem = new RedirectionInformationInapImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        RedirectionInformation ri = elem.getRedirectionInformation();
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        assertEquals(ri.getOriginalRedirectionReason(), 0);
        assertEquals(ri.getRedirectingIndicator(), 3);
        assertEquals(ri.getRedirectionCounter(), 1);
        assertEquals(ri.getRedirectionReason(), 6);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {

        RedirectionInformationInapImpl elem = new RedirectionInformationInapImpl(this.getIntData());
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 30);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

        RedirectionInformation ri = new RedirectionInformationImpl(3, 0, 1, 6);
        elem = new RedirectionInformationInapImpl(ri);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 30);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

        // int redirectingIndicator, int originalRedirectionReason, int redirectionCounter, int redirectionReason
    }

    @Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        RedirectionInformationImpl prim = new RedirectionInformationImpl(RedirectionInformation._RI_CALL_D,
                RedirectionInformation._ORR_NO_REPLY, 4, RedirectionInformation._RI_CALL_REROUTED);
        RedirectionInformationInapImpl original = new RedirectionInformationInapImpl(prim);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "redirectionInformationInap", RedirectionInformationInapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        RedirectionInformationInapImpl copy = reader.read("redirectionInformationInap", RedirectionInformationInapImpl.class);

        assertEquals(copy.getRedirectionInformation().getRedirectingIndicator(), original.getRedirectionInformation()
                .getRedirectingIndicator());
        assertEquals(copy.getRedirectionInformation().getOriginalRedirectionReason(), original.getRedirectionInformation()
                .getOriginalRedirectionReason());
        assertEquals(copy.getRedirectionInformation().getRedirectionCounter(), original.getRedirectionInformation()
                .getRedirectionCounter());
        assertEquals(copy.getRedirectionInformation().getRedirectionReason(), original.getRedirectionInformation()
                .getRedirectionReason());

    }
}
