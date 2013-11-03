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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.mobicents.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationInformationEPSTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 54, -128, 7, 11, 12, 13, 14, 15, 16, 17, -127, 5, 21, 22, 23, 24, 25, -125, 8, 31, 32, 33, 34,
                35, 36, 37, 38, -124, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -123, 0, -122, 1, 5, -121, 9, 41, 42, 43, 44, 45, 46,
                47, 48, 49 };
    }

    private byte[] getEncodedDataEUtranCgi() {
        return new byte[] { 11, 12, 13, 14, 15, 16, 17 };
    }

    private byte[] getTAId() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    private byte[] getGeographicalInformation() {
        return new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    private byte[] getDiameterIdentity() {
        return new byte[] { 41, 42, 43, 44, 45, 46, 47, 48, 49 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        LocationInformationEPSImpl impl = new LocationInformationEPSImpl();
        impl.decodeAll(asn);
        assertEquals(tag, Tag.SEQUENCE);

        assertTrue(Arrays.equals(impl.getEUtranCellGlobalIdentity().getData(), this.getEncodedDataEUtranCgi()));
        assertTrue(Arrays.equals(impl.getTrackingAreaIdentity().getData(), this.getTAId()));
        assertTrue(Arrays.equals(impl.getGeographicalInformation().getData(), this.getGeographicalInformation()));
        assertTrue(Arrays.equals(impl.getGeodeticInformation().getData(), this.getGeodeticInformation()));
        assertTrue(impl.getCurrentLocationRetrieved());
        assertEquals((int) impl.getAgeOfLocationInformation(), 5);
        assertTrue(Arrays.equals(impl.getMmeName().getData(), this.getDiameterIdentity()));
        // public DiameterIdentity getMmeName();

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        EUtranCgiImpl euc = new EUtranCgiImpl(this.getEncodedDataEUtranCgi());
        TAIdImpl ta = new TAIdImpl(this.getTAId());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(this.getGeographicalInformation());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(this.getGeodeticInformation());
        DiameterIdentityImpl di = new DiameterIdentityImpl(this.getDiameterIdentity());
        LocationInformationEPSImpl impl = new LocationInformationEPSImpl(euc, ta, null, ggi, gdi, true, 5, di);
        // EUtranCgi eUtranCellGlobalIdentity, TAId trackingAreaIdentity, MAPExtensionContainer extensionContainer,
        // GeographicalInformation geographicalInformation, GeodeticInformation geodeticInformation, boolean
        // currentLocationRetrieved,
        // Integer ageOfLocationInformation, DiameterIdentity mmeName
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "subscriberInformation" })
    public void testXMLSerialize() throws Exception {

        EUtranCgiImpl euc = new EUtranCgiImpl(this.getEncodedDataEUtranCgi());
        TAIdImpl ta = new TAIdImpl(this.getTAId());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                -70.33, -0.5, 58);
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(3, TypeOfShape.EllipsoidPointWithUncertaintyCircle, 21.5,
                171, 8, 11);
        DiameterIdentityImpl di = new DiameterIdentityImpl(this.getDiameterIdentity());
        LocationInformationEPSImpl original = new LocationInformationEPSImpl(euc, ta,
                MAPExtensionContainerTest.GetTestExtensionContainer(), ggi, gdi, true, 5, di);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "locationInformationEPS", LocationInformationEPSImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        LocationInformationEPSImpl copy = reader.read("locationInformationEPS", LocationInformationEPSImpl.class);

        assertEquals(copy.getEUtranCellGlobalIdentity().getData(), original.getEUtranCellGlobalIdentity().getData());
        assertEquals(copy.getTrackingAreaIdentity().getData(), original.getTrackingAreaIdentity().getData());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(copy.getExtensionContainer()));
        assertEquals(copy.getGeographicalInformation().getLatitude(), original.getGeographicalInformation().getLatitude());

        assertEquals(copy.getGeodeticInformation().getLatitude(), original.getGeodeticInformation().getLatitude());
        assertEquals(copy.getCurrentLocationRetrieved(), original.getCurrentLocationRetrieved());
        assertEquals(copy.getAgeOfLocationInformation(), original.getAgeOfLocationInformation());
        assertEquals(copy.getMmeName().getData(), original.getMmeName().getData());
    }

}
