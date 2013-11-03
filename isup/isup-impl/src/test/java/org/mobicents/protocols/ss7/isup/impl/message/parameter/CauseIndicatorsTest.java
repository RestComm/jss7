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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CauseIndicatorsTest {
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
        return new byte[] { (byte) 133, (byte) 149 };
    }

    private byte[] getData2() {
        return new byte[] { (byte) 133, (byte) 149, 1, 2, (byte) 0xFF };
    }

    private byte[] getDiagnosticsData() {
        return new byte[] { 1, 2, (byte) 0xFF };
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        CauseIndicatorsImpl prim = new CauseIndicatorsImpl();
        prim.decode(getData());

        assertEquals(prim.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
        assertEquals(prim.getLocation(), CauseIndicators._LOCATION_PRIVATE_NSRU);
        assertEquals(prim.getRecommendation(), 0);
        assertEquals(prim.getCauseValue(), CauseIndicators._CV_CALL_REJECTED);
        assertNull(prim.getDiagnostics());

        prim = new CauseIndicatorsImpl();
        prim.decode(getData2());

        assertEquals(prim.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
        assertEquals(prim.getLocation(), CauseIndicators._LOCATION_PRIVATE_NSRU);
        assertEquals(prim.getRecommendation(), 0);
        assertEquals(prim.getCauseValue(), CauseIndicators._CV_CALL_REJECTED);
        assertEquals(prim.getDiagnostics(), getDiagnosticsData());

        // TODO: add an encoding/decoding unittest for CodingStandard!=CauseIndicators._CODING_STANDARD_ITUT and
        // recomendations!=null (extra Recommendation byte)
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        CauseIndicatorsImpl prim = new CauseIndicatorsImpl(CauseIndicators._CODING_STANDARD_ITUT,
                CauseIndicators._LOCATION_PRIVATE_NSRU, 0, CauseIndicators._CV_CALL_REJECTED, null);

        byte[] data = getData();
        byte[] encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        prim = new CauseIndicatorsImpl(CauseIndicators._CODING_STANDARD_ITUT, CauseIndicators._LOCATION_PRIVATE_NSRU, 0,
                CauseIndicators._CV_CALL_REJECTED, getDiagnosticsData());

        data = getData2();
        encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        // TODO: add an encoding/decoding unittest for CodingStandard!=CauseIndicators._CODING_STANDARD_ITUT and
        // recomendations!=null (extra Recommendation byte)
    }

    @Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        CauseIndicatorsImpl original = new CauseIndicatorsImpl(CauseIndicators._CODING_STANDARD_NATIONAL,
                CauseIndicators._LOCATION_PRIVATE_NSRU, 1, CauseIndicators._CV_CALL_REJECTED, null);
        // int codingStandard, int location, int recommendation, int causeValue, byte[] diagnostics

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "causeIndicators", CauseIndicatorsImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CauseIndicatorsImpl copy = reader.read("causeIndicators", CauseIndicatorsImpl.class);

        assertEquals(copy.getCodingStandard(), original.getCodingStandard());
        assertEquals(copy.getLocation(), original.getLocation());
        assertEquals(copy.getRecommendation(), original.getRecommendation());
        assertEquals(copy.getCauseValue(), original.getCauseValue());
        assertNull(copy.getDiagnostics());

        original = new CauseIndicatorsImpl(CauseIndicators._CODING_STANDARD_NATIONAL, CauseIndicators._LOCATION_PRIVATE_NSRU,
                1, CauseIndicators._CV_CALL_REJECTED, getDiagnosticsData());

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "causeIndicators", CauseIndicatorsImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("causeIndicators", CauseIndicatorsImpl.class);

        assertEquals(copy.getCodingStandard(), original.getCodingStandard());
        assertEquals(copy.getLocation(), original.getLocation());
        assertEquals(copy.getRecommendation(), original.getRecommendation());
        assertEquals(copy.getCauseValue(), original.getCauseValue());
        assertEquals(copy.getDiagnostics(), original.getDiagnostics());
    }

}
