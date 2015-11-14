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
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OServiceChangeSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TServiceChangeSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class DpSpecificInfoAltTest {

    public byte[] getData1() {
        return new byte[] { 48, 25, (byte) 160, 5, (byte) 160, 3, (byte) 130, 1, 38, (byte) 161, 5, (byte) 160, 3, (byte) 131, 1, 98, (byte) 162, 9,
                (byte) 128, 7, 0, 0, 51, 51, 19, 17, 17 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        DpSpecificInfoAltImpl elem = new DpSpecificInfoAltImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);
        elem.decodeAll(ais);

        assertEquals(elem.getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.padAccessCA_9600bps);
        assertEquals(elem.getCollectedInfoSpecificInfo().getCalledPartyNumber().getCalledPartyNumber().getAddress(), "3333311111");
        assertEquals(elem.getTServiceChangeSpecificInfo().getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.automaticFacsimileGroup3);
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {

        ExtBearerServiceCode extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl(extBearerService);
        OServiceChangeSpecificInfo oServiceChangeSpecificInfo = new OServiceChangeSpecificInfoImpl(extBasicServiceCode);
        CalledPartyNumber calledPartyNumber = new CalledPartyNumberImpl();
        calledPartyNumber.setAddress("3333311111");
        CalledPartyNumberCap calledPartyNumberCap = new CalledPartyNumberCapImpl(calledPartyNumber);
        CollectedInfoSpecificInfo collectedInfoSpecificInfo = new CollectedInfoSpecificInfoImpl(calledPartyNumberCap);
        ExtTeleserviceCode extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.automaticFacsimileGroup3);
        ExtBasicServiceCode extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleservice);
        TServiceChangeSpecificInfo tServiceChangeSpecificInfo = new TServiceChangeSpecificInfoImpl(extBasicServiceCode2);
        DpSpecificInfoAltImpl elem = new DpSpecificInfoAltImpl(oServiceChangeSpecificInfo, collectedInfoSpecificInfo, tServiceChangeSpecificInfo);
//        OServiceChangeSpecificInfo oServiceChangeSpecificInfo, CollectedInfoSpecificInfo collectedInfoSpecificInfo,
//        TServiceChangeSpecificInfo tServiceChangeSpecificInfo
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.xml.serialize", "EsiBcsm" })
    public void testXMLSerializaion() throws Exception {
        ExtBearerServiceCode extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl(extBearerService);
        OServiceChangeSpecificInfo oServiceChangeSpecificInfo = new OServiceChangeSpecificInfoImpl(extBasicServiceCode);
        CalledPartyNumber calledPartyNumber = new CalledPartyNumberImpl();
        calledPartyNumber.setAddress("3333311111");
        CalledPartyNumberCap calledPartyNumberCap = new CalledPartyNumberCapImpl(calledPartyNumber);
        CollectedInfoSpecificInfo collectedInfoSpecificInfo = new CollectedInfoSpecificInfoImpl(calledPartyNumberCap);
        ExtTeleserviceCode extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.automaticFacsimileGroup3);
        ExtBasicServiceCode extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleservice);
        TServiceChangeSpecificInfo tServiceChangeSpecificInfo = new TServiceChangeSpecificInfoImpl(extBasicServiceCode2);
        DpSpecificInfoAltImpl original = new DpSpecificInfoAltImpl(oServiceChangeSpecificInfo, collectedInfoSpecificInfo, tServiceChangeSpecificInfo);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "dpSpecificInfoAlt", DpSpecificInfoAltImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        DpSpecificInfoAltImpl copy = reader.read("dpSpecificInfoAlt", DpSpecificInfoAltImpl.class);

        assertEquals(copy.getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue(), original
                .getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue());
        assertEquals(copy.getCollectedInfoSpecificInfo().getCalledPartyNumber().getCalledPartyNumber().getAddress(), original.getCollectedInfoSpecificInfo()
                .getCalledPartyNumber().getCalledPartyNumber().getAddress());
        assertEquals(copy.getTServiceChangeSpecificInfo().getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(), original
                .getTServiceChangeSpecificInfo().getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue());
    }

}
