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

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.isup.GenericNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtensionImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.BothwayThroughConnectionInd;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CallingPartyCategoryImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingLevel;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ContinueWithArgumentRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 49, (byte) 129, 3, 0, 0, 2, (byte) 166, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255,
                (byte) 167, 3, (byte) 130, 1, 1, (byte) 140, 1, 4, (byte) 176, 8, 4, 6, 0, 0, 0, 17, 33, 34, (byte) 145, 4, 1, 2, 3, 4 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 84, (byte) 129, 3, 0, 0, 2, (byte) 166, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255,
                (byte) 167, 3, (byte) 130, 1, 1, (byte) 140, 1, 4, (byte) 176, 8, 4, 6, 0, 0, 0, 17, 33, 34, (byte) 145, 4, 1, 2, 3, 4, (byte) 146, 0,
                (byte) 159, 50, 5, 0, 0, 34, 50, 51, (byte) 159, 52, 4, 5, 6, 7, 8, (byte) 159, 55, 0, (byte) 159, 56, 1, 11, (byte) 159, 57, 0, (byte) 159,
                58, 0, (byte) 191, 59, 2, (byte) 128, 0 };
    }

    public byte[] getCUGInterlock() {
        return new byte[] { 1, 2, 3, 4 };
    }

    public byte[] getCarrier() {
        return new byte[] { 5, 6, 7, 8 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ContinueWithArgumentRequestImpl elem = new ContinueWithArgumentRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getAlertingPattern().getAlertingPattern().getAlertingLevel(), AlertingLevel.Level2);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getBothwayThroughConnectionInd(), BothwayThroughConnectionInd.bothwayPathNotRequired);
        assertEquals(elem.getCallingPartysCategory().getCallingPartyCategory().getCallingPartyCategory(), CallingPartyCategory._CATEGORY_OL_RUSSIAN);
        assertEquals(elem.getGenericNumbers().size(), 1);
        GenericNumberCap gn = elem.getGenericNumbers().get(0);
        assertEquals(gn.getGenericNumber().getAddress(), "111222");
        assertEquals(elem.getCugInterlock().getData(), getCUGInterlock());
        assertFalse(elem.getCugOutgoingAccess());
        assertNull(elem.getChargeNumber());
        assertNull(elem.getCarrier());
        assertFalse(elem.getSuppressionOfAnnouncement());
        assertNull(elem.getNaOliInfo());
        assertFalse(elem.getBorInterrogationRequested());
        assertFalse(elem.getSuppressOCsi());
        assertNull(elem.getContinueWithArgumentArgExtension());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new ContinueWithArgumentRequestImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getAlertingPattern().getAlertingPattern().getAlertingLevel(), AlertingLevel.Level2);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getBothwayThroughConnectionInd(), BothwayThroughConnectionInd.bothwayPathNotRequired);
        assertEquals(elem.getCallingPartysCategory().getCallingPartyCategory().getCallingPartyCategory(), CallingPartyCategory._CATEGORY_OL_RUSSIAN);
        assertEquals(elem.getGenericNumbers().size(), 1);
        gn = elem.getGenericNumbers().get(0);
        assertEquals(gn.getGenericNumber().getAddress(), "111222");
        assertEquals(elem.getCugInterlock().getData(), getCUGInterlock());
        assertTrue(elem.getCugOutgoingAccess());
        assertEquals(elem.getChargeNumber().getLocationNumber().getAddress(), "222333");
        assertEquals(elem.getCarrier().getData(), getCarrier());
        assertTrue(elem.getSuppressionOfAnnouncement());
        assertEquals((int) elem.getNaOliInfo().getData(), 11);
        assertTrue(elem.getBorInterrogationRequested());
        assertTrue(elem.getSuppressOCsi());
        assertTrue(elem.getContinueWithArgumentArgExtension().getSuppressDCsi());
        assertFalse(elem.getContinueWithArgumentArgExtension().getSuppressNCsi());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        CallingPartyCategoryImpl callingPartyCategory = new CallingPartyCategoryImpl();
        callingPartyCategory.setCallingPartyCategory(CallingPartyCategory._CATEGORY_OL_RUSSIAN);
        CallingPartysCategoryInapImpl callingPartysCategory = new CallingPartysCategoryInapImpl(callingPartyCategory);
        ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(null, null,
                BothwayThroughConnectionInd.bothwayPathNotRequired, null, false, null, null, null);
        AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingLevel.Level2);
        AlertingPatternCapImpl alertingPatternCap = new AlertingPatternCapImpl(alertingPattern);
        ArrayList<GenericNumberCap> genericNumbers = new ArrayList<GenericNumberCap>();
        GenericNumberImpl genericNumber = new GenericNumberImpl();
        genericNumber.setAddress("111222");
        GenericNumberCapImpl gn = new GenericNumberCapImpl(genericNumber);
        genericNumbers.add(gn);
        CUGInterlockImpl cugInterlock = new CUGInterlockImpl(getCUGInterlock());

        ContinueWithArgumentRequestImpl elem = new ContinueWithArgumentRequestImpl(alertingPatternCap, CAPExtensionsTest.createTestCAPExtensions(),
                serviceInteractionIndicatorsTwo, callingPartysCategory, genericNumbers, cugInterlock, false, null, null, false, null, false, false, null);

        // AlertingPatternCap alertingPattern, CAPExtensions extensions,
        // ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
        // CallingPartysCategoryInap callingPartysCategory,
        // ArrayList<GenericNumberCap> genericNumbers,
        // CUGInterlock cugInterlock, boolean cugOutgoingAccess,
        // LocationNumberCap chargeNumber, Carrier carrier,
        // boolean suppressionOfAnnouncement, NAOliInfo naOliInfo, boolean
        // borInterrogationRequested,
        // boolean suppressOCsi, ContinueWithArgumentArgExtension
        // continueWithArgumentArgExtension

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        LocationNumberImpl locationNumber = new LocationNumberImpl();
        locationNumber.setAddress("222333");
        LocationNumberCapImpl chargeNumber = new LocationNumberCapImpl(locationNumber);
        CarrierImpl carrier = new CarrierImpl(getCarrier());
        NAOliInfoImpl naOliInfo = new NAOliInfoImpl(11);
        ContinueWithArgumentArgExtensionImpl continueWithArgumentArgExtension = new ContinueWithArgumentArgExtensionImpl(true, false, false, null);
        elem = new ContinueWithArgumentRequestImpl(alertingPatternCap, CAPExtensionsTest.createTestCAPExtensions(), serviceInteractionIndicatorsTwo,
                callingPartysCategory, genericNumbers, cugInterlock, true, chargeNumber, carrier, true, naOliInfo, true, true, continueWithArgumentArgExtension);

        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        CallingPartyCategoryImpl callingPartyCategory = new CallingPartyCategoryImpl();
        callingPartyCategory.setCallingPartyCategory(CallingPartyCategory._CATEGORY_OL_RUSSIAN);
        CallingPartysCategoryInapImpl callingPartysCategory = new CallingPartysCategoryInapImpl(callingPartyCategory);
        ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(null, null,
                BothwayThroughConnectionInd.bothwayPathNotRequired, null, false, null, null, null);
        AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingLevel.Level2);
        AlertingPatternCapImpl alertingPatternCap = new AlertingPatternCapImpl(alertingPattern);
        ArrayList<GenericNumberCap> genericNumbers = new ArrayList<GenericNumberCap>();
        GenericNumberImpl genericNumber = new GenericNumberImpl();
        genericNumber.setAddress("111222");
        GenericNumberCapImpl gn = new GenericNumberCapImpl(genericNumber);
        genericNumbers.add(gn);
        LocationNumberImpl locationNumber = new LocationNumberImpl();
        locationNumber.setAddress("222333");
        LocationNumberCapImpl chargeNumber = new LocationNumberCapImpl(locationNumber);
