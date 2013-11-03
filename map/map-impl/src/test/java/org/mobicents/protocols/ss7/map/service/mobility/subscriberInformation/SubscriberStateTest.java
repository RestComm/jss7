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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SubscriberStateTest {

    private byte[] getEncodedData1() {
        return new byte[] { (byte) 128, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { (byte) 129, 0 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 10, 1, 1 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData1();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        SubscriberStateImpl impl = new SubscriberStateImpl();
        impl.decodeAll(asn);
        assertEquals(impl.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);
        assertNull(impl.getNotReachableReason());

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new SubscriberStateImpl();
        impl.decodeAll(asn);
        assertEquals(impl.getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
        assertNull(impl.getNotReachableReason());

        rawData = getEncodedData3();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new SubscriberStateImpl();
        impl.decodeAll(asn);
        assertEquals(impl.getSubscriberStateChoice(), SubscriberStateChoice.netDetNotReachable);
        assertEquals(impl.getNotReachableReason(), NotReachableReason.imsiDetached);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        SubscriberStateImpl impl = new SubscriberStateImpl(SubscriberStateChoice.assumedIdle, null);
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new SubscriberStateImpl(SubscriberStateChoice.camelBusy, null);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new SubscriberStateImpl(SubscriberStateChoice.netDetNotReachable, NotReachableReason.imsiDetached);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        SubscriberStateImpl original = new SubscriberStateImpl(SubscriberStateChoice.assumedIdle, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "subscriberState", SubscriberStateImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        SubscriberStateImpl copy = reader.read("subscriberState", SubscriberStateImpl.class);

        assertEquals(copy.getSubscriberStateChoice(), original.getSubscriberStateChoice());
        assertEquals(copy.getNotReachableReason(), original.getNotReachableReason());

        original = new SubscriberStateImpl(SubscriberStateChoice.netDetNotReachable, NotReachableReason.imsiDetached);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "subscriberState", SubscriberStateImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("subscriberState", SubscriberStateImpl.class);

        assertEquals(copy.getSubscriberStateChoice(), original.getSubscriberStateChoice());
        assertEquals(copy.getNotReachableReason(), original.getNotReachableReason());
    }
}
