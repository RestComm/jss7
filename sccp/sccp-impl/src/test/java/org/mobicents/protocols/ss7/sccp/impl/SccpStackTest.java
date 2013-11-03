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
package org.mobicents.protocols.ss7.sccp.impl;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.mobicents.protocols.ss7.Util;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Amit Bhayani
 *
 */
public class SccpStackTest {

    private SccpStackImpl sccpStackImpl = null;

    /**
	 *
	 */
    public SccpStackTest() {
        // TODO Auto-generated constructor stub
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws IOException {

    }

    @AfterMethod
    public void tearDown() {
        this.sccpStackImpl.stop();
    }

    @Test(groups = { "sccpStack", "functional.encode" })
    public void testSerialization() throws Exception {
        this.sccpStackImpl = new SccpStackImpl("SccpStackTest");
        this.sccpStackImpl.setPersistDir(Util.getTmpTestDir());
        this.sccpStackImpl.start();

        this.sccpStackImpl.setZMarginXudtMessage(160);
        this.sccpStackImpl.setReassemblyTimerDelay(10000);
        this.sccpStackImpl.setMaxDataMessage(3952);
        this.sccpStackImpl.setRemoveSpc(false);
        this.sccpStackImpl.setSstTimerDuration_Min(5000);
        this.sccpStackImpl.setSstTimerDuration_Max(1200000);
        this.sccpStackImpl.setSstTimerDuration_IncreaseFactor(1.0);

        this.sccpStackImpl.stop();

        this.sccpStackImpl.start();

        assertEquals(this.sccpStackImpl.getZMarginXudtMessage(), 160);
        assertEquals(this.sccpStackImpl.getReassemblyTimerDelay(), 10000);
        assertEquals(this.sccpStackImpl.getMaxDataMessage(), 3952);
        assertEquals(this.sccpStackImpl.isRemoveSpc(), false);
        assertEquals(this.sccpStackImpl.getSstTimerDuration_Min(), 5000);
        assertEquals(this.sccpStackImpl.getSstTimerDuration_Max(), 1200000);
        assertEquals(this.sccpStackImpl.getSstTimerDuration_IncreaseFactor(), 1.0);

        this.sccpStackImpl.stop();
    }

}
