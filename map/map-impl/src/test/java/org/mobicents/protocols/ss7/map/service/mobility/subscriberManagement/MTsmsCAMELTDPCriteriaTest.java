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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class MTsmsCAMELTDPCriteriaTest {

    public byte[] getData() {
        return new byte[] { 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        MTsmsCAMELTDPCriteriaImpl prim = new MTsmsCAMELTDPCriteriaImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = prim.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        MTSMSTPDUType one = tPDUTypeCriterion.get(0);
        assertNotNull(one);
        assertEquals(one, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType two = tPDUTypeCriterion.get(1);
        assertNotNull(two);
        assertEquals(two, MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(prim.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        SMSTriggerDetectionPoint smsTriggerDetectionPoint = SMSTriggerDetectionPoint.smsCollectedInfo;
        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = new ArrayList<MTSMSTPDUType>();
        tPDUTypeCriterion.add(MTSMSTPDUType.smsDELIVER);
        tPDUTypeCriterion.add(MTSMSTPDUType.smsSTATUSREPORT);

        MTsmsCAMELTDPCriteriaImpl prim = new MTsmsCAMELTDPCriteriaImpl(smsTriggerDetectionPoint, tPDUTypeCriterion);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
