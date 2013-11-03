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
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.testng.annotations.Test;

/**
 * The trace is from nad1053.pcap wirehsark trace
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class InvokeTest {

    private byte[] getData() {
        return new byte[] {
                (byte) 0xa1, // Invoke Tag
                0x1d, // Length Dec 29
                0x02, 0x01,
                0x0c, // Invoke ID TAG(2) Length(1) Value(12)
                0x02, 0x01,
                0x3b, // Operation Code TAG(2), Length(1), Value(59)

                // Sequence of parameter
                0x30, 0x15,
                // Parameter 1
                0x04, 0x01, 0x0f,
                // Parameter 2
                0x04, 0x10, (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
                0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 };
    }

    private byte[] getDataFull() {
        return new byte[] { -95, 16, 2, 1, -5, -128, 1, 2, 6, 3, 40, 0, 1, 4, 3, 11, 22, 33 };
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws IOException, EncodeException {

        byte[] expected = this.getData();

        Invoke invoke = TcapFactory.createComponentInvoke();
        invoke.setInvokeId(12l);

        OperationCode oc = TcapFactory.createOperationCode();

        oc.setLocalOperationCode(59L);
        invoke.setOperationCode(oc);

        Parameter p1 = TcapFactory.createParameter();
        p1.setTagClass(Tag.CLASS_UNIVERSAL);
        p1.setTag(Tag.STRING_OCTET);
        p1.setData(new byte[] { 0x0F });

        Parameter p2 = TcapFactory.createParameter();
        p2.setTagClass(Tag.CLASS_UNIVERSAL);
        p2.setTag(Tag.STRING_OCTET);
        p2.setData(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
                0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 });

        Parameter pm = TcapFactory.createParameter();
        pm.setTagClass(Tag.CLASS_UNIVERSAL);
        pm.setTag(Tag.SEQUENCE);
        pm.setParameters(new Parameter[] { p1, p2 });
        invoke.setParameter(pm);

        AsnOutputStream asnos = new AsnOutputStream();

        invoke.encode(asnos);

        byte[] encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getDataFull();

        invoke = TcapFactory.createComponentInvoke();
        invoke.setInvokeId(-5L);
        invoke.setLinkedId(2L);
        oc = TcapFactory.createOperationCode();
        oc.setGlobalOperationCode(new long[] { 1, 0, 0, 1 });
        invoke.setOperationCode(oc);

        pm = TcapFactory.createParameter();
        pm.setTagClass(Tag.CLASS_UNIVERSAL);
        pm.setTag(Tag.STRING_OCTET);
        pm.setData(new byte[] { 11, 22, 33 });
        invoke.setParameter(pm);

        asnos = new AsnOutputStream();
        invoke.encode(asnos);

        encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));

    }

    @Test(groups = { "functional.decode" })
    public void testDecodeWithParaSequ() throws IOException, ParseException {

        byte[] b = this.getData();

        AsnInputStream asnIs = new AsnInputStream(b);

        Component comp = TcapFactory.createComponent(asnIs);
        assertEquals(ComponentType.Invoke, comp.getType());

        Invoke invokeComp = (Invoke) comp;
        assertTrue(12L == invokeComp.getInvokeId());
        OperationCode oc = invokeComp.getOperationCode();
        assertNotNull(oc);
        assertTrue(59 == oc.getLocalOperationCode());
        assertEquals(OperationCodeType.Local, oc.getOperationType());

        Parameter p = invokeComp.getParameter();
        assertEquals(Tag.CLASS_UNIVERSAL, p.getTagClass());
        assertEquals(false, p.isPrimitive());
        assertEquals(Tag.SEQUENCE, p.getTag());
        assertTrue(Arrays.equals(new byte[] { 0x04, 0x01, 0x0f, 0x04, 0x10, (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6,
                0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 },
                p.getData()));

        Parameter[] params = invokeComp.getParameter().getParameters();
        assertEquals(2, params.length);
        p = params[0];
        assertEquals(Tag.CLASS_UNIVERSAL, p.getTagClass());
        assertEquals(true, p.isPrimitive());
        assertEquals(Tag.STRING_OCTET, p.getTag());
        assertTrue(Arrays.equals(new byte[] { 0x0f }, p.getData()));
        p = params[1];
        assertEquals(Tag.CLASS_UNIVERSAL, p.getTagClass());
        assertEquals(true, p.isPrimitive());
        assertEquals(Tag.STRING_OCTET, p.getTag());
        assertTrue(Arrays.equals(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62,
                0x36, 0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }, p.getData()));

        b = this.getDataFull();
        asnIs = new AsnInputStream(b);

        comp = TcapFactory.createComponent(asnIs);
        assertEquals(ComponentType.Invoke, comp.getType());

        invokeComp = (Invoke) comp;
        assertEquals(-5L, (long) invokeComp.getInvokeId());
        assertEquals(2L, (long) invokeComp.getLinkedId());
        oc = invokeComp.getOperationCode();
        assertNotNull(oc);
        assertEquals(OperationCodeType.Global, oc.getOperationType());
        assertTrue(Arrays.equals(new long[] { 1, 0, 0, 1 }, oc.getGlobalOperationCode()));

        p = invokeComp.getParameter();
        assertEquals(Tag.CLASS_UNIVERSAL, p.getTagClass());
        assertEquals(true, p.isPrimitive());
        assertEquals(Tag.STRING_OCTET, p.getTag());
        assertTrue(Arrays.equals(new byte[] { 11, 22, 33 }, p.getData()));
    }

}
