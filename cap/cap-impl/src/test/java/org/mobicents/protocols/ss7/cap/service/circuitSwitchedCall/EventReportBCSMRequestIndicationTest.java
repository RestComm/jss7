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
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.ReceivingSideIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.mobicents.protocols.ss7.inap.primitives.MiscCallInfoImpl;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class EventReportBCSMRequestIndicationTest {

	public byte[] getData1() {
		return new byte[] { 48, 13, (byte) 128, 1, 9, (byte) 163, 3, (byte) 129, 1, 1, (byte) 164, 3, (byte) 128, 1, 0 };
	}

	public byte[] getData2() {
		return new byte[] { 48, 16, (byte) 128, 1, 4, (byte) 162, 6, (byte) 162, 4, (byte) 128, 2, (byte) 132, (byte) 144, (byte) 163, 3, (byte) 129, 1, 2 };
	}

	public byte[] getData3() {
		return new byte[] { 48, 41, (byte) 128, 1, 4, (byte) 162, 6, (byte) 162, 4, (byte) 128, 2, (byte) 132, (byte) 144, (byte) 163, 3, (byte) 129, 1, 2,
				(byte) 164, 3, (byte) 128, 1, 0, (byte) 165, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
	}

	public byte[] getDataFailureCause() {
		return new byte[] { -124, -112 };
	}

	@Test(groups = { "functional.decode","circuitSwitchedCall"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		EventReportBCSMRequestIndicationImpl elem = new EventReportBCSMRequestIndicationImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
		assertEquals(elem.getLegID().getReceivingSideID(), LegType.leg1);
		assertEquals(elem.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.request);
		assertNull(elem.getEventSpecificInformationBCSM());
		assertNull(elem.getExtensions());

		data = this.getData2();
		ais = new AsnInputStream(data);
		elem = new EventReportBCSMRequestIndicationImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
		assertTrue(Arrays.equals(elem.getEventSpecificInformationBCSM().getRouteSelectFailureSpecificInfo().getFailureCause().getData(), getDataFailureCause()));
		assertEquals(elem.getLegID().getReceivingSideID(), LegType.leg2);
		assertNull(elem.getMiscCallInfo());
		assertNull(elem.getExtensions());

		data = this.getData3();
		ais = new AsnInputStream(data);
		elem = new EventReportBCSMRequestIndicationImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
		assertTrue(Arrays.equals(elem.getEventSpecificInformationBCSM().getRouteSelectFailureSpecificInfo().getFailureCause().getData(), getDataFailureCause()));
		assertEquals(elem.getLegID().getReceivingSideID(), LegType.leg2);
		assertEquals(elem.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.request);
		assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
	}

	@Test(groups = { "functional.encode","circuitSwitchedCall"})
	public void testEncode() throws Exception {

		ReceivingSideIDImpl legID = new ReceivingSideIDImpl(LegType.leg1);
		MiscCallInfoImpl miscCallInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.request, null);

		EventReportBCSMRequestIndicationImpl elem = new EventReportBCSMRequestIndicationImpl(EventTypeBCSM.oDisconnect, null, legID, miscCallInfo, null);
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
		
		
		CauseCapImpl failureCause = new CauseCapImpl(getDataFailureCause());
		RouteSelectFailureSpecificInfoImpl routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfoImpl(failureCause);
		EventSpecificInformationBCSMImpl eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo);
		legID = new ReceivingSideIDImpl(LegType.leg2);

		elem = new EventReportBCSMRequestIndicationImpl(EventTypeBCSM.routeSelectFailure, eventSpecificInformationBCSM, legID, null, null);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));


		elem = new EventReportBCSMRequestIndicationImpl(EventTypeBCSM.routeSelectFailure, eventSpecificInformationBCSM, legID, miscCallInfo,
				CAPExtensionsTest.createTestCAPExtensions());
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));
		
//		EventTypeBCSM eventTypeBCSM, EventSpecificInformationBCSM eventSpecificInformationBCSM, ReceivingSideID legID,
//		MiscCallInfo miscCallInfo, CAPExtensions extensions
	}
}

