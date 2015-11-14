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

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

public class OfferedCamel4CSIsTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 2, 1, (byte) 148 };
    }

//    private byte[] getEncodedData2() {
//        // short form - without the first bit string bit
//        return new byte[] { 3, 1, (byte) 148 };
//    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        OfferedCamel4CSIsImpl imp = new OfferedCamel4CSIsImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(imp.getOCsi());
        assertFalse(imp.getDCsi());
        assertFalse(imp.getVtCsi());
        assertTrue(imp.getTCsi());
        assertFalse(imp.getMtSmsCsi());
        assertTrue(imp.getMgCsi());
        assertFalse(imp.getPsiEnhancements());


//        rawData = getEncodedData2();
//        asn = new AsnInputStream(rawData);
//
//        tag = asn.readTag();
//        imp = new OfferedCamel4CSIsImpl();
//        imp.decodeAll(asn);
//
//        assertEquals(tag, Tag.STRING_BIT);
//        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
//
//        assertTrue(imp.getOCsi());
//        assertFalse(imp.getDCsi());
//        assertFalse(imp.getVtCsi());
//        assertTrue(imp.getTCsi());
//        assertFalse(imp.getMtSmsCsi());
//        assertTrue(imp.getMgCsi());
//        assertFalse(imp.getPsiEnhancements());
    }

    private void assertTrue(boolean initiateCallAttempt) {
        // TODO Auto-generated method stub
        
    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        OfferedCamel4CSIsImpl imp = new OfferedCamel4CSIsImpl(true, false, false, true, false, true, false);
//        boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi, boolean mgCsi,
//        boolean psiEnhancements

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);
        assertTrue(Arrays.equals(getEncodedData(), asnOS.toByteArray()));
    }

}
