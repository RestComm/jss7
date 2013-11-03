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
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SM_RP_OATest {

    private byte[] getEncodedData_Msisdn() {
        return new byte[] { (byte) 130, 7, (byte) 145, (byte) 147, 51, 88, 38, 101, 89 };
    }

    private byte[] getEncodedData_ServiceCentreAddressOA() {
        return new byte[] { -124, 7, -111, -127, 16, 7, 17, 17, -15 };
    }

    private byte[] getEncodedData_No() {
        return new byte[] { (byte) 133, 0 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData_ServiceCentreAddressOA();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        SM_RP_OAImpl oa = new SM_RP_OAImpl();
        oa.decodeAll(asn);

        assertEquals(tag, 4);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        AddressString nnm = oa.getServiceCentreAddressOA();
        assertEquals(nnm.getAddressNature(), AddressNature.international_number);
        assertEquals(nnm.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(nnm.getAddress(), "18017011111");

        rawData = getEncodedData_Msisdn();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        oa = new SM_RP_OAImpl();
        oa.decodeAll(asn);

        assertEquals(tag, 2);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        ISDNAddressString msisdn = oa.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "393385625695");

        rawData = getEncodedData_No();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        oa = new SM_RP_OAImpl();
        oa.decodeAll(asn);

        assertEquals(tag, 5);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        assertEquals(oa.getServiceCentreAddressOA(), null);
        assertEquals(oa.getMsisdn(), null);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        AddressStringImpl astr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "18017011111");
        SM_RP_OAImpl oa = new SM_RP_OAImpl();
        oa.setServiceCentreAddressOA(astr);

        AsnOutputStream asnOS = new AsnOutputStream();
        oa.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData_ServiceCentreAddressOA();
        assertTrue(Arrays.equals(rawData, encodedData));

        ISDNAddressStringImpl isdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "393385625695");
        oa = new SM_RP_OAImpl();
        oa.setMsisdn(isdn);

        asnOS = new AsnOutputStream();
        oa.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_Msisdn();
        assertTrue(Arrays.equals(rawData, encodedData));

        oa = new SM_RP_OAImpl();

        asnOS = new AsnOutputStream();
        oa.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_No();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.sms" })
    public void testSerialization() throws Exception {
        AddressStringImpl astr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "18017011111");
        SM_RP_OAImpl original = new SM_RP_OAImpl();

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
        SM_RP_OAImpl copy = (SM_RP_OAImpl) o;

        // test result
        assertEquals(copy.getServiceCentreAddressOA(), original.getServiceCentreAddressOA());

        ISDNAddressStringImpl isdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "393385625695");
        original = new SM_RP_OAImpl();
        original.setMsisdn(isdn);
        // serialize
        out = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        // deserialize
        pickled = out.toByteArray();
        in = new ByteArrayInputStream(pickled);
        ois = new ObjectInputStream(in);
        o = ois.readObject();
        copy = (SM_RP_OAImpl) o;

        // test result
        assertEquals(copy.getMsisdn(), original.getMsisdn());
    }
}
