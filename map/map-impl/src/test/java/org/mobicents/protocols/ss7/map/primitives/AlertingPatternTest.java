/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.primitives;

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
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingLevel;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class AlertingPatternTest {
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

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = new byte[] { (byte) 0x04, 0x01, 0x07 };

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        AlertingPatternImpl addNum = new AlertingPatternImpl();
        addNum.decodeAll(asn);
        assertNull(addNum.getAlertingLevel());
        assertNotNull(addNum.getAlertingCategory());

        assertEquals(addNum.getAlertingCategory(), AlertingCategory.Category4);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { (byte) 0x04, 0x01, 0x07 };

        AlertingPatternImpl addNum = new AlertingPatternImpl(AlertingCategory.Category4);

        AsnOutputStream asnOS = new AsnOutputStream();
        addNum.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "primitives" })
    public void testSerialization() throws Exception {
        AlertingPatternImpl original = new AlertingPatternImpl(AlertingCategory.Category4);
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
        AlertingPatternImpl copy = (AlertingPatternImpl) o;

        // test result
        assertEquals(copy.getAlertingCategory(), original.getAlertingCategory());
        assertEquals(copy, original);
        assertNull(copy.getAlertingLevel());

        original = new AlertingPatternImpl(AlertingLevel.Level1);
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
        copy = (AlertingPatternImpl) o;

        // test result
        assertEquals(copy.getAlertingLevel(), original.getAlertingLevel());
        assertEquals(copy, original);
        assertNull(copy.getAlertingCategory());
    }
}