//        CarrierImpl carrier = new CarrierImpl(getCarrier());
        NAOliInfoImpl naOliInfo = new NAOliInfoImpl(11);
        ContinueWithArgumentArgExtensionImpl continueWithArgumentArgExtension = new ContinueWithArgumentArgExtensionImpl(true, false, false, null);

        ContinueWithArgumentRequestImpl original = new ContinueWithArgumentRequestImpl(alertingPatternCap, CAPExtensionsTest.createTestCAPExtensions(),
                serviceInteractionIndicatorsTwo, callingPartysCategory, genericNumbers, null, true, chargeNumber, null, true, naOliInfo, true, true,
                continueWithArgumentArgExtension);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "continueWithArgumentRequest", ContinueWithArgumentRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ContinueWithArgumentRequestImpl copy = reader.read("continueWithArgumentRequest", ContinueWithArgumentRequestImpl.class);

        assertEquals(original.getAlertingPattern().getAlertingPattern().getAlertingLevel(), copy.getAlertingPattern().getAlertingPattern().getAlertingLevel());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(original.getExtensions()));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));
        assertEquals(original.getServiceInteractionIndicatorsTwo().getBothwayThroughConnectionInd(), copy.getServiceInteractionIndicatorsTwo()
                .getBothwayThroughConnectionInd());
        assertEquals(original.getCallingPartysCategory().getCallingPartyCategory().getCallingPartyCategory(), copy.getCallingPartysCategory()
                .getCallingPartyCategory().getCallingPartyCategory());
        assertEquals(original.getGenericNumbers().size(), copy.getGenericNumbers().size());
        GenericNumberCap gnn = original.getGenericNumbers().get(0);
        GenericNumberCap gnn2 = copy.getGenericNumbers().get(0);
        assertEquals(gnn.getGenericNumber().getAddress(), gnn2.getGenericNumber().getAddress());
//        assertEquals(original.getCugInterlock().getData(), copy.getCugInterlock().getData());
        assertEquals(original.getCugOutgoingAccess(), copy.getCugOutgoingAccess());
        assertEquals(original.getChargeNumber().getLocationNumber().getAddress(), copy.getChargeNumber().getLocationNumber().getAddress());
//        assertEquals(original.getCarrier().getData(), copy.getCarrier().getData());
        assertEquals(original.getSuppressionOfAnnouncement(), copy.getSuppressionOfAnnouncement());
        assertEquals((int) original.getNaOliInfo().getData(), (int) copy.getNaOliInfo().getData());
        assertEquals(original.getBorInterrogationRequested(), copy.getBorInterrogationRequested());
        assertEquals(original.getSuppressOCsi(), copy.getSuppressOCsi());
        assertEquals(original.getContinueWithArgumentArgExtension().getSuppressDCsi(), copy.getContinueWithArgumentArgExtension().getSuppressDCsi());
        assertEquals(original.getContinueWithArgumentArgExtension().getSuppressNCsi(), copy.getContinueWithArgumentArgExtension().getSuppressNCsi());
    }
}
