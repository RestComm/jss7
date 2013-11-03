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
import org.mobicents.protocols.ss7.cap.EsiBcsm.OAbandonSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TNoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAbandonSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
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
    }
}
