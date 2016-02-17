/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.sccp.impl.mgmt.mtp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.Mtp3CongestionType;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.Mtp3PrimitiveMessage;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.Mtp3PrimitiveMessageType;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.Mtp3StatusType;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.Mtp3UnavailabiltyCauseType;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.SccpMgmtMessage;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.SccpMgmtMessageType;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test condition when SSN is not available in one stack aka prohibited
 *
 * @author baranowb
 */
public class MtpPrimitivesTest extends SccpHarness {

    private SccpAddress a1, a2;

    public MtpPrimitivesTest() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "MtpPrimitivesTestSccpStack1";
        this.sccpStack2Name = "MtpPrimitivesTestSccpStack2";
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    protected void createStack1() {
        sccpStack1 = createStack(sccpStack1Name);
        sccpProvider1 = sccpStack1.getSccpProvider();
    }

    protected void createStack2() {
        sccpStack2 = createStack(sccpStack2Name);
        sccpProvider2 = sccpStack2.getSccpProvider();
    }

    @Override
    protected SccpStackImpl createStack(String name) {
        SccpStackImpl stack = new SccpStackImplProxy(name);
        final String dir = Util.getTmpTestDir();
        if (dir != null) {
            stack.setPersistDir(dir);
        }
        return stack;
    }

//    @BeforeMethod
//    public void setUp() throws Exception {
//        super.setUp();
//    }
//
//    @AfterMethod
//    public void tearDown() {
//        super.tearDown();
//    }

