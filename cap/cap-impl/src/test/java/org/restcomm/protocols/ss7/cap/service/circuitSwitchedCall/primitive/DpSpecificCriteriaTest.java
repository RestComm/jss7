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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteriaAlt;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MidCallControlInfo;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ChangeOfLocationImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DpSpecificCriteriaAltImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DpSpecificCriteriaImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.MidCallControlInfoImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DpSpecificCriteriaTest {

    public byte[] getData1() {
        return new byte[] { (byte) 129, 2, 3, (byte) 232 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 162, 3, (byte) 128, 1, 10 };
    }

    public byte[] getData3() {
        return new byte[] { (byte) 163, 4, (byte) 160, 2, (byte) 131, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        DpSpecificCriteriaImpl elem = new DpSpecificCriteriaImpl();
        int tag = ais.readTag();
        assertEquals(tag, DpSpecificCriteriaImpl._ID_applicationTimer);
        elem.decodeAll(ais);
        assertEquals((int) elem.getApplicationTimer(), 1000);
        assertNull(elem.getMidCallControlInfo());
        assertNull(elem.getDpSpecificCriteriaAlt());


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new DpSpecificCriteriaImpl();
        tag = ais.readTag();
        assertEquals(tag, DpSpecificCriteriaImpl._ID_midCallControlInfo);
        elem.decodeAll(ais);
        assertNull(elem.getApplicationTimer());
        assertEquals((int) elem.getMidCallControlInfo().getMinimumNumberOfDigits(), 10);
        assertNull(elem.getDpSpecificCriteriaAlt());


        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new DpSpecificCriteriaImpl();
        tag = ais.readTag();
        assertEquals(tag, DpSpecificCriteriaImpl._ID_dpSpecificCriteriaAlt);
        elem.decodeAll(ais);
        assertNull(elem.getApplicationTimer());
        assertNull(elem.getMidCallControlInfo());
        assertTrue(elem.getDpSpecificCriteriaAlt().getChangeOfPositionControlInfo().get(0).isInterSystemHandOver());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        DpSpecificCriteriaImpl elem = new DpSpecificCriteriaImpl(1000);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));


        MidCallControlInfo midCallControlInfo = new MidCallControlInfoImpl(10, null, null, null, null, null);
        elem = new DpSpecificCriteriaImpl(midCallControlInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));


        ArrayList<ChangeOfLocation> changeOfPositionControlInfo = new ArrayList<ChangeOfLocation>();
        ChangeOfLocation changeOfLocation = new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interSystemHandOver);
        changeOfPositionControlInfo.add(changeOfLocation);
        DpSpecificCriteriaAlt dpSpecificCriteriaAlt = new DpSpecificCriteriaAltImpl(changeOfPositionControlInfo, null);
        elem = new DpSpecificCriteriaImpl(dpSpecificCriteriaAlt);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        DpSpecificCriteriaImpl original = new DpSpecificCriteriaImpl(1000);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "dpSpecificCriteria", DpSpecificCriteriaImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        DpSpecificCriteriaImpl copy = reader.read("dpSpecificCriteria", DpSpecificCriteriaImpl.class);

        assertEquals((int) copy.getApplicationTimer(), (int) original.getApplicationTimer());
        assertNull(copy.getMidCallControlInfo());
        assertNull(copy.getDpSpecificCriteriaAlt());


        MidCallControlInfo midCallControlInfo = new MidCallControlInfoImpl(10, null, null, null, null, null);
        original = new DpSpecificCriteriaImpl(midCallControlInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "dpSpecificCriteria", DpSpecificCriteriaImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("dpSpecificCriteria", DpSpecificCriteriaImpl.class);

        assertNull(copy.getApplicationTimer());
        assertEquals(copy.getMidCallControlInfo().getMinimumNumberOfDigits(), original.getMidCallControlInfo().getMinimumNumberOfDigits());
        assertNull(copy.getDpSpecificCriteriaAlt());


        ArrayList<ChangeOfLocation> changeOfPositionControlInfo = new ArrayList<ChangeOfLocation>();
        ChangeOfLocation changeOfLocation = new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interSystemHandOver);
        changeOfPositionControlInfo.add(changeOfLocation);
        DpSpecificCriteriaAlt dpSpecificCriteriaAlt = new DpSpecificCriteriaAltImpl(changeOfPositionControlInfo, null);
        original = new DpSpecificCriteriaImpl(dpSpecificCriteriaAlt);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "dpSpecificCriteria", DpSpecificCriteriaImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("dpSpecificCriteria", DpSpecificCriteriaImpl.class);

        assertNull(copy.getApplicationTimer());
        assertNull(copy.getMidCallControlInfo());
        assertEquals(copy.getDpSpecificCriteriaAlt().getChangeOfPositionControlInfo().get(0).isInterSystemHandOver(), original.getDpSpecificCriteriaAlt()
                .getChangeOfPositionControlInfo().get(0).isInterSystemHandOver());
    }
}
