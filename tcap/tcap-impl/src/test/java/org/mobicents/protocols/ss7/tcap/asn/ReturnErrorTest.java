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
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class ReturnErrorTest {

    private byte[] getDataWithoutParameter() {
        return new byte[] {
                // 0xA3 - Return ReturnError TAG
                (byte) 0xA3,
                // 0x06 - Len
                0x06,
                // 0x02 - InvokeID Tag
                0x02,
                // 0x01 - Len
                0x01,
                // 0x05
                0x05,
                // 0x02 - ReturnError Code Tag
                0x02,
                // 0x01 - Len
                0x01,
                // 0x0F
                0x0F };
    }

    private byte[] getDataWithParameter() {
        return new byte[] {
                // 0xA3 - Return ReturnError TAG
                (byte) 0xA3,
                // 0x06 - Len
                0x19,
                // 0x02 - InvokeID Tag
                0x02,
                // 0x01 - Len
                0x01,
                // 0x05
                0x05,
                // 0x02 - ReturnError Code Tag
                0x02,
                // 0x01 - Len
                0x01,
                // 0x0F
                0x0F,
                // parameter
                (byte) 0xA0,// some tag.1
                17, (byte) 0x80,// some tag.1.1
                2, 0x11, 0x11, (byte) 0xA1,// some tag.1.2
                04, (byte) 0x82, // some tag.1.3 ?
                2, 0x00, 0x00, (byte) 0x82, 1,// some tag.1.4
                12, (byte) 0x83, // some tag.1.5
                2, 0x33, 0x33, // some trash here

        };
    }

    private byte[] getDataLongErrorCode() {
        return new byte[] { -93, 8, 2, 1, -1, 6, 3, 40, 22, 33 };
    }

    private byte[] getParameterData() {
        return new byte[] { -128, 2, 17, 17, -95, 4, -126, 2, 0, 0, -126, 1, 12, -125, 2, 51, 51 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws IOException, ParseException {

        byte[] b = getDataWithoutParameter();
        AsnInputStream asnIs = new AsnInputStream(b);
        Component comp = TcapFactory.createComponent(asnIs);

        assertEquals(ComponentType.ReturnError, comp.getType(), "Wrong component Type");
        ReturnError re = (ReturnError) comp;
        assertEquals(new Long(5), re.getInvokeId(), "Wrong invoke ID");
        assertNotNull(re.getErrorCode(), "No error code.");
        ErrorCode ec = re.getErrorCode();
        assertEquals(ErrorCodeType.Local, ec.getErrorType(), "Wrong error code type.");
        long lec = ec.getLocalErrorCode();
        assertEquals(lec, 15, "wrong data content.");
        assertNull(re.getParameter());

        b = getDataWithParameter();
        asnIs = new AsnInputStream(b);
        comp = TcapFactory.createComponent(asnIs);

        assertEquals(ComponentType.ReturnError, comp.getType(), "Wrong component Type");
        re = (ReturnError) comp;
        assertEquals(new Long(5), re.getInvokeId(), "Wrong invoke ID");
        assertNotNull(re.getErrorCode(), "No error code.");
        ec = re.getErrorCode();
        assertEquals(ErrorCodeType.Local, ec.getErrorType(), "Wrong error code type.");
        lec = ec.getLocalErrorCode();
        assertEquals(lec, 15, "wrong data content.");

        assertNotNull(re.getParameter(), "Parameter should not be null");
        Parameter p = re.getParameter();
        assertEquals(0x00, p.getTag(), "Wrong parameter tag."); // 0x00 - since A is for tag class etc.
        assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, p.getTagClass(), "Wrong parameter tagClass.");
        assertNotNull(p.getParameters(), "Parameters array is null.");
        assertEquals(4, p.getParameters().length, "Wrong number of parameters in array.");
        assertTrue(Arrays.equals(this.getParameterData(), p.getData()));

        b = getDataLongErrorCode();
        asnIs = new AsnInputStream(b);
        comp = TcapFactory.createComponent(asnIs);

        assertEquals(ComponentType.ReturnError, comp.getType(), "Wrong component Type");
        re = (ReturnError) comp;
        assertEquals(new Long(-1L), re.getInvokeId(), "Wrong invoke ID");
        assertNotNull(re.getErrorCode(), "No error code.");
        ec = re.getErrorCode();
        assertEquals(ErrorCodeType.Global, ec.getErrorType(), "Wrong error code type.");
        long[] gec = ec.getGlobalErrorCode();
        assertTrue(Arrays.equals(new long[] { 1, 0, 22, 33 }, gec), "wrong data content.");
        assertNull(re.getParameter());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws IOException, EncodeException {

        byte[] expected = this.getDataWithoutParameter();
        ReturnError re = TcapFactory.createComponentReturnError();
        re.setInvokeId(5l);
        ErrorCode ec = TcapFactory.createErrorCode();
        ec.setLocalErrorCode(15L);
        re.setErrorCode(ec);

        AsnOutputStream asnos = new AsnOutputStream();
        re.encode(asnos);
        byte[] encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getDataWithParameter();
        re = TcapFactory.createComponentReturnError();
        re.setInvokeId(5l);
        ec = TcapFactory.createErrorCode();
        ec.setLocalErrorCode(15L);
        re.setErrorCode(ec);
        Parameter pm = TcapFactory.createParameter();
        pm.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
        pm.setTag(0);
        pm.setPrimitive(false);
        pm.setData(getParameterData());
        re.setParameter(pm);

        asnos = new AsnOutputStream();
        re.encode(asnos);
        encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getDataLongErrorCode();
        re = TcapFactory.createComponentReturnError();
        re.setInvokeId(-1L);
        ec = TcapFactory.createErrorCode();
        ec.setGlobalErrorCode(new long[] { 1, 0, 22, 33 });
        re.setErrorCode(ec);

        asnos = new AsnOutputStream();
        re.encode(asnos);
        encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));
    }
}
