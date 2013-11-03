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
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class TcUniTest {

    private byte[] data1 = new byte[] { -31, 22, -57, 0, -24, 18, -23, 16, -49, 1, 0, -47, 2, 9, 53, -14, 7, 1, 2, 3, 4, 5, 6, 7 };

    private byte[] data2 = new byte[] { -31, 27, -57, 0, -7, 3, -37, 1, 66, -24, 18, -23, 16, -49, 1, 0, -47, 2, 9, 53, -14, 7, 1, 2, 3, 4, 5, 6, 7 };

    private byte[] parData = new byte[] { 1, 2, 3, 4, 5, 6, 7 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        // 1
        AsnInputStream ais = new AsnInputStream(this.data1);
        int tag = ais.readTag();
        assertEquals(tag, TCUniMessage._TAG_UNI);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        TCUniMessage tcm = TcapFactory.createTCUniMessage(ais);

        assertNull(tcm.getDialogPortion());
        assertEquals(tcm.getComponent().length, 1);
        Component cmp = tcm.getComponent()[0];
        assertEquals(cmp.getType(), ComponentType.InvokeLast);
        Invoke inv = (Invoke) cmp;
        assertFalse(inv.isNotLast());
        assertEquals((long) inv.getInvokeId(), 0);
        assertNull(inv.getCorrelationId());
        assertEquals((long) inv.getOperationCode().getPrivateOperationCode(), 2357);
        Parameter p = inv.getParameter();
        assertEquals(p.getTag(), 18);
        assertEquals(p.getTagClass(), Tag.CLASS_PRIVATE);
        assertFalse(p.isPrimitive());
        assertEquals(p.getData(), parData);

        // 2
        ais = new AsnInputStream(this.data2);
        tag = ais.readTag();
        assertEquals(tag, TCUniMessage._TAG_UNI);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        tcm = TcapFactory.createTCUniMessage(ais);

        assertEquals(tcm.getComponent().length, 1);
        cmp = tcm.getComponent()[0];
        assertEquals(cmp.getType(), ComponentType.InvokeLast);
        inv = (Invoke) cmp;
        assertFalse(inv.isNotLast());
        assertEquals((long) inv.getInvokeId(), 0);
        assertNull(inv.getCorrelationId());
        assertEquals((long) inv.getOperationCode().getPrivateOperationCode(), 2357);
        p = inv.getParameter();
        assertEquals(p.getTag(), 18);
        assertEquals(p.getTagClass(), Tag.CLASS_PRIVATE);
        assertFalse(p.isPrimitive());
        assertEquals(p.getData(), parData);

        DialogPortion dp = tcm.getDialogPortion();
        assertNull(dp.getProtocolVersion());
        ApplicationContext ac = dp.getApplicationContext();
        assertEquals(ac.getInteger(), 66);
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        // 1
        Component[] cc = new Component[1];
        Invoke inv = TcapFactory.createComponentInvoke();
        cc[0] = inv;
        inv.setInvokeId(0L);
        inv.setNotLast(false);
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setPrivateOperationCode(2357L);
        inv.setOperationCode(oc);
        Parameter p = TcapFactory.createParameterSet();
        p.setData(parData);
        inv.setParameter(p);

        TCUniMessage tcm = TcapFactory.createTCUniMessage();
        tcm.setComponent(cc);

        AsnOutputStream aos = new AsnOutputStream();
        tcm.encode(aos);
        byte[] encodedData = aos.toByteArray();
        byte[] expectedData = data1;
        assertEquals(encodedData, expectedData);

        // 2
        tcm = TcapFactory.createTCUniMessage();
        tcm.setComponent(cc);

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
