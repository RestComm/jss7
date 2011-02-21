/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.router;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class AddressInformationTest {
    private final static String ADDRESS_INFORMATION_1 = " #ISDN_MOBILE#NATIONAL#9023629581# ";

    public AddressInformationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class AddressInformation.
     */
    @Test
    public void testSerialize() throws Exception {
        AddressInformation ai = new AddressInformation(-1,
                NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL,
                "9023629581", -1);

        // Writes 
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(ai, "AddressInformation", AddressInformation.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output
                .toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        AddressInformation aiOut = reader.read("AddressInformation",
                AddressInformation.class);

        assertEquals(-1, aiOut.getTranslationType());
        assertEquals(NumberingPlan.ISDN_MOBILE, aiOut.getNumberingPlan());
        assertEquals(NatureOfAddress.NATIONAL, aiOut.getNatureOfAddress());
        assertEquals("9023629581", aiOut.getDigits());
        assertEquals(-1, aiOut.getSubsystem());
    }

    /**
     * Test of getTranslationType method, of class AddressInformation.
     */
    @Test
    public void testToString() {
        AddressInformation ai = new AddressInformation(-1,
                NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL,
                "9023629581", -1);
        assertEquals(ADDRESS_INFORMATION_1, ai.toString());
    }

}