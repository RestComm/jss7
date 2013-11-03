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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtBasicServiceCodeTest {

    private byte[] getEncodedData1() {
        return new byte[] { (byte) 130, 1, 22 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { (byte) 131, 1, 17 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { (byte) 131, 1, 16 };
    }

    private byte[] getData1() {
        return new byte[] { 22 };
    }

    private byte[] getData2() {
        return new byte[] { 17 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData1();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        ExtBasicServiceCodeImpl impl = new ExtBasicServiceCodeImpl();
        impl.decodeAll(asn);
        assertTrue(Arrays.equals(impl.getExtBearerService().getData(), this.getData1()));
        assertNull(impl.getExtTeleservice());

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtBasicServiceCodeImpl();
        impl.decodeAll(asn);
        assertTrue(Arrays.equals(impl.getExtTeleservice().getData(), this.getData2()));
        assertNull(impl.getExtBearerService());

        rawData = getEncodedData3();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtBasicServiceCodeImpl();
        impl.decodeAll(asn);
        assertEquals(impl.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);
        assertNull(impl.getExtBearerService());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        {
            ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(this.getData1());
            ExtBasicServiceCodeImpl impl = new ExtBasicServiceCodeImpl(b);
            AsnOutputStream asnOS = new AsnOutputStream();
            impl.encodeAll(asnOS);
            byte[] encodedData = asnOS.toByteArray();
            byte[] rawData = getEncodedData1();
            assertTrue(Arrays.equals(rawData, encodedData));
        }

        {
            ExtTeleserviceCodeImpl b = new ExtTeleserviceCodeImpl(this.getData2());
            ExtBasicServiceCodeImpl impl = new ExtBasicServiceCodeImpl(b);
            AsnOutputStream asnOS = new AsnOutputStream();
            impl.encodeAll(asnOS);
            byte[] encodedData = asnOS.toByteArray();
            byte[] rawData = getEncodedData2();
            assertTrue(Arrays.equals(rawData, encodedData));
        }

        {
            ExtTeleserviceCodeImpl b = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
            ExtBasicServiceCodeImpl impl = new ExtBasicServiceCodeImpl(b);
            AsnOutputStream asnOS = new AsnOutputStream();
            impl.encodeAll(asnOS);
            byte[] encodedData = asnOS.toByteArray();
            byte[] rawData = getEncodedData3();
            assertTrue(Arrays.equals(rawData, encodedData));
        }
    }

    @Test(groups = { "functional.xml.serialize", "primitives.extBasic" })
    public void testXMLSerializaionExtBasic() throws Exception {
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(this.getData1());
        ExtBasicServiceCodeImpl original = new ExtBasicServiceCodeImpl(b);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "extBasicServiceCode", ExtBasicServiceCodeImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ExtBasicServiceCodeImpl copy = reader.read("extBasicServiceCode", ExtBasicServiceCodeImpl.class);

        assertEquals(copy.getExtBearerService().getBearerServiceCodeValue(), original.getExtBearerService()
                .getBearerServiceCodeValue());
        assertNull(copy.getExtTeleservice());
    }

    @Test(groups = { "functional.xml.serialize", "primitives.extTele" })
    public void testXMLSerializaionExtTele() throws Exception {
        ExtTeleserviceCodeImpl b = new ExtTeleserviceCodeImpl(this.getData2());
        ExtBasicServiceCodeImpl original = new ExtBasicServiceCodeImpl(b);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "extBasicServiceCode", ExtBasicServiceCodeImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ExtBasicServiceCodeImpl copy = reader.read("extBasicServiceCode", ExtBasicServiceCodeImpl.class);

        assertEquals(copy.getExtTeleservice().getTeleserviceCodeValue(), original.getExtTeleservice().getTeleserviceCodeValue());
        assertNull(copy.getExtBearerService());
    }
}
