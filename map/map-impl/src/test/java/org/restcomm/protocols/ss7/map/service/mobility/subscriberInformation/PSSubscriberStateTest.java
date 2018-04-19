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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoice;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PSSubscriberStateImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PSSubscriberStateTest {

    private byte[] getEncodedDataDetached() {
        return new byte[] { -127, 0 };
    }

    private byte[] getEncodedDataActiveReachableForPaging() {
        return new byte[] { -91, 10, 48, 3, -128, 1, 5, 48, 3, -128, 1, 6 };
    }

    private byte[] getEncodedDataNotReachableReason() {
        return new byte[] { 10, 1, 0 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedDataDetached();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        PSSubscriberStateImpl impl = new PSSubscriberStateImpl();
        assertEquals(tag, PSSubscriberStateImpl._ID_ps_Detached);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertTrue(asn.isTagPrimitive());
        impl.decodeAll(asn);
        assertEquals(impl.getChoice(), PSSubscriberStateChoice.psDetached);
        assertNull(impl.getPDPContextInfoList());
        assertNull(impl.getNetDetNotReachable());

        rawData = getEncodedDataActiveReachableForPaging();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new PSSubscriberStateImpl();
        assertEquals(tag, PSSubscriberStateImpl._ID_ps_PDP_ActiveReachableForPaging);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertFalse(asn.isTagPrimitive());
        impl.decodeAll(asn);
        assertEquals(impl.getChoice(), PSSubscriberStateChoice.psPDPActiveReachableForPaging);
        assertEquals(impl.getPDPContextInfoList().size(), 2);
        assertEquals(impl.getPDPContextInfoList().get(0).getPdpContextIdentifier(), 5);
        assertEquals(impl.getPDPContextInfoList().get(1).getPdpContextIdentifier(), 6);
        assertNull(impl.getNetDetNotReachable());

        rawData = getEncodedDataNotReachableReason();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new PSSubscriberStateImpl();
        assertEquals(tag, Tag.ENUMERATED);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(asn.isTagPrimitive());
        impl.decodeAll(asn);
        assertEquals(impl.getChoice(), PSSubscriberStateChoice.netDetNotReachable);
        assertNull(impl.getPDPContextInfoList());
        assertEquals(impl.getNetDetNotReachable(), NotReachableReason.msPurged);

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        PSSubscriberStateImpl impl = new PSSubscriberStateImpl(PSSubscriberStateChoice.psDetached, null, null);
        // PSSubscriberStateChoice choice, NotReachableReason netDetNotReachable, ArrayList<PDPContextInfo> pdpContextInfoList
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataDetached();
        assertTrue(Arrays.equals(rawData, encodedData));

        ArrayList<PDPContextInfo> pdpContextInfoList = new ArrayList<PDPContextInfo>();
        PDPContextInfoImpl ci1 = new PDPContextInfoImpl(5, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        PDPContextInfoImpl ci2 = new PDPContextInfoImpl(6, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        pdpContextInfoList.add(ci1);
        pdpContextInfoList.add(ci2);
        impl = new PSSubscriberStateImpl(PSSubscriberStateChoice.psPDPActiveReachableForPaging, null, pdpContextInfoList);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataActiveReachableForPaging();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new PSSubscriberStateImpl(PSSubscriberStateChoice.netDetNotReachable, NotReachableReason.msPurged, null);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataNotReachableReason();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "subscriberInformation" })
    public void testXMLSerialize() throws Exception {

        PSSubscriberStateImpl original = new PSSubscriberStateImpl(PSSubscriberStateChoice.psDetached, null, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "psSubscriberState", PSSubscriberStateImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        PSSubscriberStateImpl copy = reader.read("psSubscriberState", PSSubscriberStateImpl.class);

        assertEquals(copy.getChoice(), original.getChoice());
        assertNull(copy.getPDPContextInfoList());
        assertNull(copy.getNetDetNotReachable());


        original = new PSSubscriberStateImpl(PSSubscriberStateChoice.netDetNotReachable, NotReachableReason.imsiDetached, null);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "psSubscriberState", PSSubscriberStateImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("psSubscriberState", PSSubscriberStateImpl.class);

        assertEquals(copy.getChoice(), original.getChoice());
        assertNull(copy.getPDPContextInfoList());
        assertEquals(copy.getNetDetNotReachable(), original.getNetDetNotReachable());


        ArrayList<PDPContextInfo> lst = new ArrayList<PDPContextInfo>();
        PDPContextInfoImpl pdpCI = new PDPContextInfoImpl(10, true, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        lst.add(pdpCI);
        PDPContextInfoImpl pdpCI2 = new PDPContextInfoImpl(11, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        lst.add(pdpCI2);
        original = new PSSubscriberStateImpl(PSSubscriberStateChoice.psPDPActiveNotReachableForPaging, null, lst);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "psSubscriberState", PSSubscriberStateImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("psSubscriberState", PSSubscriberStateImpl.class);

        assertEquals(copy.getChoice(), original.getChoice());
        assertNull(copy.getNetDetNotReachable());
        assertEquals(copy.getPDPContextInfoList().size(), original.getPDPContextInfoList().size());
        assertEquals(copy.getPDPContextInfoList().get(0).getPdpContextIdentifier(), original.getPDPContextInfoList().get(0).getPdpContextIdentifier());
        assertEquals(copy.getPDPContextInfoList().get(1).getPdpContextIdentifier(), original.getPDPContextInfoList().get(1).getPdpContextIdentifier());
    }

}
