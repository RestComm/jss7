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

/**
 * 
 * @author kulikov
 */
public class MTPInfoTest {

    private final static String MTP_INFO = "linkset#14083#14155#0";

    public MTPInfoTest() {
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
     * Test of getName method, of class MTPInfo.
     */
    @Test
    public void testToString() {
        MTPInfo mtpInfo = new MTPInfo("linkset", 14083, 14155, 0);
        assertEquals(MTP_INFO, mtpInfo.toString());
    }

    /**
     * Test of getOpc method, of class MTPInfo.
     */
    @Test
    public void testSerialize() throws Exception {
        MTPInfo mtpInfo = new MTPInfo("linkset", 14083, 14155, 0);

        // Writes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(mtpInfo, "MTPInfo", MTPInfo.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output
                .toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        MTPInfo mtpInfoOut = reader.read("MTPInfo", MTPInfo.class);

        assertEquals("linkset", mtpInfoOut.getName());
        assertEquals(14083, mtpInfoOut.getOpc());
        assertEquals(14155, mtpInfoOut.getDpc());
        assertEquals(0, mtpInfoOut.getSls());
    }

}