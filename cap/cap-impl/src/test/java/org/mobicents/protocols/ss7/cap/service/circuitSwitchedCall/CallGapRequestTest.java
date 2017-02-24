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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.gap.*;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CriticalityType;
import org.mobicents.protocols.ss7.cap.api.primitives.ExtensionField;
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.*;
import org.mobicents.protocols.ss7.cap.gap.*;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.ExtensionFieldImpl;
import org.mobicents.protocols.ss7.cap.primitives.ScfIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.*;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.*;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 *
 */
public class CallGapRequestTest {

    public static final int SERVICE_KEY = 821;
    public static final int DURATION = 60;
    public static final int GAP_INTERVAL = 1;

    public static final int ELEMENTARY_MESSAGE_ID = 1;
    public static final int NUMBER_OF_REPETITIONS = 5;
    public static final int DURATION_IF = 5;
    public static final int INTERVAL = 5;

    // Max parameters
    public byte[] getData() {
        return new byte[] { 48, 71, (byte) 160, 22, 48, 20, (byte) 160, 12, (byte) 189, 10, (byte) 128, 4, 48, 69, 91, 84,
                (byte) 129, 2, 3, 53, (byte) 129, 4, 12, 32, 23, 56, (byte) 161, 6, (byte) 128, 1, 60, (byte) 129,
                1, (byte) 255, (byte) 130, 1, 0, (byte) 163, 18, (byte) 160, 16, (byte) 160, 14, (byte) 160, 3, (byte) 128, 1, 1,
                (byte) 129, 1, 5, (byte) 130, 1, 5, (byte) 131, 1, 5, (byte) 164, 14, 48, 12, 2, 4, (byte) 128,
                0, 0, 0, (byte) 129, 4, 13, 14, 15, 16};
    }

    public byte[] getData2() {
        return new byte[] { 48, 22, (byte) 160, 12, (byte) 189, 10, (byte) 128, 4, 48, 69, 91, 84, (byte) 129, 2, 3, 53,
                (byte) 161, 6, (byte) 128, 1, 60, (byte) 129, 1, (byte) 255 };
    }

    // Digits for CalledAddressAndService
    public byte[] getDigitsData() {
        return new byte[] {48, 69, 91, 84};
    }

    // Digits for ScfID
    public byte[] getDigitsData1() {
        return new byte[] {12, 32, 23, 56};
    }

    // Digits for ExtensionField
    public byte[] getDigitsData2() {
        return new byte[] {13, 14, 15, 16};
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream ais = new AsnInputStream(data);
        CallGapRequestImpl elem = new CallGapRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getCalledAddressValue().getData(), getDigitsData());
        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getScfID().getData(), getDigitsData1());

        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);

        assertEquals(elem.getControlType(), ControlType.sCPOverloaded);

        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getMessageID().getElementaryMessageID().intValue(), ELEMENTARY_MESSAGE_ID);
        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getNumberOfRepetitions().intValue(), NUMBER_OF_REPETITIONS);
        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getDuration().intValue(), DURATION_IF);
        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getInterval().intValue(), INTERVAL);

        assertEquals(elem.getExtensions().getExtensionFields().get(0).getLocalCode().intValue(), Integer.MIN_VALUE);
        assertEquals(elem.getExtensions().getExtensionFields().get(0).getCriticalityType(), CriticalityType.typeIgnore);
        assertEquals(elem.getExtensions().getExtensionFields().get(0).getData(), getDigitsData2());


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new CallGapRequestImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getCalledAddressValue().getData(), getDigitsData());

        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);

        assertNull(elem.getControlType());
        assertNull(elem.getGapTreatment());
        assertNull(elem.getExtensions());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        Digits digits = new DigitsImpl(getDigitsData());
        CalledAddressAndService calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
        ScfID scfId = new ScfIDImpl(getDigitsData1());
        CompoundCriteria compoundCriteria = new CompoundCriteriaImpl(basicGapCriteria, scfId);
        GapCriteria gapCriteria = new GapCriteriaImpl(compoundCriteria);

        GapIndicators gapIndicators = new GapIndicatorsImpl(DURATION, -GAP_INTERVAL);

        MessageID messageID = new MessageIDImpl(ELEMENTARY_MESSAGE_ID);
        InbandInfo inbandInfo = new InbandInfoImpl(messageID, NUMBER_OF_REPETITIONS, DURATION_IF, INTERVAL);
        InformationToSend informationToSend = new InformationToSendImpl(inbandInfo);
        GapTreatment gapTreatment = new GapTreatmentImpl(informationToSend);

        ArrayList<ExtensionField> fieldsList = new ArrayList<>();
        ExtensionField extensionField = new ExtensionFieldImpl(Integer.MIN_VALUE, CriticalityType.typeIgnore, getDigitsData2());
        fieldsList.add(extensionField);
        CAPExtensions cAPExtensions = new CAPExtensionsImpl(fieldsList);

        CallGapRequestImpl elem = new CallGapRequestImpl(gapCriteria, gapIndicators, ControlType.sCPOverloaded, gapTreatment, cAPExtensions);

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));


        gapCriteria = new GapCriteriaImpl(basicGapCriteria);
        elem = new CallGapRequestImpl(gapCriteria, gapIndicators, null, null, null);

        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        //Min parameters
        GenericNumberImpl gn = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        Digits digits = new DigitsImpl(gn);

        BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(digits);

        GapCriteriaImpl gapCriteria = new GapCriteriaImpl(basicGapCriteria);

        GapIndicatorsImpl gapIndicators = new GapIndicatorsImpl(60, -1);

        CallGapRequestImpl original = new CallGapRequestImpl(gapCriteria, gapIndicators, null, null, null);
        original.setInvokeId(24);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "callGapArg", CallGapRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);

        CallGapRequestImpl copy = reader.read("callGapArg", CallGapRequestImpl.class);

        assertTrue(isEqual(original, copy));


        //Max paramters
        gn = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        digits = new DigitsImpl(gn);

        CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
