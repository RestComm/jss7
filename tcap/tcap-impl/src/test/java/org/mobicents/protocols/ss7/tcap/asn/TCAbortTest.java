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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.TCAPTestUtils;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class TCAbortTest {

    private byte[] getDataDialogPort() {
        return new byte[] { 0x67, 0x2E, 0x49, 0x04, 0x7B, (byte) 0xA5, 0x34, 0x13, 0x6B, 0x26, 0x28, 0x24, 0x06, 0x07, 0x00,
                0x11, (byte) 0x86, 0x05, 0x01, 0x01, 0x01, (byte) 0xA0, 0x19, 0x64, 0x17, (byte) 0x80, 0x01, 0x00, (byte) 0xBE,
                0x12, 0x28, 0x10, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01, (byte) 0xA0, 0x05, (byte) 0xA3, 0x03,
                0x0A, 0x01, 0x00 };
    }

    private byte[] getBadEncodedMAPUserAbortInfo() {
        return new byte[] { 103, 44, 73, 4, 0, 1, 0, 20, 107, 36, 40, 34, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 23, 100, 21,
                -128, 1, 0, -66, 16, 40, 14, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 3, -92, 1, -128 };
    }

    private byte[] getDataAbortCause() {
        return new byte[] { 103, 9, 73, 4, 123, -91, 52, 19, 74, 1, 126 };
    }

    private byte[] getDestTrId() {
        return new byte[] { 0x7B, (byte) 0xA5, 0x34, 0x13 };
    }

    @Test(groups = { "functional.encode" })
    public void testBasicTCAbortTestEncode() throws IOException, EncodeException, ParseException {

        // This Raw data is taken from ussd-abort- from msc2.txt
        byte[] expected = getDataDialogPort();

        TCAbortMessageImpl tcAbortMessage = new TCAbortMessageImpl();
        tcAbortMessage.setDestinationTransactionId(getDestTrId());

        DialogPortion dp = TcapFactory.createDialogPortion();
        dp.setUnidirectional(false);
        DialogAbortAPDU dapdu = TcapFactory.createDialogAPDUAbort();
        AbortSource as = TcapFactory.createAbortSource();
        as.setAbortSourceType(AbortSourceType.User);
        dapdu.setAbortSource(as);

        UserInformationImpl userInformation = new UserInformationImpl();
        userInformation.setOid(true);
        userInformation.setOidValue(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 });

        userInformation.setAsn(true);
        userInformation.setEncodeType(new byte[] { (byte) 0xA3, 0x03, (byte) 0x0A, 0x01, 0x00 });

        dapdu.setUserInformation(userInformation);

        dp.setDialogAPDU(dapdu);

        tcAbortMessage.setDialogPortion(dp);

        AsnOutputStream aos = new AsnOutputStream();

        tcAbortMessage.encode(aos);

        byte[] data = aos.toByteArray();

        System.out.println(dump(data, data.length, false));

        TCAPTestUtils.compareArrays(expected, data);

        expected = getDataAbortCause();

        tcAbortMessage = new TCAbortMessageImpl();
        tcAbortMessage.setDestinationTransactionId(getDestTrId());
        tcAbortMessage.setPAbortCause(PAbortCauseType.AbnormalDialogue);

        aos = new AsnOutputStream();
        tcAbortMessage.encode(aos);
        data = aos.toByteArray();

        TCAPTestUtils.compareArrays(expected, data);

    }

    @Test(groups = { "functional.decode" })
    public void testBasicTCAbortTestDecode() throws IOException, ParseException {

        // This Raw data is taken from ussd-abort- from msc2.txt
        byte[] data = getDataDialogPort();

        AsnInputStream ais = new AsnInputStream(data);
        int tag = ais.readTag();
        assertEquals(TCAbortMessage._TAG, tag, "Expected TCAbort");

        TCAbortMessageImpl impl = new TCAbortMessageImpl();
        impl.decode(ais);

        assertTrue(Arrays.equals(impl.getDestinationTransactionId(), getDestTrId()));

        DialogPortion dp = impl.getDialogPortion();

        assertNotNull(dp);
        assertFalse(dp.isUnidirectional());

        DialogAPDU dialogApdu = dp.getDialogAPDU();

        assertNotNull(dialogApdu);

        data = getDataAbortCause();
        ais = new AsnInputStream(data);
        tag = ais.readTag();
        assertEquals(TCAbortMessage._TAG, tag, "Expected TCAbort");

        impl = new TCAbortMessageImpl();
        impl.decode(ais);

        assertTrue(Arrays.equals(impl.getDestinationTransactionId(), getDestTrId()));
        // assertTrue(2074424339 == impl.getDestinationTransactionId());

        dp = impl.getDialogPortion();
        assertNull(dp);
        assertEquals(PAbortCauseType.AbnormalDialogue, impl.getPAbortCause());
    }

    public static final String dump(byte[] buff, int size, boolean asBits) {
        String s = "";
        for (int i = 0; i < size; i++) {
            String ss = null;
            if (!asBits) {
                ss = Integer.toHexString(buff[i] & 0xff);
            } else {
                ss = Integer.toBinaryString(buff[i] & 0xff);
            }
            ss = fillInZeroPrefix(ss, asBits);
            s += " " + ss;
        }
        return s;
    }

    public static final String fillInZeroPrefix(String ss, boolean asBits) {
        if (asBits) {
            if (ss.length() < 8) {
                for (int j = ss.length(); j < 8; j++) {
                    ss = "0" + ss;
                }
            }
        } else {
            // hex
            if (ss.length() < 2) {

                ss = "0" + ss;
            }
        }

        return ss;
    }

}
