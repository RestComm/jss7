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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

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
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeIfTariffSwitchImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeInformationImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class TimeInformationTest {

    public byte[] getData1() {
        return new byte[] { (byte) 128, 1, 26 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 161, 4, (byte) 128, 2, 3, (byte) 232 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        TimeInformationImpl elem = new TimeInformationImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals((int) elem.getTimeIfNoTariffSwitch(), 26);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new TimeInformationImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getTimeIfTariffSwitch().getTimeSinceTariffSwitch(), 1000);
        assertNull(elem.getTimeIfTariffSwitch().getTariffSwitchInterval());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        TimeInformationImpl elem = new TimeInformationImpl(26);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        TimeIfTariffSwitchImpl tit = new TimeIfTariffSwitchImpl(1000, null);
        elem = new TimeInformationImpl(tit);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerializaion() throws Exception {
        TimeInformationImpl original = new TimeInformationImpl(26);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "timeInformation", TimeInformationImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        TimeInformationImpl copy = reader.read("timeInformation", TimeInformationImpl.class);

        assertEquals(copy.getTimeIfNoTariffSwitch(), original.getTimeIfNoTariffSwitch());
        assertNull(copy.getTimeIfTariffSwitch());

        TimeIfTariffSwitchImpl tit = new TimeIfTariffSwitchImpl(1000, null);
        original = new TimeInformationImpl(tit);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "timeInformation", TimeInformationImpl.class);
        writer.close();

        rawData = baos.toByteArray();

        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("timeInformation", TimeInformationImpl.class);

        assertEquals(copy.getTimeIfTariffSwitch().getTariffSwitchInterval(), original.getTimeIfTariffSwitch()
                .getTariffSwitchInterval());
        assertNull(copy.getTimeIfNoTariffSwitch());
    }
}
