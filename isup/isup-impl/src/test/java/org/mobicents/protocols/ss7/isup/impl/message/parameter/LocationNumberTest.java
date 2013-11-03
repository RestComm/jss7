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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class LocationNumberTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private byte[] getData() {
        return new byte[] { (byte) 131, (byte) 193, 0x21, 0x43, 0x05 };
    }

    private byte[] getData2() {
        return new byte[] { 0, 11 };
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        LocationNumberImpl prim = new LocationNumberImpl();
        prim.decode(getData());

        assertEquals(prim.getAddress(), "12345");
        assertEquals(prim.getNatureOfAddressIndicator(), LocationNumber._NAI_NATIONAL_SN);
        assertEquals(prim.getNumberingPlanIndicator(), LocationNumber._NPI_TELEX);
        assertEquals(prim.getInternalNetworkNumberIndicator(), LocationNumber._INN_ROUTING_NOT_ALLOWED);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), LocationNumber._APRI_ALLOWED);
        assertEquals(prim.getScreeningIndicator(), LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);
        assertTrue(prim.isOddFlag());

        prim = new LocationNumberImpl();
        prim.decode(getData2());

        assertEquals(prim.getAddress(), "");
        assertEquals(prim.getNatureOfAddressIndicator(), 0);
        assertEquals(prim.getNumberingPlanIndicator(), 0);
        assertEquals(prim.getInternalNetworkNumberIndicator(), 0);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), LocationNumber._APRI_NOT_AVAILABLE);
        assertEquals(prim.getScreeningIndicator(), LocationNumber._SI_NETWORK_PROVIDED);
        assertFalse(prim.isOddFlag());

    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        LocationNumberImpl prim = new LocationNumberImpl(LocationNumber._NAI_NATIONAL_SN, "12345", LocationNumber._NPI_TELEX,
                LocationNumber._INN_ROUTING_NOT_ALLOWED, LocationNumber._APRI_ALLOWED,
                LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);
        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator,
        // int addressRepresentationREstrictedIndicator, int screeningIndicator

        byte[] data = getData();
        byte[] encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        prim = new LocationNumberImpl(0, "", 0, 0, LocationNumber._APRI_NOT_AVAILABLE, LocationNumber._SI_NETWORK_PROVIDED);

        data = getData2();
        encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        LocationNumberImpl original = new LocationNumberImpl(LocationNumber._NAI_NATIONAL_SN, "12345",
                LocationNumber._NPI_TELEX, LocationNumber._INN_ROUTING_NOT_ALLOWED, LocationNumber._APRI_ALLOWED,
                LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "locationNumber", LocationNumberImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        LocationNumberImpl copy = reader.read("locationNumber", LocationNumberImpl.class);

        assertEquals(copy.getNatureOfAddressIndicator(), original.getNatureOfAddressIndicator());
        assertEquals(copy.getAddress(), original.getAddress());
        assertEquals(copy.getNumberingPlanIndicator(), original.getNumberingPlanIndicator());
        assertEquals(copy.getInternalNetworkNumberIndicator(), original.getInternalNetworkNumberIndicator());
        assertEquals(copy.getAddressRepresentationRestrictedIndicator(), original.getAddressRepresentationRestrictedIndicator());
        assertEquals(copy.getScreeningIndicator(), original.getScreeningIndicator());
        assertEquals(copy.isOddFlag(), original.isOddFlag());
    }

    // /**
    // * @throws IOException
    // */
    // public LocationNumberTest() throws IOException {
    // super.badBodies.add(new byte[1]);
    //
    // // super.goodBodies.add(getBody( false, LocationNumber._NAI_NATIONAL_SN, LocationNumberImpl._INN_ROUTING_ALLOWED,
    // LocationNumberImpl._NPI_ISDN,
    // // LocationNumberImpl._APRI_NOT_AVAILABLE, LocationNumberImpl._SI_NETWORK_PROVIDED, getSixDigits()));
    // super.goodBodies.add(getBody( false, LocationNumber._NAI_NATIONAL_SN, LocationNumberImpl._INN_ROUTING_ALLOWED,
    // LocationNumberImpl._NPI_ISDN,
    // LocationNumberImpl._APRI_ALLOWED, LocationNumberImpl._SI_NETWORK_PROVIDED, getSixDigits()));
    //
    // }
    //
    // private byte[] getBody(boolean isODD, int _NAI, int _INN, int _NPI, int _APR, int _SI, byte[] digits) throws IOException
    // {
    // ByteArrayOutputStream bos = new ByteArrayOutputStream();
    // // we will use odd number of digits, so we leave zero as MSB
    //
    // int nai = _NAI;
    // if (isODD)
    // nai |= 0x01 << 7;
    // int bit3 = _SI;
    // bit3 |= _APR << 2;
    // bit3 |= _NPI << 4;
    // bit3 |= _INN << 7;
    //
    //
    // bos.write(nai);
    // bos.write(bit3);
    // bos.write(digits);
    // return bos.toByteArray();
    // }
    // @Test(groups = { "functional.encode","functional.decode","parameter"})
    // public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
    // IllegalAccessException, InvocationTargetException, IOException, ParameterException {
    // LocationNumberImpl bci = new LocationNumberImpl(getBody( false, LocationNumber._NAI_NATIONAL_SN,
    // LocationNumberImpl._INN_ROUTING_ALLOWED, LocationNumberImpl._NPI_ISDN,
    // LocationNumberImpl._APRI_NOT_AVAILABLE, LocationNumberImpl._SI_NETWORK_PROVIDED, getSixDigits()));
    //
    // String[] methodNames = { "isOddFlag", "getNatureOfAddressIndicator", "getInternalNetworkNumberIndicator",
    // "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator",
    // "getScreeningIndicator", "getAddress" };
    // Object[] expectedValues = { false, LocationNumber._NAI_NATIONAL_SN, LocationNumberImpl._INN_ROUTING_ALLOWED,
    // LocationNumberImpl._NPI_ISDN,
    // LocationNumberImpl._APRI_NOT_AVAILABLE, LocationNumberImpl._SI_NETWORK_PROVIDED, getSixDigitsString() };
    // super.testValues(bci, methodNames, expectedValues);
    // }
    //
    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // * org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent
    // * ()
    // */
    //
    // public AbstractISUPParameter getTestedComponent() {
    // // return new LocationNumberImpl(1,"1",1,1,1,1);
    // return new LocationNumberImpl(LocationNumber._NAI_NATIONAL_SN, getSixDigitsString(), LocationNumberImpl._NPI_ISDN,
    // LocationNumberImpl._INN_ROUTING_ALLOWED, LocationNumberImpl._APRI_ALLOWED, LocationNumberImpl._SI_NETWORK_PROVIDED);
    // // public LocationNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator, int
    // internalNetworkNumberIndicator, int addressRepresentationREstrictedIndicator,
    // // int screeningIndicator) {
    // }

}
