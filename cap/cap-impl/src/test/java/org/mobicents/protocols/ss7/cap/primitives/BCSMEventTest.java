/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DpSpecificCriteriaImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class BCSMEventTest {

    public byte[] getData1() {
        return new byte[] { 48, 11, (byte) 128, 1, 6, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 19, (byte) 128, 1, 5, (byte) 129, 1, 1, (byte) 162, 3, (byte) 129, 1, 1, (byte) 190, 3,
                (byte) 129, 1, 111, (byte) 159, 50, 0 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        BCSMEventImpl elem = new BCSMEventImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
        assertEquals(elem.getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getLegID().getSendingSideID(), LegType.leg2);
        assertNull(elem.getDpSpecificCriteria());
        assertFalse(elem.getAutomaticRearm());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new BCSMEventImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
        assertEquals(elem.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertEquals(elem.getLegID().getReceivingSideID(), LegType.leg1);
        assertEquals((int) elem.getDpSpecificCriteria().getApplicationTimer(), 111);
        assertTrue(elem.getAutomaticRearm());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        LegIDImpl legID = new LegIDImpl(true, LegType.leg2);
        BCSMEventImpl elem = new BCSMEventImpl(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, legID, null, false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        legID = new LegIDImpl(false, LegType.leg1);
        DpSpecificCriteriaImpl dsc = new DpSpecificCriteriaImpl(111);
        elem = new BCSMEventImpl(EventTypeBCSM.oCalledPartyBusy, MonitorMode.notifyAndContinue, legID, dsc, true);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        // EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID, DpSpecificCriteria dpSpecificCriteria, boolean
        // automaticRearm
    }
}
