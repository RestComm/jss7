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

package org.restcomm.protocols.ss7.cap.primitives;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.primitives.AChChargingAddressImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.primitives.LegIDImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class AChChargingAddressTest {

    public byte[] getData1() {
        return new byte[] { (byte) 162, 3, (byte) 129, 1, 2 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 159, 50, 1, 5 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        AChChargingAddressImpl elem = new AChChargingAddressImpl();
        int tag = ais.readTag();
        assertEquals(tag, AChChargingAddressImpl._ID_legID);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        elem.decodeAll(ais);
        assertEquals(elem.getLegID().getReceivingSideID(), LegType.leg2);
        assertEquals(elem.getSrfConnection(), 0);


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new AChChargingAddressImpl();
        tag = ais.readTag();
        assertEquals(tag, AChChargingAddressImpl._ID_srfConnection);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        elem.decodeAll(ais);
        assertNull(elem.getLegID());
        assertEquals(elem.getSrfConnection(), 5);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        LegID legID = new LegIDImpl(false, LegType.leg2);
        AChChargingAddressImpl elem = new AChChargingAddressImpl(legID);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));


        elem = new AChChargingAddressImpl(5);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        LegID legID = new LegIDImpl(false, LegType.leg2);
        AChChargingAddressImpl original = new AChChargingAddressImpl(legID);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "aChChargingAddress", AChChargingAddressImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        AChChargingAddressImpl copy = reader.read("aChChargingAddress", AChChargingAddressImpl.class);

        assertEquals(copy.getLegID().getReceivingSideID(), original.getLegID().getReceivingSideID());
        assertEquals(copy.getSrfConnection(), original.getSrfConnection());


        original = new AChChargingAddressImpl(5);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "aChChargingAddress", AChChargingAddressImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("aChChargingAddress", AChChargingAddressImpl.class);

        assertNull(copy.getLegID());
        assertEquals(copy.getSrfConnection(), original.getSrfConnection());
    }

}