//        GapOnService gapOnService = new GapOnServiceImpl(SERVICE_KEY);
        basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
        byte[] data2 = new byte[]{12, 32, 23, 56};
        ScfIDImpl scfId = new ScfIDImpl(data2);
        CompoundCriteriaImpl compoundCriteria = new CompoundCriteriaImpl(basicGapCriteria, null);
        gapCriteria = new GapCriteriaImpl(compoundCriteria);

        gapIndicators = new GapIndicatorsImpl(60, -1);

        ArrayList<VariablePart> aL = new ArrayList<VariablePart>();
        aL.add(new VariablePartImpl(new VariablePartDateImpl(2015, 6, 27)));
        aL.add(new VariablePartImpl(new VariablePartTimeImpl(15, 10)));
        aL.add(new VariablePartImpl(new Integer(145)));
        VariableMessageImpl vm = new VariableMessageImpl(145, aL);
        MessageIDImpl mi = new MessageIDImpl(vm);
        InbandInfoImpl inbandInfo = new InbandInfoImpl(mi, new Integer(5), new Integer(8), new Integer(2));
        InformationToSendImpl informationToSend = new InformationToSendImpl(inbandInfo);
        GapTreatmentImpl gapTreatment = new GapTreatmentImpl(informationToSend);

        ArrayList<ExtensionField> fieldsList = new ArrayList<>();
        byte[] data3 = new byte[]{13, 14, 15, 16};
        ExtensionFieldImpl extensionField = new ExtensionFieldImpl(Integer.MIN_VALUE, CriticalityType.typeIgnore, data3);
        fieldsList.add(extensionField);
        CAPExtensionsImpl cAPExtensions = new CAPExtensionsImpl(fieldsList);

        original = new CallGapRequestImpl(gapCriteria, gapIndicators, ControlType.sCPOverloaded, gapTreatment, cAPExtensions);
        original.setInvokeId(24);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "callGapArg", CallGapRequestImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        copy = reader.read("callGapArg", CallGapRequestImpl.class);

        assertTrue(isEqual(original, copy));
    }

    private boolean isEqual(CallGapRequestImpl o1, CallGapRequestImpl o2) {
        if (o1 == o2)
            return true;
        if (o1 == null && o2 != null || o1 != null && o2 == null)
            return false;
        if (o1 == null && o2 == null)
            return true;
        if (!o1.toString().equals(o2.toString()))
            return false;
        return true;
    }
}
