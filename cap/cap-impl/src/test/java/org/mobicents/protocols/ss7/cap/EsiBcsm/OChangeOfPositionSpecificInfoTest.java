/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class OChangeOfPositionSpecificInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 200 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 19, (byte) 191, 50, 4, 2, 2, 0, (byte) 200, (byte) 191, 51, 9, (byte) 133, 5, 82, (byte) 240, 16, (byte) 128, (byte) 232,
                (byte) 135, 0 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        OChangeOfPositionSpecificInfoImpl elem = new OChangeOfPositionSpecificInfoImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);

        elem.decodeAll(ais);
        assertEquals((int) elem.getLocationInformation().getAgeOfLocationInformation(), 200);
        assertNull(elem.getMetDPCriteriaList());


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new OChangeOfPositionSpecificInfoImpl();
        tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);

        elem.decodeAll(ais);
        assertEquals((int) elem.getLocationInformation().getAgeOfLocationInformation(), 200);

        assertEquals(elem.getMetDPCriteriaList().size(), 2);
        assertEquals(elem.getMetDPCriteriaList().get(0).getLeavingLocationAreaId().getLac(), 33000);
        assertTrue(elem.getMetDPCriteriaList().get(1).getInterSystemHandOverToGSM());
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {

        LocationInformation locationInformation = new LocationInformationImpl(200, null, null, null, null, null, null, null, null, false, false, null, null);
        OChangeOfPositionSpecificInfoImpl elem = new OChangeOfPositionSpecificInfoImpl(locationInformation, null);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData1());


        ArrayList<MetDPCriterion> metDPCriteriaList = new ArrayList<MetDPCriterion>();
        LAIFixedLength value = new LAIFixedLengthImpl(250, 1, 33000);
        MetDPCriterion met1 = new MetDPCriterionImpl(value, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);
        metDPCriteriaList.add(met1);
        MetDPCriterion met2 = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);
        metDPCriteriaList.add(met2);
        elem = new OChangeOfPositionSpecificInfoImpl(locationInformation, metDPCriteriaList);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData2());
    }

    @Test(groups = { "functional.xml.serialize", "EsiBcsm" })
    public void testXMLSerializaion() throws Exception {
        LocationInformation locationInformation = new LocationInformationImpl(200, null, null, null, null, null, null, null, null, false, false, null, null);
        OChangeOfPositionSpecificInfoImpl original = new OChangeOfPositionSpecificInfoImpl(locationInformation, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "oChangeOfPositionSpecificInfo", OChangeOfPositionSpecificInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        OChangeOfPositionSpecificInfoImpl copy = reader.read("oChangeOfPositionSpecificInfo", OChangeOfPositionSpecificInfoImpl.class);

        assertEquals((int) copy.getLocationInformation().getAgeOfLocationInformation(), (int) original.getLocationInformation().getAgeOfLocationInformation());
        assertNull(copy.getMetDPCriteriaList());


        ArrayList<MetDPCriterion> metDPCriteriaList = new ArrayList<MetDPCriterion>();
        LAIFixedLength value = new LAIFixedLengthImpl(250, 1, 33000);
        MetDPCriterion met1 = new MetDPCriterionImpl(value, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);
        metDPCriteriaList.add(met1);
        MetDPCriterion met2 = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);
        metDPCriteriaList.add(met2);
        original = new OChangeOfPositionSpecificInfoImpl(locationInformation, metDPCriteriaList);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "oChangeOfPositionSpecificInfo", OChangeOfPositionSpecificInfoImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("oChangeOfPositionSpecificInfo", OChangeOfPositionSpecificInfoImpl.class);

        assertEquals((int) copy.getLocationInformation().getAgeOfLocationInformation(), (int) original.getLocationInformation().getAgeOfLocationInformation());
        assertEquals(copy.getMetDPCriteriaList().size(), original.getMetDPCriteriaList().size());
        assertEquals(copy.getMetDPCriteriaList().get(0).getLeavingLocationAreaId().getLac(), original.getMetDPCriteriaList().get(0).getLeavingLocationAreaId()
                .getLac());
        assertEquals(copy.getMetDPCriteriaList().get(1).getInterSystemHandOverToGSM(), original.getMetDPCriteriaList().get(1).getInterSystemHandOverToGSM());
    }

}
