/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.TCAPTestUtils;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcapAnsi.asn.ApplicationContextImpl;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TCUniMessageImpl;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class TcUnidirectionalTest {

    private byte[] getData() {
        return new byte[] { 97, 45, 107, 27, 40, 25, 6, 7, 0, 17, -122, 5, 1, 2, 1, -96, 14, 96, 12, -128, 2, 7, -128, -95, 6,
                6, 4, 40, 2, 3, 4, 108, 14, -95, 12, 2, 1, -128, 2, 2, 2, 79, 4, 3, 1, 2, 3 };
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws IOException, EncodeException {

//        byte[] expected = getData();
//
//        TCUniMessageImpl tcUniMessage = new TCUniMessageImpl();
//
//        DialogPortion dp = TcapFactory.createDialogPortion();
//        dp.setUnidirectional(true);
//        DialogRequestAPDU dapdu = TcapFactory.createDialogAPDURequest();
//        ApplicationContext acn = new ApplicationContextImpl();
//        acn.setOid(new long[] { 1, 0, 2, 3, 4 });
//        dapdu.setApplicationContextName(acn);
//        dp.setDialogAPDU(dapdu);
//        tcUniMessage.setDialogPortion(dp);
//
//        Invoke invComp = TcapFactory.createComponentInvoke();
//        invComp.setInvokeId(-128l);
//        OperationCode oc = TcapFactory.createOperationCode();
//        oc.setLocalOperationCode(591L);
//        invComp.setOperationCode(oc);
//        Parameter p = TcapFactory.createParameter();
//        p.setTagClass(Tag.CLASS_UNIVERSAL);
//        p.setTag(Tag.STRING_OCTET);
//        p.setData(new byte[] { 1, 2, 3 });
//        invComp.setParameter(p);
//        tcUniMessage.setComponent(new Component[] { invComp });
//
//        AsnOutputStream aos = new AsnOutputStream();
//        tcUniMessage.encode(aos);
//        byte[] data = aos.toByteArray();
//        TCAPTestUtils.compareArrays(expected, data);
    }

    @Test(groups = { "functional.decode" })
    public void testDencode() throws IOException, ParseException {

//        byte[] b = this.getData();
//
//        AsnInputStream ais = new AsnInputStream(b);
//        int tag = ais.readTag();
//        assertEquals(TCUniMessage._TAG, tag);
//        TCUniMessage tcm = TcapFactory.createTCUniMessage(ais);
//
//        DialogPortion dp = tcm.getDialogPortion();
//        Component[] comp = tcm.getComponent();
//
//        assertNotNull(dp);
//        assertNotNull(dp.getDialogAPDU());
//        assertEquals(true, dp.isUnidirectional());
//        assertEquals(DialogAPDUType.UniDirectional, dp.getDialogAPDU().getType());
//
//        assertNotNull(comp);
//        assertEquals(1, comp.length);
    }
}
