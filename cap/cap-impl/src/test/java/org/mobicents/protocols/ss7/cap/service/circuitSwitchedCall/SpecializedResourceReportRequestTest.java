/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

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
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author kiss.balazs@alerant.hu
 *
 */
public class SpecializedResourceReportRequestTest {

    public byte[] getData1() {
        return new byte[] { (byte) 159, 50, 0 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 159, 51, 0 };
    }

    public byte[] getData3() {
        return new byte[] { 5, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        int tag = ais.readTag();
        SpecializedResourceReportRequestImpl elem = new SpecializedResourceReportRequestImpl(true);
        elem.decodeAll(ais);
        assertTrue(elem.getAllAnnouncementsComplete());
        assertFalse(elem.getFirstAnnouncementStarted());

        data = this.getData2();
        ais = new AsnInputStream(data);
        tag = ais.readTag();
        elem = new SpecializedResourceReportRequestImpl(true);
        elem.decodeAll(ais);
        assertFalse(elem.getAllAnnouncementsComplete());
        assertTrue(elem.getFirstAnnouncementStarted());

        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new SpecializedResourceReportRequestImpl(false);
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertFalse(elem.getAllAnnouncementsComplete());
        assertFalse(elem.getFirstAnnouncementStarted());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        SpecializedResourceReportRequestImpl elem = new SpecializedResourceReportRequestImpl(true, false, true);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
        // boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted, boolean isCAPVersion4orLater

        elem = new SpecializedResourceReportRequestImpl(false, true, true);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        elem = new SpecializedResourceReportRequestImpl(false, false, false);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        SpecializedResourceReportRequestImpl original = new SpecializedResourceReportRequestImpl(false, true, true);
        original.setLinkedId(12L);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "specializedResourceReport", SpecializedResourceReportRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);
        System.out.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        SpecializedResourceReportRequestImpl copy = reader.read("specializedResourceReport",
                SpecializedResourceReportRequestImpl.class);

        assertEquals(original.getAllAnnouncementsComplete(), copy.getAllAnnouncementsComplete());
        assertEquals(original.getFirstAnnouncementStarted(), copy.getFirstAnnouncementStarted());
        assertTrue(isEqual(original, copy));
    }

    private boolean isEqual(SpecializedResourceReportRequestImpl o1, SpecializedResourceReportRequestImpl o2) {
        if (o1 == o2)
            return true;
        if (o1 == null && o2 != null || o1 != null && o2 == null)
            return false;
        if (o1 == null && o2 == null)
            return true;
        if (!o1.toString().equals(o2.toString()))
            return false;
        return true;
    }

}
