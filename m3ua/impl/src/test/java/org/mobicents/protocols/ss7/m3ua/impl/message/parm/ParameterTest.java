/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.m3ua.impl.message.parm;

import org.mobicents.protocols.ss7.m3ua.impl.message.parms.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.parms.ParameterFactoryImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class ParameterTest {

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    public ParameterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private short getTag(byte[] data) {
        return (short) ((data[0] & 0xff) << 8 | (data[1] & 0xff));
    }

    private short getLen(byte[] data) {
        return (short) ((data[2] & 0xff) << 8 | (data[3] & 0xff));
    }

    private byte[] getValue(byte[] data) {
        byte[] value = new byte[data.length - 4];
        System.arraycopy(data, 4, value, 0, value.length);
        return value;
    }
    /**
     * Test of getOpc method, of class ProtocolDataImpl.
     */
    @Test
    public void testProtocolData1() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ProtocolDataImpl p1 = factory.createProtocolData(1408, 14150, 1, 1, 0, 0, new byte[]{1, 2, 3, 4});
        p1.encode(out);

        byte[] data = out.toByteArray();

        ProtocolDataImpl p2 = (ProtocolDataImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p1.getSI(), p2.getSI());
        assertEquals(p1.getNI(), p2.getNI());
        assertEquals(p1.getMP(), p2.getMP());
        assertEquals(p1.getSLS(), p2.getSLS());

        boolean isDataCorrect = Arrays.equals(p1.getData(), p2.getData());
        assertTrue("Data mismatch", isDataCorrect);
    }

}