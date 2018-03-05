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

package org.restcomm.protocols.ss7.cap.errors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorCode;
import org.restcomm.protocols.ss7.cap.api.errors.CancelProblem;
import org.restcomm.protocols.ss7.cap.api.errors.RequestedInfoErrorParameter;
import org.restcomm.protocols.ss7.cap.api.errors.TaskRefusedParameter;
import org.restcomm.protocols.ss7.cap.api.errors.UnavailableNetworkResource;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageCancelFailedImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageParameterlessImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageRequestedInfoErrorImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageSystemFailureImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageTaskRefusedImpl;
import org.restcomm.protocols.ss7.cap.isup.BearerCapImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;
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

    @Test(groups = { "functional.xml.serialize", "errors.primitive" })
    public void testXMLSerialize_CancelFailed() throws Exception {

        CAPErrorMessageCancelFailedImpl original = new CAPErrorMessageCancelFailedImpl(CancelProblem.tooLate);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "messageCancelFailed", CAPErrorMessageCancelFailedImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CAPErrorMessageCancelFailedImpl copy = reader.read("messageCancelFailed", CAPErrorMessageCancelFailedImpl.class);

        assertEquals((long) copy.getErrorCode(), (long) original.getErrorCode());
        assertEquals(copy.getCancelProblem(), original.getCancelProblem());

    }

    @Test(groups = { "functional.xml.serialize", "errors.primitive" })
    public void testXMLSerialize_Parameterless() throws Exception {

        CAPErrorMessageParameterlessImpl original = new CAPErrorMessageParameterlessImpl((long) CAPErrorCode.unknownPDPID);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "parameterless", CAPErrorMessageParameterlessImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CAPErrorMessageParameterlessImpl copy = reader.read("parameterless", CAPErrorMessageParameterlessImpl.class);

        assertEquals((long) copy.getErrorCode(), (long) original.getErrorCode());

    }

    @Test(groups = { "functional.xml.serialize", "errors.primitive" })
    public void testXMLSerialize_RequestedInfoError() throws Exception {

        CAPErrorMessageRequestedInfoErrorImpl original = new CAPErrorMessageRequestedInfoErrorImpl(
                RequestedInfoErrorParameter.requestedInfoNotAvailable);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "requestedInfoError", CAPErrorMessageRequestedInfoErrorImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CAPErrorMessageRequestedInfoErrorImpl copy = reader.read("requestedInfoError", CAPErrorMessageRequestedInfoErrorImpl.class);

        assertEquals((long) copy.getErrorCode(), (long) original.getErrorCode());
        assertEquals(copy.getRequestedInfoErrorParameter(), original.getRequestedInfoErrorParameter());

    }

    @Test(groups = { "functional.xml.serialize", "errors.primitive" })
    public void testXMLSerialize_SystemFailure() throws Exception {

        CAPErrorMessageSystemFailureImpl original = new CAPErrorMessageSystemFailureImpl(
                UnavailableNetworkResource.endUserFailure);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "systemFailure", CAPErrorMessageSystemFailureImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CAPErrorMessageSystemFailureImpl copy = reader.read("systemFailure", CAPErrorMessageSystemFailureImpl.class);

        assertEquals((long) copy.getErrorCode(), (long) original.getErrorCode());
        assertEquals(copy.getUnavailableNetworkResource(), original.getUnavailableNetworkResource());

    }

    @Test(groups = { "functional.xml.serialize", "errors.primitive" })
    public void testXMLSerialize_TaskRefused() throws Exception {

        CAPErrorMessageTaskRefusedImpl original = new CAPErrorMessageTaskRefusedImpl(TaskRefusedParameter.unobtainable);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "taskRefused", CAPErrorMessageTaskRefusedImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CAPErrorMessageTaskRefusedImpl copy = reader.read("taskRefused", CAPErrorMessageTaskRefusedImpl.class);

        assertEquals((long) copy.getErrorCode(), (long) original.getErrorCode());
        assertEquals(copy.getTaskRefusedParameter(), original.getTaskRefusedParameter());

    }
}
