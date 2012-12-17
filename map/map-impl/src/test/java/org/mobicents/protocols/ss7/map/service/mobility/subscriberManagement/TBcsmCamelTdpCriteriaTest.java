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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class TBcsmCamelTdpCriteriaTest {
	
	public byte[] getData() {
		return new byte[] {48, 19, 10, 1, 13, -96, 6, -126, 1, 22, -125, 1, 16, -95, 6, 4, 1, 7, 4, 1, 6 };
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		TBcsmCamelTdpCriteriaImpl prim = new TBcsmCamelTdpCriteriaImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
		
		assertEquals(prim.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
		
		assertNotNull(prim.getBasicServiceCriteria());
		assertEquals(prim.getBasicServiceCriteria().size(),2);
		ExtBasicServiceCode basicServiceOne = prim.getBasicServiceCriteria().get(0);
		assertNotNull(basicServiceOne);
		assertEquals(basicServiceOne.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);
		
		ExtBasicServiceCode basicServiceTwo =  prim.getBasicServiceCriteria().get(1);
		assertNotNull(basicServiceTwo);
		assertEquals(basicServiceTwo.getExtTeleservice().getTeleserviceCodeValue(),TeleserviceCodeValue.allSpeechTransmissionServices);
		
		ArrayList<CauseValue> oCauseValueCriteria = prim.getTCauseValueCriteria();
		assertNotNull(oCauseValueCriteria);
		assertEquals(oCauseValueCriteria.size(),2);
		assertNotNull(oCauseValueCriteria.get(0));
		assertEquals(oCauseValueCriteria.get(0).getData(),7);
		assertNotNull(oCauseValueCriteria.get(1));
		assertEquals(oCauseValueCriteria.get(1).getData(),6);
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
	
		TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tBusy;
		ArrayList<ExtBasicServiceCode> basicServiceCriteria = new ArrayList<ExtBasicServiceCode>();
		ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
		ExtTeleserviceCode extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
		ExtBasicServiceCodeImpl basicServiceOne = new ExtBasicServiceCodeImpl(b);
		ExtBasicServiceCodeImpl basicServiceTwo = new ExtBasicServiceCodeImpl(extTeleservice);
		basicServiceCriteria.add(basicServiceOne);
		basicServiceCriteria.add(basicServiceTwo);
		
		ArrayList<CauseValue> tCauseValueCriteria = new ArrayList<CauseValue>();
		tCauseValueCriteria.add(new CauseValueImpl(7));
		tCauseValueCriteria.add(new CauseValueImpl(6));
		TBcsmCamelTdpCriteriaImpl prim = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, 
				basicServiceCriteria, tCauseValueCriteria);
		
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);

		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
	}
}
