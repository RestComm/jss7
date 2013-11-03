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

import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.testng.annotations.Test;

/**
 * @author Amit Bhayani
 *
 */
public class OAnswerSpecificInfoTest {

    /**
	 *
	 */
    public OAnswerSpecificInfoTest() {
        // TODO Auto-generated constructor stub
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(0, "111222333", 1, 1);
        CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        ExtTeleserviceCodeImpl extTeleservice2 = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allShortMessageServices);

        ExtBasicServiceCode extBasicSer = new ExtBasicServiceCodeImpl(extTeleservice);
        ExtBasicServiceCode extBasicSer2 = new ExtBasicServiceCodeImpl(extTeleservice2);

        OAnswerSpecificInfoImpl original = new OAnswerSpecificInfoImpl(forwardingDestinationNumber, false, true, null,
                extBasicSer, extBasicSer2);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "oAnswerSpecificInfo", OAnswerSpecificInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        OAnswerSpecificInfoImpl copy = reader.read("oAnswerSpecificInfo", OAnswerSpecificInfoImpl.class);

        assertEquals(copy.getDestinationAddress().getCalledPartyNumber().getAddress(), original.getDestinationAddress()
                .getCalledPartyNumber().getAddress());
        assertEquals(copy.getOrCall(), original.getOrCall());
        assertEquals(copy.getForwardedCall(), original.getForwardedCall());
        assertEquals(copy.getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(), original
                .getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue());
        assertEquals(copy.getExtBasicServiceCode2().getExtTeleservice().getTeleserviceCodeValue(), original
                .getExtBasicServiceCode2().getExtTeleservice().getTeleserviceCodeValue());
    }
}
