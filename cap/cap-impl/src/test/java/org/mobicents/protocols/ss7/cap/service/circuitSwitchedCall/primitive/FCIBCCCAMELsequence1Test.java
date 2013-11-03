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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FreeFormatData;
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class FCIBCCCAMELsequence1Test {

    public byte[] getData1() {
        return new byte[] { 48, 14, (byte) 128, 4, 4, 5, 6, 7, (byte) 161, 3, (byte) 128, 1, 2, (byte) 130, 1, 1 };
    }

    public byte[] getDataFFD() {
        return new byte[] { 4, 5, 6, 7 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        FCIBCCCAMELsequence1Impl elem = new FCIBCCCAMELsequence1Impl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertTrue(Arrays.equals(elem.getFreeFormatData().getData(), this.getDataFFD()));
        assertEquals(elem.getPartyToCharge().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg2);
        FreeFormatData ffd = new FreeFormatDataImpl(getDataFFD());
        FCIBCCCAMELsequence1Impl elem = new FCIBCCCAMELsequence1Impl(ffd, partyToCharge, AppendFreeFormatData.append);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // byte[] freeFormatData, SendingSideID partyToCharge, AppendFreeFormatData appendFreeFormatData
    }
}
