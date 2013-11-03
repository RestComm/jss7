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

package org.mobicents.protocols.ss7.tcapAnsi;

import static org.testng.Assert.*;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for setDialogIdRange(long start, long end)
 *
 * @author sergey vetyutnev
 *
 */
public class DialogIdRangeTest extends SccpHarness {

    private TCAPStackImpl tcapStack1;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
        this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

        peer1Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
        peer2Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();

        this.tcapStack1 = new TCAPStackImpl("Test", this.sccpProvider1, 8);

        this.tcapStack1.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {
        this.tcapStack1.stop();
        super.tearDown();

    }

    @Test(groups = { "functional.settings" })
    public void uniMsgTest() throws Exception {

        Dialog d;

        this.tcapStack1.stop();

        this.tcapStack1.setDialogIdRangeStart(20);
        this.tcapStack1.setDialogIdRangeEnd(10020);
        this.tcapStack1.start();
        this.tcapStack1.stop();

        this.tcapStack1.setDialogIdRangeStart(20);
        this.tcapStack1.setDialogIdRangeEnd(10019);
        try {
            this.tcapStack1.start();
            this.tcapStack1.stop();
            fail("Must be exception");
        } catch (Exception e) {
        }

        this.tcapStack1.setDialogIdRangeStart(20000);
        this.tcapStack1.setDialogIdRangeEnd(20);
        try {
            this.tcapStack1.start();
            this.tcapStack1.stop();
            fail("Must be exception");
        } catch (Exception e) {
        }

        this.tcapStack1.setDialogIdRangeStart(-1);
        this.tcapStack1.setDialogIdRangeEnd(20000);
        try {
            this.tcapStack1.start();
            this.tcapStack1.stop();
            fail("Must be exception");
        } catch (Exception e) {
        }

        this.tcapStack1.setDialogIdRangeStart(1);
        this.tcapStack1.setDialogIdRangeEnd(20000000000L);
        try {
            this.tcapStack1.start();
            this.tcapStack1.stop();
            fail("Must be exception");
        } catch (Exception e) {
        }

        this.tcapStack1.setDialogIdRangeStart(20);
        this.tcapStack1.setDialogIdRangeEnd(10020);
        this.tcapStack1.start();

        d = this.tcapStack1.getProvider().getNewDialog(peer1Address, peer2Address);
        assertEquals((long) d.getLocalDialogId(), 20);

        this.tcapStack1.setMaxDialogs(5000);
        try {
            this.tcapStack1.setMaxDialogs(15000);
            fail("Must be exception");
        } catch (Exception e) {
        }

    }
}
