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

package org.mobicents.protocols.ss7.map.smstpdu;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class UserDataTest {

    public byte[] getData1() {
        return new byte[] { 4, 47, 0, 32, 4, 67, 0, 32, 4, 60, 4, 48, 4, 72, 4, 56, 0, 44, 4, 60, 4, 75, 0, 32, 4, 50, 0, 32,
                4, 58, 4, 56, 4, 61, 4, 62, 0, 32, 4, 63, 4, 62, 4, 57, 4, 52, 4, 81, 4, 60 };
    }

    public byte[] getData2() {
        return new byte[] { 5, 0, 3, -21, 2, 1, -88, -7, 48, 124, 94, -2, -127, -62, -96, 49, -102, -2, 6, -47, -53, -30, 124,
                24, 52, 7, -47, -13, 97, -8, -68, 12, -102, -37, -13, 97, 125, -38, 30, 46, -45, 127, -96, 118, 30, -12, -90,
                -61, -27, -31, 121, -102, -99, -98, -97, 64, 115, -112, 57, -83, -105, -93, 89, 115, 116, 120, 14, -54, -121,
                65, 115, 16, 49, 60, 71, -105, -45, -96, 57, -2, -83, -50, -125, -20, -19, -16, 57, -84, 79, -69, 65, -27, 52,
                -56, 29, 38, -65, 93, 97, 16, -4, 77, 127, -73, 65, -16, 119, -103, -100, 7, -83, 65, -52, -78, -69, -52, -54,
                -121, 65, -12, -9, -103, 28, 6, -63, -33, -16, -73, -70, 12, -94, -105, -59, 101, -48, 124, -99, 118, -25, 65 };
    }

    public byte[] getData3() {
        return new byte[] { 4, 31, 4, 32, 4, 24, 4, 18, 4, 21, 4, 34, 0, 32, 4, 31, 4, 30, 4, 20, 4, 32, 4, 35, 4, 22, 4, 21,
                4, 29, 4, 44, 4, 26, 4, 16, 0, 33, 4, 26, 4, 16, 4, 26, 0, 32, 4, 34, 4, 43, 0, 44, 4, 39, 4, 34, 4, 30, 0, 32,
                4, 29, 4, 30, 4, 18, 4, 30, 4, 19, 4, 30, 0, 63, 4, 39, 4, 21, 4, 28, 0, 32, 4, 23, 4, 16, 4, 29, 4, 24, 4, 28,
                4, 16, 4, 21, 4, 40, 4, 44, 4, 33, 4, 47, 0, 63, 4, 24, 4, 29, 4, 29, 4, 16 };
    }

    public byte[] getData4() {
        return new byte[] { 6, 8, 4, 0, -47, 3, 2, 0, 32, 4, 62, 4, 66, 0, 32, 4, 61, 4, 53, 4, 51, 4, 62, 0, 32, 4, 67, 4, 59,
                4, 53, 4, 66, 4, 56, 4, 66, 0, 32, 4, 65, 4, 48, 4, 60, 4, 75, 4, 57, 0, 32, 4, 55, 4, 48, 4, 60, 4, 53, 4, 71,
                4, 48, 4, 66, 4, 53, 4, 59, 4, 76, 4, 61, 4, 75, 4, 57, 0, 32, 4, 48, 4, 61, 4, 51, 4, 53, 4, 59, 4, 62, 4, 71,
                4, 53, 4, 58, 0, 33, 4, 31, 4, 64, 4, 62, 4, 65, 4, 66, 4, 62, 0, 32, 4, 67, 4, 59, 4, 53, 4, 66, 0, 44, 4, 48,
                0, 32, 4, 66, 4, 75, 0, 32, 4, 58, 4, 48, 4, 58 };
    }

    public byte[] getData5() {
        return new byte[] { 65, 80, 29, -28, 14, -49, 65, -8, 55, -5, 77, 118, -65, 93, 73, -48, 27, -82, 14, -45, 79, -96,
                -13, 91, -82, 14, -113, -47, -11, 60, -56, -2, 38, -41, 65, 111, -6, -102, 93, 31, -93, -45, -20, -76, -117,
                24, -42, -93, -53, 32, -15, 123, -114, 94, -41, 65, -16, 119, 59, 77, 63, -127, -36, 101, 80, -5, 125, -82,
                -69, 0 };
    }

    public byte[] getData6() {
        return new byte[] { 32, 16, 8, 4, 2, -127, 64, -81, -51, 43, -14, -38, -68, 20, 32, 16, 8, 4, 2, -127, 80, 45, 16,
                -120, 4, 2, -75, 82, 32, 16, 8, 4, 2, -127, 64, 32, 16, 8, 4, 2, -127, 64, 32, 16, 8, -92, 0, -127, 64, 32, 16,
                8, 4, 2, -127, 0 };
    }

    @Test(groups = { "functional.decode", "smstpdu" })
    public void testDecode() throws Exception {

        UserDataImpl impl = new UserDataImpl(this.getData1(), new DataCodingSchemeImpl(8), 50, false, null);
        impl.decode();
        assertEquals(impl.getDecodedMessage(),
                "\u042f \u0443 \u043c\u0430\u0448\u0438,\u043c\u044b \u0432 \u043a\u0438\u043d\u043e \u043f\u043e\u0439\u0434\u0451\u043c");
        assertNull(impl.getDecodedUserDataHeader());

        impl = new UserDataImpl(this.getData2(), new DataCodingSchemeImpl(0), 160, true, null);
        impl.decode();
        assertEquals(
                impl.getDecodedMessage(),
                "Tyapse? a chto tebya s tyapse svyazivaet? my otprasilis' s fizrh,shas ya s Dashei sxojy vmagazin ei nado.a potom poedy k Lene,ya togda popoje tebe skiny ");
        assertNotNull(impl.getDecodedUserDataHeader());
        Map<Integer, byte[]> mp = impl.getDecodedUserDataHeader().getAllData();
        assertEquals(mp.size(), 1);
        assertTrue(Arrays.equals(mp.get(0), new byte[] { -21, 2, 1 }));

        impl = new UserDataImpl(this.getData3(), new DataCodingSchemeImpl(8), 114, false, null);
        impl.decode();
        assertEquals(
                impl.getDecodedMessage(),
                "\u041f\u0420\u0418\u0412\u0415\u0422 \u041f\u041e\u0414\u0420\u0423\u0416\u0415\u041d\u042c\u041a\u0410!\u041a\u0410\u041a \u0422\u042b,\u0427\u0422\u041e \u041d\u041e\u0412\u041e\u0413\u041e?\u0427\u0415\u041c \u0417\u0410\u041d\u0418\u041c\u0410\u0415\u0428\u042c\u0421\u042f?\u0418\u041d\u041d\u0410");
        assertNull(impl.getDecodedUserDataHeader());

        impl = new UserDataImpl(this.getData4(), new DataCodingSchemeImpl(8), 139, true, null);
        impl.decode();
        assertEquals(
                impl.getDecodedMessage(),
                " \u043e\u0442 \u043d\u0435\u0433\u043e \u0443\u043b\u0435\u0442\u0438\u0442 \u0441\u0430\u043c\u044b\u0439 \u0437\u0430\u043c\u0435\u0447\u0430\u0442\u0435\u043b\u044c\u043d\u044b\u0439 \u0430\u043d\u0433\u0435\u043b\u043e\u0447\u0435\u043a!\u041f\u0440\u043e\u0441\u0442\u043e \u0443\u043b\u0435\u0442,\u0430 \u0442\u044b \u043a\u0430\u043a");
        assertNotNull(impl.getDecodedUserDataHeader());
        mp = impl.getDecodedUserDataHeader().getAllData();
        assertEquals(mp.size(), 1);
        assertTrue(Arrays.equals(mp.get(8), new byte[] { 0, -47, 3, 2 }));

        impl = new UserDataImpl(this.getData5(), new DataCodingSchemeImpl(0), 79, false, null);
        impl.decode();
        assertEquals(impl.getDecodedMessage(),
                "A u nas xolodno.I opjat' gorjachuy vodu otkluchili.Dazhe boshku pomit' ne mogu.");
        assertNull(impl.getDecodedUserDataHeader());

        impl = new UserDataImpl(this.getData6(), new DataCodingSchemeImpl(0), 63, false, null);
        impl.decode();
        assertEquals(impl.getDecodedMessage(), "        /\\_/\\\n       (-  \u00a4  -)                    \n          ");
        assertNull(impl.getDecodedUserDataHeader());

    }

    @Test(groups = { "functional.encode", "smstpdu" })
    public void testEncode() throws Exception {

        UserDataImpl impl = new UserDataImpl(
                "\u042f \u0443 \u043c\u0430\u0448\u0438,\u043c\u044b \u0432 \u043a\u0438\u043d\u043e \u043f\u043e\u0439\u0434\u0451\u043c",
                new DataCodingSchemeImpl(8), null, null);
        impl.encode();
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData1()));
        assertEquals(impl.getEncodedUserDataLength(), 50);
        assertFalse(impl.getEncodedUserDataHeaderIndicator());

        UserDataHeaderImpl udh = new UserDataHeaderImpl();
        udh.addInformationElement(0, new byte[] { -21, 2, 1 });
        impl = new UserDataImpl(
                "Tyapse? a chto tebya s tyapse svyazivaet? my otprasilis' s fizrh,shas ya s Dashei sxojy vmagazin ei nado.a potom poedy k Lene,ya togda popoje tebe skiny ",
                new DataCodingSchemeImpl(0), udh, null);
        impl.encode();
        assertTrue(impl.getEncodedUserDataHeaderIndicator());
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData2()));
        assertEquals(impl.getEncodedUserDataLength(), 160);

        impl = new UserDataImpl(
                "\u041f\u0420\u0418\u0412\u0415\u0422 \u041f\u041e\u0414\u0420\u0423\u0416\u0415\u041d\u042c\u041a\u0410!\u041a\u0410\u041a \u0422\u042b,\u0427\u0422\u041e \u041d\u041e\u0412\u041e\u0413\u041e?\u0427\u0415\u041c \u0417\u0410\u041d\u0418\u041c\u0410\u0415\u0428\u042c\u0421\u042f?\u0418\u041d\u041d\u0410",
                new DataCodingSchemeImpl(8), null, null);
        impl.encode();
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData3()));
        assertEquals(impl.getEncodedUserDataLength(), 114);

        udh = new UserDataHeaderImpl();
        udh.addInformationElement(8, new byte[] { 0, -47, 3, 2 });
        impl = new UserDataImpl(
                " \u043e\u0442 \u043d\u0435\u0433\u043e \u0443\u043b\u0435\u0442\u0438\u0442 \u0441\u0430\u043c\u044b\u0439 \u0437\u0430\u043c\u0435\u0447\u0430\u0442\u0435\u043b\u044c\u043d\u044b\u0439 \u0430\u043d\u0433\u0435\u043b\u043e\u0447\u0435\u043a!\u041f\u0440\u043e\u0441\u0442\u043e \u0443\u043b\u0435\u0442,\u0430 \u0442\u044b \u043a\u0430\u043a",
                new DataCodingSchemeImpl(8), udh, null);
        impl.encode();
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData4()));
        assertEquals(impl.getEncodedUserDataLength(), 139);

        impl = new UserDataImpl("A u nas xolodno.I opjat' gorjachuy vodu otkluchili.Dazhe boshku pomit' ne mogu.",
                new DataCodingSchemeImpl(0), null, null);
        impl.encode();
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData5()));
        assertEquals(impl.getEncodedUserDataLength(), 79);

        impl = new UserDataImpl("        /\\_/\\\n       (-  \u00a4  -)                    \n          ",
                new DataCodingSchemeImpl(0), null, null);
        impl.encode();
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData6()));
        assertEquals(impl.getEncodedUserDataLength(), 63);

    }
}
