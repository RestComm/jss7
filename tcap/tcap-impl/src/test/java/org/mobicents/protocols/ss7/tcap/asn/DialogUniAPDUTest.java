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

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class DialogUniAPDUTest {

    private byte[] getData() {
        return new byte[] { 96, 27, (byte) 128, 2, 7, (byte) 128, (byte) 161, 6, 6, 4, 4, 2, 2, 2, (byte) 190, 13, 40, 11, 6,
                4, 1, 1, 2, 3, (byte) 160, 3, 11, 22, 33 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        byte[] b = getData();
        AsnInputStream asnIs = new AsnInputStream(b);
        int tag = asnIs.readTag();
        assertEquals(0, tag);
        DialogUniAPDU d = TcapFactory.createDialogAPDUUni();
        d.decode(asnIs);
        assertTrue(Arrays.equals(new long[] { 0, 4, 2, 2, 2 }, d.getApplicationContextName().getOid()));
        UserInformation ui = d.getUserInformation();
        assertNotNull(ui);
        assertTrue(Arrays.equals(new byte[] { 11, 22, 33 }, ui.getEncodeType()));

        AsnOutputStream aos = new AsnOutputStream();
        d.encode(aos);
        assertTrue(Arrays.equals(b, aos.toByteArray()));
    }
}
