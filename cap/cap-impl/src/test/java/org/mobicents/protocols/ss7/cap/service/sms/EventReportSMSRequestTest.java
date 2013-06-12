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
package org.mobicents.protocols.ss7.cap.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.EsiSms.OSmsFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.EventSpecificInformationSMSImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.mobicents.protocols.ss7.inap.primitives.MiscCallInfoImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class EventReportSMSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 35, -128, 1, 3, -95, 5, -96, 3, -128, 1, 2, -94, 3, -128, 1, 1, -86, 18, 48, 5, 2, 1,
                2, -127, 0, 48, 9, 2, 1, 3, 10, 1, 1, -127, 1, -1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        EventReportSMSRequestImpl prim = new EventReportSMSRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getEventTypeSMS(), EventTypeSMS.oSmsSubmission);
        EventSpecificInformationSMS eventSpecificInformationSMS = prim.getEventSpecificInformationSMS();
        assertNotNull(eventSpecificInformationSMS.getOSmsFailureSpecificInfo());
        OSmsFailureSpecificInfo oSmsFailureSpecificInfo = eventSpecificInformationSMS.getOSmsFailureSpecificInfo();
        assertEquals(oSmsFailureSpecificInfo.getFailureCause(), MOSMSCause.facilityNotSupported);
        assertNull(eventSpecificInformationSMS.getOSmsSubmissionSpecificInfo());
        assertNull(eventSpecificInformationSMS.getTSmsFailureSpecificInfo());
        assertNull(eventSpecificInformationSMS.getTSmsDeliverySpecificInfo());

        assertNotNull(prim.getMiscCallInfo());

        // extensions
        CAPExtensionsTest.checkTestCAPExtensions(prim.getExtensions());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        EventTypeSMS eventTypeSMS = EventTypeSMS.oSmsSubmission;
        OSmsFailureSpecificInfo oSmsFailureSpecificInfo = new OSmsFailureSpecificInfoImpl(
                MOSMSCause.facilityNotSupported);
        EventSpecificInformationSMSImpl eventSpecificInformationSMS = new EventSpecificInformationSMSImpl(
                oSmsFailureSpecificInfo);
        MiscCallInfo miscCallInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);

        CAPExtensions extensions = CAPExtensionsTest.createTestCAPExtensions();

        EventReportSMSRequestImpl prim = new EventReportSMSRequestImpl(eventTypeSMS, eventSpecificInformationSMS,
                miscCallInfo, extensions);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
