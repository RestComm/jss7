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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAI_GSM0224;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SCIBillingChargingCharacteristicsTest {

    public byte[] getData1() {
        return new byte[] { 4, 29, (byte) 160, 27, (byte) 160, 9, (byte) 128, 1, 1, (byte) 129, 1, 2, (byte) 130, 1, 3,
                (byte) 161, 14, (byte) 160, 9, (byte) 128, 1, 1, (byte) 129, 1, 2, (byte) 130, 1, 3, (byte) 129, 1, 100 };
    }

    public byte[] getData2() {
        return new byte[] { 4, 16, (byte) 161, 14, (byte) 160, 9, (byte) 128, 1, 1, (byte) 129, 1, 2, (byte) 130, 1, 3,
                (byte) 129, 1, 100 };
    }

    public byte[] getData3() {
        return new byte[] { 4, 2, (byte) 162, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        SCIBillingChargingCharacteristicsImpl elem = new SCIBillingChargingCharacteristicsImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        this.testCAI_GSM0224(elem.getAOCBeforeAnswer().getAOCInitial());
        this.testCAI_GSM0224(elem.getAOCBeforeAnswer().getAOCSubsequent().getCAI_GSM0224());
        assertEquals((int) elem.getAOCBeforeAnswer().getAOCSubsequent().getTariffSwitchInterval(), 100);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new SCIBillingChargingCharacteristicsImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        this.testCAI_GSM0224(elem.getAOCSubsequent().getCAI_GSM0224());
        assertEquals((int) elem.getAOCSubsequent().getTariffSwitchInterval(), 100);

        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new SCIBillingChargingCharacteristicsImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertNotNull(elem.getAOCExtension());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        CAI_GSM0224Impl gsm224 = new CAI_GSM0224Impl(1, 2, 3, null, null, null, null);
        AOCSubsequentImpl aocSubsequent = new AOCSubsequentImpl(gsm224, 100);
        AOCBeforeAnswerImpl aocBeforeAnswer = new AOCBeforeAnswerImpl(gsm224, aocSubsequent);
        // CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent
        SCIBillingChargingCharacteristicsImpl elem = new SCIBillingChargingCharacteristicsImpl(aocBeforeAnswer);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        elem = new SCIBillingChargingCharacteristicsImpl(aocSubsequent);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        CAMELSCIBillingChargingCharacteristicsAltImpl aocExtension = new CAMELSCIBillingChargingCharacteristicsAltImpl();
        elem = new SCIBillingChargingCharacteristicsImpl(aocExtension);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));
    }

    private void testCAI_GSM0224(CAI_GSM0224 gsm224) {
        assertEquals((int) gsm224.getE1(), 1);
        assertEquals((int) gsm224.getE2(), 2);
        assertEquals((int) gsm224.getE3(), 3);
        assertNull(gsm224.getE4());
        assertNull(gsm224.getE5());
        assertNull(gsm224.getE6());
        assertNull(gsm224.getE7());
    }
}
