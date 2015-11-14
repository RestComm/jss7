/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class ProvideSubscriberInfoRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 12, (byte) 128, 6, 17, 33, 34, 51, 67, 68, (byte) 162, 2, (byte) 128, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 62, (byte) 128, 6, 17, 33, 34, 51, 67, 68, (byte) 129, 4, 11, 22, 33, 44, (byte) 162, 2, (byte) 128, 0, (byte) 163, 39,
                (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161,
                3, 31, 32, 33, (byte) 132, 1, 4 };
    }

    private byte[] getLmsiData() {
        return new byte[] { 11, 22, 33, 44 };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ProvideSubscriberInfoRequestImpl asc = new ProvideSubscriberInfoRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        IMSI imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertTrue(asc.getRequestedInfo().getLocationInformation());
        assertFalse(asc.getRequestedInfo().getSubscriberState());

        assertNull(asc.getLmsi());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getCallPriority());


        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertTrue(asc.getRequestedInfo().getLocationInformation());
        assertFalse(asc.getRequestedInfo().getSubscriberState());

        assertEquals(asc.getLmsi().getData(), getLmsiData());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

        assertEquals(asc.getCallPriority(), EMLPPPriority.priorityLevel4);

    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberInformation" })
    public void testEncode() throws Exception {

        IMSIImpl imsi = new IMSIImpl("111222333444");
        RequestedInfoImpl requestedInfo = new RequestedInfoImpl(true, false, null, false, null, false, false, false);
        ProvideSubscriberInfoRequestImpl asc = new ProvideSubscriberInfoRequestImpl(imsi, null, requestedInfo, null, null);
//        IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo, MAPExtensionContainer extensionContainer,
//        EMLPPPriority callPriority

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        LMSIImpl lmsi = new LMSIImpl(getLmsiData());
        asc = new ProvideSubscriberInfoRequestImpl(imsi, lmsi, requestedInfo, MAPExtensionContainerTest.GetTestExtensionContainer(), EMLPPPriority.priorityLevel4);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
