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

package org.mobicents.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DateAndTimeTest {

    public byte[] getData1() {
        return new byte[] { (byte) 129, 7, 2, 17, 33, 3, 1, 112, (byte) 129 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        DateAndTimeImpl elem = new DateAndTimeImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getYear(), 2011);
        assertEquals(elem.getMonth(), 12);
        assertEquals(elem.getDay(), 30);
        assertEquals(elem.getHour(), 10);
        assertEquals(elem.getMinute(), 7);
        assertEquals(elem.getSecond(), 18);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        DateAndTimeImpl elem = new DateAndTimeImpl(2011, 12, 30, 10, 7, 18);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 1);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }
}
