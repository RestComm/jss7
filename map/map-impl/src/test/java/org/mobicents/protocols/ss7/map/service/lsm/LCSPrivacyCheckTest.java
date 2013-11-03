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
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class LCSPrivacyCheckTest {
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

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = new byte[] { 0x30, 0x06, (byte) 0x80, 0x01, 0x00, (byte) 0x81, 0x01, 0x02 };

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        LCSPrivacyCheckImpl lcsPrivacyCheck = new LCSPrivacyCheckImpl();
        lcsPrivacyCheck.decodeAll(asn);

        assertEquals(lcsPrivacyCheck.getCallSessionUnrelated(), PrivacyCheckRelatedAction.allowedWithoutNotification);
        assertEquals(lcsPrivacyCheck.getCallSessionRelated(), PrivacyCheckRelatedAction.allowedIfNoResponse);

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { 0x30, 0x06, (byte) 0x80, 0x01, 0x00, (byte) 0x81, 0x01, 0x02 };

        PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithoutNotification;
        PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedIfNoResponse;

        LCSPrivacyCheckImpl lcsPrivacyCheck = new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);
        AsnOutputStream asnOS = new AsnOutputStream();
        lcsPrivacyCheck.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.lsm" })
    public void testSerialization() throws Exception {
        PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithoutNotification;
        PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedIfNoResponse;

        LCSPrivacyCheckImpl original = new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);

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
        LCSPrivacyCheckImpl copy = (LCSPrivacyCheckImpl) o;

        // test result
        assertEquals(copy, original);
    }
}
