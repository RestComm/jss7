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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import static org.testng.Assert.*;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultNotLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class ReturnResultTest {

    private byte[] data1 = new byte[] { (byte) 0xea, 0x1d, (byte) 0xcf, 0x01, 0x00, (byte) 0xf2, 0x18, (byte) 0x89, 0x04, (byte) 0xfe, 0x3a, 0x2f, (byte) 0xe5,
            (byte) 0x9f, (byte) 0x81, 0x38, 0x05, 0x00, 0x00, 0x00, 0x26, 0x31, (byte) 0x95, 0x03, 0x00, 0x0c, 0x06, (byte) 0x9f, 0x31, 0x01, 0x00 };

    private byte[] data2 = new byte[] { -18, 5, -49, 1, -1, -14, 0 };

    private byte[] parData = new byte[] { (byte) 0x89, 0x04, (byte) 0xfe, 0x3a, 0x2f, (byte) 0xe5, (byte) 0x9f, (byte) 0x81, 0x38, 0x05, 0x00, 0x00, 0x00,
            0x26, 0x31, (byte) 0x95, 0x03, 0x00, 0x0c, 0x06, (byte) 0x9f, 0x31, 0x01, 0x00 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        // 1
        AsnInputStream ais = new AsnInputStream(this.data1);
        int tag = ais.readTag();
        assertEquals(tag, ReturnResultLast._TAG_RETURN_RESULT_LAST);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        ReturnResultLast rrl = TcapFactory.createComponentReturnResultLast();
        rrl.decode(ais);

        assertEquals((long) rrl.getCorrelationId(), 0);
        Parameter p = rrl.getParameter();
        assertEquals(p.getTag(), 18);
        assertEquals(p.getTagClass(), Tag.CLASS_PRIVATE);
        assertFalse(p.isPrimitive());
        assertEquals(p.getData(), parData);

        // 2
        ais = new AsnInputStream(this.data2);
        tag = ais.readTag();
        assertEquals(tag, ReturnResultNotLast._TAG_RETURN_RESULT_NOT_LAST);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        ReturnResultNotLast rrnl = TcapFactory.createComponentReturnResultNotLast();
        rrnl.decode(ais);

        assertEquals((long) rrnl.getCorrelationId(), -1);
        p = rrnl.getParameter();
        assertEquals(p.getTag(), 18);
        assertEquals(p.getTagClass(), Tag.CLASS_PRIVATE);
        assertFalse(p.isPrimitive());
        assertEquals(p.getData(), new byte[0]);
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        // 1
        ReturnResultLast rrl = TcapFactory.createComponentReturnResultLast();
        rrl.setCorrelationId(0L);
        Parameter p = TcapFactory.createParameterSet();
        p.setData(parData);
        rrl.setParameter(p);

        AsnOutputStream aos = new AsnOutputStream();
        rrl.encode(aos);
        byte[] encodedData = aos.toByteArray();
        byte[] expectedData = data1;
        assertEquals(encodedData, expectedData);

        // 2
        ReturnResultNotLast rrnl = TcapFactory.createComponentReturnResultNotLast();
        rrnl.setCorrelationId(-1L);
        p = TcapFactory.createParameterSet();
        rrnl.setParameter(p);

        aos = new AsnOutputStream();
        rrnl.encode(aos);
        encodedData = aos.toByteArray();
        expectedData = data2;
        assertEquals(encodedData, expectedData);
    }
}
