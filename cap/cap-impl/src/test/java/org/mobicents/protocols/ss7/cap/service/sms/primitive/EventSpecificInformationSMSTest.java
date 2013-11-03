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
package org.mobicents.protocols.ss7.cap.service.sms.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.EsiSms.OSmsFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiSms.OSmsSubmissionSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiSms.TSmsDeliverySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiSms.TSmsFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiSms.OSmsSubmissionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiSms.TSmsDeliverySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class EventSpecificInformationSMSTest {

	public byte[] getData1() {
		return new byte[] { -96, 3, -128, 1, 2 };
	};
	
	public byte[] getData2() {
		return new byte[] {-95, 0};
	};
	
	public byte[] getData3() {
		return new byte[] { -94, 3, -128, 1, 6};
	};
	
	public byte[] getData4() {
		return new byte[] {-93, 0};
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		//option 1
		byte[] data = this.getData1();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		EventSpecificInformationSMSImpl prim = new EventSpecificInformationSMSImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, EventSpecificInformationSMSImpl._ID_oSmsFailureSpecificInfo);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
		
		assertNotNull(prim.getOSmsFailureSpecificInfo());
		OSmsFailureSpecificInfo oSmsFailureSpecificInfo = prim.getOSmsFailureSpecificInfo();
		assertEquals(oSmsFailureSpecificInfo.getFailureCause(), MOSMSCause.facilityNotSupported);
		assertNull(prim.getOSmsSubmissionSpecificInfo());
		assertNull(prim.getTSmsFailureSpecificInfo());
		assertNull(prim.getTSmsDeliverySpecificInfo());
		
		//option 2
		data = this.getData2();
		asn = new AsnInputStream(data);
		tag = asn.readTag();
		prim = new EventSpecificInformationSMSImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, EventSpecificInformationSMSImpl._ID_oSmsSubmissionSpecificInfo);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
		
		assertNull(prim.getOSmsFailureSpecificInfo());
		assertNotNull(prim.getOSmsSubmissionSpecificInfo());
		assertNull(prim.getTSmsFailureSpecificInfo());
		assertNull(prim.getTSmsDeliverySpecificInfo());
		
		//option 3
		data = this.getData3();
		asn = new AsnInputStream(data);
		tag = asn.readTag();
		prim = new EventSpecificInformationSMSImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, EventSpecificInformationSMSImpl._ID_tSmsFailureSpecificInfo);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
		
		assertNull(prim.getOSmsFailureSpecificInfo());
		assertNull(prim.getOSmsSubmissionSpecificInfo());
		assertNotNull(prim.getTSmsFailureSpecificInfo());
		TSmsFailureSpecificInfo tSmsFailureSpecificInfo = prim.getTSmsFailureSpecificInfo();
		assertEquals(tSmsFailureSpecificInfo.GetFailureCause().getData(),6);
		assertNull(prim.getTSmsDeliverySpecificInfo());
		
		//option 4
		data = this.getData4();
		asn = new AsnInputStream(data);
		tag = asn.readTag();
		prim = new EventSpecificInformationSMSImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, EventSpecificInformationSMSImpl._ID_tSmsDeliverySpecificInfo);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
		
		assertNull(prim.getOSmsFailureSpecificInfo());
		assertNull(prim.getOSmsSubmissionSpecificInfo());
		assertNull(prim.getTSmsFailureSpecificInfo());
		assertNotNull(prim.getTSmsDeliverySpecificInfo());
		
		
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		
		MTSMSCauseImpl failureCause = new MTSMSCauseImpl(6);
		OSmsFailureSpecificInfo oSmsFailureSpecificInfo = new OSmsFailureSpecificInfoImpl(MOSMSCause.facilityNotSupported);
		OSmsSubmissionSpecificInfo oSmsSubmissionSpecificInfo = new OSmsSubmissionSpecificInfoImpl();
		TSmsFailureSpecificInfo tSmsFailureSpecificInfo = new TSmsFailureSpecificInfoImpl(failureCause);
		TSmsDeliverySpecificInfo tSmsDeliverySpecificInfo = new TSmsDeliverySpecificInfoImpl();
	
		//option 1
		EventSpecificInformationSMSImpl prim = new EventSpecificInformationSMSImpl(oSmsFailureSpecificInfo);
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));
		
		//option 2
		prim = new EventSpecificInformationSMSImpl(oSmsSubmissionSpecificInfo);
		asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
		
		//option 3
		prim = new EventSpecificInformationSMSImpl(tSmsFailureSpecificInfo);
		asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData3()));
		
		//option 4
		prim = new EventSpecificInformationSMSImpl(tSmsDeliverySpecificInfo);
		asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData4()));
	}
	
}
