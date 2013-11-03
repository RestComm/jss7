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

package org.mobicents.protocols.ss7.map.dialog;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPSimpleDialogTest {

    private byte[] getDataAcceptInfo() {
        return new byte[] { -95, 41, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getDataCloseInfo() {
        return new byte[] { -94, 41, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testDecode() throws Exception {

        AsnInputStream asnIs = new AsnInputStream(this.getDataAcceptInfo());

        int tag = asnIs.readTag();
        assertEquals(tag, 1);

        MAPAcceptInfoImpl accInfo = new MAPAcceptInfoImpl();
        accInfo.decodeAll(asnIs);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(accInfo.getExtensionContainer()));

        asnIs = new AsnInputStream(this.getDataCloseInfo());

        tag = asnIs.readTag();
        assertEquals(tag, 2);

        MAPCloseInfoImpl closeInfo = new MAPCloseInfoImpl();
        closeInfo.decodeAll(asnIs);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(closeInfo.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testEncode() throws Exception {

        byte[] b = this.getDataAcceptInfo();
        MAPAcceptInfoImpl accInfo = new MAPAcceptInfoImpl();
        accInfo.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        AsnOutputStream asnOS = new AsnOutputStream();
        accInfo.encodeAll(asnOS);
        byte[] data = asnOS.toByteArray();
        assertTrue(Arrays.equals(b, data));

        b = this.getDataCloseInfo();
        MAPCloseInfoImpl closeInfo = new MAPCloseInfoImpl();
        closeInfo.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();
        closeInfo.encodeAll(asnOS);
        data = asnOS.toByteArray();
        assertTrue(Arrays.equals(b, data));
    }

}
