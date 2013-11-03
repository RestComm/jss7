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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationNumberMapTest {

    private byte[] getData() {
        return new byte[] { -126, 8, -125, -63, 8, 2, -105, 1, 32, 0 };
    }

    private byte[] getIntData() {
        return new byte[] { -125, -63, 8, 2, -105, 1, 32, 0 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] rawData = getData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        LocationNumberMapImpl impl = new LocationNumberMapImpl();
        impl.decodeAll(asn);
        LocationNumber ln = impl.getLocationNumber();

        assertTrue(Arrays.equals(impl.getData(), this.getIntData()));
        assertEquals(ln.getNatureOfAddressIndicator(), LocationNumber._NAI_NATIONAL_SN);
        assertTrue(ln.getAddress().equals("80207910020"));
        assertEquals(ln.getNumberingPlanIndicator(), LocationNumber._NPI_TELEX);
        assertEquals(ln.getInternalNetworkNumberIndicator(), LocationNumber._INN_ROUTING_NOT_ALLOWED);
        assertEquals(ln.getAddressRepresentationRestrictedIndicator(), LocationNumber._APRI_ALLOWED);
        assertEquals(ln.getScreeningIndicator(), LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        LocationNumberMapImpl impl = new LocationNumberMapImpl(this.getIntData());
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 2);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getData();
        assertTrue(Arrays.equals(rawData, encodedData));

        LocationNumberImpl ln = new LocationNumberImpl(LocationNumber._NAI_NATIONAL_SN, "80207910020",
                LocationNumber._NPI_TELEX, LocationNumber._INN_ROUTING_NOT_ALLOWED, LocationNumber._APRI_ALLOWED,
                LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);
        impl = new LocationNumberMapImpl(ln);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 2);
        encodedData = asnOS.toByteArray();
        rawData = getData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator, int
        // addressRepresentationREstrictedIndicator,
        // int screeningIndicator
    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        LocationNumberImpl ln = new LocationNumberImpl(LocationNumber._NAI_NATIONAL_SN, "80207910020",
                LocationNumber._NPI_TELEX, LocationNumber._INN_ROUTING_NOT_ALLOWED, LocationNumber._APRI_ALLOWED,
                LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);
        LocationNumberMapImpl original = new LocationNumberMapImpl(ln);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "locationNumberMap", LocationNumberMapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        LocationNumberMapImpl copy = reader.read("locationNumberMap", LocationNumberMapImpl.class);

        assertEquals(copy.getLocationNumber().getAddress(), original.getLocationNumber().getAddress());
        assertEquals(copy.getLocationNumber().getAddressRepresentationRestrictedIndicator(), original.getLocationNumber()
                .getAddressRepresentationRestrictedIndicator());
        assertEquals(copy.getLocationNumber().getInternalNetworkNumberIndicator(), original.getLocationNumber()
                .getInternalNetworkNumberIndicator());
        assertEquals(copy.getLocationNumber().getNatureOfAddressIndicator(), original.getLocationNumber()
                .getNatureOfAddressIndicator());
        assertEquals(copy.getLocationNumber().getNumberingPlanIndicator(), original.getLocationNumber()
                .getNumberingPlanIndicator());
        assertEquals(copy.getLocationNumber().getScreeningIndicator(), original.getLocationNumber().getScreeningIndicator());

    }

}
