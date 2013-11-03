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

package org.mobicents.protocols.ss7.sccp.impl.mgmt.mtp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
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

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
    }

    /**
     * Test of configure method, of class SccpStackImpl.
     */
    @Test(groups = { "mtp", "functional.mgmt" })
    public void testPauseAndResume() throws Exception {
        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 8);
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 8);

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
    }

    @Test(groups = { "mtp", "functional.mgmt" })
    public void testStatus_1() throws Exception {

        doTestStatus(Mtp3UnavailabiltyCauseType.CAUSE_INACCESSIBLE);
    }

    @Test(groups = { "mtp", "functional.mgmt" })
    public void testStatus_2() throws Exception {

        doTestStatus(Mtp3UnavailabiltyCauseType.CAUSE_UNKNOWN);
    }

    protected void doTestStatus(Mtp3UnavailabiltyCauseType type) throws Exception {
        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 8);
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 8);

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
                Mtp3StatusCause.UserPartUnavailability_UnequippedRemoteUser, 0);

        Thread.currentThread().sleep(500);
        // now s1 thinks s2 is not available
        assertTrue(u1.getMessages().size() == 0, "U1 Received message, it should not!");
        assertTrue(u2.getMessages().size() == 0, "U2 Received message, it should not!");

        // lets check stack functional.mgmt messages

        SccpStackImplProxy stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 1, "U1 did not receive Mtp3 Primitve, it should !");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0, "U1 received Management message, it should not!");
        Mtp3PrimitiveMessage rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        Mtp3PrimitiveMessage emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_STATUS, 2,
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
        this.mtp3UserPart1.sendStatusMessageToLocalUser(getStack2PC(), cs, 0);

        Thread.sleep(15000);
        stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 2, "U1 did not receive Mtp3 Primitve, it should !");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U1 did not receive Management message, it should !");
        rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_STATUS, 2, Mtp3StatusType.RemoteUserUnavailable,
                Mtp3CongestionType.NULL, Mtp3UnavailabiltyCauseType.CAUSE_UNEQUIPED);
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        rmtpPause = stack.getManagementProxy().getMtp3Messages().get(1);
        emtpPause = new Mtp3PrimitiveMessage(1, Mtp3PrimitiveMessageType.MTP3_STATUS, 2, Mtp3StatusType.RemoteUserUnavailable,
                Mtp3CongestionType.NULL, type);
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        // check for SSA - note SSN is set to 1, since its whole peer
        // now second message MUST be SSA here
        SccpMgmtMessage rmsg1_ssa = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg1_ssa = new SccpMgmtMessage(2, SccpMgmtMessageType.SSA.getType(), 1, 2, 0);

        assertEquals(rmsg1_ssa, emsg1_ssa, "Failed to match management message in U1");

        // check other Stack for SST
        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U2 did not receive Management message, it should !");
        SccpMgmtMessage rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg2_sst = new SccpMgmtMessage(0, SccpMgmtMessageType.SST.getType(), 1, 2, 0);
        assertEquals(rmsg2_sst, emsg2_sst, "Failed to match management message in U2");

        u1.send();

        Thread.currentThread().sleep(500);
        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U2 did not receive Management message, it should !");
        assertTrue(u2.getMessages().size() == 1, "U2 did not receive message, it should! ");
        assertTrue(u2.check(), "U2 received bad message");
    }

    // /**
    // * @param stack2pc
    // * @return
    // */
    // protected static byte[] createStatusPrimitive(int pc, Mtp3StatusType status, Mtp3CongestionType congType,
    // Mtp3UnavailabiltyCauseType unavType) {
    //
    // byte[] b= new byte[]{
    // 0,
    // (byte)(Mtp3PrimitiveMessageType.MTP3_STATUS.getType() & 0x00FF),
    // (byte)(status.getType() & 0xFF),
    // (byte)(pc >> 24 & 0xFF),
    // (byte)(pc >> 16 & 0xFF),
    // (byte)(pc >> 8 & 0xFF),
    // (byte)(pc & 0xFF),
    // (byte)(congType.getType() >> 8 & 0xFF),
    // (byte)(congType.getType() & 0xFF),
    // (byte)(unavType.getType() >> 8 & 0xFF),
    // (byte)(unavType.getType() & 0xFF)
    //
    // };
    // return b;
    // }
    //
    // protected static byte[] createPausePrimitive(int pc) throws Exception
    // {
    // byte[] b= new byte[]{
    // 0,
    // (byte)(Mtp3PrimitiveMessageType.MTP3_PAUSE.getType() & 0x00FF),
    // (byte)(pc >> 24 & 0xFF),
    // (byte)(pc >> 16 & 0xFF),
    // (byte)(pc >> 8 & 0xFF),
    // (byte)(pc & 0xFF)
    // };
    // return b;
    // }
    // protected static byte[] createResumePrimitive(int pc) throws Exception
    // {
    // byte[] b= new byte[]{
    // 0,
    // (byte)(Mtp3PrimitiveMessageType.MTP3_RESUME.getType() & 0x00FF),
    // (byte)(pc >> 24 & 0xFF),
    // (byte)(pc >> 16 & 0xFF),
    // (byte)(pc >> 8 & 0xFF),
    // (byte)(pc & 0xFF)
    // };
    // return b;
    // }

}
