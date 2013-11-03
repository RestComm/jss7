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

package org.mobicents.protocols.ss7.map.smstpdu;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AbsoluteTimeStampTest {

    public byte[] getData() {
        return new byte[] { 112, 80, 81, 81, 0, 20, 33 };
    }

    @Test(groups = { "functional.decode", "smstpdu" })
    public void testDecode() throws Exception {

        InputStream stm = new ByteArrayInputStream(this.getData());
        AbsoluteTimeStampImpl impl = AbsoluteTimeStampImpl.createMessage(stm);
        assertEquals(impl.getYear(), 7);
        assertEquals(impl.getMonth(), 5);
        assertEquals(impl.getDay(), 15);
        assertEquals(impl.getHour(), 15);
        assertEquals(impl.getMinute(), 0);
        assertEquals(impl.getSecond(), 41);
        assertEquals(impl.getTimeZone(), 12);
    }

    @Test(groups = { "functional.encode", "smstpdu" })
    public void testEncode() throws Exception {

        AbsoluteTimeStampImpl impl = new AbsoluteTimeStampImpl(7, 5, 15, 15, 0, 41, 12);
        ByteArrayOutputStream stm = new ByteArrayOutputStream();
        impl.encodeData(stm);
        assertTrue(Arrays.equals(stm.toByteArray(), this.getData()));
    }
}
