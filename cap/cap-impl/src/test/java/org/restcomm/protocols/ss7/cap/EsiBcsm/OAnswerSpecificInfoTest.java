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
import org.restcomm.protocols.ss7.cap.EsiBcsm.ChargeIndicatorImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.OAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ChargeIndicator;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ChargeIndicatorValue;
import org.restcomm.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.testng.annotations.Test;

/**
 * @author Amit Bhayani
 * @author sergey vetyutnev
 *
 */
public class OAnswerSpecificInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 25, (byte) 159, 50, 7, (byte) 128, (byte) 144, 17, 33, 34, 51, 3, (byte) 159, 52, 0, (byte) 191, 54, 3, (byte) 131, 1, 16,
                (byte) 191, 55, 3, (byte) 131, 1, 32 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 17, (byte) 159, 50, 7, (byte) 128, (byte) 144, 17, 33, 34, 51, 3, (byte) 159, 51, 0, (byte) 159, 53, 1, 1 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        OAnswerSpecificInfoImpl elem = new OAnswerSpecificInfoImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);

        elem.decodeAll(ais);
        assertEquals(elem.getDestinationAddress().getCalledPartyNumber().getAddress(), "111222333");
        assertFalse(elem.getOrCall());
        assertTrue(elem.getForwardedCall());
        assertNull(elem.getChargeIndicator());
        assertEquals(elem.getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);
        assertEquals(elem.getExtBasicServiceCode2().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allShortMessageServices);


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new OAnswerSpecificInfoImpl();
        tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);

        elem.decodeAll(ais);
        assertEquals(elem.getDestinationAddress().getCalledPartyNumber().getAddress(), "111222333");
        assertTrue(elem.getOrCall());
        assertFalse(elem.getForwardedCall());
        assertEquals(elem.getChargeIndicator().getChargeIndicatorValue(), ChargeIndicatorValue.noCharge);
        assertNull(elem.getExtBasicServiceCode());
        assertNull(elem.getExtBasicServiceCode2());
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(0, "111222333", 1, 1);
        CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        ExtTeleserviceCodeImpl extTeleservice2 = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allShortMessageServices);

        ExtBasicServiceCode extBasicSer = new ExtBasicServiceCodeImpl(extTeleservice);
        ExtBasicServiceCode extBasicSer2 = new ExtBasicServiceCodeImpl(extTeleservice2);

        OAnswerSpecificInfoImpl elem = new OAnswerSpecificInfoImpl(forwardingDestinationNumber, false, true, null,
                extBasicSer, extBasicSer2);
//        CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall,
//        ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode, ExtBasicServiceCode extBasicServiceCode2
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData1());


        ChargeIndicator chargeIndicator = new ChargeIndicatorImpl(ChargeIndicatorValue.noCharge);
        elem = new OAnswerSpecificInfoImpl(forwardingDestinationNumber, true, false, chargeIndicator,
                null, null);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData2());
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
        writer.setIndentation("\t");
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
        assertNull(copy.getChargeIndicator());
        assertEquals(copy.getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(), original
                .getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue());
        assertEquals(copy.getExtBasicServiceCode2().getExtTeleservice().getTeleserviceCodeValue(), original
                .getExtBasicServiceCode2().getExtTeleservice().getTeleserviceCodeValue());


        ChargeIndicator chargeIndicator = new ChargeIndicatorImpl(ChargeIndicatorValue.noCharge);
        original = new OAnswerSpecificInfoImpl(forwardingDestinationNumber, true, false, chargeIndicator, null, null);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "oAnswerSpecificInfo", OAnswerSpecificInfoImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("oAnswerSpecificInfo", OAnswerSpecificInfoImpl.class);

        assertEquals(copy.getDestinationAddress().getCalledPartyNumber().getAddress(), original.getDestinationAddress()
                .getCalledPartyNumber().getAddress());
        assertEquals(copy.getOrCall(), original.getOrCall());
        assertEquals(copy.getForwardedCall(), original.getForwardedCall());
        assertEquals(copy.getChargeIndicator().getChargeIndicatorValue(), original.getChargeIndicator().getChargeIndicatorValue());
        assertNull(copy.getExtBasicServiceCode());
        assertNull(copy.getExtBasicServiceCode2());
    }
}
