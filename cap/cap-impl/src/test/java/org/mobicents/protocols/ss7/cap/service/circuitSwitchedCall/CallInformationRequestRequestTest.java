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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CallInformationRequestRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 16, (byte) 160, 9, 10, 1, 1, 10, 1, 2, 10, 1, 30, (byte) 163, 3, (byte) 128, 1, 2 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 36, (byte) 160, 9, 10, 1, 1, 10, 1, 2, 10, 1, 30, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129,
                0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 163, 3, (byte) 128, 1, 2 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        CallInformationRequestRequestImpl elem = new CallInformationRequestRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getRequestedInformationTypeList().get(0), RequestedInformationType.callStopTime);
        assertEquals(elem.getRequestedInformationTypeList().get(1), RequestedInformationType.callConnectedElapsedTime);
        assertEquals(elem.getRequestedInformationTypeList().get(2), RequestedInformationType.releaseCause);
        assertEquals(elem.getLegID().getSendingSideID(), LegType.leg2);
        assertNull(elem.getExtensions());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new CallInformationRequestRequestImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getRequestedInformationTypeList().get(0), RequestedInformationType.callStopTime);
        assertEquals(elem.getRequestedInformationTypeList().get(1), RequestedInformationType.callConnectedElapsedTime);
        assertEquals(elem.getRequestedInformationTypeList().get(2), RequestedInformationType.releaseCause);
        assertEquals(elem.getLegID().getSendingSideID(), LegType.leg2);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        ArrayList<RequestedInformationType> requestedInformationTypeList = new ArrayList<RequestedInformationType>();
        requestedInformationTypeList.add(RequestedInformationType.callStopTime);
        requestedInformationTypeList.add(RequestedInformationType.callConnectedElapsedTime);
        requestedInformationTypeList.add(RequestedInformationType.releaseCause);
        SendingSideIDImpl legID = new SendingSideIDImpl(LegType.leg2);

        CallInformationRequestRequestImpl elem = new CallInformationRequestRequestImpl(requestedInformationTypeList, null,
                legID);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
        // ArrayList<RequestedInformationType> requestedInformationTypeList, CAPExtensions extensions, SendingSideID legID

        elem = new CallInformationRequestRequestImpl(requestedInformationTypeList, CAPExtensionsTest.createTestCAPExtensions(),
                legID);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }
}
