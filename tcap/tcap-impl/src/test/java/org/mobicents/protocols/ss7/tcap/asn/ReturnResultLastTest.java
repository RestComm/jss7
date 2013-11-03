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

package org.mobicents.protocols.ss7.tcap.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class ReturnResultLastTest {

    private byte[] getLDataEmpty() {
        return new byte[] { (byte) 162, 3, 2, 1, 0 };
    }

    private byte[] getNLDataEmpty() {
        return new byte[] { (byte) 167, 3, 2, 1, 0 };
    }

    private byte[] getLDataCommon() {
        return new byte[] { (byte) 162, 19, 2, 1, 1, 48, 14, 2, 1, 45, 48, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    private byte[] getNLDataCommon() {
        return new byte[] { (byte) 167, 19, 2, 1, 1, 48, 14, 2, 1, 45, 48, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    private byte[] getParameterData() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecodeWithParaSequ() throws IOException, ParseException {

        byte[] b = this.getLDataEmpty();
        AsnInputStream asnIs = new AsnInputStream(b);
        Component comp = TcapFactory.createComponent(asnIs);
        assertEquals(ComponentType.ReturnResultLast, comp.getType());

        ReturnResultLast rrl = (ReturnResultLast) comp;
        assertTrue(0L == rrl.getInvokeId());
        OperationCode oc = rrl.getOperationCode();
        assertNull(oc);
        Parameter p = rrl.getParameter();
        assertNull(p);

        b = this.getNLDataEmpty();
        asnIs = new AsnInputStream(b);
        comp = TcapFactory.createComponent(asnIs);
        assertEquals(ComponentType.ReturnResult, comp.getType());

        ReturnResult rr = (ReturnResult) comp;
        assertTrue(0L == rr.getInvokeId());
        oc = rr.getOperationCode();
        assertNull(oc);
        p = rr.getParameter();
        assertNull(p);

        b = this.getLDataCommon();
        asnIs = new AsnInputStream(b);
        comp = TcapFactory.createComponent(asnIs);
        assertEquals(ComponentType.ReturnResultLast, comp.getType());

        rrl = (ReturnResultLast) comp;
        assertTrue(1L == rrl.getInvokeId());
        oc = rrl.getOperationCode();
        assertNotNull(oc);
        assertTrue(45 == oc.getLocalOperationCode());
        p = rrl.getParameter();
        assertNotNull(p);
        assertEquals(Tag.CLASS_UNIVERSAL, p.getTagClass());
        assertEquals(false, p.isPrimitive());
        assertEquals(Tag.SEQUENCE, p.getTag());
        assertTrue(Arrays.equals(this.getParameterData(), p.getData()));

        b = this.getNLDataCommon();
        asnIs = new AsnInputStream(b);
        comp = TcapFactory.createComponent(asnIs);
        assertEquals(ComponentType.ReturnResult, comp.getType());

        rr = (ReturnResult) comp;
        assertTrue(1L == rr.getInvokeId());
        oc = rr.getOperationCode();
        assertNotNull(oc);
        assertTrue(45 == oc.getLocalOperationCode());
        p = rr.getParameter();
        assertNotNull(p);
        assertEquals(Tag.CLASS_UNIVERSAL, p.getTagClass());
        assertEquals(false, p.isPrimitive());
        assertEquals(Tag.SEQUENCE, p.getTag());
        assertTrue(Arrays.equals(this.getParameterData(), p.getData()));
    }

    @Test(groups = { "functional.decode" })
    public void testEncode() throws IOException, EncodeException {

        byte[] expected = this.getLDataEmpty();
        ReturnResultLast rrl = TcapFactory.createComponentReturnResultLast();
        rrl.setInvokeId(0l);

        AsnOutputStream asnos = new AsnOutputStream();
        rrl.encode(asnos);
        byte[] encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getNLDataEmpty();
        ReturnResult rr = TcapFactory.createComponentReturnResult();
        rr.setInvokeId(0l);

        asnos = new AsnOutputStream();
        rr.encode(asnos);
        encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getLDataCommon();
        rrl = TcapFactory.createComponentReturnResultLast();
        rrl.setInvokeId(1l);
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode(45L);
        rrl.setOperationCode(oc);
        Parameter pm = TcapFactory.createParameter();
        pm.setTagClass(Tag.CLASS_UNIVERSAL);
        pm.setTag(Tag.SEQUENCE);
        pm.setPrimitive(false);
        pm.setData(getParameterData());
        rrl.setParameter(pm);

        asnos = new AsnOutputStream();
        rrl.encode(asnos);
        encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getNLDataCommon();
        rr = TcapFactory.createComponentReturnResult();
        rr.setInvokeId(1l);
        oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode(45L);
        rr.setOperationCode(oc);
        pm = TcapFactory.createParameter();
        pm.setTagClass(Tag.CLASS_UNIVERSAL);
        pm.setTag(Tag.SEQUENCE);
        pm.setPrimitive(false);
        pm.setData(getParameterData());
        rr.setParameter(pm);

        asnos = new AsnOutputStream();
        rr.encode(asnos);
        encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));
    }

}
