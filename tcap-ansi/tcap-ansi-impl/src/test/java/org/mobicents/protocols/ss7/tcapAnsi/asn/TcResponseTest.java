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
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCResponseMessage;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

@Test(groups = { "asn" })
public class TcResponseTest {

    private byte[] data1 = new byte[] { (byte) 0xe4, 0x3c, (byte) 0xc7, 0x04, 0x14, 0x00, 0x00, 0x00, (byte) 0xe8, 0x34, (byte) 0xea, 0x32, (byte) 0xcf, 0x01,
            0x01, (byte) 0xf2, 0x2d, (byte) 0x96, 0x01, 0x13, (byte) 0x8e, 0x02, 0x06, 0x00, (byte) 0x95, 0x03, 0x00, 0x0c, 0x10, (byte) 0x9f, 0x4e, 0x01,
            0x01, (byte) 0x99, 0x03, 0x7a, 0x0d, 0x11, (byte) 0x9f, 0x5d, 0x07, 0x00, 0x00, 0x21, 0x06, 0x36, 0x54, 0x10, (byte) 0x97, 0x01, 0x07, (byte) 0x9f,
            0x73, 0x01, 0x00, (byte) 0x9f, 0x75, 0x01, 0x00, (byte) 0x98, 0x01, 0x02 };

    private byte[] data2 = new byte[] { -28, 11, -57, 4, 20, 0, 0, 0, -7, 3, -37, 1, 66 };

    private byte[] trId = new byte[] { 20, 0, 0, 0 };

    private byte[] parData = new byte[] { -106, 1, 19, -114, 2, 6, 0, -107, 3, 0, 12, 16, -97, 78, 1, 1, -103, 3, 122, 13, 17, -97, 93, 7, 0, 0, 33, 6, 54, 84,
            16, -105, 1, 7, -97, 115, 1, 0, -97, 117, 1, 0, -104, 1, 2 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        // 1
        AsnInputStream ais = new AsnInputStream(this.data1);
        int tag = ais.readTag();
        assertEquals(tag, TCResponseMessage._TAG_RESPONSE);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        TCResponseMessage tcm = TcapFactory.createTCResponseMessage(ais);

        assertEquals(tcm.getDestinationTransactionId(), trId);
        assertNull(tcm.getDialogPortion());
        assertEquals(tcm.getComponent().length, 1);
        Component cmp = tcm.getComponent()[0];
        assertEquals(cmp.getType(), ComponentType.ReturnResultLast);
        ReturnResultLast rrl = (ReturnResultLast) cmp;
        assertEquals((long) rrl.getCorrelationId(), 1);
        Parameter p = rrl.getParameter();
        assertEquals(p.getTag(), 18);
        assertEquals(p.getTagClass(), Tag.CLASS_PRIVATE);
        assertFalse(p.isPrimitive());
        assertEquals(p.getData(), parData);

        // 2
        ais = new AsnInputStream(this.data2);
        tag = ais.readTag();
        assertEquals(tag, TCResponseMessage._TAG_RESPONSE);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        tcm = TcapFactory.createTCResponseMessage(ais);

        assertEquals(tcm.getDestinationTransactionId(), trId);
        DialogPortion dp = tcm.getDialogPortion();
        assertNull(dp.getProtocolVersion());
        ApplicationContext ac = dp.getApplicationContext();
        assertEquals(ac.getInteger(), 66);
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());

        assertNull(tcm.getComponent());

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        // 1
        Component[] cc = new Component[1];
        ReturnResultLast rrl = TcapFactory.createComponentReturnResultLast();
        cc[0] = rrl;
        rrl.setCorrelationId(1L);
        Parameter p = TcapFactory.createParameterSet();
        p.setData(parData);
        rrl.setParameter(p);

        TCResponseMessage tcm = TcapFactory.createTCResponseMessage();
        tcm.setDestinationTransactionId(trId);
        tcm.setComponent(cc);

        AsnOutputStream aos = new AsnOutputStream();
        tcm.encode(aos);
        byte[] encodedData = aos.toByteArray();
        byte[] expectedData = data1;
        assertEquals(encodedData, expectedData);

        // 2
        tcm = TcapFactory.createTCResponseMessage();
        tcm.setDestinationTransactionId(trId);

        DialogPortion dp = TcapFactory.createDialogPortion();
        ApplicationContext ac = TcapFactory.createApplicationContext(66);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        aos = new AsnOutputStream();
        tcm.encode(aos);
        encodedData = aos.toByteArray();
        expectedData = data2;
        assertEquals(encodedData, expectedData);

    }

}

