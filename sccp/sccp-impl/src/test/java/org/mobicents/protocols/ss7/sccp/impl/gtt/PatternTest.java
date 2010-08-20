/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.gtt;

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
public class PatternTest {

    public PatternTest() {
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
     * Test of getResult method, of class Pattern.
     */
    @Test
    public void testGetResult() {
        Pattern pattern = new Pattern("1101/rem 0,4/ins 0,9023629581");
        String result = pattern.getResult("1101");
        assertEquals("9023629581", result);
    }

    /**
     * Test of matches method, of class Pattern.
     */
    @Test
    public void testMatches() {
        Pattern pattern = new Pattern("1101/rem 0,4/ins 0,9023629581");
        assertTrue(pattern.matches("1101"));
    }

}