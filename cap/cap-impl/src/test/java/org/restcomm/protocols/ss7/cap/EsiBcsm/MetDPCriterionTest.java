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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.EsiBcsm.MetDPCriterionAltImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.MetDPCriterionImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterionAlt;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class MetDPCriterionTest {

    public byte[] getData1() {
        return new byte[] { (byte) 128, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 129, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData3() {
        return new byte[] { (byte) 130, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData4() {
        return new byte[] { (byte) 131, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData5() {
        return new byte[] { (byte) 132, 5, (byte) 145, (byte) 240, 16, 85, (byte) 240 };
    }

    public byte[] getData6() {
        return new byte[] { (byte) 133, 5, (byte) 145, (byte) 240, 16, 85, (byte) 240 };
    }

    public byte[] getData7() {
        return new byte[] { (byte) 134, 0 };
    }

    public byte[] getData8() {
        return new byte[] { (byte) 135, 0 };
    }

    public byte[] getData9() {
        return new byte[] { (byte) 136, 0 };
    }

    public byte[] getData10() {
        return new byte[] { (byte) 137, 0 };
    }

    public byte[] getData11() {
        return new byte[] { (byte) 170, 0 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        MetDPCriterionImpl elem = new MetDPCriterionImpl();
        int tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_enteringCellGlobalId);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertEquals(elem.getEnteringCellGlobalId().getLac(), 22000);


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_leavingCellGlobalId);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertEquals(elem.getLeavingCellGlobalId().getLac(), 22000);


        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_enteringServiceAreaId);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertEquals(elem.getEnteringServiceAreaId().getLac(), 22000);


        data = this.getData4();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_leavingServiceAreaId);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertEquals(elem.getLeavingServiceAreaId().getLac(), 22000);


        data = this.getData5();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_enteringLocationAreaId);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertEquals(elem.getEnteringLocationAreaId().getLac(), 22000);


        data = this.getData6();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_leavingLocationAreaId);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertEquals(elem.getLeavingLocationAreaId().getLac(), 22000);


        data = this.getData7();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_interSystemHandOverToUMTS);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertTrue(elem.getInterSystemHandOverToUMTS());


        data = this.getData8();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_interSystemHandOverToGSM);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertTrue(elem.getInterSystemHandOverToGSM());


        data = this.getData9();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_interPLMNHandOver);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertTrue(elem.getInterPLMNHandOver());


        data = this.getData10();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_interMSCHandOver);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertTrue(elem.getInterMSCHandOver());


        data = this.getData11();
        ais = new AsnInputStream(data);
        elem = new MetDPCriterionImpl();
        tag = ais.readTag();
        assertEquals(tag, MetDPCriterionImpl._ID_metDPCriterionAlt);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        elem.decodeAll(ais);
        assertNotNull(elem.getMetDPCriterionAlt());
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {

        CellGlobalIdOrServiceAreaIdFixedLength value = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(201, 1, 22000, 55);
        // int mcc, int mnc, int lac, int cellIdOrServiceAreaCode
        MetDPCriterionImpl elem = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringCellGlobalId);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData1());


        elem = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingCellGlobalId);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData2());


        elem = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringServiceAreaId);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData3());


        elem = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingServiceAreaId);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData4());


        LAIFixedLength lai = new LAIFixedLengthImpl(190, 1, 22000);
        // int mcc, int mnc, int lac
        elem = new MetDPCriterionImpl(lai, MetDPCriterionImpl.LAIFixedLength_Option.enteringLocationAreaId);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData5());


        elem = new MetDPCriterionImpl(lai, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData6());


        elem = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToUMTS);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData7());


        elem = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData8());


        elem = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interPLMNHandOver);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData9());


        elem = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interMSCHandOver);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData10());


        MetDPCriterionAlt metDPCriterionAlt = new MetDPCriterionAltImpl();
        elem = new MetDPCriterionImpl(metDPCriterionAlt);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData11());
    }

    @Test(groups = { "functional.xml.serialize", "EsiBcsm" })
    public void testXMLSerializaion() throws Exception {
        CellGlobalIdOrServiceAreaIdFixedLength value = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(201, 1, 22000, 55);
        // int mcc, int mnc, int lac, int cellIdOrServiceAreaCode
        MetDPCriterionImpl original = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringCellGlobalId);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MetDPCriterionImpl copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getEnteringCellGlobalId().getLac(), original.getEnteringCellGlobalId().getLac());


        original = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingCellGlobalId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getLeavingCellGlobalId().getLac(), original.getLeavingCellGlobalId().getLac());


        original = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringServiceAreaId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getEnteringServiceAreaId().getLac(), original.getEnteringServiceAreaId().getLac());


        original = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingServiceAreaId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getLeavingServiceAreaId().getLac(), original.getLeavingServiceAreaId().getLac());


        LAIFixedLength lai = new LAIFixedLengthImpl(190, 1, 22000);
        original = new MetDPCriterionImpl(lai, MetDPCriterionImpl.LAIFixedLength_Option.enteringLocationAreaId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getEnteringLocationAreaId().getLac(), original.getEnteringLocationAreaId().getLac());


        original = new MetDPCriterionImpl(lai, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getLeavingLocationAreaId().getLac(), original.getLeavingLocationAreaId().getLac());


        original = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToUMTS);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getInterSystemHandOverToUMTS(), original.getInterSystemHandOverToUMTS());


        original = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getInterSystemHandOverToGSM(), original.getInterSystemHandOverToGSM());


        original = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interPLMNHandOver);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getInterPLMNHandOver(), original.getInterPLMNHandOver());


        original = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interMSCHandOver);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getInterMSCHandOver(), original.getInterMSCHandOver());


        MetDPCriterionAlt metDPCriterionAlt = new MetDPCriterionAltImpl();
        original = new MetDPCriterionImpl(metDPCriterionAlt);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertNotNull(copy.getMetDPCriterionAlt());
    }

}
