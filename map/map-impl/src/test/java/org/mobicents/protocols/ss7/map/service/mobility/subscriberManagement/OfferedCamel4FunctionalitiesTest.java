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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
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

    @Test(groups = { "functional.decode", "service.lsm" })
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

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        OfferedCamel4FunctionalitiesImpl imp = new OfferedCamel4FunctionalitiesImpl(true, false, true, false, true, false, true, false, false, true, false,
                true, false, true, false, true, true, true, false, false);
        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);
        assertTrue(Arrays.equals(getEncodedData(), asnOS.toByteArray()));
    }

}
