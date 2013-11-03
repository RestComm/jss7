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

package org.mobicents.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationInfoWithLMSITest {

    private byte[] getEncodedData() {
        return new byte[] { -96, 15, -127, 7, -111, -105, 48, 115, 0, 34, -14, 4, 4, 0, 3, 98, 49 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { -96, 65, -127, 6, -88, 33, 67, 101, -121, 9, 4, 4, 4, 3, 2, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
                32, 33, -123, 0, -122, 6, -71, -119, 103, 69, 35, -15 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        LocationInfoWithLMSIImpl liw = new LocationInfoWithLMSIImpl();
        liw.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        ISDNAddressString nnm = liw.getNetworkNodeNumber();
        assertEquals(nnm.getAddressNature(), AddressNature.international_number);
        assertEquals(nnm.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(nnm.getAddress(), "79033700222");
        assertTrue(Arrays.equals(new byte[] { 0, 3, 98, 49 }, liw.getLMSI().getData()));

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        liw = new LocationInfoWithLMSIImpl();
        liw.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        nnm = liw.getNetworkNodeNumber();
        assertEquals(nnm.getAddressNature(), AddressNature.national_significant_number);
        assertEquals(nnm.getNumberingPlan(), NumberingPlan.national);
        assertEquals(nnm.getAddress(), "1234567890");
        assertTrue(Arrays.equals(new byte[] { 4, 3, 2, 1 }, liw.getLMSI().getData()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(liw.getExtensionContainer()));
        assertEquals(liw.getAdditionalNumberType(), AdditionalNumberType.sgsn);
        nnm = liw.getAdditionalNumber();
        assertEquals(nnm.getAddressNature(), AddressNature.network_specific_number);
        assertEquals(nnm.getNumberingPlan(), NumberingPlan.private_plan);
        assertEquals(nnm.getAddress(), "987654321");
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        ISDNAddressString nnm = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79033700222");
        LMSIImpl lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });
        LocationInfoWithLMSIImpl liw = new LocationInfoWithLMSIImpl(nnm, lmsi, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        liw.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        nnm = new ISDNAddressStringImpl(AddressNature.national_significant_number, NumberingPlan.national, "1234567890");
        ISDNAddressStringImpl an = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.private_plan,
                "987654321");
        lmsi = new LMSIImpl(new byte[] { 4, 3, 2, 1 });
        liw = new LocationInfoWithLMSIImpl(nnm, lmsi, MAPExtensionContainerTest.GetTestExtensionContainer(),
                AdditionalNumberType.sgsn, an);

        asnOS.reset();
        liw.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

    @Test(groups = { "functional.serialize", "service.sms" })
    public void testSerialization() throws Exception {
        ISDNAddressString nnm = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79033700222");
        LMSIImpl lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });
        LocationInfoWithLMSIImpl original = new LocationInfoWithLMSIImpl(nnm, lmsi, null, null, null);

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
