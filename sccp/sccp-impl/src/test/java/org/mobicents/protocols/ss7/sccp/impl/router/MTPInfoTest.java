/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.router;

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
    public void testGetInstance() {
        MTPInfo mtpInfo = MTPInfo.getInstance(MTP_INFO);
        assertEquals("linkset", mtpInfo.getName());
        assertEquals(14083, mtpInfo.getOpc());
        assertEquals(14155, mtpInfo.getDpc());
        assertEquals(0, mtpInfo.getSls());
    }


}