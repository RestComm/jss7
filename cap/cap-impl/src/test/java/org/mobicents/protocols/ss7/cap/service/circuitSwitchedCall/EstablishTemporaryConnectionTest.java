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

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.ScfIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.BothwayThroughConnectionInd;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class EstablishTemporaryConnectionTest {

    public byte[] getData1() {
        return new byte[] { 48, 49, (byte) 128, 5, 1, 1, 1, 17, 34, (byte) 129, 5, 64, 1, 2, 3, 4, (byte) 131, 4, 5, 6, 7, 8,
                (byte) 164, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 167,
                3, (byte) 130, 1, 1, (byte) 159, 50, 1, 11 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 52, (byte) 128, 5, 1, 1, 1, 17, 34, (byte) 129, 5, 64, 1, 2, 3, 4, (byte) 131, 4, 5, 6, 7, 8,
                (byte) 164, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 166,
                3, (byte) 130, 1, 1, (byte) 135, 1, 8, (byte) 159, 50, 1, 11 };
    }

    public byte[] getCorrelationIDDigits() {
        return new byte[] { 1, 2, 3, 4 };
    }

    public byte[] getScfIDData() {
        return new byte[] { 5, 6, 7, 8 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        EstablishTemporaryConnectionRequestImpl elem = new EstablishTemporaryConnectionRequestImpl(false);
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getNatureOfAddressIndicator(), 1);
        assertTrue(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getAddress().equals("1122"));
        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getNumberQualifierIndicator(), 1);
        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getNumberingPlanIndicator(), 0);
        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getAddressRepresentationRestrictedIndicator(), 0);
        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getScreeningIndicator(), 1);
        assertEquals(elem.getCorrelationID().getGenericDigits().getEncodingScheme(), 2);
        assertEquals(elem.getCorrelationID().getGenericDigits().getTypeOfDigits(), 0);
        assertTrue(Arrays.equals(elem.getCorrelationID().getGenericDigits().getEncodedDigits(), getCorrelationIDDigits()));
        assertTrue(Arrays.equals(elem.getScfID().getData(), getScfIDData()));
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getBothwayThroughConnectionInd(),
                BothwayThroughConnectionInd.bothwayPathNotRequired);
        assertNull(elem.getCallSegmentID());
        assertEquals((int) elem.getNAOliInfo().getData(), 11);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new EstablishTemporaryConnectionRequestImpl(true);
        tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getNatureOfAddressIndicator(), 1);
        assertTrue(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getAddress().equals("1122"));
        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getNumberQualifierIndicator(), 1);
        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getNumberingPlanIndicator(), 0);
        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getAddressRepresentationRestrictedIndicator(), 0);
        assertEquals(elem.getAssistingSSPIPRoutingAddress().getGenericNumber().getScreeningIndicator(), 1);
        assertEquals(elem.getCorrelationID().getGenericDigits().getEncodingScheme(), 2);
        assertEquals(elem.getCorrelationID().getGenericDigits().getTypeOfDigits(), 0);
        assertTrue(Arrays.equals(elem.getCorrelationID().getGenericDigits().getEncodedDigits(), getCorrelationIDDigits()));
        assertTrue(Arrays.equals(elem.getScfID().getData(), getScfIDData()));
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getBothwayThroughConnectionInd(),
                BothwayThroughConnectionInd.bothwayPathNotRequired);
        assertEquals((int) elem.getCallSegmentID(), 8);
        assertEquals((int) elem.getNAOliInfo().getData(), 11);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        GenericNumberImpl genericNumber = new GenericNumberImpl(1, "1122", 1, 0, 0, false, 1);
        // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
        // addressRepresentationREstrictedIndicator,
        // boolean numberIncomplete, int screeningIndicator
        DigitsImpl assistingSSPIPRoutingAddress = new DigitsImpl(genericNumber);
        GenericDigitsImpl genericDigits = new GenericDigitsImpl(2, 0, getCorrelationIDDigits());
        // int encodingScheme, int typeOfDigits, int[] digits
        DigitsImpl correlationID = new DigitsImpl(genericDigits);
        ScfIDImpl scfID = new ScfIDImpl(getScfIDData());
        ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(null,
                null, BothwayThroughConnectionInd.bothwayPathNotRequired, null, false, null, null, null);
        // ForwardServiceInteractionInd forwardServiceInteractionInd,
        // BackwardServiceInteractionInd backwardServiceInteractionInd, BothwayThroughConnectionInd bothwayThroughConnectionInd,
        // ConnectedNumberTreatmentInd connectedNumberTreatmentInd, boolean nonCUGCall, HoldTreatmentIndicator
        // holdTreatmentIndicator,
        // CwTreatmentIndicator cwTreatmentIndicator, EctTreatmentIndicator ectTreatmentIndicator
        NAOliInfoImpl naOliInfo = new NAOliInfoImpl(11);

        EstablishTemporaryConnectionRequestImpl elem = new EstablishTemporaryConnectionRequestImpl(
                assistingSSPIPRoutingAddress, correlationID, scfID, CAPExtensionsTest.createTestCAPExtensions(), null,
                serviceInteractionIndicatorsTwo, null, naOliInfo, null, null, null, false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // Digits assistingSSPIPRoutingAddress, Digits correlationID, ScfID scfID, CAPExtensions extensions,
        // Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID, NAOliInfo
        // naOliInfo,
        // LocationNumberCap chargeNumber, OriginalCalledNumberCap originalCalledPartyID, CallingPartyNumberCap
        // callingPartyNumber,
        // boolean isCAPVersion3orLater

        elem = new EstablishTemporaryConnectionRequestImpl(assistingSSPIPRoutingAddress, correlationID, scfID,
                CAPExtensionsTest.createTestCAPExtensions(), null, serviceInteractionIndicatorsTwo, 8, naOliInfo, null, null,
                null, true);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }
}
