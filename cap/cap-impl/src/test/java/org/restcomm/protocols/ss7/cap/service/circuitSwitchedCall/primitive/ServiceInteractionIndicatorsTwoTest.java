/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BackwardServiceInteractionInd;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConferenceTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConnectedNumberTreatmentInd;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CwTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EctTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ForwardServiceInteractionInd;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.HoldTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.BackwardServiceInteractionIndImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ForwardServiceInteractionIndImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.BothwayThroughConnectionInd;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ServiceInteractionIndicatorsTwoTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 130, 1, 0 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 30, (byte) 160, 3, (byte) 129, 1, 1, (byte) 161, 3, (byte) 129, 1, 2, (byte) 130, 1, 0, (byte) 132, 1, 2, (byte) 141, 0,
                (byte) 159, 50, 1, 1, (byte) 159, 51, 1, 2, (byte) 159, 52, 1, 1 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ServiceInteractionIndicatorsTwoImpl elem = new ServiceInteractionIndicatorsTwoImpl();

        int tag = ais.readTag();
        assertEquals(ais.getTag(), Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);
        elem.decodeAll(ais);

        assertEquals(elem.getBothwayThroughConnectionInd(), BothwayThroughConnectionInd.bothwayPathRequired);

        assertNull(elem.getForwardServiceInteractionInd());
        assertNull(elem.getBackwardServiceInteractionInd());
        assertNull(elem.getConnectedNumberTreatmentInd());
        assertFalse(elem.getNonCUGCall());
        assertNull(elem.getHoldTreatmentIndicator());
        assertNull(elem.getCwTreatmentIndicator());
        assertNull(elem.getEctTreatmentIndicator());


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new ServiceInteractionIndicatorsTwoImpl();

        tag = ais.readTag();
        assertEquals(ais.getTag(), Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);
        elem.decodeAll(ais);


        assertEquals(elem.getForwardServiceInteractionInd().getConferenceTreatmentIndicator(), ConferenceTreatmentIndicator.acceptConferenceRequest);
        assertEquals(elem.getBackwardServiceInteractionInd().getConferenceTreatmentIndicator(), ConferenceTreatmentIndicator.rejectConferenceRequest);
        assertEquals(elem.getBothwayThroughConnectionInd(), BothwayThroughConnectionInd.bothwayPathRequired);
        assertEquals(elem.getConnectedNumberTreatmentInd(), ConnectedNumberTreatmentInd.presentCalledINNumber);
        assertTrue(elem.getNonCUGCall());
        assertEquals(elem.getHoldTreatmentIndicator(), HoldTreatmentIndicator.acceptHoldRequest);
        assertEquals(elem.getCwTreatmentIndicator(), CwTreatmentIndicator.rejectCw);
        assertEquals(elem.getEctTreatmentIndicator(), EctTreatmentIndicator.acceptEctRequest);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        ServiceInteractionIndicatorsTwoImpl elem = new ServiceInteractionIndicatorsTwoImpl(null, null, BothwayThroughConnectionInd.bothwayPathRequired, null,
                false, null, null, null);

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData1());


        ForwardServiceInteractionInd forwardServiceInteractionInd = new ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.acceptConferenceRequest, null, null);
        BackwardServiceInteractionInd backwardServiceInteractionInd = new BackwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.rejectConferenceRequest, null);
        elem = new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, backwardServiceInteractionInd,
                BothwayThroughConnectionInd.bothwayPathRequired, ConnectedNumberTreatmentInd.presentCalledINNumber, true,
                HoldTreatmentIndicator.acceptHoldRequest, CwTreatmentIndicator.rejectCw, EctTreatmentIndicator.acceptEctRequest);

        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData2());

        // ForwardServiceInteractionInd forwardServiceInteractionInd,
        // BackwardServiceInteractionInd backwardServiceInteractionInd, BothwayThroughConnectionInd bothwayThroughConnectionInd,
        // ConnectedNumberTreatmentInd connectedNumberTreatmentInd, boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator,
        // CwTreatmentIndicator cwTreatmentIndicator, EctTreatmentIndicator ectTreatmentIndicator

        // TODO: implement full testing for CAP V4
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerialize() throws Exception {

        ForwardServiceInteractionInd forwardServiceInteractionInd = new ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.acceptConferenceRequest,
                null, null);
        BackwardServiceInteractionInd backwardServiceInteractionInd = new BackwardServiceInteractionIndImpl(
                ConferenceTreatmentIndicator.rejectConferenceRequest, null);
        ServiceInteractionIndicatorsTwoImpl original = new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, backwardServiceInteractionInd,
                BothwayThroughConnectionInd.bothwayPathRequired, ConnectedNumberTreatmentInd.presentCalledINNumber, true,
                HoldTreatmentIndicator.acceptHoldRequest, CwTreatmentIndicator.rejectCw, EctTreatmentIndicator.acceptEctRequest);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "serviceInteractionIndicatorsTwo", ServiceInteractionIndicatorsTwoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ServiceInteractionIndicatorsTwoImpl copy = reader.read("serviceInteractionIndicatorsTwo", ServiceInteractionIndicatorsTwoImpl.class);

        assertEquals(original.getForwardServiceInteractionInd().getConferenceTreatmentIndicator(), copy.getForwardServiceInteractionInd().getConferenceTreatmentIndicator());
        assertEquals(original.getBackwardServiceInteractionInd().getConferenceTreatmentIndicator(), copy.getBackwardServiceInteractionInd().getConferenceTreatmentIndicator());
        assertEquals(original.getBothwayThroughConnectionInd(), copy.getBothwayThroughConnectionInd());
        assertEquals(original.getConnectedNumberTreatmentInd(), copy.getConnectedNumberTreatmentInd());
        assertEquals(original.getNonCUGCall(), copy.getNonCUGCall());
        assertEquals(original.getHoldTreatmentIndicator(), copy.getHoldTreatmentIndicator());
        assertEquals(original.getCwTreatmentIndicator(), copy.getCwTreatmentIndicator());
        assertEquals(original.getEctTreatmentIndicator(), copy.getEctTreatmentIndicator());
    }
}
