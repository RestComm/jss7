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

package org.mobicents.protocols.ss7.sccp.impl.mgmt.ssp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSubSystemImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.Mtp3PrimitiveMessage;
import org.mobicents.protocols.ss7.sccp.impl.mgmt.Mtp3PrimitiveMessageType;
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
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 */
public class SSPTest extends SccpHarness {

    private SccpAddress a1, a2;

    public SSPTest() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "sspTestSccpStack1";
        this.sccpStack2Name = "sspTestSccpStack2";
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

    @Test(groups = { "ssp", "functional.mgmt" })
    public void testDummy() throws Exception {
        int i = 1;
        assertTrue(i == 1);
    }

    /**
     * Test of configure method, of class SccpStackImpl.
     */
    @Test(groups = { "ssp", "functional.mgmt" })
    public void testRemoteRoutingBasedOnSsn() throws Exception {

        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 8);
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        sccpStack1.setSstTimerDuration_Min(5000);
        sccpStack1.setSstTimerDuration_IncreaseFactor(1);

        u1.register();
        // u2.register();
        // this will cause: u1 stack will receive SSP, u2 stack will get SST and message.

        u1.send();
        u2.send();

        Thread.sleep(100);

        assertTrue(u1.getMessages().size() == 1, "U1 did not receiv message, it should!");
        assertTrue(u1.check(), "Inproper message not received!");
        assertTrue(u2.getMessages().size() == 0, "U2 Received message, it should not!");

        // now lets check functional.mgmt part

