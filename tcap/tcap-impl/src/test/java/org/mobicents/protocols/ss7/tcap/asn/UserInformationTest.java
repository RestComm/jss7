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
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class UserInformationTest {

    @Test(groups = { "functional.decode" })
    public void testUserInformationDecode() throws Exception {

        // This raw data is from wireshark trace of TCAP - MAP
        byte[] data = new byte[] { (byte) 0xbe, 0x25, 0x28, 0x23, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01,
                (byte) 0xa0, 0x18, (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03,
                0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98, (byte) 0x86,
                0x03, (byte) 0xf0, 0x00, 0x00 };

        AsnInputStream asin = new AsnInputStream(data);
        int tag = asin.readTag();
        assertEquals(UserInformation._TAG, tag);

        UserInformation userInformation = new UserInformationImpl();
        userInformation.decode(asin);

        assertTrue(userInformation.isOid());

        assertTrue(Arrays.equals(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 }, userInformation.getOidValue()));

        assertFalse(userInformation.isInteger());

        assertTrue(userInformation.isAsn());
        assertTrue(Arrays.equals(new byte[] { (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24,
                (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
                (byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 }, userInformation.getEncodeType()));

    }

    @Test(groups = { "functional.encode" })
    public void testUserInformationEncode() throws IOException, EncodeException {

        byte[] encodedData = new byte[] { (byte) 0xbe, 0x25, 0x28, 0x23, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01,
                (byte) 0xa0, 0x18, (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03,
                0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98, (byte) 0x86,
                0x03, (byte) 0xf0, 0x00, 0x00 };

        UserInformation userInformation = new UserInformationImpl();

        userInformation.setOid(true);
        userInformation.setOidValue(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 });

        userInformation.setAsn(true);
        userInformation.setEncodeType(new byte[] { (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24,
                (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
                (byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 });

        AsnOutputStream asnos = new AsnOutputStream();
        userInformation.encode(asnos);

        byte[] userInfData = asnos.toByteArray();

        String s = TcBeginTest.dump(userInfData, userInfData.length, false);
        assertTrue(Arrays.equals(encodedData, userInfData));

    }

    public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testFailuuure() throws Exception {
        byte[] encoded = new byte[] { -66, 15, 40, 13, 6, 7, 4, 0, 0, 1, 0, 19, 2, -126, 2, 4, -112 };

        UserInformation _ui = new UserInformationImpl();
        _ui.setArbitrary(true);
        BitSetStrictLength bs = new BitSetStrictLength(4);
        bs.set(0);
        bs.set(3);
        _ui.setEncodeBitStringType(bs);
        _ui.setAsn(false);
        _ui.setOid(true);
        _ui.setOidValue(_ACN_);
        AsnOutputStream asnOutput = new AsnOutputStream();
        _ui.encode(asnOutput);
        byte[] buf = asnOutput.toByteArray();
        assertTrue(Arrays.equals(encoded, buf));

        AsnInputStream asnInput = new AsnInputStream(buf);
        asnInput.readTag();
        UserInformationImpl _ui2 = new UserInformationImpl();
        _ui2.decode(asnInput);
        assertTrue(_ui2.isOid());
        assertTrue(_ui2.isArbitrary());
        assertTrue(Arrays.equals(_ACN_, _ui2.getOidValue()));
        BitSetStrictLength bs2 = _ui2.getEncodeBitStringType();
        assertEquals(4, bs2.getStrictLength());
        assertEquals(true, bs2.get(0));
        assertEquals(false, bs2.get(1));
        assertEquals(false, bs2.get(2));
        assertEquals(true, bs2.get(3));

    }

}
