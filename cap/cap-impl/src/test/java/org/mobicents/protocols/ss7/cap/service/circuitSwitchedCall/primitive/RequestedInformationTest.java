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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.DateAndTimeImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class RequestedInformationTest {

    public byte[] getData1() {
        return new byte[] { 48, 8, (byte) 128, 1, 2, (byte) 161, 3, (byte) 130, 1, 0 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 14, (byte) 128, 1, 1, (byte) 161, 9, (byte) 129, 7, 2, 17, 33, 3, 1, 112, (byte) 129 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 9, (byte) 128, 1, 30, (byte) 161, 4, (byte) 158, 2, (byte) 130, (byte) 145 };
    }

    public byte[] getData4() {
        return new byte[] { 48, 8, (byte) 128, 1, 0, (byte) 161, 3, (byte) 128, 1, 11 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        RequestedInformationImpl elem = new RequestedInformationImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getRequestedInformationType(), RequestedInformationType.callConnectedElapsedTime);
        assertEquals((int) elem.getCallConnectedElapsedTimeValue(), 0);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new RequestedInformationImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getRequestedInformationType(), RequestedInformationType.callStopTime);
        assertEquals(elem.getCallStopTimeValue().getYear(), 2011);
        assertEquals(elem.getCallStopTimeValue().getMonth(), 12);
        assertEquals(elem.getCallStopTimeValue().getDay(), 30);
        assertEquals(elem.getCallStopTimeValue().getHour(), 10);
        assertEquals(elem.getCallStopTimeValue().getMinute(), 7);
        assertEquals(elem.getCallStopTimeValue().getSecond(), 18);

        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new RequestedInformationImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getRequestedInformationType(), RequestedInformationType.releaseCause);
        CauseIndicators ci = elem.getReleaseCauseValue().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 17);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 2);

        data = this.getData4();
        ais = new AsnInputStream(data);
        elem = new RequestedInformationImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getRequestedInformationType(), RequestedInformationType.callAttemptElapsedTime);
        assertEquals((int) elem.getCallAttemptElapsedTimeValue(), 11);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        RequestedInformationImpl elem = new RequestedInformationImpl(RequestedInformationType.callConnectedElapsedTime, 0);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        DateAndTimeImpl dt = new DateAndTimeImpl(2011, 12, 30, 10, 7, 18);
        elem = new RequestedInformationImpl(dt);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        CauseIndicatorsImpl ci = new CauseIndicatorsImpl(0, 2, 0, 17, null);
        // int codingStandard, int location, int recommendation, int causeValue, byte[] diagnostics
        CauseCapImpl cc = new CauseCapImpl(ci);
        elem = new RequestedInformationImpl(cc);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));

        elem = new RequestedInformationImpl(RequestedInformationType.callAttemptElapsedTime, 11);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData4()));
    }
}
