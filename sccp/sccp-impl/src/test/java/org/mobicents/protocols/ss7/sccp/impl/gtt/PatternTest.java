/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.gtt;

import org.testng.annotations.*;
import static org.testng.Assert.*;

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

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    /**
     * Test of getResult method, of class Pattern.
     */
    @Test(groups = { "gtt"})
    public void testGetResult() {
        Pattern pattern = new Pattern("1101/rem 0,4/ins 0,9023629581");
        String result = pattern.getResult("1101");
        assertEquals( result,"9023629581");
    }

    /**
     * Test of matches method, of class Pattern.
     */
    @Test(groups = { "gtt"})
    public void testMatches() {
        Pattern pattern = new Pattern("1101/rem 0,4/ins 0,9023629581");
        assertTrue(pattern.matches("1101"));
    }

}
