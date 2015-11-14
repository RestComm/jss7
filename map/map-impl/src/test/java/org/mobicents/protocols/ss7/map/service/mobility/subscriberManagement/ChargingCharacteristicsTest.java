/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.*;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class ChargingCharacteristicsTest {

    public byte[] getData1() {
        return new byte[] { 4, 2, 8, 0 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 2, 4, 0 };
    };

    public byte[] getData3() {
        return new byte[] { 4, 2, 2, 0 };
    };

    public byte[] getData4() {
        return new byte[] { 4, 2, 1, 0 };
    };

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {
        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ChargingCharacteristicsImpl prim = new ChargingCharacteristicsImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.isNormalCharging());
        assertFalse(prim.isPrepaidCharging());
        assertFalse(prim.isFlatRateChargingCharging());
        assertFalse(prim.isChargingByHotBillingCharging());


        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ChargingCharacteristicsImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(prim.isNormalCharging());
        assertTrue(prim.isPrepaidCharging());
        assertFalse(prim.isFlatRateChargingCharging());
        assertFalse(prim.isChargingByHotBillingCharging());


        data = this.getData3();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ChargingCharacteristicsImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(prim.isNormalCharging());
        assertFalse(prim.isPrepaidCharging());
        assertTrue(prim.isFlatRateChargingCharging());
        assertFalse(prim.isChargingByHotBillingCharging());


        data = this.getData4();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ChargingCharacteristicsImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(prim.isNormalCharging());
        assertFalse(prim.isPrepaidCharging());
        assertFalse(prim.isFlatRateChargingCharging());
        assertTrue(prim.isChargingByHotBillingCharging());
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {
        ChargingCharacteristicsImpl prim = new ChargingCharacteristicsImpl(true, false, false, false);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertEquals(asn.toByteArray(), this.getData1());


        prim = new ChargingCharacteristicsImpl(false, true, false, false);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertEquals(asn.toByteArray(), this.getData2());


        prim = new ChargingCharacteristicsImpl(false, false, true, false);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertEquals(asn.toByteArray(), this.getData3());


        prim = new ChargingCharacteristicsImpl(false, false, false, true);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertEquals(asn.toByteArray(), this.getData4());
    }

}
