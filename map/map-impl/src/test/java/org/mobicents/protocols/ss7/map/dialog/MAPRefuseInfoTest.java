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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextNameImpl;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPRefuseInfoTest {

    private byte[] getData() {
        return new byte[] { (byte) 0xA3, 0x03, (byte) 0x0A, 0x01, 0x00 };
    }

    private byte[] getDataFull() {
        return new byte[] { -93, 51, 10, 1, 2, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 6, 5, 42, 3, 4, 5, 6 };
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testDecode() throws Exception {
        // The raw data is from last packet of long ussd-abort from msc2.txt
        byte[] data = this.getData();

        AsnInputStream asnIs = new AsnInputStream(data);

        int tag = asnIs.readTag();
        assertEquals(tag, 3);

        MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
        mapRefuseInfoImpl.decodeAll(asnIs);

        Reason reason = mapRefuseInfoImpl.getReason();

        assertNotNull(reason);

        assertEquals(reason, Reason.noReasonGiven);

        data = this.getDataFull();
        asnIs = new AsnInputStream(data);

        tag = asnIs.readTag();
        assertEquals(tag, 3);

        mapRefuseInfoImpl = new MAPRefuseInfoImpl();
        mapRefuseInfoImpl.decodeAll(asnIs);

        reason = mapRefuseInfoImpl.getReason();
        assertNotNull(reason);
        assertEquals(reason, Reason.invalidOriginatingReference);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mapRefuseInfoImpl.getExtensionContainer()));
        assertNotNull(mapRefuseInfoImpl.getAlternativeAcn());
        assertTrue(Arrays.equals(new long[] { 1, 2, 3, 4, 5, 6 }, mapRefuseInfoImpl.getAlternativeAcn().getOid()));
    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testEncode() throws Exception {

        MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
        mapRefuseInfoImpl.setReason(Reason.noReasonGiven);
        AsnOutputStream asnOS = new AsnOutputStream();
        mapRefuseInfoImpl.encodeAll(asnOS);
        byte[] data = asnOS.toByteArray();
        assertTrue(Arrays.equals(this.getData(), data));

        mapRefuseInfoImpl = new MAPRefuseInfoImpl();
        mapRefuseInfoImpl.setReason(Reason.invalidOriginatingReference);
        ApplicationContextName acn = new ApplicationContextNameImpl();
        acn.setOid(new long[] { 1, 2, 3, 4, 5, 6 });
        mapRefuseInfoImpl.setAlternativeAcn(acn);
        mapRefuseInfoImpl.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
        asnOS = new AsnOutputStream();
        mapRefuseInfoImpl.encodeAll(asnOS);
        data = asnOS.toByteArray();
        assertTrue(Arrays.equals(this.getDataFull(), data));

    }

}
