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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.DateAndTimeImpl;
import org.mobicents.protocols.ss7.cap.primitives.ReceivingSideIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.RequestedInformationImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CallInformationReportRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 44, (byte) 160, 37, 48, 8, (byte) 128, 1, 2, (byte) 161, 3, (byte) 130, 1, 0, 48, 14,
                (byte) 128, 1, 1, (byte) 161, 9, (byte) 129, 7, 2, 17, 33, 3, 1, 112, (byte) 129, 48, 9, (byte) 128, 1, 30,
                (byte) 161, 4, (byte) 158, 2, (byte) 130, (byte) 145, (byte) 163, 3, (byte) 129, 1, 2 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 64, (byte) 160, 37, 48, 8, (byte) 128, 1, 2, (byte) 161, 3, (byte) 130, 1, 0, 48, 14,
                (byte) 128, 1, 1, (byte) 161, 9, (byte) 129, 7, 2, 17, 33, 3, 1, 112, (byte) 129, 48, 9, (byte) 128, 1, 30,
                (byte) 161, 4, (byte) 158, 2, (byte) 130, (byte) 145, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2,
                1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 163, 3, (byte) 129, 1, 2 };
    }

    public byte[] getDataInt1() {
        return new byte[] { -126, -111 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        CallInformationReportRequestImpl elem = new CallInformationReportRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getRequestedInformationList().get(0).getRequestedInformationType(),
                RequestedInformationType.callConnectedElapsedTime);
        assertEquals((int) elem.getRequestedInformationList().get(0).getCallConnectedElapsedTimeValue(), 0);
        assertEquals(elem.getRequestedInformationList().get(1).getRequestedInformationType(),
                RequestedInformationType.callStopTime);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getYear(), 2011);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getMonth(), 12);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getDay(), 30);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getHour(), 10);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getMinute(), 7);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getSecond(), 18);
        assertEquals(elem.getRequestedInformationList().get(2).getRequestedInformationType(),
                RequestedInformationType.releaseCause);
        assertTrue(Arrays.equals(elem.getRequestedInformationList().get(2).getReleaseCauseValue().getData(), getDataInt1()));
        assertEquals(elem.getLegID().getReceivingSideID(), LegType.leg2);
        assertNull(elem.getExtensions());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new CallInformationReportRequestImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getRequestedInformationList().get(0).getRequestedInformationType(),
                RequestedInformationType.callConnectedElapsedTime);
        assertEquals((int) elem.getRequestedInformationList().get(0).getCallConnectedElapsedTimeValue(), 0);
        assertEquals(elem.getRequestedInformationList().get(1).getRequestedInformationType(),
                RequestedInformationType.callStopTime);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getYear(), 2011);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getMonth(), 12);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getDay(), 30);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getHour(), 10);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getMinute(), 7);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getSecond(), 18);
        assertEquals(elem.getRequestedInformationList().get(2).getRequestedInformationType(),
                RequestedInformationType.releaseCause);
        assertTrue(Arrays.equals(elem.getRequestedInformationList().get(2).getReleaseCauseValue().getData(), getDataInt1()));
        assertEquals(elem.getLegID().getReceivingSideID(), LegType.leg2);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        ArrayList<RequestedInformation> requestedInformationList = new ArrayList<RequestedInformation>();
        RequestedInformationImpl ri = new RequestedInformationImpl(RequestedInformationType.callConnectedElapsedTime, 0);
        requestedInformationList.add(ri);
        DateAndTimeImpl callStopTimeValue = new DateAndTimeImpl(2011, 12, 30, 10, 7, 18);
        ri = new RequestedInformationImpl(callStopTimeValue);
        requestedInformationList.add(ri);
        CauseCapImpl releaseCauseValue = new CauseCapImpl(getDataInt1());
        ri = new RequestedInformationImpl(releaseCauseValue);
        requestedInformationList.add(ri);
        ReceivingSideIDImpl legID = new ReceivingSideIDImpl(LegType.leg2);

        CallInformationReportRequestImpl elem = new CallInformationReportRequestImpl(requestedInformationList, null, legID);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        elem = new CallInformationReportRequestImpl(requestedInformationList, CAPExtensionsTest.createTestCAPExtensions(),
                legID);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }
}
