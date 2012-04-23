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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TNoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class EventSpecificInformationBCSMTest {

	public byte[] getData1() {
		return new byte[] { (byte) 162, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
	}

	public byte[] getData2() {
		return new byte[] { (byte) 163, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
	}

	public byte[] getData3() {
		return new byte[] { (byte) 164, 0 };
	}

	public byte[] getData4() {
		return new byte[] { (byte) 165, 0 };
	}

	public byte[] getData5() {
		return new byte[] { (byte) 167, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
	}

	public byte[] getData6() {
		return new byte[] { (byte) 168, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
	}

	public byte[] getData7() {
		return new byte[] { (byte) 169, 13, (byte) 159, 50, 0, (byte) 159, 52, 7, 3, (byte) 144, 33, 114, 16, (byte) 144, 0 };
	}

	public byte[] getData8() {
		return new byte[] { (byte) 170, 0 };
	}

	public byte[] getData9() {
		return new byte[] { (byte) 172, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
	}

	@Test(groups = { "functional.decode","circuitSwitchedCall.primitive"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		EventSpecificInformationBCSMImpl elem = new EventSpecificInformationBCSMImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		CauseIndicators ci = elem.getRouteSelectFailureSpecificInfo().getFailureCause().getCauseIndicators();
		assertEquals(ci.getCauseValue(), 16);
		assertEquals(ci.getCodingStandard(), 0);
		assertEquals(ci.getLocation(), 4);

		data = this.getData2();
		ais = new AsnInputStream(data);
		elem = new EventSpecificInformationBCSMImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		ci = elem.getOCalledPartyBusySpecificInfo().getBusyCause().getCauseIndicators();
		assertEquals(ci.getCauseValue(), 16);
		assertEquals(ci.getCodingStandard(), 0);
		assertEquals(ci.getLocation(), 4);

		data = this.getData3();
		ais = new AsnInputStream(data);
		elem = new EventSpecificInformationBCSMImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertNotNull(elem.getONoAnswerSpecificInfo());

		data = this.getData4();
		ais = new AsnInputStream(data);
		elem = new EventSpecificInformationBCSMImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertNotNull(elem.getOAnswerSpecificInfo());

		data = this.getData5();
		ais = new AsnInputStream(data);
		elem = new EventSpecificInformationBCSMImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		ci = elem.getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators();
		assertEquals(ci.getCauseValue(), 16);
		assertEquals(ci.getCodingStandard(), 0);
		assertEquals(ci.getLocation(), 4);

		data = this.getData6();
		ais = new AsnInputStream(data);
		elem = new EventSpecificInformationBCSMImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		ci = elem.getTBusySpecificInfo().getBusyCause().getCauseIndicators();
		assertEquals(ci.getCauseValue(), 16);
		assertEquals(ci.getCodingStandard(), 0);
		assertEquals(ci.getLocation(), 4);
		assertFalse(elem.getTBusySpecificInfo().getCallForwarded());
		assertFalse(elem.getTBusySpecificInfo().getRouteNotPermitted());
		assertNull(elem.getTBusySpecificInfo().getForwardingDestinationNumber());

		data = this.getData7();
		ais = new AsnInputStream(data);
		elem = new EventSpecificInformationBCSMImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertTrue(elem.getTNoAnswerSpecificInfo().getCallForwarded());
		CalledPartyNumber cpn = elem.getTNoAnswerSpecificInfo().getForwardingDestinationNumber().getCalledPartyNumber();
		assertFalse(cpn.isOddFlag());
		assertEquals(cpn.getNumberingPlanIndicator(), 1);
		assertEquals(cpn.getInternalNetworkNumberIndicator(), 1);
		assertEquals(cpn.getNatureOfAddressIndicator(), 3);
		assertTrue(cpn.getAddress().equals("1227010900"));

		data = this.getData8();
		ais = new AsnInputStream(data);
		elem = new EventSpecificInformationBCSMImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertNotNull(elem.getTAnswerSpecificInfo());

		data = this.getData9();
		ais = new AsnInputStream(data);
		elem = new EventSpecificInformationBCSMImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		ci = elem.getTDisconnectSpecificInfo().getReleaseCause().getCauseIndicators();
		assertEquals(ci.getCauseValue(), 16);
		assertEquals(ci.getCodingStandard(), 0);
		assertEquals(ci.getLocation(), 4);
	}

	@Test(groups = { "functional.encode","circuitSwitchedCall.primitive"})
	public void testEncode() throws Exception {

		CauseIndicators causeIndicators = new CauseIndicatorsImpl(0, 4, 16, null);
		CauseCap failureCause = new CauseCapImpl(causeIndicators);
		RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfoImpl(failureCause);
		EventSpecificInformationBCSMImpl elem = new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo);
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

		causeIndicators = new CauseIndicatorsImpl(0, 4, 16, null);
		CauseCap busyCause = new CauseCapImpl(causeIndicators);
		OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfoImpl = new OCalledPartyBusySpecificInfoImpl(busyCause);
		elem = new EventSpecificInformationBCSMImpl(oCalledPartyBusySpecificInfoImpl);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

		ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo = new ONoAnswerSpecificInfoImpl();
		elem = new EventSpecificInformationBCSMImpl(oNoAnswerSpecificInfo);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));

		OAnswerSpecificInfoImpl oAnswerSpecificInfo = new OAnswerSpecificInfoImpl();
		elem = new EventSpecificInformationBCSMImpl(oAnswerSpecificInfo);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData4()));

		causeIndicators = new CauseIndicatorsImpl(0, 4, 16, null);
		CauseCap releaseCause = new CauseCapImpl(causeIndicators);
		ODisconnectSpecificInfoImpl oDisconnectSpecificInfo = new ODisconnectSpecificInfoImpl(releaseCause);
		elem = new EventSpecificInformationBCSMImpl(oDisconnectSpecificInfo);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData5()));

		causeIndicators = new CauseIndicatorsImpl(0, 4, 16, null);
		busyCause = new CauseCapImpl(causeIndicators);
		TBusySpecificInfoImpl tBusySpecificInfo = new TBusySpecificInfoImpl(busyCause, false, false, null);
		elem = new EventSpecificInformationBCSMImpl(tBusySpecificInfo);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData6()));

		CalledPartyNumber cpn = new CalledPartyNumberImpl(3, "1227010900", 1, 1);
		// int natureOfAddresIndicator, String address, int  numberingPlanIndicator, int internalNetworkNumberIndicator
		CalledPartyNumberCap cpnc = new CalledPartyNumberCapImpl(cpn);
		TNoAnswerSpecificInfoImpl tNoAnswerSpecificInfo = new TNoAnswerSpecificInfoImpl(true, cpnc);
		elem = new EventSpecificInformationBCSMImpl(tNoAnswerSpecificInfo);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData7()));

		TAnswerSpecificInfoImpl tAnswerSpecificInfo = new TAnswerSpecificInfoImpl();
		elem = new EventSpecificInformationBCSMImpl(tAnswerSpecificInfo);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData8()));

		causeIndicators = new CauseIndicatorsImpl(0, 4, 16, null);
		releaseCause = new CauseCapImpl(causeIndicators);
		TDisconnectSpecificInfoImpl tDisconnectSpecificInfo = new TDisconnectSpecificInfoImpl(releaseCause);
		elem = new EventSpecificInformationBCSMImpl(tDisconnectSpecificInfo);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData9()));
	}
}

