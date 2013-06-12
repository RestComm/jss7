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

package org.mobicents.protocols.ss7.cap.errors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.errors.CancelProblem;
import org.mobicents.protocols.ss7.cap.api.errors.RequestedInfoErrorParameter;
import org.mobicents.protocols.ss7.cap.api.errors.TaskRefusedParameter;
import org.mobicents.protocols.ss7.cap.api.errors.UnavailableNetworkResource;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ErrorMessageEncodingTest {

    public byte[] getDataTaskRefused() {
        return new byte[] { 10, 1, 2 };
    }

    public byte[] getDataSystemFailure() {
        return new byte[] { 10, 1, 3 };
    }

    public byte[] getDataRequestedInfoError() {
        return new byte[] { 10, 1, 1 };
    }

    public byte[] getDataCancelFailed() {
        return new byte[] { 10, 1, 1 };
    }

    @Test(groups = { "functional.decode", "errors.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getDataTaskRefused();
        AsnInputStream ais = new AsnInputStream(data);
        CAPErrorMessageTaskRefusedImpl elem = new CAPErrorMessageTaskRefusedImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(tag, Tag.ENUMERATED);
        assertEquals(elem.getTaskRefusedParameter(), TaskRefusedParameter.congestion);

        data = this.getDataSystemFailure();
        ais = new AsnInputStream(data);
        CAPErrorMessageSystemFailureImpl elem2 = new CAPErrorMessageSystemFailureImpl();
        tag = ais.readTag();
        elem2.decodeAll(ais);
        assertEquals(tag, Tag.ENUMERATED);
        assertEquals(elem2.getUnavailableNetworkResource(), UnavailableNetworkResource.resourceStatusFailure);

        data = this.getDataRequestedInfoError();
        ais = new AsnInputStream(data);
        CAPErrorMessageRequestedInfoErrorImpl elem3 = new CAPErrorMessageRequestedInfoErrorImpl();
        tag = ais.readTag();
        elem3.decodeAll(ais);
        assertEquals(tag, Tag.ENUMERATED);
        assertEquals(elem3.getRequestedInfoErrorParameter(), RequestedInfoErrorParameter.unknownRequestedInfo);

        data = this.getDataCancelFailed();
        ais = new AsnInputStream(data);
        CAPErrorMessageCancelFailedImpl elem4 = new CAPErrorMessageCancelFailedImpl();
        tag = ais.readTag();
        elem4.decodeAll(ais);
        assertEquals(tag, Tag.ENUMERATED);
        assertEquals(elem4.getCancelProblem(), CancelProblem.tooLate);
    }

    @Test(groups = { "functional.encode", "errors.primitive" })
    public void testEncode() throws Exception {

        CAPErrorMessageTaskRefusedImpl elem = new CAPErrorMessageTaskRefusedImpl(TaskRefusedParameter.congestion);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getDataTaskRefused()));

        CAPErrorMessageSystemFailureImpl elem2 = new CAPErrorMessageSystemFailureImpl(
                UnavailableNetworkResource.resourceStatusFailure);
        aos = new AsnOutputStream();
        elem2.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getDataSystemFailure()));

        CAPErrorMessageRequestedInfoErrorImpl elem3 = new CAPErrorMessageRequestedInfoErrorImpl(
                RequestedInfoErrorParameter.unknownRequestedInfo);
        aos = new AsnOutputStream();
        elem3.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getDataRequestedInfoError()));

        CAPErrorMessageCancelFailedImpl elem4 = new CAPErrorMessageCancelFailedImpl(CancelProblem.tooLate);
        aos = new AsnOutputStream();
        elem4.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getDataCancelFailed()));
    }
}
