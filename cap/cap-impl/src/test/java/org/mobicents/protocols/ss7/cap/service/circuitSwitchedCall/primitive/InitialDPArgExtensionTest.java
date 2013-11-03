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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InitialDPArgExtensionTest {

    public byte[] getData1() {
        return new byte[] { (byte) 191, 59, 8, (byte) 129, 6, (byte) 145, 34, 112, 87, 0, 112 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 191, 59, 12, (byte) 128, 4, (byte) 152, 17, 17, 17, (byte) 129, 4, 1, 16, 34, 34 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        InitialDPArgExtensionImpl elem = new InitialDPArgExtensionImpl(false);
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getGmscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getGmscAddress().getAddress().equals("2207750007"));
        assertNull(elem.getForwardingDestinationNumber());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new InitialDPArgExtensionImpl(true);
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getGmscAddress().getNumberingPlan(), NumberingPlan.national);
        assertTrue(elem.getGmscAddress().getAddress().equals("111111"));
        CalledPartyNumber cpn = elem.getForwardingDestinationNumber().getCalledPartyNumber();
        assertTrue(cpn.getAddress().equals("2222"));
        assertEquals(cpn.getInternalNetworkNumberIndicator(), 0);
        assertEquals(cpn.getNatureOfAddressIndicator(), 1);
        assertEquals(cpn.getNumberingPlanIndicator(), 1);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        InitialDPArgExtensionImpl elem = new InitialDPArgExtensionImpl(gmscAddress, null, null, null, null, null, null, null,
                null, null, null, false, null, false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 59);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.national, "111111");
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(1, "2222", 1, 0);
        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator
        CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
        elem = new InitialDPArgExtensionImpl(gmscAddress, forwardingDestinationNumber, null, null, null, null, null, null,
                null, null, null, false, null, true);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 59);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        // ISDNAddressString gmscAddress, CalledPartyNumberCap forwardingDestinationNumber, MSClassmark2 msClassmark2, IMEI
        // imei,
        // SupportedCamelPhases supportedCamelPhases, OfferedCamel4Functionalities offeredCamel4Functionalities,
        // BearerCapability bearerCapability2,
        // ExtBasicServiceCode extBasicServiceCode2, HighLayerCompatibilityInap highLayerCompatibility2, LowLayerCompatibility
        // lowLayerCompatibility,
        // LowLayerCompatibility lowLayerCompatibility2, boolean enhancedDialledServicesAllowed, UUData uuData, boolean
        // isCAPVersion3orLater
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerialize() throws Exception {

        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(1, "2222", 1, 0);
        CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
        InitialDPArgExtensionImpl original = new InitialDPArgExtensionImpl(gmscAddress, forwardingDestinationNumber, null,
                null, null, null, null, null, null, null, null, false, null, false);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "initialDPArgExtension", InitialDPArgExtensionImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        InitialDPArgExtensionImpl copy = reader.read("initialDPArgExtension", InitialDPArgExtensionImpl.class);

        assertEquals(copy.getGmscAddress().getAddress(), original.getGmscAddress().getAddress());
        assertEquals(copy.getForwardingDestinationNumber().getCalledPartyNumber().getAddress(), original
                .getForwardingDestinationNumber().getCalledPartyNumber().getAddress());

    }
}
