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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.testng.annotations.Test;

/**
 * @author Amit Bhayani
 *
 */
public class OCalledPartyBusySpecificInfoTest {

    public byte[] getIntData() {
        return new byte[] { (byte) 132, (byte) 144 };
    }

    /**
	 *
	 */
    public OCalledPartyBusySpecificInfoTest() {
        // TODO Auto-generated constructor stub
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {

        CauseCapImpl cause = new CauseCapImpl(this.getIntData());
        OCalledPartyBusySpecificInfoImpl original = new OCalledPartyBusySpecificInfoImpl(cause);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "OCalledPartyBusySpecificInfoImpl", OCalledPartyBusySpecificInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        OCalledPartyBusySpecificInfoImpl copy = reader.read("OCalledPartyBusySpecificInfoImpl",
                OCalledPartyBusySpecificInfoImpl.class);

        assertEquals(copy.getBusyCause().getCauseIndicators().getLocation(), original.getBusyCause().getCauseIndicators()
                .getLocation());
        assertEquals(copy.getBusyCause().getCauseIndicators().getCauseValue(), original.getBusyCause().getCauseIndicators()
                .getCauseValue());
        assertEquals(copy.getBusyCause().getCauseIndicators().getCodingStandard(), original.getBusyCause().getCauseIndicators()
                .getCodingStandard());

    }

}