        SccpStackImplProxy stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U1 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U1 did not receive Management message, it should !");
        SccpMgmtMessage rmsg1_ssp = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg1_ssp = new SccpMgmtMessage(0, SccpMgmtMessageType.SSP.getType(), getSSN(), 2, 0);
        assertEquals(rmsg1_ssp, emsg1_ssp, "Failed to match management message in U1");

        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0,
                "U2 did not receive Management message, it should !");

        Thread.sleep(6000);

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U2 did not receive Management message, it should !");
        SccpMgmtMessage rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg2_sst = new SccpMgmtMessage(0, SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
        assertEquals(rmsg2_sst, emsg2_sst, "Failed to match management message in U2");

        assertTrue(rmsg2_sst.getTstamp() >= rmsg1_ssp.getTstamp(), "Out of sync messages, SST received before SSP.");

        // register;
        u2.register();
        Thread.sleep(5000);
        stack = (SccpStackImplProxy) sccpStack1;
        // double check first message.
        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U1 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2,
                "U1 did not receive Management message, it should !");
        rmsg1_ssp = stack.getManagementProxy().getMgmtMessages().get(0);
        emsg1_ssp = new SccpMgmtMessage(0, SccpMgmtMessageType.SSP.getType(), getSSN(), 2, 0);
        assertEquals(rmsg1_ssp, emsg1_ssp, "Failed to match management message in U1");

        // now second message MUST be SSA here
        SccpMgmtMessage rmsg1_ssa = stack.getManagementProxy().getMgmtMessages().get(1);
        SccpMgmtMessage emsg1_ssa = new SccpMgmtMessage(1, SccpMgmtMessageType.SSA.getType(), getSSN(), 2, 0);

        assertEquals(rmsg1_ssa, emsg1_ssa, "Failed to match management message in U1");

        // now lets check other one
        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2,
                "U2 did not receive Management message, it should !");
        rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
        emsg2_sst = new SccpMgmtMessage(0, SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
        assertEquals(rmsg2_sst, emsg2_sst, "Failed to match management message in U2");

        rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(1);
        emsg2_sst = new SccpMgmtMessage(1, SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
        assertEquals(rmsg2_sst, emsg2_sst, "Failed to match management message in U2");
        assertTrue(rmsg2_sst.getTstamp() >= rmsg1_ssp.getTstamp(), "Out of sync messages, SST received before SSP.");

        // now lets wait and check if there is nothing more
        Thread.sleep(5000);
        stack = (SccpStackImplProxy) sccpStack1;
        // double check first message.
        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U1 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2,
                "U1 received more functional.mgmt messages than it should !");

        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2,
                "U2 received more functional.mgmt messages than it should!");

        // try to send;

        u1.send();

        Thread.sleep(100);

        assertTrue(u1.getMessages().size() == 1, "U1 did not receiv message, it should!");
        assertTrue(u1.check(), "Inproper message not received!");
        assertTrue(u2.getMessages().size() == 1, "U2 did not receiv message, it should!");

        // TODO: should we check flags in MgmtProxies.

    }

    /**
     * At first the SSN is not available and henvce U1 should receive SSP. After that MTP3Pause recevied for peer(u2, pc2). The
     * resume and all should work again
     */
    @Test(groups = { "ssp", "functional.mgmt" })
    public void testRemoteRoutingBasedOnSsn1() throws Exception {

        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 8);
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        sccpStack1.setSstTimerDuration_Min(5000);
        sccpStack1.setSstTimerDuration_IncreaseFactor(1);

        u1.register();
        // u2.register();
        // this will cause: u1 stack will receive SSP, u2 stack will get SST and message.

        u1.send();
        u2.send();

        Thread.sleep(100);

        assertTrue(u1.getMessages().size() == 1, "U1 did not receiv message, it should!");
        assertTrue(u1.check(), "Inproper message not received!");
        assertTrue(u2.getMessages().size() == 0, "U2 Received message, it should not!");

        // now lets check mgmt part

        SccpStackImplProxy stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U1 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U1 did not receive Management message, it should !");
        SccpMgmtMessage rmsg1_ssp = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg1_ssp = new SccpMgmtMessage(0, SccpMgmtMessageType.SSP.getType(), getSSN(), 2, 0);
        assertEquals(rmsg1_ssp, emsg1_ssp, "Failed to match management message in U1");

        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0,
                "U2 did not receive Management message, it should !");

        Thread.sleep(6000);

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, "U2 received Mtp3 Primitve, it should not!");
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1,
                "U2 did not receive Management message, it should !");
        SccpMgmtMessage rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg2_sst = new SccpMgmtMessage(0, SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
        assertEquals(rmsg2_sst, emsg2_sst, "Failed to match management message in U2");

        assertTrue(rmsg2_sst.getTstamp() >= rmsg1_ssp.getTstamp(), "Out of sync messages, SST received before SSP.");

        // super.data1.add(createPausePrimitive(getStack2PC()));
        this.mtp3UserPart1.sendPauseMessageToLocalUser(getStack2PC());

        // register;
        u2.register();
        Thread.sleep(5000);
        stack = (SccpStackImplProxy) sccpStack1;
        // double check first message.
        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 1);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1);
        rmsg1_ssp = stack.getManagementProxy().getMgmtMessages().get(0);
        emsg1_ssp = new SccpMgmtMessage(0, SccpMgmtMessageType.SSP.getType(), getSSN(), 2, 0);
        assertEquals(rmsg1_ssp, emsg1_ssp, "Failed to match management message in U1");

        // //now second message MUST be SSA here
        // SccpMgmtMessage rmsg1_ssa = stack.getManagementProxy().getMgmtMessages().get(1);
        // SccpMgmtMessage emsg1_ssa = new SccpMgmtMessage(1,SccpMgmtMessageType.SSA.getType(), getSSN(), 2, 0);
        //
        // assertEquals( rmsg1_ssa,emsg1_ssa,"Failed to match management message in U1");

        // //now lets check other one
        // //check if there is no SST
        // stack = (SccpStackImplProxy) sccpStack2;
        //
        // assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, it should not!","U2 received Mtp3 Primitve);
        // assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2, it should !","U2 did not receive Management
        // message);
        // rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
        // emsg2_sst = new SccpMgmtMessage(0,SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
        // assertEquals( rmsg2_sst,emsg2_sst,"Failed to match management message in U2");
        //
        // rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(1);
        // emsg2_sst = new SccpMgmtMessage(1,SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
        // assertEquals( rmsg2_sst,emsg2_sst,"Failed to match management message in U2");
        // assertTrue(rmsg2_sst.getTstamp()>=rmsg1_ssp.getTstamp(), SST received before SSP.","Out of sync messages);
        //
        // //now lets wait and check if there is nothing more
        // Thread.currentThread().sleep(12000);
        // stack = (SccpStackImplProxy) sccpStack1;
        // //double check first message.
        // assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, it should not!","U1 received Mtp3 Primitve);
        // assertTrue(stack.getManagementProxy().getMgmtMessages().size() ==
        // 2,"U1 received more mgmt messages than it should !");
        //
        // stack = (SccpStackImplProxy) sccpStack2;
        //
        // assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, it should not!","U2 received Mtp3 Primitve);
        // assertTrue(stack.getManagementProxy().getMgmtMessages().size() ==
        // 2,"U2 received more mgmt messages than it should!");
        //
        // //try to send;
        //
        // u1.send();
        //
        // Thread.currentThread().sleep(1000);
        //
        // assertTrue( u1.getMessages().size() == 1, it should!","U1 did not receiv message);
        // assertTrue( u1.check(),"Inproper message not received!");
        // assertTrue( u2.getMessages().size() == 1, it should!","U2 did not receiv message);

        // TODO: should we check flags in MgmtProxies.

    }

    /**
     * Test of configure method, of class SccpStackImpl.
     */
    @Test(groups = { "ssp", "functional.mgmt" })
    public void RecdMsgForProhibitedSsnTest() throws Exception {

        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 8);
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        sccpStack1.setSstTimerDuration_Min(5000);
        sccpStack1.setSstTimerDuration_IncreaseFactor(1);

        u1.register();
        // u2.register();
        // this will cause: u1 stack will receive SSP, u2 stack will get SST and message.
        Thread.sleep(100);

        RemoteSubSystemImpl rss = (RemoteSubSystemImpl) sccpStack1.getSccpResource().getRemoteSsn(1);
        u1.send();
        Thread.sleep(200);
        assertEquals(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMgmtMessages().size(), 1);
        rss.setRemoteSsnProhibited(false);
        u1.send();
        Thread.sleep(200);
        // we do not send SSP during a second after sending
        assertEquals(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMgmtMessages().size(), 1);

        Thread.sleep(2000);
        rss.setRemoteSsnProhibited(false);
        u1.send();
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMgmtMessages().size(), 2);
    }

    /**
     * Test of configure method, of class SccpStackImpl.
     */
    @Test(groups = { "ssp", "functional.mgmt" })
    public void ConsernedSpcTest() throws Exception {

        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 8);
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        sccpStack1.setSstTimerDuration_Min(5000);
        sccpStack1.setSstTimerDuration_IncreaseFactor(1);

        sccpStack1.getSccpResource().addConcernedSpc(1, getStack2PC());

        Thread.sleep(100);

        assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 0);

        u1.register();
        Thread.sleep(100);

        assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 1);
        assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().get(0).getType(),
                SccpMgmtMessageType.SSA);

        u1.deregister();
        Thread.sleep(100);

        assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 2);
        assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().get(1).getType(),
                SccpMgmtMessageType.SSP);

        // Now test when the MTP3Pause's and then Resume's, SSA should be sent

        u1.register();
        Thread.sleep(100);

        assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 3);
        assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().get(2).getType(),
                SccpMgmtMessageType.SSA);

        // Pause Stack2PC
        this.mtp3UserPart1.sendPauseMessageToLocalUser(getStack2PC());
        Thread.sleep(100);

        assertTrue(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMtp3Messages().size() == 1,
                "U1 did not receive Mtp3 Primitve, it should !");
        Mtp3PrimitiveMessage rmtpPause = ((SccpStackImplProxy) sccpStack1).getManagementProxy().getMtp3Messages().get(0);
        Mtp3PrimitiveMessage emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_PAUSE, getStack2PC());
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        // Resume Stack2PC
        this.mtp3UserPart1.sendResumeMessageToLocalUser(getStack2PC());
        Thread.sleep(100);

        assertTrue(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMtp3Messages().size() == 2,
                "U1 did not receive Mtp3 Primitve, it should !");
        rmtpPause = ((SccpStackImplProxy) sccpStack1).getManagementProxy().getMtp3Messages().get(1);
        emtpPause = new Mtp3PrimitiveMessage(1, Mtp3PrimitiveMessageType.MTP3_RESUME, getStack2PC());
        assertEquals(rmtpPause, emtpPause, "Failed to match management message in U1");

        // And stack2 should receive SSA
        assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 4);
        assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().get(3).getType(),
                SccpMgmtMessageType.SSA);

    }

    protected static byte[] createPausePrimitive(int pc) throws Exception {
        byte[] b = new byte[] { 0, (byte) (Mtp3PrimitiveMessageType.MTP3_PAUSE.getType() & 0x00FF), (byte) (pc >> 24 & 0xFF),
                (byte) (pc >> 16 & 0xFF), (byte) (pc >> 8 & 0xFF), (byte) (pc & 0xFF) };
        return b;
    }

}
