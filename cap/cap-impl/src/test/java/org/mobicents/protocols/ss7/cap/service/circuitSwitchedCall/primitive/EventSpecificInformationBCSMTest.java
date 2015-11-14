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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.EsiBcsm.CallAcceptedSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.DpSpecificInfoAltImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.MidCallEventsImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OAbandonSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OChangeOfPositionSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OMidCallSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OServiceChangeSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OTermSeizedSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TChangeOfPositionSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TMidCallSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TNoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.CallAcceptedSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.DpSpecificInfoAlt;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.MidCallEvents;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAbandonSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OServiceChangeSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OTermSeizedSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TChangeOfPositionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TMidCallSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class EventSpecificInformationBCSMTest {

    public byte[] getData1() {
        return new byte[] { (byte) 162, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 163, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData3() {
        return new byte[] { (byte) 164, 0 };
    }

    public byte[] getData4() {
        return new byte[] { (byte) 165, 0 };
    }

    public byte[] getData5() {
        return new byte[] { (byte) 167, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData6() {
        return new byte[] { (byte) 168, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData7() {
        return new byte[] { (byte) 169, 13, (byte) 159, 50, 0, (byte) 159, 52, 7, 3, (byte) 144, 33, 114, 16, (byte) 144, 0 };
    }

    public byte[] getData8() {
        return new byte[] { (byte) 170, 0 };
    }

    public byte[] getData9() {
        return new byte[] { (byte) 172, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData10() {
        return new byte[] { (byte) 181, 3, (byte) 159, 50, 0 };
    }

    public byte[] getData11() {
        return new byte[] { (byte) 166, 9, (byte) 161, 7, (byte) 131, 5, 99, 1, 2, 3, 4 };
    }

    public byte[] getData12() {
        return new byte[] { (byte) 171, 9, (byte) 161, 7, (byte) 131, 5, 99, 1, 2, 3, 4 };
    }

    public byte[] getData13() {
        return new byte[] { (byte) 173, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 135 };
    }

    public byte[] getData14() {
        return new byte[] { (byte) 180, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 135 };
    }

    public byte[] getData15() {
        return new byte[] { (byte) 191, 50, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 135 };
    }

    public byte[] getData16() {
        return new byte[] { (byte) 191, 51, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 135 };
    }

    public byte[] getData17() {
        return new byte[] { (byte) 191, 52, 7, (byte) 160, 5, (byte) 160, 3, (byte) 130, 1, 38 };
    }

    public byte[] getDigitsData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        EventSpecificInformationBCSMImpl elem = new EventSpecificInformationBCSMImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        CauseIndicators ci = elem.getRouteSelectFailureSpecificInfo().getFailureCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        ci = elem.getOCalledPartyBusySpecificInfo().getBusyCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);

        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertNotNull(elem.getONoAnswerSpecificInfo());

        data = this.getData4();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertNotNull(elem.getOAnswerSpecificInfo());

        data = this.getData5();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        ci = elem.getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);

        data = this.getData6();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        ci = elem.getTBusySpecificInfo().getBusyCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);
        assertFalse(elem.getTBusySpecificInfo().getCallForwarded());
        assertFalse(elem.getTBusySpecificInfo().getRouteNotPermitted());
        assertNull(elem.getTBusySpecificInfo().getForwardingDestinationNumber());

        data = this.getData7();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertTrue(elem.getTNoAnswerSpecificInfo().getCallForwarded());
        CalledPartyNumber cpn = elem.getTNoAnswerSpecificInfo().getForwardingDestinationNumber().getCalledPartyNumber();
        assertFalse(cpn.isOddFlag());
        assertEquals(cpn.getNumberingPlanIndicator(), 1);
        assertEquals(cpn.getInternalNetworkNumberIndicator(), 1);
        assertEquals(cpn.getNatureOfAddressIndicator(), 3);
        assertTrue(cpn.getAddress().equals("1227010900"));

        data = this.getData8();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertNotNull(elem.getTAnswerSpecificInfo());

        data = this.getData9();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        ci = elem.getTDisconnectSpecificInfo().getReleaseCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);

        data = this.getData10();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertTrue(elem.getOAbandonSpecificInfo().getRouteNotPermitted());

        data = this.getData11();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getOMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits(), getDigitsData());

        data = this.getData12();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getTMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits(), getDigitsData());

        data = this.getData13();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals((int) elem.getOTermSeizedSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), 135);

        data = this.getData14();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals((int) elem.getCallAcceptedSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), 135);

        data = this.getData15();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals((int) elem.getOChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), 135);

        data = this.getData16();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals((int) elem.getTChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), 135);

        data = this.getData17();
        ais = new AsnInputStream(data);
        elem = new EventSpecificInformationBCSMImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getDpSpecificInfoAlt().getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.padAccessCA_9600bps);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        CauseIndicators causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap failureCause = new CauseCapImpl(causeIndicators);
        RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfoImpl(failureCause);
        EventSpecificInformationBCSMImpl elem = new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap busyCause = new CauseCapImpl(causeIndicators);
        OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfoImpl = new OCalledPartyBusySpecificInfoImpl(busyCause);
        elem = new EventSpecificInformationBCSMImpl(oCalledPartyBusySpecificInfoImpl);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo = new ONoAnswerSpecificInfoImpl();
        elem = new EventSpecificInformationBCSMImpl(oNoAnswerSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));

        OAnswerSpecificInfoImpl oAnswerSpecificInfo = new OAnswerSpecificInfoImpl();
        elem = new EventSpecificInformationBCSMImpl(oAnswerSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData4()));

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap releaseCause = new CauseCapImpl(causeIndicators);
        ODisconnectSpecificInfoImpl oDisconnectSpecificInfo = new ODisconnectSpecificInfoImpl(releaseCause);
        elem = new EventSpecificInformationBCSMImpl(oDisconnectSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData5()));

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        busyCause = new CauseCapImpl(causeIndicators);
        TBusySpecificInfoImpl tBusySpecificInfo = new TBusySpecificInfoImpl(busyCause, false, false, null);
        elem = new EventSpecificInformationBCSMImpl(tBusySpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData6()));

        CalledPartyNumber cpn = new CalledPartyNumberImpl(3, "1227010900", 1, 1);
        // int natureOfAddresIndicator, String address, int
        // numberingPlanIndicator, int internalNetworkNumberIndicator
        CalledPartyNumberCap cpnc = new CalledPartyNumberCapImpl(cpn);
        TNoAnswerSpecificInfoImpl tNoAnswerSpecificInfo = new TNoAnswerSpecificInfoImpl(true, cpnc);
        elem = new EventSpecificInformationBCSMImpl(tNoAnswerSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData7()));

        TAnswerSpecificInfoImpl tAnswerSpecificInfo = new TAnswerSpecificInfoImpl();
        elem = new EventSpecificInformationBCSMImpl(tAnswerSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData8()));
        
        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        releaseCause = new CauseCapImpl(causeIndicators);
        TDisconnectSpecificInfoImpl tDisconnectSpecificInfo = new TDisconnectSpecificInfoImpl(releaseCause);
        elem = new EventSpecificInformationBCSMImpl(tDisconnectSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData9()));

        OAbandonSpecificInfo oAbandonSpecificInfo = new OAbandonSpecificInfoImpl(true);
        elem = new EventSpecificInformationBCSMImpl(oAbandonSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData10()));


        GenericDigits genericDigits = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BINARY, GenericDigits._TOD_BGCI, getDigitsData());
        Digits dtmfDigits = new DigitsImpl(genericDigits);
        MidCallEvents midCallEvents = new MidCallEventsImpl(dtmfDigits, true);
        OMidCallSpecificInfoImpl oMidCallSpecificInfo = new OMidCallSpecificInfoImpl(midCallEvents);
        elem = new EventSpecificInformationBCSMImpl(oMidCallSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData11()));

        TMidCallSpecificInfo tMidCallSpecificInfo = new TMidCallSpecificInfoImpl(midCallEvents);
        elem = new EventSpecificInformationBCSMImpl(tMidCallSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData12()));

        LocationInformation locationInformation = new LocationInformationImpl(135, null, null, null, null, null, null, null, null, false, false, null, null);
        OTermSeizedSpecificInfo oTermSeizedSpecificInfo = new OTermSeizedSpecificInfoImpl(locationInformation);
        elem = new EventSpecificInformationBCSMImpl(oTermSeizedSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData13()));

        CallAcceptedSpecificInfo callAcceptedSpecificInfo = new CallAcceptedSpecificInfoImpl(locationInformation);
        elem = new EventSpecificInformationBCSMImpl(callAcceptedSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData14()));

        OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo = new OChangeOfPositionSpecificInfoImpl(locationInformation, null);
        elem = new EventSpecificInformationBCSMImpl(oChangeOfPositionSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData15()));

        TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo = new TChangeOfPositionSpecificInfoImpl(locationInformation, null);
        elem = new EventSpecificInformationBCSMImpl(tChangeOfPositionSpecificInfo);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData16()));

        ExtBearerServiceCode extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl(extBearerService);
        OServiceChangeSpecificInfo oServiceChangeSpecificInfo = new OServiceChangeSpecificInfoImpl(extBasicServiceCode);
        DpSpecificInfoAlt dpSpecificInfoAlt = new DpSpecificInfoAltImpl(oServiceChangeSpecificInfo, null, null);
        elem = new EventSpecificInformationBCSMImpl(dpSpecificInfoAlt);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData17()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        CauseIndicators causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap failureCause = new CauseCapImpl(causeIndicators);
        RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfoImpl(failureCause);
        EventSpecificInformationBCSMImpl original = new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        EventSpecificInformationBCSMImpl copy = reader.read("eventSpecificInformationBCSM",
                EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getRouteSelectFailureSpecificInfo().getFailureCause().getCauseIndicators().getCauseValue(), original
                .getRouteSelectFailureSpecificInfo().getFailureCause().getCauseIndicators().getCauseValue());

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap busyCause = new CauseCapImpl(causeIndicators);
        OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfoImpl = new OCalledPartyBusySpecificInfoImpl(busyCause);
        original = new EventSpecificInformationBCSMImpl(oCalledPartyBusySpecificInfoImpl);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getOCalledPartyBusySpecificInfo().getBusyCause().getCauseIndicators().getCauseValue(), original
                .getOCalledPartyBusySpecificInfo().getBusyCause().getCauseIndicators().getCauseValue());

        ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo = new ONoAnswerSpecificInfoImpl();
        original = new EventSpecificInformationBCSMImpl(oNoAnswerSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertNotNull(copy.getONoAnswerSpecificInfo());

        OAnswerSpecificInfoImpl oAnswerSpecificInfo = new OAnswerSpecificInfoImpl();
        original = new EventSpecificInformationBCSMImpl(oAnswerSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertNotNull(copy.getOAnswerSpecificInfo());

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap releaseCause = new CauseCapImpl(causeIndicators);
        ODisconnectSpecificInfoImpl oDisconnectSpecificInfo = new ODisconnectSpecificInfoImpl(releaseCause);
        original = new EventSpecificInformationBCSMImpl(oDisconnectSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCauseValue(), original
                .getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCauseValue());

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        busyCause = new CauseCapImpl(causeIndicators);
        TBusySpecificInfoImpl tBusySpecificInfo = new TBusySpecificInfoImpl(busyCause, false, false, null);
        original = new EventSpecificInformationBCSMImpl(tBusySpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTBusySpecificInfo().getBusyCause().getCauseIndicators().getCauseValue(), original
                .getTBusySpecificInfo().getBusyCause().getCauseIndicators().getCauseValue());

        CalledPartyNumber cpn = new CalledPartyNumberImpl(3, "1227010900", 1, 1);
        // int natureOfAddresIndicator, String address, int
        // numberingPlanIndicator, int internalNetworkNumberIndicator
        CalledPartyNumberCap cpnc = new CalledPartyNumberCapImpl(cpn);
        TNoAnswerSpecificInfoImpl tNoAnswerSpecificInfo = new TNoAnswerSpecificInfoImpl(true, cpnc);
        original = new EventSpecificInformationBCSMImpl(tNoAnswerSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTNoAnswerSpecificInfo().getForwardingDestinationNumber().getCalledPartyNumber().getAddress(),
                original.getTNoAnswerSpecificInfo().getForwardingDestinationNumber().getCalledPartyNumber().getAddress());

        CalledPartyNumber calledPartyNumber = new CalledPartyNumberImpl();
        calledPartyNumber.setAddress("73645");
        calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
        calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
        calledPartyNumber.setNatureOfAddresIndicator(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
        CalledPartyNumberCap destinationAddress = new CalledPartyNumberCapImpl(calledPartyNumber);
        TAnswerSpecificInfoImpl tAnswerSpecificInfo = new TAnswerSpecificInfoImpl(destinationAddress, false, true, null, null,
                null);
        // CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall,
        // ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode,
        // ExtBasicServiceCode extBasicServiceCode2
        original = new EventSpecificInformationBCSMImpl(tAnswerSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTAnswerSpecificInfo().getDestinationAddress().getCalledPartyNumber().getAddress(), original
                .getTAnswerSpecificInfo().getDestinationAddress().getCalledPartyNumber().getAddress());
        assertEquals(copy.getTAnswerSpecificInfo().getForwardedCall(), original.getTAnswerSpecificInfo().getForwardedCall());
        assertEquals(copy.getTAnswerSpecificInfo().getOrCall(), original.getTAnswerSpecificInfo().getOrCall());

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        releaseCause = new CauseCapImpl(causeIndicators);
        TDisconnectSpecificInfoImpl tDisconnectSpecificInfo = new TDisconnectSpecificInfoImpl(releaseCause);
        original = new EventSpecificInformationBCSMImpl(tDisconnectSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTDisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCauseValue(), original
                .getTDisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCauseValue());

        OAbandonSpecificInfo oAbandonSpecificInfo = new OAbandonSpecificInfoImpl(true);
        original = new EventSpecificInformationBCSMImpl(oAbandonSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getOAbandonSpecificInfo().getRouteNotPermitted(), original.getOAbandonSpecificInfo()
                .getRouteNotPermitted());


        GenericDigits genericDigits = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BINARY, GenericDigits._TOD_BGCI, getDigitsData());
        Digits dtmfDigits = new DigitsImpl(genericDigits);
        MidCallEvents midCallEvents = new MidCallEventsImpl(dtmfDigits, true);
        OMidCallSpecificInfoImpl oMidCallSpecificInfo = new OMidCallSpecificInfoImpl(midCallEvents);
        original = new EventSpecificInformationBCSMImpl(oMidCallSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getOMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits(), original
                .getOMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits());


        TMidCallSpecificInfo tMidCallSpecificInfo = new TMidCallSpecificInfoImpl(midCallEvents);
        original = new EventSpecificInformationBCSMImpl(tMidCallSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits(), original
                .getTMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits());


        LocationInformation locationInformation = new LocationInformationImpl(135, null, null, null, null, null, null, null, null, false, false, null, null);
        OTermSeizedSpecificInfo oTermSeizedSpecificInfo = new OTermSeizedSpecificInfoImpl(locationInformation);
        original = new EventSpecificInformationBCSMImpl(oTermSeizedSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals((int) copy.getOTermSeizedSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), (int) original
                .getOTermSeizedSpecificInfo().getLocationInformation().getAgeOfLocationInformation());


        CallAcceptedSpecificInfo callAcceptedSpecificInfo = new CallAcceptedSpecificInfoImpl(locationInformation);
        original = new EventSpecificInformationBCSMImpl(callAcceptedSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals((int) copy.getCallAcceptedSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), (int) original
                .getCallAcceptedSpecificInfo().getLocationInformation().getAgeOfLocationInformation());


        OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo = new OChangeOfPositionSpecificInfoImpl(locationInformation, null);
        original = new EventSpecificInformationBCSMImpl(oChangeOfPositionSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals((int) copy.getOChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), (int) original
                .getOChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation());


        TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo = new TChangeOfPositionSpecificInfoImpl(locationInformation, null);
        original = new EventSpecificInformationBCSMImpl(tChangeOfPositionSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals((int) copy.getTChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), (int) original
                .getTChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation());


        ExtBearerServiceCode extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl(extBearerService);
        OServiceChangeSpecificInfo oServiceChangeSpecificInfo = new OServiceChangeSpecificInfoImpl(extBasicServiceCode);
        DpSpecificInfoAlt dpSpecificInfoAlt = new DpSpecificInfoAltImpl(oServiceChangeSpecificInfo, null, null);
        original = new EventSpecificInformationBCSMImpl(dpSpecificInfoAlt);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getDpSpecificInfoAlt().getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue(),
                original.getDpSpecificInfoAlt().getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue());

    }
}
