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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class OfferedCamel4FunctionalitiesTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 4, 4, (byte) 170, 85, (byte) 192 };
        // 0, 2, 4, 6 - 0
        // 9, 11, 13, 15 - 1
        // 16, 17 - 2
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        OfferedCamel4FunctionalitiesImpl imp = new OfferedCamel4FunctionalitiesImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(imp.getInitiateCallAttempt());
        assertFalse(imp.getSplitLeg());
        assertTrue(imp.getMoveLeg());
        assertFalse(imp.getDisconnectLeg());
        assertTrue(imp.getEntityReleased());
        assertFalse(imp.getDfcWithArgument());
        assertTrue(imp.getPlayTone());
        assertFalse(imp.getDtmfMidCall());

        assertFalse(imp.getChargingIndicator());
        assertTrue(imp.getAlertingDP());
        assertFalse(imp.getLocationAtAlerting());
        assertTrue(imp.getChangeOfPositionDP());
        assertFalse(imp.getOrInteractions());
        assertTrue(imp.getWarningToneEnhancements());
        assertFalse(imp.getCfEnhancements());
        assertTrue(imp.getSubscribedEnhancedDialledServices());

        assertTrue(imp.getServingNetworkEnhancedDialledServices());
        assertTrue(imp.getCriteriaForChangeOfPositionDP());
        assertFalse(imp.getServiceChangeDP());
        assertFalse(imp.getCollectInformation());
    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        OfferedCamel4FunctionalitiesImpl imp = new OfferedCamel4FunctionalitiesImpl(true, false, true, false, true, false, true, false, false, true, false,
                true, false, true, false, true, true, true, false, false);
        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);
        assertTrue(Arrays.equals(getEncodedData(), asnOS.toByteArray()));
    }

}
