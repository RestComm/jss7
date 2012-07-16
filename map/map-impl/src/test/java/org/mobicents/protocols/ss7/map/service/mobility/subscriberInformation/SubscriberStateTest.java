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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.*;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class SubscriberStateTest {

	private byte[] getEncodedData1() {
		return new byte[] { (byte) 128, 0 };
	}

	private byte[] getEncodedData2() {
		return new byte[] { (byte) 129, 0 };
	}

	private byte[] getEncodedData3() {
		return new byte[] { 10, 1, 1 };
	}

	@Test(groups = { "functional.decode","primitives"})
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData1();
		AsnInputStream asn = new AsnInputStream(rawData);
		int tag = asn.readTag();
		SubscriberStateImpl impl = new SubscriberStateImpl();
		impl.decodeAll(asn);
		assertEquals(impl.getSubscriberStateChoice(),SubscriberStateChoice.assumedIdle);
		assertNull(impl.getNotReachableReason());		
		
		rawData = getEncodedData2();
		asn = new AsnInputStream(rawData);
		tag = asn.readTag();
		impl = new SubscriberStateImpl();
		impl.decodeAll(asn);
		assertEquals(impl.getSubscriberStateChoice(),SubscriberStateChoice.camelBusy);
		assertNull(impl.getNotReachableReason());		
		
		rawData = getEncodedData3();
		asn = new AsnInputStream(rawData);
		tag = asn.readTag();
		impl = new SubscriberStateImpl();
		impl.decodeAll(asn);
		assertEquals(impl.getSubscriberStateChoice(), SubscriberStateChoice.netDetNotReachable);
		assertEquals(impl.getNotReachableReason(), NotReachableReason.imsiDetached);		
	}
	
	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {

		SubscriberStateImpl impl = new SubscriberStateImpl(SubscriberStateChoice.assumedIdle, null);
		AsnOutputStream asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS);
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData1();
		assertTrue(Arrays.equals(rawData, encodedData));		

		impl = new SubscriberStateImpl(SubscriberStateChoice.camelBusy, null);
		asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS);
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData2();
		assertTrue(Arrays.equals(rawData, encodedData));		

		impl = new SubscriberStateImpl(SubscriberStateChoice.netDetNotReachable, NotReachableReason.imsiDetached);
		asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS);
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData3();
		assertTrue(Arrays.equals(rawData, encodedData));		
	}
}
