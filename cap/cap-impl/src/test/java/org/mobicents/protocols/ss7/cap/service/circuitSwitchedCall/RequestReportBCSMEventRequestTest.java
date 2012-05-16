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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.primitives.BCSMEventImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class RequestReportBCSMEventRequestTest {

	public byte[] getData1() {
		return new byte[] { 48, 93, (byte) 160, 91, 48, 11, (byte) 128, 1, 4, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 5,
				(byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 6, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11,
				(byte) 128, 1, 7, (byte) 129, 1, 1, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 9, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1,
				1, 48, 11, (byte) 128, 1, 9, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 10, (byte) 129, 1, 1, (byte) 162, 3,
				(byte) 128, 1, 1 };
	}

	public byte[] getData2() {
		return new byte[] { 48, 113, (byte) 160, 91, 48, 11, (byte) 128, 1, 4, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 5,
				(byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 6, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11,
				(byte) 128, 1, 7, (byte) 129, 1, 1, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 9, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1,
				1, 48, 11, (byte) 128, 1, 9, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 10, (byte) 129, 1, 1, (byte) 162, 3,
				(byte) 128, 1, 1, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
	}

	@Test(groups = { "functional.decode","circuitSwitchedCall"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		RequestReportBCSMEventRequestImpl elem = new RequestReportBCSMEventRequestImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals(elem.getBCSMEventList().size(), 7);
		assertEquals(elem.getBCSMEventList().get(0).getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
		assertEquals(elem.getBCSMEventList().get(0).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(0).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(1).getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
		assertEquals(elem.getBCSMEventList().get(1).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(1).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(2).getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
		assertEquals(elem.getBCSMEventList().get(2).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(2).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(3).getEventTypeBCSM(), EventTypeBCSM.oAnswer);
		assertEquals(elem.getBCSMEventList().get(3).getMonitorMode(), MonitorMode.notifyAndContinue);
		assertEquals(elem.getBCSMEventList().get(3).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(4).getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
		assertEquals(elem.getBCSMEventList().get(4).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(4).getLegID().getSendingSideID(), LegType.leg1);
		assertEquals(elem.getBCSMEventList().get(5).getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
		assertEquals(elem.getBCSMEventList().get(5).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(5).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(6).getEventTypeBCSM(), EventTypeBCSM.oAbandon);
		assertEquals(elem.getBCSMEventList().get(6).getMonitorMode(), MonitorMode.notifyAndContinue);
		assertEquals(elem.getBCSMEventList().get(6).getLegID().getSendingSideID(), LegType.leg1);

		data = this.getData2();
		ais = new AsnInputStream(data);
		elem = new RequestReportBCSMEventRequestImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals(elem.getBCSMEventList().size(), 7);
		assertEquals(elem.getBCSMEventList().get(0).getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
		assertEquals(elem.getBCSMEventList().get(0).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(0).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(1).getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
		assertEquals(elem.getBCSMEventList().get(1).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(1).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(2).getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
		assertEquals(elem.getBCSMEventList().get(2).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(2).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(3).getEventTypeBCSM(), EventTypeBCSM.oAnswer);
		assertEquals(elem.getBCSMEventList().get(3).getMonitorMode(), MonitorMode.notifyAndContinue);
		assertEquals(elem.getBCSMEventList().get(3).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(4).getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
		assertEquals(elem.getBCSMEventList().get(4).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(4).getLegID().getSendingSideID(), LegType.leg1);
		assertEquals(elem.getBCSMEventList().get(5).getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
		assertEquals(elem.getBCSMEventList().get(5).getMonitorMode(), MonitorMode.interrupted);
		assertEquals(elem.getBCSMEventList().get(5).getLegID().getSendingSideID(), LegType.leg2);
		assertEquals(elem.getBCSMEventList().get(6).getEventTypeBCSM(), EventTypeBCSM.oAbandon);
		assertEquals(elem.getBCSMEventList().get(6).getMonitorMode(), MonitorMode.notifyAndContinue);
		assertEquals(elem.getBCSMEventList().get(6).getLegID().getSendingSideID(), LegType.leg1);
		assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
	}

	@Test(groups = { "functional.encode","circuitSwitchedCall"})
	public void testEncode() throws Exception {
		
		ArrayList<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
		LegIDImpl legID = new LegIDImpl(true, LegType.leg2);
		BCSMEventImpl be = new BCSMEventImpl(EventTypeBCSM.routeSelectFailure, MonitorMode.interrupted, legID, null, false);
		bcsmEventList.add(be);
		legID = new LegIDImpl(true, LegType.leg2);
		be = new BCSMEventImpl(EventTypeBCSM.oCalledPartyBusy, MonitorMode.interrupted, legID, null, false);
		bcsmEventList.add(be);
		legID = new LegIDImpl(true, LegType.leg2);
		be = new BCSMEventImpl(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, legID, null, false);
		bcsmEventList.add(be);
		legID = new LegIDImpl(true, LegType.leg2);
		be = new BCSMEventImpl(EventTypeBCSM.oAnswer, MonitorMode.notifyAndContinue, legID, null, false);
		bcsmEventList.add(be);
		legID = new LegIDImpl(true, LegType.leg1);
		be = new BCSMEventImpl(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, legID, null, false);
		bcsmEventList.add(be);
		legID = new LegIDImpl(true, LegType.leg2);
		be = new BCSMEventImpl(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, legID, null, false);
		bcsmEventList.add(be);
		legID = new LegIDImpl(true, LegType.leg1);
		be = new BCSMEventImpl(EventTypeBCSM.oAbandon, MonitorMode.notifyAndContinue, legID, null, false);
		bcsmEventList.add(be);
		// EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID, DpSpecificCriteria dpSpecificCriteria, boolean automaticRearm

		RequestReportBCSMEventRequestImpl elem = new RequestReportBCSMEventRequestImpl(bcsmEventList, null);
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

		elem = new RequestReportBCSMEventRequestImpl(bcsmEventList, CAPExtensionsTest.createTestCAPExtensions());
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
	}
}

