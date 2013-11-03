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

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
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
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class LCSClientIDTest {
    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getData() {
        return new byte[] { (byte) 0xa0, 0x1b, (byte) 0x80, 0x01, 0x02, (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x13,
                (byte) 0x80, 0x01, 0x0f, (byte) 0x82, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65,
                0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 };
    }

    public byte[] getDataFull() {
        return new byte[] { -96, 69, -128, 1, 2, -95, 7, -128, 5, -111, 68, 51, 34, 17, -126, 6, -111, 85, 68, 51, 34, 17,
                -125, 1, 0, -92, 19, -128, 1, 15, -126, 14, 110, 114, -5, 28, -122, -61, 101, 110, 114, -5, 28, -122, -61, 101,
                -123, 2, 11, 12, -90, 19, -128, 1, 15, -127, 14, 110, 114, -5, 28, -122, -61, 101, 110, 114, -5, 28, -122, -61,
                101 };
    }

    public byte[] getDataAPN() {
        return new byte[] { 11, 12 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, 0);

        LCSClientIDImpl lcsClientID = new LCSClientIDImpl();
        lcsClientID.decodeAll(asn);

        assertNotNull(lcsClientID.getLCSClientType());
        assertEquals(lcsClientID.getLCSClientType(), LCSClientType.plmnOperatorServices);

        assertNotNull(lcsClientID.getLCSClientInternalID());
        assertEquals(lcsClientID.getLCSClientInternalID(), LCSClientInternalID.broadcastService);

        assertNull(lcsClientID.getLCSClientExternalID());
        assertNull(lcsClientID.getLCSClientDialedByMS());
        assertNull(lcsClientID.getLCSAPN());
        assertNull(lcsClientID.getLCSRequestorID());

        LCSClientName lcsClientName = lcsClientID.getLCSClientName();
        assertNotNull(lcsClientName);
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        USSDString nameString = lcsClientName.getNameString();
        assertTrue(nameString.getString(null).equals("ndmgapp2ndmgapp2"));

        data = getDataFull();

        asn = new AsnInputStream(data);
        tag = asn.readTag();

        lcsClientID = new LCSClientIDImpl();
        lcsClientID.decodeAll(asn);

        assertNotNull(lcsClientID.getLCSClientType());
        assertEquals(lcsClientID.getLCSClientType(), LCSClientType.plmnOperatorServices);

        assertNotNull(lcsClientID.getLCSClientInternalID());
        assertEquals(lcsClientID.getLCSClientInternalID(), LCSClientInternalID.broadcastService);

        lcsClientName = lcsClientID.getLCSClientName();
        assertNotNull(lcsClientName);
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        nameString = lcsClientName.getNameString();
        assertEquals(nameString.getString(null), "ndmgapp2ndmgapp2");

        assertTrue(lcsClientID.getLCSClientExternalID().getExternalAddress().getAddress().equals("44332211"));
        assertTrue(lcsClientID.getLCSClientDialedByMS().getAddress().equals("5544332211"));
        assertTrue(Arrays.equals(lcsClientID.getLCSAPN().getData(), getDataAPN()));
        assertEquals(lcsClientID.getLCSRequestorID().getDataCodingScheme().getCode(), 0x0f);
        assertTrue(lcsClientID.getLCSRequestorID().getRequestorIDString().getString(null).equals("ndmgapp2ndmgapp2"));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        byte[] data = getData();

        USSDString nameString = MAPParameterFactory.createUSSDString("ndmgapp2ndmgapp2");
        LCSClientName lcsClientName = new LCSClientNameImpl(new CBSDataCodingSchemeImpl(0x0f), nameString, null);

        LCSClientIDImpl lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null,
                LCSClientInternalID.broadcastService, lcsClientName, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        lcsClientID.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        data = getDataFull();

        ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "44332211");
        LCSClientExternalID extId = new LCSClientExternalIDImpl(externalAddress, null);
        AddressStringImpl clientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "5544332211");
        APNImpl apn = new APNImpl(getDataAPN());
        LCSRequestorIDImpl reqId = new LCSRequestorIDImpl(new CBSDataCodingSchemeImpl(0x0f), nameString, null);

        lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, extId, LCSClientInternalID.broadcastService,
                lcsClientName, clientDialedByMS, apn, reqId);

        asnOS = new AsnOutputStream();
        lcsClientID.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.lsm" })
    public void testSerialization() throws Exception {
        USSDString nameString = MAPParameterFactory.createUSSDString("ndmgapp2ndmgapp2");
        LCSClientName lcsClientName = new LCSClientNameImpl(new CBSDataCodingSchemeImpl(0x0f), nameString, null);

        LCSClientIDImpl original = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null,
                LCSClientInternalID.broadcastService, lcsClientName, null, null, null);
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
        LCSClientIDImpl copy = (LCSClientIDImpl) o;

        // test result
        assertEquals(copy, original);
    }
}
