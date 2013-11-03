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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
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
public class TBusySpecificInfoTest {

    public byte[] getData1() {
        return new byte[] { (byte) 168, 20, (byte) 128, 2, (byte) 132, (byte) 144, (byte) 159, 50, 0, (byte) 159, 51, 0,
                (byte) 159, 52, 7, (byte) 128, (byte) 144, 17, 33, 34, 51, 3 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        EventSpecificInformationBCSMImpl elem = new EventSpecificInformationBCSMImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        CauseIndicators ci = elem.getTBusySpecificInfo().getBusyCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);
        assertTrue(elem.getTBusySpecificInfo().getCallForwarded());
        assertTrue(elem.getTBusySpecificInfo().getRouteNotPermitted());
        CalledPartyNumberCap fdn = elem.getTBusySpecificInfo().getForwardingDestinationNumber();
        CalledPartyNumber cpn = fdn.getCalledPartyNumber();
        assertTrue(cpn.getAddress().endsWith("111222333"));
        assertEquals(cpn.getNatureOfAddressIndicator(), 0);
        assertEquals(cpn.getNumberingPlanIndicator(), 1);
        assertEquals(cpn.getInternalNetworkNumberIndicator(), 1);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        CauseIndicators causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap busyCause = new CauseCapImpl(causeIndicators);
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(0, "111222333", 1, 1);
        CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
        TBusySpecificInfoImpl tBusySpecificInfo = new TBusySpecificInfoImpl(busyCause, true, true, forwardingDestinationNumber);
        EventSpecificInformationBCSMImpl elem = new EventSpecificInformationBCSMImpl(tBusySpecificInfo);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // int natureOfAddresIndicator, String address, int
        // numberingPlanIndicator, int internalNetworkNumberIndicator
        // CauseCap busyCause, boolean callForwarded, boolean routeNotPermitted,
        // CalledPartyNumberCap forwardingDestinationNumber
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        CauseIndicators causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap busyCause = new CauseCapImpl(causeIndicators);
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(0, "111222333", 1, 1);
        CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
        TBusySpecificInfoImpl original = new TBusySpecificInfoImpl(busyCause, true, true, forwardingDestinationNumber);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "tBusySpecificInfo", TBusySpecificInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        TBusySpecificInfoImpl copy = reader.read("tBusySpecificInfo", TBusySpecificInfoImpl.class);

        assertEquals(copy.getForwardingDestinationNumber().getCalledPartyNumber().getAddress(), original
                .getForwardingDestinationNumber().getCalledPartyNumber().getAddress());
        assertEquals(copy.getCallForwarded(), original.getCallForwarded());
        assertEquals(copy.getRouteNotPermitted(), original.getRouteNotPermitted());
        assertEquals(copy.getBusyCause().getCauseIndicators().getCauseValue(), original.getBusyCause().getCauseIndicators()
                .getCauseValue());
    }
}
