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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
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
 * @author Amit Bhayani
 *
 */
public class TimeIfTariffSwitchTest {

    public byte[] getData1() {
        return new byte[] { 48, 6, (byte) 128, 1, 11, (byte) 129, 1, 22 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        TimeIfTariffSwitchImpl elem = new TimeIfTariffSwitchImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getTimeSinceTariffSwitch(), 11);
        assertEquals((int) elem.getTariffSwitchInterval(), 22);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        TimeIfTariffSwitchImpl elem = new TimeIfTariffSwitchImpl(11, 22);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerializaion() throws Exception {
        TimeIfTariffSwitchImpl original = new TimeIfTariffSwitchImpl(11, 22);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "timeIfTariffSwitch", TimeIfTariffSwitchImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        TimeIfTariffSwitchImpl copy = reader.read("timeIfTariffSwitch", TimeIfTariffSwitchImpl.class);

        assertEquals(copy.getTimeSinceTariffSwitch(), original.getTimeSinceTariffSwitch());
        assertEquals(copy.getTariffSwitchInterval(), original.getTariffSwitchInterval());

    }
}
