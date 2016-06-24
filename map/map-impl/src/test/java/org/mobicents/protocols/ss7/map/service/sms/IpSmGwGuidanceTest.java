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

package org.mobicents.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class IpSmGwGuidanceTest {

    private byte[] getEncodedData() {
        return new byte[] {32,6,2,1,10,2,1,20};
    }

    private byte[] getEncodedDataFull() {
        return new byte[] {32,47,2,1,10,2,1,20,48,39,-96,32,48,10,6,3,42,3,4,11,12,13,14,15,
                48,5,6,3,42,3,6,48,11,6,3,42,3,5,21,22,23,24,25,26,-95,3,31,32,33};
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        IpSmGwGuidanceImpl ipSmGwGuidance = new IpSmGwGuidanceImpl();
        ipSmGwGuidance.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(ipSmGwGuidance.getMinimumDeliveryTimeValue(), new Integer(10));
        assertEquals(ipSmGwGuidance.getRecommendedDeliveryTimeValue(), new Integer(20));

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ipSmGwGuidance = new IpSmGwGuidanceImpl();
        ipSmGwGuidance.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(ipSmGwGuidance.getMinimumDeliveryTimeValue(), new Integer(10));
        assertEquals(ipSmGwGuidance.getRecommendedDeliveryTimeValue(), new Integer(20));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ipSmGwGuidance.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        IpSmGwGuidanceImpl liw = new IpSmGwGuidanceImpl(10, 20, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        liw.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, 0);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        liw = new IpSmGwGuidanceImpl(10, 20, MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS.reset();
        liw.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

    @Test(groups = { "functional.serialize", "service.sms" })
    public void testSerialization() throws Exception {
        ISDNAddressString nnm = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79033700222");
        LMSIImpl lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });
        LocationInfoWithLMSIImpl original = new LocationInfoWithLMSIImpl(nnm, lmsi, null, true, null);

        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        // deserialize
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        LocationInfoWithLMSIImpl copy = (LocationInfoWithLMSIImpl) o;

        // test result
        assertEquals(copy.getNetworkNodeNumber(), original.getNetworkNodeNumber());
        assertEquals(copy.getLMSI(), original.getLMSI());
    }
}
