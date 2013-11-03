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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InformationToSendTest {

    public byte[] getData1() {
        return new byte[] { (byte) 160, 8, (byte) 160, 3, (byte) 128, 1, 91, (byte) 129, 1, 1 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 161, 6, (byte) 128, 1, 5, (byte) 129, 1, 100 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        InformationToSendImpl elem = new InformationToSendImpl();
        int tag = ais.readTag();
        assertEquals(tag, 0);
        elem.decodeAll(ais);
        assertEquals((int) elem.getInbandInfo().getMessageID().getElementaryMessageID(), 91);
        assertEquals((int) elem.getInbandInfo().getNumberOfRepetitions(), 1);
        assertNull(elem.getInbandInfo().getDuration());
        assertNull(elem.getInbandInfo().getInterval());
        assertNull(elem.getTone());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new InformationToSendImpl();
        tag = ais.readTag();
        assertEquals(tag, 1);
        elem.decodeAll(ais);
        assertNull(elem.getInbandInfo());
        assertEquals(elem.getTone().getToneID(), 5);
        assertEquals((int) elem.getTone().getDuration(), 100);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        MessageIDImpl messageID = new MessageIDImpl(91);
        InbandInfoImpl inbandInfo = new InbandInfoImpl(messageID, 1, null, null);
        // MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval
        InformationToSendImpl elem = new InformationToSendImpl(inbandInfo);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        ToneImpl tone = new ToneImpl(5, 100);
        // int toneID, Integer duration
        elem = new InformationToSendImpl(tone);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }
}
