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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConferenceTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ForwardServiceInteractionInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.GenericNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.OriginalCalledNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.RedirectingPartyIDCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ForwardServiceInteractionIndImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.inap.isup.RedirectionInformationInapImpl;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ConnectRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 9, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 17, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16, (byte) 174, 6, 4, 4, 1, 2, 3, 4 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 75, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16, (byte) 129, 3, 0, 0, 8, (byte) 134, 6, (byte) 131,
                20, 7, 1, 9, 0, (byte) 170, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1,
                (byte) 255, (byte) 156, 1, 10, (byte) 157, 6, (byte) 131, 20, 7, 1, 9, 0, (byte) 158, 2, 3, 97, (byte) 174, 6,
                4, 4, 1, 2, 3, 4, (byte) 159, 55, 0, (byte) 159, 56, 0, (byte) 159, 57, 1, 40 };
    }

    public byte[] getData4() {
        return new byte[] { 48, 52, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16, (byte) 139, 4, 11, 12, 13, 14, (byte) 175, 5, (byte) 160, 3, (byte) 129, 1, 2,
                (byte) 147, 7, 4, 0, 0, 0, 112, 119, 119, (byte) 181, 3, (byte) 128, 1, 5, (byte) 159, 31, 4, 21, 22, 23, 24, (byte) 159, 32, 0, (byte) 159,
                58, 0, (byte) 159, 59, 0 };
    }

    public byte[] getDataGenericNumber() {
        return new byte[] { 1, 2, 3, 4 };
    }

    public byte[] getOriginalCalledPartyID() {
        return new byte[] { -125, 20, 7, 1, 9, 0 };
    }

    public byte[] getCallingPartysCategory() {
        return new byte[] { 10 };
    }

    public byte[] getRedirectingPartyID() {
        return new byte[] { -125, 20, 7, 1, 9, 0 };
    }

    public byte[] getRedirectionInformation() {
        return new byte[] { 3, 97 };
    }

    public byte[] getCarrierData() {
        return new byte[] { 11, 12, 13, 14 };
    }

    public byte[] getCUGInterlockData() {
        return new byte[] { 21, 22, 23, 24 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ConnectRequestImpl elem = new ConnectRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNatureOfAddressIndicator(), 2);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNumberingPlanIndicator(), 1);
        assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"));
        assertNull(elem.getCarrier());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getChargeNumber());
        assertNull(elem.getLegToBeConnected());
        assertNull(elem.getCUGInterlock());
        assertFalse(elem.getCugOutgoingAccess());
        assertFalse(elem.getBorInterrogationRequested());
        assertFalse(elem.getSuppressNCSI());


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new ConnectRequestImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNatureOfAddressIndicator(), 2);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNumberingPlanIndicator(), 1);
        assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"));
        assertEquals(elem.getGenericNumbers().size(), 1);
        assertTrue(Arrays.equals(elem.getGenericNumbers().get(0).getData(), getDataGenericNumber()));
        assertNull(elem.getCarrier());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getChargeNumber());
        assertNull(elem.getLegToBeConnected());
        assertNull(elem.getCUGInterlock());
        assertFalse(elem.getCugOutgoingAccess());
        assertFalse(elem.getBorInterrogationRequested());
        assertFalse(elem.getSuppressNCSI());


        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new ConnectRequestImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNatureOfAddressIndicator(), 2);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNumberingPlanIndicator(), 1);
        assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"));
        assertEquals(elem.getGenericNumbers().size(), 1);
        assertTrue(Arrays.equals(elem.getGenericNumbers().get(0).getData(), getDataGenericNumber()));
        assertEquals(elem.getAlertingPattern().getAlertingPattern().getAlertingCategory(), AlertingCategory.Category5);
        assertTrue(Arrays.equals(elem.getOriginalCalledPartyID().getData(), getOriginalCalledPartyID()));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertTrue(Arrays.equals(elem.getCallingPartysCategory().getData(), getCallingPartysCategory()));
        assertTrue(Arrays.equals(elem.getRedirectingPartyID().getData(), getRedirectingPartyID()));
        assertTrue(Arrays.equals(elem.getRedirectionInformation().getData(), getRedirectionInformation()));
        assertTrue(elem.getSuppressionOfAnnouncement());
        assertTrue(elem.getOCSIApplicable());
        assertEquals((int) elem.getNAOliInfo().getData(), 40);
        assertNull(elem.getCarrier());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getChargeNumber());
        assertNull(elem.getLegToBeConnected());
        assertNull(elem.getCUGInterlock());
        assertFalse(elem.getCugOutgoingAccess());
        assertFalse(elem.getBorInterrogationRequested());
        assertFalse(elem.getSuppressNCSI());

        data = this.getData4();
        ais = new AsnInputStream(data);
        elem = new ConnectRequestImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNatureOfAddressIndicator(), 2);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNumberingPlanIndicator(), 1);
        assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"));

        assertNull(elem.getGenericNumbers());
        assertNull(elem.getAlertingPattern());
        assertNull(elem.getOriginalCalledPartyID());
        assertNull(elem.getExtensions());
        assertNull(elem.getCallingPartysCategory());
        assertNull(elem.getRedirectingPartyID());
        assertNull(elem.getRedirectionInformation());
        assertFalse(elem.getSuppressionOfAnnouncement());
        assertFalse(elem.getOCSIApplicable());
        assertNull(elem.getNAOliInfo());
        assertEquals(elem.getCarrier().getData(), getCarrierData());
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getForwardServiceInteractionInd().getConferenceTreatmentIndicator(),
                ConferenceTreatmentIndicator.rejectConferenceRequest);
        assertEquals(elem.getChargeNumber().getLocationNumber().getAddress(), "0000077777");
        assertEquals(elem.getLegToBeConnected().getSendingSideID(), LegType.leg5);
        assertEquals(elem.getCUGInterlock().getData(), getCUGInterlockData());
        assertTrue(elem.getCugOutgoingAccess());
        assertTrue(elem.getBorInterrogationRequested());
        assertTrue(elem.getSuppressNCSI());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        ArrayList<CalledPartyNumberCap> calledPartyNumbers = new ArrayList<CalledPartyNumberCap>();
        CalledPartyNumberImpl cpn = new CalledPartyNumberImpl(2, "972201", 1, 2);
        CalledPartyNumberCapImpl calledPartyNumber = new CalledPartyNumberCapImpl(cpn);
        calledPartyNumbers.add(calledPartyNumber);
        DestinationRoutingAddressImpl destinationRoutingAddress = new DestinationRoutingAddressImpl(calledPartyNumbers);

        ConnectRequestImpl elem = new ConnectRequestImpl(destinationRoutingAddress, null, null, null, null, null, null, null,
                null, null, null, null, null, false, false, false, null, false, false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        ArrayList<GenericNumberCap> genericNumbers = new ArrayList<GenericNumberCap>();
        GenericNumberCapImpl genericNumberCap = new GenericNumberCapImpl(getDataGenericNumber());
        genericNumbers.add(genericNumberCap);
        elem = new ConnectRequestImpl(destinationRoutingAddress, null, null, null, null, null, null, null, genericNumbers,
                null, null, null, null, false, false, false, null, false, false);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category5);
        AlertingPatternCapImpl alertingPatternCap = new AlertingPatternCapImpl(alertingPattern);
        OriginalCalledNumberCapImpl originalCalledPartyID = new OriginalCalledNumberCapImpl(getOriginalCalledPartyID());
        CallingPartysCategoryInapImpl callingPartysCategory = new CallingPartysCategoryInapImpl(getCallingPartysCategory());
        RedirectingPartyIDCapImpl redirectingPartyID = new RedirectingPartyIDCapImpl(getRedirectingPartyID());
        RedirectionInformationInapImpl redirectionInformation = new RedirectionInformationInapImpl(getRedirectionInformation());
        NAOliInfoImpl naoliInfo = new NAOliInfoImpl(40);


        elem = new ConnectRequestImpl(destinationRoutingAddress, alertingPatternCap, originalCalledPartyID,
                CAPExtensionsTest.createTestCAPExtensions(), null, callingPartysCategory, redirectingPartyID,
                redirectionInformation, genericNumbers, null, null, null, null, false, true, true, naoliInfo, false, false);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));


        Carrier carrier = new CarrierImpl(getCarrierData());
        ForwardServiceInteractionInd forwardServiceInteractionInd = new ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.rejectConferenceRequest, null, null);
        ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, null, null,
                null, false, null, null, null);
        LocationNumber locationNumber = new LocationNumberImpl();
        locationNumber.setNatureOfAddresIndicator(LocationNumber._NAI_INTERNATIONAL_NUMBER);
        locationNumber.setAddress("0000077777");
        LocationNumberCap chargeNumber = new LocationNumberCapImpl(locationNumber);
        LegID legToBeConnected = new LegIDImpl(true, LegType.leg5);
        CUGInterlock cugInterlock = new CUGInterlockImpl(getCUGInterlockData());
        elem = new ConnectRequestImpl(destinationRoutingAddress, null, null, null, carrier, null, null, null, null, serviceInteractionIndicatorsTwo,
                chargeNumber, legToBeConnected, cugInterlock, true, false, false, null, true, true);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData4()));

        // DestinationRoutingAddressImpl destinationRoutingAddress, AlertingPatternCap alertingPattern,
        // OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions, Carrier carrier, CallingPartysCategoryInap
        // callingPartysCategory,
        // RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation,
        // ArrayList<GenericNumberCap> genericNumbers,
        // ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber, LegID
        // legToBeConnected, CUGInterlock cugInterlock,
        // boolean cugOutgoingAccess, boolean suppressionOfAnnouncement, boolean ocsIApplicable, NAOliInfo naoliInfo, boolean
        // borInterrogationRequested
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        ArrayList<CalledPartyNumberCap> calledPartyNumbers = new ArrayList<CalledPartyNumberCap>();
        CalledPartyNumberImpl cpn = new CalledPartyNumberImpl(2, "972201", 1, 2);
        CalledPartyNumberCapImpl calledPartyNumber = new CalledPartyNumberCapImpl(cpn);
        calledPartyNumbers.add(calledPartyNumber);
        DestinationRoutingAddressImpl destinationRoutingAddress = new DestinationRoutingAddressImpl(calledPartyNumbers);

        ArrayList<GenericNumberCap> genericNumbers = new ArrayList<GenericNumberCap>();
        GenericNumberCapImpl genericNumberCap = new GenericNumberCapImpl(getDataGenericNumber());
        genericNumbers.add(genericNumberCap);

        AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category5);
        AlertingPatternCapImpl alertingPatternCap = new AlertingPatternCapImpl(alertingPattern);
        OriginalCalledNumberCapImpl originalCalledPartyID = new OriginalCalledNumberCapImpl(getOriginalCalledPartyID());
        CallingPartysCategoryInapImpl callingPartysCategory = new CallingPartysCategoryInapImpl(getCallingPartysCategory());
        RedirectingPartyIDCapImpl redirectingPartyID = new RedirectingPartyIDCapImpl(getRedirectingPartyID());
        RedirectionInformationInapImpl redirectionInformation = new RedirectionInformationInapImpl(getRedirectionInformation());
        NAOliInfoImpl naoliInfo = new NAOliInfoImpl(40);

        ConnectRequestImpl original = new ConnectRequestImpl(destinationRoutingAddress, alertingPatternCap,
                originalCalledPartyID, CAPExtensionsTest.createTestCAPExtensions(), null, callingPartysCategory,
                redirectingPartyID, redirectionInformation, genericNumbers, null, null, null, null, false, true, true,
                naoliInfo, false, false);
        original.setInvokeId(24);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "connectRequest", ConnectRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ConnectRequestImpl copy = reader.read("connectRequest", ConnectRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getDestinationRoutingAddress().getCalledPartyNumber().size(), original.getDestinationRoutingAddress()
                .getCalledPartyNumber().size());
        assertEquals(copy.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), original.getDestinationRoutingAddress().getCalledPartyNumber().get(0)
                .getCalledPartyNumber().getInternalNetworkNumberIndicator());
        assertEquals(copy.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"), original.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getAddress().equals("972201"));
        assertEquals(copy.getAlertingPattern().getAlertingPattern().getAlertingCategory(), original.getAlertingPattern()
                .getAlertingPattern().getAlertingCategory());
        assertEquals(copy.getOriginalCalledPartyID().getData(), original.getOriginalCalledPartyID().getData());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));
        assertEquals(copy.getCallingPartysCategory().getData(), original.getCallingPartysCategory().getData());
        assertEquals(copy.getRedirectingPartyID().getData(), original.getRedirectingPartyID().getData());
        assertEquals(copy.getRedirectionInformation().getData(), original.getRedirectionInformation().getData());
        assertEquals(copy.getGenericNumbers().size(), original.getGenericNumbers().size());
        assertEquals(copy.getGenericNumbers().get(0).getData(), original.getGenericNumbers().get(0).getData());
        assertEquals(copy.getSuppressionOfAnnouncement(), original.getSuppressionOfAnnouncement());
        assertEquals(copy.getOCSIApplicable(), original.getOCSIApplicable());
        assertEquals(copy.getNAOliInfo().getData(), original.getNAOliInfo().getData());


        Carrier carrier = new CarrierImpl(getCarrierData());
        ForwardServiceInteractionInd forwardServiceInteractionInd = new ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.rejectConferenceRequest, null, null);
        ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, null, null,
                null, false, null, null, null);
        LocationNumber locationNumber = new LocationNumberImpl();
        locationNumber.setNatureOfAddresIndicator(LocationNumber._NAI_INTERNATIONAL_NUMBER);
        locationNumber.setAddress("0000077777");
        LocationNumberCap chargeNumber = new LocationNumberCapImpl(locationNumber);
        LegID legToBeConnected = new LegIDImpl(true, LegType.leg5);
        CUGInterlock cugInterlock = new CUGInterlockImpl(getCUGInterlockData());
        original = new ConnectRequestImpl(destinationRoutingAddress, null, null, null, carrier, null, null, null, null, serviceInteractionIndicatorsTwo,
                chargeNumber, legToBeConnected, cugInterlock, true, false, false, null, true, true);
        original.setInvokeId(24);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "connectRequest", ConnectRequestImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("connectRequest", ConnectRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getDestinationRoutingAddress().getCalledPartyNumber().size(), original.getDestinationRoutingAddress()
                .getCalledPartyNumber().size());
        assertEquals(copy.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), original.getDestinationRoutingAddress().getCalledPartyNumber().get(0)
                .getCalledPartyNumber().getInternalNetworkNumberIndicator());
        assertEquals(copy.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"), original.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getAddress().equals("972201"));

        assertNull(copy.getGenericNumbers());
        assertNull(copy.getAlertingPattern());
        assertNull(copy.getOriginalCalledPartyID());
        assertNull(copy.getExtensions());
        assertNull(copy.getCallingPartysCategory());
        assertNull(copy.getRedirectingPartyID());
        assertNull(copy.getRedirectionInformation());
        assertEquals(copy.getSuppressionOfAnnouncement(), original.getSuppressionOfAnnouncement());
        assertEquals(copy.getOCSIApplicable(), original.getOCSIApplicable());
        assertNull(copy.getNAOliInfo());
        assertEquals(copy.getCarrier().getData(), original.getCarrier().getData());
        assertEquals(copy.getServiceInteractionIndicatorsTwo().getForwardServiceInteractionInd().getConferenceTreatmentIndicator(),
                original.getServiceInteractionIndicatorsTwo().getForwardServiceInteractionInd().getConferenceTreatmentIndicator());
        assertEquals(copy.getChargeNumber().getLocationNumber().getAddress(), original.getChargeNumber().getLocationNumber().getAddress());
        assertEquals(copy.getLegToBeConnected().getSendingSideID(), original.getLegToBeConnected().getSendingSideID());
        assertEquals(copy.getCUGInterlock().getData(), original.getCUGInterlock().getData());
        assertEquals(copy.getCugOutgoingAccess(), original.getCugOutgoingAccess());
        assertEquals(copy.getBorInterrogationRequested(), original.getBorInterrogationRequested());
        assertEquals(copy.getSuppressNCSI(), original.getSuppressNCSI());
    }
}
