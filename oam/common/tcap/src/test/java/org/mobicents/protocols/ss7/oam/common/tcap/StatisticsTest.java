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

package org.mobicents.protocols.ss7.oam.common.tcap;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmProvider;
import org.mobicents.protocols.ss7.oam.common.jmxss7.Ss7Management;
import org.mobicents.protocols.ss7.oam.common.statistics.CounterProviderManagement;
import org.mobicents.protocols.ss7.oam.common.tcap.TcapManagementJmx;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class StatisticsTest extends SccpHarness {
    public static final long WAIT_TIME = 500;
    private static final int _WAIT_TIMEOUT = 90000;
    private static final int _WAIT_REMOVE = 30000;
    public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };
    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;
    private TCAPListenerWrapper tcapListenerWrapper;

    private Ss7Management ss7Man;
    private CounterProviderManagement counterProvider;
    private AlarmProvider alarmProvider;
    private TcapManagementJmx tcapManagement;

    public StatisticsTest() {

    }

    @BeforeClass
    public void setUpClass() {
        this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
        this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";
        System.out.println("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        System.out.println("tearDownClass");
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("setUp");
        super.setUp();

        peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

        this.tcapStack1 = new TCAPStackImpl("Test", this.sccpProvider1, 8);
        this.tcapStack2 = new TCAPStackImpl("Test", this.sccpProvider2, 8);

        this.tcapListenerWrapper = new TCAPListenerWrapper();
        this.tcapStack1.getProvider().addTCListener(tcapListenerWrapper);

        this.tcapStack1.start();
        this.tcapStack2.start();
        this.tcapStack1.setDoNotSendProtocolVersion(false);
        this.tcapStack2.setDoNotSendProtocolVersion(false);
        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);

        // create test classes
        this.client = new Client(this.tcapStack1, peer1Address, peer2Address);
        this.server = new Server(this.tcapStack2, peer2Address, peer1Address);

        ss7Man = new Ss7Management();
        ss7Man.setRmiPort(9998);
        ss7Man.start();

        counterProvider = new CounterProviderManagement(ss7Man);
        counterProvider.start();

        alarmProvider = new AlarmProvider(ss7Man, ss7Man);
        alarmProvider.start();

        tcapManagement = new TcapManagementJmx(ss7Man, tcapStack1);
        tcapManagement.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {

        tcapManagement.stop();
        alarmProvider.stop();
        counterProvider.stop();
        ss7Man.stop();

        this.tcapStack1.stop();
        this.tcapStack2.stop();
        super.tearDown();

    }

    @Test(groups = { "functional.flow" })
    public void simpleTCWithDialogTest() throws Exception {

        client.startClientDialog();
//        assertNotNull(client.dialog.getLocalAddress());
//        assertNull(client.dialog.getRemoteDialogId());
//
//        client.sendBegin();
//        client.waitFor(WAIT_TIME);
//
//        server.sendContinue();
//        assertNotNull(server.dialog.getLocalAddress());
//        assertNotNull(server.dialog.getRemoteDialogId());
//
//        client.waitFor(WAIT_TIME);
//        client.sendEnd(TerminationType.Basic);
//        assertNotNull(client.dialog.getLocalAddress());
//        assertNotNull(client.dialog.getRemoteDialogId());
//
//        client.waitFor(WAIT_TIME);
//        // waitForEnd();
//
//        client.compareEvents(clientExpectedEvents);
//        server.compareEvents(serverExpectedEvents);

    }

    private class TCAPListenerWrapper implements TCListener {

        @Override
        public void onTCUni(TCUniIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCBegin(TCBeginIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCContinue(TCContinueIndication ind) {
//            assertEquals(ind.getComponents().length, 2);
//            ReturnResultLast rrl = (ReturnResultLast) ind.getComponents()[0];
//            Invoke inv = (Invoke) ind.getComponents()[1];
//
//            // operationCode is not sent via ReturnResultLast because it does not contain a Parameter
//            // so operationCode is taken from a sent Invoke
//            assertEquals((long) rrl.getInvokeId(), 1);
//            assertEquals((long) rrl.getOperationCode().getLocalOperationCode(), 12);
//
//            // second Invoke has its own operationCode and it has linkedId to the second sent Invoke
//            assertEquals((long) inv.getInvokeId(), 1);
//            assertEquals((long) inv.getOperationCode().getLocalOperationCode(), 14);
//            assertEquals((long) inv.getLinkedId(), 2);
//
//            // we should see operationCode of the second sent Invoke
//            Invoke linkedInv = inv.getLinkedInvoke();
//            assertEquals((long) linkedInv.getOperationCode().getLocalOperationCode(), 13);
        }

        @Override
        public void onTCEnd(TCEndIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCUserAbort(TCUserAbortIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCPAbort(TCPAbortIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCNotice(TCNoticeIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDialogReleased(Dialog d) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onInvokeTimeout(Invoke tcInvokeRequest) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDialogTimeout(Dialog d) {
            // TODO Auto-generated method stub

        }

    }

}
