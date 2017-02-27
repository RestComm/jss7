/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tcap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
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

        peer1Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        peer2Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();

        this.tcapStack1 = new TCAPStackImpl("DialogIdRangeTest", this.sccpProvider1, 8);

//        this.tcapStack1.start();
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
    public void dialogIdRangeTest() throws Exception {

        Dialog d;

        this.tcapStack1.start();

        this.tcapStack1.setDialogIdRangeStart(20);
        this.tcapStack1.setDialogIdRangeEnd(10020);

        this.tcapStack1.setDialogIdRangeStart(20);
        try {
            this.tcapStack1.setDialogIdRangeEnd(10019);
            fail("Must be exception");
        } catch (Exception e) {
        }

        this.tcapStack1.setDialogIdRangeStart(20);
        try {
            this.tcapStack1.setDialogIdRangeEnd(10);
            fail("Must be exception");
        } catch (Exception e) {
        }

        this.tcapStack1.setDialogIdRangeStart(20);
        try {
            this.tcapStack1.setDialogIdRangeStart(-1);
            fail("Must be exception");
        } catch (Exception e) {
        }

        this.tcapStack1.setDialogIdRangeStart(20);
        try {
            this.tcapStack1.setDialogIdRangeEnd(20000000000L);
            fail("Must be exception");
        } catch (Exception e) {
        }

        this.tcapStack1.setDialogIdRangeStart(20);
        this.tcapStack1.setDialogIdRangeEnd(10020);

        this.tcapStack1.stop();
        this.tcapStack1.start();        

        d = this.tcapStack1.getProvider().getNewDialog(peer1Address, peer2Address);
        assertEquals((long) d.getLocalDialogId(), 21);

        this.tcapStack1.setMaxDialogs(5000);
        try {
            this.tcapStack1.setMaxDialogs(15000);
            fail("Must be exception");
        } catch (Exception e) {
        }

        // setting of curDialogId to 10999
        this.tcapStack1.setDialogIdRangeEnd(31001);
        this.tcapStack1.setDialogIdRangeStart(21001);
        this.tcapStack1.setDialogIdRangeStart(1);
        this.tcapStack1.setDialogIdRangeEnd(11000);

        d = this.tcapStack1.getProvider().getNewDialog(peer1Address, peer2Address);
        assertEquals((long) d.getLocalDialogId(), 11000);
        d = this.tcapStack1.getProvider().getNewDialog(peer1Address, peer2Address);
        assertEquals((long) d.getLocalDialogId(), 1);
        d = this.tcapStack1.getProvider().getNewDialog(peer1Address, peer2Address);
        assertEquals((long) d.getLocalDialogId(), 2);
        d = this.tcapStack1.getProvider().getNewDialog(peer1Address, peer2Address);
        assertEquals((long) d.getLocalDialogId(), 3);

        this.tcapStack1.stop();
    }

    @Test(groups = { "functional.settings" })
    public void seqRangeTest() throws Exception {
        this.tcapStack1.start();

        int sls = ((TCAPProviderImpl) this.tcapStack1.getProvider()).getNextSeqControl();
        assertEquals(sls, 1);

        for (int i1 = 2; i1 < 256; i1++) {
            sls = ((TCAPProviderImpl) this.tcapStack1.getProvider()).getNextSeqControl();
            assertEquals(sls, i1);
        }

        sls = ((TCAPProviderImpl) this.tcapStack1.getProvider()).getNextSeqControl();
        assertEquals(sls, 0);
        sls = ((TCAPProviderImpl) this.tcapStack1.getProvider()).getNextSeqControl();
        assertEquals(sls, 1);
        sls = ((TCAPProviderImpl) this.tcapStack1.getProvider()).getNextSeqControl();
        assertEquals(sls, 2);

        this.tcapStack1.stop();;
    }
}
