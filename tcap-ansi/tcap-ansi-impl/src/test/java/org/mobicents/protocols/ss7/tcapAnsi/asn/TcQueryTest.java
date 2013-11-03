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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class TcQueryTest {

    // no DialogPortion, 1 Invoke
    private byte[] data1 = new byte[] { (byte) 0xe2, 0x33, (byte) 0xc7, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xe8, 0x2b, (byte) 0xe9, 0x29, (byte) 0xcf, 0x01,
            0x00, (byte) 0xd1, 0x02, 0x09, 0x35, (byte) 0xf2, 0x20, (byte) 0x9f, 0x69, 0x00, (byte) 0x9f, 0x74, 0x00, (byte) 0x9f, (byte) 0x81, 0x00, 0x01,
            0x08, (byte) 0x88, 0x05, 0x16, 0x19, 0x32, 0x04, 0x00, (byte) 0x9f, (byte) 0x81, 0x41, 0x01, 0x01, (byte) 0x9f, (byte) 0x81, 0x43, 0x05, 0x22,
            0x22, 0x22, 0x22, 0x22 };

    // DialogPortion[Version + ACN], ReturnResult + ReturnError
    private byte[] data2 = new byte[] { -29, 42, -57, 4, 3, 3, 4, 4, -7, 7, -38, 1, 3, -36, 2, 48, 11, -24, 25, -22, 12, -49, 1, 1, -14, 7, 1, 2, 3, 4, 5, 6,
            7, -21, 9, -49, 1, 1, -44, 2, 0, -56, -14, 0 };

    // DialogPortion[ACN], no components
    private byte[] data3 = new byte[] { -29, 11, -57, 4, 3, 3, 4, 4, -7, 3, -37, 1, 66 };

    // 1 good component, 1 bad component
    private byte[] data4 = new byte[] { (byte) 227, 34, (byte) 199, 4, 3, 3, 4, 4, (byte) 232, 26, (byte) 234, 13, (byte) 207, 2, 1, 0, (byte) 242, 7, 1, 2, 3,
            4, 5, 6, 7, (byte) 235, 9, (byte) 207, 1, 1, -44, 2, 0, (byte) 200, (byte) 242, 0 };

    private byte[] trId = new byte[] { 0, 0, 0, 0 };
    private byte[] trId2 = new byte[] { 3, 3, 4, 4 };

    private byte[] parData = new byte[] { -97, 105, 0, -97, 116, 0, -97, -127, 0, 1, 8, -120, 5, 22, 25, 50, 4, 0, -97, -127, 65, 1, 1, -97, -127, 67, 5, 34,
            34, 34, 34, 34 };

    private byte[] parData2 = new byte[] { 1, 2, 3, 4, 5, 6, 7 };

    private long[] acn = new long[] { 1, 8, 11 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        // 1
        AsnInputStream ais = new AsnInputStream(this.data1);
        int tag = ais.readTag();
        assertEquals(tag, TCQueryMessage._TAG_QUERY_WITH_PERM);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        TCQueryMessage tcm = TcapFactory.createTCQueryMessage(ais);

        assertTrue(tcm.getDialogTermitationPermission());
        assertEquals(tcm.getOriginatingTransactionId(), trId);
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
        assertEquals(tag, TCQueryMessage._TAG_QUERY_WITHOUT_PERM);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        tcm = TcapFactory.createTCQueryMessage(ais);

        assertFalse(tcm.getDialogTermitationPermission());
        assertEquals(tcm.getOriginatingTransactionId(), trId2);
        DialogPortion dp = tcm.getDialogPortion();
        ProtocolVersion pv = dp.getProtocolVersion();
        assertTrue(pv.isT1_114_1996Supported());
        assertTrue(pv.isT1_114_2000Supported());
        ApplicationContext ac = dp.getApplicationContext();
        assertEquals(ac.getOid(), acn);
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());

        assertEquals(tcm.getComponent().length, 2);
        cmp = tcm.getComponent()[0];
        assertEquals(cmp.getType(), ComponentType.ReturnResultLast);
        ReturnResultLast rrl = (ReturnResultLast) cmp;
        assertEquals((long)rrl.getCorrelationId(), 1);
        p = rrl.getParameter();
        assertEquals(p.getTag(), 18);
        assertEquals(p.getTagClass(), Tag.CLASS_PRIVATE);
        assertFalse(p.isPrimitive());
        assertEquals(p.getData(), parData2);

        cmp = tcm.getComponent()[1];
        assertEquals(cmp.getType(), ComponentType.ReturnError);
        ReturnError re = (ReturnError) cmp;
        assertEquals((long) re.getCorrelationId(), 1);
        ErrorCode ec = re.getErrorCode();
        assertEquals((long) ec.getPrivateErrorCode(), 200);
        p = re.getParameter();
        assertEquals(p.getTag(), 18);
        assertEquals(p.getTagClass(), Tag.CLASS_PRIVATE);
        assertFalse(p.isPrimitive());
        assertEquals(p.getData().length, 0);

        // 3
        ais = new AsnInputStream(this.data3);
        tag = ais.readTag();
        assertEquals(tag, TCQueryMessage._TAG_QUERY_WITHOUT_PERM);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        tcm = TcapFactory.createTCQueryMessage(ais);

        assertFalse(tcm.getDialogTermitationPermission());
        assertEquals(tcm.getOriginatingTransactionId(), trId2);
        dp = tcm.getDialogPortion();
        assertNull(dp.getProtocolVersion());
        ac = dp.getApplicationContext();
        assertEquals(ac.getInteger(), 66);
        assertNull(dp.getConfidentiality());
        assertNull(dp.getSecurityContext());
        assertNull(dp.getUserInformation());

        assertNull(tcm.getComponent());

        // 4
        ais = new AsnInputStream(this.data4);
        tag = ais.readTag();
        assertEquals(tag, TCQueryMessage._TAG_QUERY_WITHOUT_PERM);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        tcm = TcapFactory.createTCQueryMessage(ais);

        assertFalse(tcm.getDialogTermitationPermission());
        assertEquals(tcm.getOriginatingTransactionId(), trId2);
        assertNull(tcm.getDialogPortion());

        assertEquals(tcm.getComponent().length, 2);
        cmp = tcm.getComponent()[0];
        assertEquals(cmp.getType(), ComponentType.Reject);
        Reject rej = (Reject) cmp;
        assertTrue(rej.isLocalOriginated());
        assertNull(rej.getCorrelationId());
        assertEquals(rej.getProblem(), RejectProblem.generalBadlyStructuredCompPortion);

        cmp = tcm.getComponent()[1];
        assertEquals(cmp.getType(), ComponentType.ReturnError);
        re = (ReturnError) cmp;
        assertEquals((long) re.getCorrelationId(), 1);
        ec = re.getErrorCode();
        assertEquals((long) ec.getPrivateErrorCode(), 200);
        p = re.getParameter();
        assertEquals(p.getTag(), 18);
        assertEquals(p.getTagClass(), Tag.CLASS_PRIVATE);
        assertFalse(p.isPrimitive());
        assertEquals(p.getData().length, 0);
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

        TCQueryMessage tcm = TcapFactory.createTCQueryMessage();
        tcm.setOriginatingTransactionId(trId);
        tcm.setComponent(cc);
        tcm.setDialogTermitationPermission(true);

        AsnOutputStream aos = new AsnOutputStream();
        tcm.encode(aos);
        byte[] encodedData = aos.toByteArray();
        byte[] expectedData = data1;
        assertEquals(encodedData, expectedData);

        // 2
        cc = new Component[2];
        ReturnResultLast rr = TcapFactory.createComponentReturnResultLast();
        cc[0] = rr;
        rr.setCorrelationId(1L);
        p = TcapFactory.createParameterSet();
        p.setData(parData2);
        rr.setParameter(p);
        ReturnError re = TcapFactory.createComponentReturnError();
        cc[1] = re;
        re.setCorrelationId(1L);
        ErrorCode ec = TcapFactory.createErrorCode();
        ec.setPrivateErrorCode(200L);
        re.setErrorCode(ec);
        p = TcapFactory.createParameterSet();
        re.setParameter(p);

        tcm = TcapFactory.createTCQueryMessage();
        tcm.setOriginatingTransactionId(trId2);
        tcm.setComponent(cc);
        tcm.setDialogTermitationPermission(false);

        DialogPortion dp = TcapFactory.createDialogPortion();
        ProtocolVersion pv = TcapFactory.createProtocolVersionFull();
        dp.setProtocolVersion(pv);
        ApplicationContext ac = TcapFactory.createApplicationContext(acn);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        aos = new AsnOutputStream();
        tcm.encode(aos);
        encodedData = aos.toByteArray();
        expectedData = data2;
        assertEquals(encodedData, expectedData);

        // 3
        tcm = TcapFactory.createTCQueryMessage();
        tcm.setOriginatingTransactionId(trId2);
        tcm.setDialogTermitationPermission(false);

        dp = TcapFactory.createDialogPortion();
        ac = TcapFactory.createApplicationContext(66);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        aos = new AsnOutputStream();
        tcm.encode(aos);
        encodedData = aos.toByteArray();
        expectedData = data3;
        assertEquals(encodedData, expectedData);
    }

}