    /**
     * Test of configure method, of class SccpStackImpl.
     */
    @Test(groups = { "mtp", "functional.mgmt" })
    public void testPauseAndResume() throws Exception {
        super.setUp();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        // register, to SSNs are up.
        u1.register();
        u2.register();

        // now, other tests check bidirectional com, it works.
        // now on one side we will inject pause, try to send message, check on other side
        // inject resume, send message and check on other side.

        // super.data1.add(createPausePrimitive(getStack2PC()));
        this.mtp3UserPart1.sendPauseMessageToLocalUser(getStack2PC());

        Thread.currentThread().sleep(500);
        // now s1 thinks s2 is not available
        assertTrue(u1.getMessages().size() == 0, "U1 Received message, it should not!");
        assertTrue(u2.getMessages().size() == 0, "U2 Received message, it should not!");

        // lets check stack functional.mgmt messages

        SccpStackImplProxy stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 1, "U1 did not receive Mtp3 Primitve, it should !");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0, "U1 received Management message, it should not!");
        Mtp3PrimitiveMessage rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        Mtp3PrimitiveMessage emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_PAUSE, 2);
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0, "U2 received Management message, it should not!");

        // now send msg from s1
        u1.send();

        Thread.currentThread().sleep(500);

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0, "U2 received Management message, it should not!");
        assertTrue(u2.getMessages().size() == 0, "U2 received message, it should not! ");

        // noooow lets inject mtp3_resume and retry
        // super.data1.add(createResumePrimitive(getStack2PC()));
        this.mtp3UserPart1.sendResumeMessageToLocalUser(getStack2PC());

        Thread.currentThread().sleep(500);
        stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 2, "U1 did not receive Mtp3 Primitve, it should !");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0, "U1 received Management message, it should not!");
        rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_PAUSE, 2);
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        Mtp3PrimitiveMessage rmtpResume = stack.getManagementProxy().getMtp3Messages().get(1);
        Mtp3PrimitiveMessage emtpResume = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_RESUME, 2);
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        u1.send();

        Thread.currentThread().sleep(500);
        stack = (SccpStackImplProxy) sccpStack2;
        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0, "U2 received Management message, it should not!");
        assertTrue(u2.getMessages().size() == 1, "U2 did not receive message, it should! ");
        assertTrue(u2.check(), "U2 received bad message");

        super.tearDown();
    }

    @Test(groups = { "mtp", "functional.mgmt" })
    public void testStatus_1() throws Exception {
        super.setUp();

        doTestStatus(Mtp3UnavailabiltyCauseType.CAUSE_INACCESSIBLE);

        super.tearDown();
    }

    @Test(groups = { "mtp", "functional.mgmt" })
    public void testStatus_2() throws Exception {
        super.setUp();

        doTestStatus(Mtp3UnavailabiltyCauseType.CAUSE_UNKNOWN);

        super.tearDown();
    }

    @Test(groups = { "mtp", "functional.mgmt" })
    public void testStatus_3() throws Exception {
        super.setUp();
        sccpStack1.setSccpProtocolVersion(SccpProtocolVersion.ANSI);
        sccpStack2.setSccpProtocolVersion(SccpProtocolVersion.ANSI);
        super.tearDown();
        super.setUp();

        doTestStatus(Mtp3UnavailabiltyCauseType.CAUSE_INACCESSIBLE);

        sccpStack1.setSccpProtocolVersion(SccpProtocolVersion.ITU);
        sccpStack2.setSccpProtocolVersion(SccpProtocolVersion.ITU);

        super.tearDown();
    }

    protected void doTestStatus(Mtp3UnavailabiltyCauseType type) throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        // register, to SSNs are up.
        u1.register();
        u2.register();

        // now, other tests check bidirectional com, it works.
        // now on one side we will inject pause, try to send message, check on other side
        // inject resume, send message and check on other side.

        // super.data1.add(createStatusPrimitive(getStack2PC(),Mtp3StatusType.RemoteUserUnavailable,Mtp3CongestionType.NULL,Mtp3UnavailabiltyCauseType.CAUSE_UNEQUIPED));
        this.mtp3UserPart1.sendStatusMessageToLocalUser(getStack2PC(),
                Mtp3StatusCause.UserPartUnavailability_UnequippedRemoteUser, 0, 0);

        Thread.currentThread().sleep(500);
        // now s1 thinks s2 is not available
        assertTrue(u1.getMessages().size() == 0, "U1 Received message, it should not!");
        assertTrue(u2.getMessages().size() == 0, "U2 Received message, it should not!");

        // lets check stack functional.mgmt messages

        SccpStackImplProxy stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 1, "U1 did not receive Mtp3 Primitve, it should !");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0, "U1 received Management message, it should not!");
        Mtp3PrimitiveMessage rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        Mtp3PrimitiveMessage emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_STATUS, getStack2PC(),
                Mtp3StatusType.RemoteUserUnavailable, Mtp3CongestionType.NULL, Mtp3UnavailabiltyCauseType.CAUSE_UNEQUIPED);
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0, "U2 received Management message, it should not!");

        // now send msg from s1
        u1.send();

        Thread.currentThread().sleep(500);

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0, "U2 received Management message, it should not!");
        assertTrue(u2.getMessages().size() == 0, "U2 received message, it should not! ");

        // //noooow lets inject another status, this will enable SST/SSA
        // super.data1.add(createStatusPrimitive(getStack2PC(), Mtp3StatusType.RemoteUserUnavailable, Mtp3CongestionType.NULL,
        // type));
        Mtp3StatusCause cs = Mtp3StatusCause.UserPartUnavailability_Unknown;
        switch (type) {
            case CAUSE_INACCESSIBLE:
                cs = Mtp3StatusCause.UserPartUnavailability_InaccessibleRemoteUser;
                break;
            case CAUSE_UNEQUIPED:
                cs = Mtp3StatusCause.UserPartUnavailability_UnequippedRemoteUser;
                break;
        }
        this.mtp3UserPart1.sendStatusMessageToLocalUser(getStack2PC(), cs, 0, 0);

        Thread.sleep(15000); // 15000
        stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 2, "U1 did not receive Mtp3 Primitve, it should !");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U1 did not receive Management message, it should !");
        rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_STATUS, getStack2PC(), Mtp3StatusType.RemoteUserUnavailable,
                Mtp3CongestionType.NULL, Mtp3UnavailabiltyCauseType.CAUSE_UNEQUIPED);
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        rmtpPause = stack.getManagementProxy().getMtp3Messages().get(1);
        emtpPause = new Mtp3PrimitiveMessage(1, Mtp3PrimitiveMessageType.MTP3_STATUS, getStack2PC(), Mtp3StatusType.RemoteUserUnavailable,
                Mtp3CongestionType.NULL, type);
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        // check for SSA - note SSN is set to 1, since its whole peer
        // now second message MUST be SSA here
        SccpMgmtMessage rmsg1_ssa = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg1_ssa = new SccpMgmtMessage(2, SccpMgmtMessageType.SSA.getType(), 1, getStack2PC(), 0);

        assertEquals(rmsg1_ssa, emsg1_ssa, "Failed to match management message in U1");

        // check other Stack for SST
        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U2 did not receive Management message, it should !");
        SccpMgmtMessage rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg2_sst = new SccpMgmtMessage(0, SccpMgmtMessageType.SST.getType(), 1, getStack2PC(), 0);
        assertEquals(rmsg2_sst, emsg2_sst, "Failed to match management message in U2");

        u1.send();

        Thread.currentThread().sleep(500);
        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U2 did not receive Management message, it should !");
        assertTrue(u2.getMessages().size() == 1, "U2 did not receive message, it should! ");
        assertTrue(u2.check(), "U2 received bad message");
    }

}
