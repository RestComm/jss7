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

package org.mobicents.protocols.ss7.tcapAnsi;

import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpManagementEventListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCListener;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;
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
public class PreviewModeFunctionalTest {
    private SccpHarnessPreview sccpProv = new SccpHarnessPreview();
    private TCAPStackImplWrapper tcapStack1;
    private TCAPListenerHarness tcapListener;
    protected List<TestEvent> observerdEvents;
    protected int sequence;

    public PreviewModeFunctionalTest() {
    }

    @BeforeClass
    public void setUpClass() {
        // this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
        // this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";
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

        this.tcapStack1 = new TCAPStackImplWrapper(this.sccpProv, 8, "PreviewModeFunctionalTest");

        // this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack1.setPreviewMode(true);

        this.tcapStack1.start();

        tcapListener = new TCAPListenerHarness();
        this.tcapStack1.getProvider().addTCListener(tcapListener);

        observerdEvents = new ArrayList<TestEvent>();
        sequence = 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {
        this.tcapStack1.getProvider().addTCListener(tcapListener);

        this.tcapStack1.stop();
    }

    /**
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-END + ReturnError(systemFailure)
     */
    @Test(groups = { "functional.flow" })
    public void beginEndTest() throws Exception {

//        MessageFactory fact = new MessageFactoryImpl(null);
//
//        byte[] m1 = new byte[] { 98, -127, -109, 72, 4, 0, 0, 0, 1, 107, 108, 40, 106, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 95,
//                96, 93, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -66, 76, 40, 74, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96,
//                63, -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39,
//                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
//                24, 25, 26, -95, 3, 31, 32, 33, 108, 29, -95, 27, 2, 1, 1, 2, 1, 59, 48, 19, 4, 1, 15, 4, 5, -86, -40, 108, 54,
//                2, -128, 7, -111, 19, 38, -120, -125, 0, -14 };
//        byte[] m2 = new byte[] { 100, 60, 73, 4, 0, 0, 0, 1, 107, 42, 40, 40, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 29, 97, 27,
//                -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -94, 3, 2, 1, 0, -93, 5, -95, 3, 2, 1, 0, 108, 8, -93, 6,
//                2, 1, 1, 2, 1, 34 };
//
//        SccpAddress addr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 101, null, 6);
//        SccpAddress addr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 102, null, 6);
//        // RoutingIndicator ri, int dpc, GlobalTitle gt, int ssn
//
//        long stamp = System.currentTimeMillis();
//        List<TestEvent> expectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Invoke, null, 1, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.End, null, 2, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.ReturnError, null, 3, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp);
//        expectedEvents.add(te);
//
//        SccpDataMessageImpl msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr2, addr1, m1, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr1, addr2, m2, 0, 0, false, null, null);
//        msg.setIncomingOpc(102);
//        msg.setIncomingDpc(101);
//        this.sccpProv.sccpListener.onMessage(msg);
//
//        EventTestHarness.doCompareEvents(observerdEvents, expectedEvents);
    }

    /**
     * Responses as ReturnResult (this case is simulated) and ReturnResultLast
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-CONTINUE + ReturnResult (addProcessUnstructuredSSResponse) TC-CONTINUE
     * TC-END + ReturnResultLast (addProcessUnstructuredSSResponse)
     */
    @Test(groups = { "functional.flow" })
    public void beginContContEndTest() throws Exception {

//        MessageFactory fact = new MessageFactoryImpl(null);
//
//        byte[] m1 = new byte[] { 98, -127, -109, 72, 4, 0, 0, 0, 1, 107, 108, 40, 106, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 95,
//                96, 93, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -66, 76, 40, 74, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96,
//                63, -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39,
//                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
//                24, 25, 26, -95, 3, 31, 32, 33, 108, 29, -95, 27, 2, 1, 1, 2, 1, 59, 48, 19, 4, 1, 15, 4, 5, -86, -40, 108, 54,
//                2, -128, 7, -111, 19, 38, -120, -125, 0, -14 };
//        byte[] m2 = new byte[] { 101, 92, 72, 4, 0, 0, 0, 1, 73, 4, 0, 0, 0, 1, 107, 42, 40, 40, 6, 7, 0, 17, -122, 5, 1, 1, 1,
//                -96, 29, 97, 27, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -94, 3, 2, 1, 0, -93, 5, -95, 3, 2, 1,
//                0, 108, 34, -89, 32, 2, 1, 1, 48, 27, 2, 1, 59, 48, 22, 4, 1, 15, 4, 17, -39, 119, 93, 14, 18, -121, -39, 97,
//                -9, -72, 12, 74, -49, 65, 53, 24, 12 };
//        byte[] m3 = new byte[] { 101, 12, 72, 4, 0, 0, 0, 1, 73, 4, 0, 0, 0, 1 };
//        byte[] m4 = new byte[] { 100, 42, 73, 4, 0, 0, 0, 1, 108, 34, -94, 32, 2, 1, 1, 48, 27, 2, 1, 59, 48, 22, 4, 1, 15, 4,
//                17, -39, 119, 93, 14, 18, -121, -39, 97, -9, -72, 12, 74, -49, 65, 53, 24, 12 };
//
//        GlobalTitle gt1 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "11111");
//        GlobalTitle gt2 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "22222");
//        SccpAddress addr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, 6); // 101
//        SccpAddress addr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt2, 6); // 102
//
//        long stamp = System.currentTimeMillis();
//        List<TestEvent> expectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Invoke, null, 1, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.ReturnResult, null, 3, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Continue, null, 4, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.End, null, 5, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.ReturnResultLast, null, 6, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 7, stamp);
//        expectedEvents.add(te);
//
//        SccpDataMessageImpl msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr2, addr1, m1, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr1, addr2, m2, 0, 0, false, null, null);
//        msg.setIncomingOpc(102);
//        msg.setIncomingDpc(101);
//        this.sccpProv.sccpListener.onMessage(msg);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr2, addr1, m3, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr1, addr2, m4, 0, 0, false, null, null);
//        msg.setIncomingOpc(102);
//        msg.setIncomingDpc(101);
//        this.sccpProv.sccpListener.onMessage(msg);
//
//        EventTestHarness.doCompareEvents(observerdEvents, expectedEvents);
    }

    /**
     * TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest TC-CONTINUE + ExtensionContainer +
     * addUnstructuredSSRequest TC-CONTINUE + addUnstructuredSSResponse TC-END + addProcessUnstructuredSSResponse
     */
    @Test(groups = { "functional.flow" })
    public void crossInvokeTest() throws Exception {

//        MessageFactory fact = new MessageFactoryImpl(null);
//
//        byte[] m1 = new byte[] { 98, -127, -109, 72, 4, 0, 0, 0, 1, 107, 108, 40, 106, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 95,
//                96, 93, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -66, 76, 40, 74, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96,
//                63, -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39,
//                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
//                24, 25, 26, -95, 3, 31, 32, 33, 108, 29, -95, 27, 2, 1, 1, 2, 1, 59, 48, 19, 4, 1, 15, 4, 5, -86, -40, 108, 54,
//                2, -128, 7, -111, 19, 38, -120, -125, 0, -14 };
//        byte[] m2 = new byte[] { 101, -127, -92, 72, 4, 0, 0, 0, 1, 73, 4, 0, 0, 0, 1, 107, 100, 40, 98, 6, 7, 0, 17, -122, 5,
//                1, 1, 1, -96, 87, 97, 85, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -94, 3, 2, 1, 0, -93, 5, -95,
//                3, 2, 1, 0, -66, 56, 40, 54, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 43, -95, 41, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3,
//                4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
//                33, 108, 48, -95, 46, 2, 1, 1, 2, 1, 60, 48, 38, 4, 1, 15, 4, 33, -45, 50, -69, 60, -90, -125, 98, -87, 107,
//                -104, -51, -122, -121, -31, 101, 57, 72, -106, -110, -90, -35, 103, -6, -37, 93, 6, -51, 82, -57, 112, -69, 60,
//                7 };
//        byte[] m3 = new byte[] { 101, 32, 72, 4, 0, 0, 0, 1, 73, 4, 0, 0, 0, 1, 108, 18, -94, 16, 2, 1, 1, 48, 11, 2, 1, 60,
//                48, 6, 4, 1, 15, 4, 1, 49 };
//        byte[] m4 = new byte[] { 100, 33, 73, 4, 0, 0, 0, 1, 108, 25, -94, 23, 2, 1, 1, 48, 18, 2, 1, 59, 48, 13, 4, 1, 15, 4,
//                8, 84, 116, -40, -67, 6, -27, -33, 117 };
//
//        SccpAddress addr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 101, null, 6);
//        SccpAddress addr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 102, null, 6);
//
//        long stamp = System.currentTimeMillis();
//        List<TestEvent> expectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Invoke, null, 1, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Invoke, null, 3, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Continue, null, 4, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.ReturnResultLast, null, 5, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.End, null, 6, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.ReturnResultLast, null, 7, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 8, stamp);
//        expectedEvents.add(te);
//
//        SccpDataMessageImpl msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr2, addr1, m1, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        assertEquals(this.tcapStack1.getDialogPreviewList().size(), 1);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr1, addr2, m2, 0, 0, false, null, null);
//        msg.setIncomingOpc(102);
//        msg.setIncomingDpc(101);
//        this.sccpProv.sccpListener.onMessage(msg);
//        assertEquals(this.tcapStack1.getDialogPreviewList().size(), 2);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr2, addr1, m3, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        assertEquals(this.tcapStack1.getDialogPreviewList().size(), 2);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr1, addr2, m4, 0, 0, false, null, null);
//        msg.setIncomingOpc(102);
//        msg.setIncomingDpc(101);
//        this.sccpProv.sccpListener.onMessage(msg);
//        assertEquals(this.tcapStack1.getDialogPreviewList().size(), 0);
//
//        EventTestHarness.doCompareEvents(observerdEvents, expectedEvents);
    }

    /**
     * TC-BEGIN + ForwardSMRequest_V2 TC-END + ForwardSMResponse_V2
     */
    @Test(groups = { "functional.flow" })
    public void noOperCodeInReturnTest() throws Exception {

//        MessageFactory fact = new MessageFactoryImpl(null);
//
//        byte[] m1 = new byte[] { 98, -127, -103, 72, 4, 0, 0, 0, 1, 107, 108, 40, 106, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 95,
//                96, 93, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 21, 2, -66, 76, 40, 74, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96,
//                63, -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39,
//                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
//                24, 25, 26, -95, 3, 31, 32, 33, 108, 35, -95, 33, 2, 1, 1, 2, 1, 46, 48, 25, -128, 6, 82, -112, 25, 83, -105,
//                -103, -126, 6, -111, 17, 33, 34, 51, -13, 4, 5, 21, 22, 23, 24, 25, 5, 0 };
//        byte[] m2 = new byte[] { 100, 57, 73, 4, 0, 0, 0, 1, 107, 42, 40, 40, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 29, 97, 27,
//                -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 21, 2, -94, 3, 2, 1, 0, -93, 5, -95, 3, 2, 1, 0, 108, 5, -94, 3,
//                2, 1, 1 };
//
//        GlobalTitle gt1 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "11111");
//        GlobalTitle gt2 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "22222");
//        SccpAddress addr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, 6); // 101
//        SccpAddress addr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt2, 6); // 102
//
//        long stamp = System.currentTimeMillis();
//        List<TestEvent> expectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Invoke, null, 1, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.End, null, 2, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.ReturnResultLast, null, 3, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp);
//        expectedEvents.add(te);
//
//        SccpDataMessageImpl msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr2, addr1, m1, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr1, addr2, m2, 0, 0, false, null, null);
//        msg.setIncomingOpc(102);
//        msg.setIncomingDpc(101);
//        this.sccpProv.sccpListener.onMessage(msg);
//
//        // ReturnResultLast without a Paramater: no oparation code i transmitted in ReturnResultLast - it must be obtained from
//        // saved Invoke
//        Object o = observerdEvents.get(3).getEvent();
//        ReturnResultLastImpl rrl = (ReturnResultLastImpl) o;
//        assertEquals((long) rrl.getOperationCode().getLocalOperationCode(), 46);
//
//        EventTestHarness.doCompareEvents(observerdEvents, expectedEvents);
    }

    /**
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-ABORT(Reason=ACN_Not_Supprted) + alternativeApplicationContextName
     */
    @Test(groups = { "functional.flow" })
    public void abortTest() throws Exception {

//        MessageFactory fact = new MessageFactoryImpl(null);
//
//        byte[] m1 = new byte[] { 98, -127, -109, 72, 4, 0, 0, 0, 1, 107, 108, 40, 106, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 95,
//                96, 93, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -66, 76, 40, 74, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96,
//                63, -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39,
//                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
//                24, 25, 26, -95, 3, 31, 32, 33, 108, 29, -95, 27, 2, 1, 1, 2, 1, 59, 48, 19, 4, 1, 15, 4, 5, -86, -40, 108, 54,
//                2, -128, 7, -111, 19, 38, -120, -125, 0, -14 };
//        byte[] m2 = new byte[] { 103, 65, 73, 4, 0, 0, 0, 1, 107, 57, 40, 55, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 44, 97, 42,
//                -128, 2, 7, -128, -95, 4, 6, 2, 42, 3, -94, 3, 2, 1, 1, -93, 5, -95, 3, 2, 1, 2, -66, 18, 40, 16, 6, 7, 4, 0,
//                0, 1, 1, 1, 1, -96, 5, -93, 3, 10, 1, 0 };
//
//        GlobalTitle gt1 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "11111");
//        GlobalTitle gt2 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "22222");
//        SccpAddress addr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, 6); // 101
//        SccpAddress addr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt2, 6); // 102
//
//        long stamp = System.currentTimeMillis();
//        List<TestEvent> expectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Invoke, null, 1, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.UAbort, null, 2, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp);
//        expectedEvents.add(te);
//
//        SccpDataMessageImpl msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr2, addr1, m1, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr1, addr2, m2, 0, 0, false, null, null);
//        msg.setIncomingOpc(102);
//        msg.setIncomingDpc(101);
//        this.sccpProv.sccpListener.onMessage(msg);
//
//        EventTestHarness.doCompareEvents(observerdEvents, expectedEvents);
    }

    /**
     * TC-BEGIN + addProcessUnstructuredSSRequest DialogTimeout
     */
    @Test(groups = { "functional.flow" })
    public void dialogTimeoutTest() throws Exception {

//        MessageFactory fact = new MessageFactoryImpl(null);
//
//        byte[] m1 = new byte[] { 98, -127, -109, 72, 4, 0, 0, 0, 1, 107, 108, 40, 106, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 95,
//                96, 93, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -66, 76, 40, 74, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96,
//                63, -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39,
//                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
//                24, 25, 26, -95, 3, 31, 32, 33, 108, 29, -95, 27, 2, 1, 1, 2, 1, 59, 48, 19, 4, 1, 15, 4, 5, -86, -40, 108, 54,
//                2, -128, 7, -111, 19, 38, -120, -125, 0, -14 };
//
//        GlobalTitle gt1 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "11111");
//        GlobalTitle gt2 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "22222");
//        SccpAddress addr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, 6); // 101
//        SccpAddress addr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt2, 6); // 102
//
//        long stamp = System.currentTimeMillis();
//        List<TestEvent> expectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Invoke, null, 1, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + 2000);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + 2000);
//        expectedEvents.add(te);
//
//        this.tcapStack1.setInvokeTimeout(1000);
//        this.tcapStack1.setDialogIdleTimeout(2000);
//
//        SccpDataMessageImpl msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr2, addr1, m1, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        assertEquals(this.tcapStack1.getDialogPreviewList().size(), 1);
//
//        Thread.sleep(3000);
//        assertEquals(this.tcapStack1.getDialogPreviewList().size(), 0);
//
//        EventTestHarness.doCompareEvents(observerdEvents, expectedEvents);
    }

    /**
     * TC-BEGIN + addProcessUnstructuredSSRequest DialogTimeout
     */
    @Test(groups = { "functional.flow" })
    public void tooManyDialogsTest() throws Exception {

//        MessageFactory fact = new MessageFactoryImpl(null);
//
//        byte[] m1 = new byte[] { 98, -127, -109, 72, 4, 0, 0, 0, 1, 107, 108, 40, 106, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 95,
//                96, 93, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -66, 76, 40, 74, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96,
//                63, -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39,
//                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
//                24, 25, 26, -95, 3, 31, 32, 33, 108, 29, -95, 27, 2, 1, 1, 2, 1, 59, 48, 19, 4, 1, 15, 4, 5, -86, -40, 108, 54,
//                2, -128, 7, -111, 19, 38, -120, -125, 0, -14 };
//        byte[] m2 = new byte[] { 98, -127, -103, 72, 4, 0, 0, 0, 2, 107, 108, 40, 106, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 95,
//                96, 93, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 21, 2, -66, 76, 40, 74, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96,
//                63, -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39,
//                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
//                24, 25, 26, -95, 3, 31, 32, 33, 108, 35, -95, 33, 2, 1, 1, 2, 1, 46, 48, 25, -128, 6, 82, -112, 25, 83, -105,
//                -103, -126, 6, -111, 17, 33, 34, 51, -13, 4, 5, 21, 22, 23, 24, 25, 5, 0 };
//
//        GlobalTitle gt1 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "11111");
//        GlobalTitle gt2 = GlobalTitle.getInstance(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "22222");
//        SccpAddress addr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, 6); // 101
//        SccpAddress addr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt2, 6); // 102
//
//        long stamp = System.currentTimeMillis();
//        List<TestEvent> expectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Invoke, null, 1, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Begin, null, 2, stamp);
//        expectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Invoke, null, 3, stamp);
//        expectedEvents.add(te);
//
//        this.tcapStack1.setMaxDialogs(1);
//
//        SccpDataMessageImpl msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr2, addr1, m1, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        assertEquals(this.tcapStack1.getDialogPreviewList().size(), 1);
//        msg = (SccpDataMessageImpl) fact.createDataMessageClass1(addr1, addr2, m2, 0, 0, false, null, null);
//        msg.setIncomingOpc(101);
//        msg.setIncomingDpc(102);
//        this.sccpProv.sccpListener.onMessage(msg);
//        assertEquals(this.tcapStack1.getDialogPreviewList().size(), 1);
//
//        this.tcapStack1.setMaxDialogs(100);
//        this.sccpProv.sccpListener.onMessage(msg);
//        assertEquals(this.tcapStack1.getDialogPreviewList().size(), 2);
//
//        EventTestHarness.doCompareEvents(observerdEvents, expectedEvents);
    }

    private class SccpHarnessPreview implements SccpProvider {

        @Override
        public void deregisterSccpListener(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getMaxUserDataLength(SccpAddress arg0, SccpAddress arg1, int networkId) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public MessageFactory getMessageFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ParameterFactory getParameterFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        protected SccpListener sccpListener;

        @Override
        public void registerSccpListener(int arg0, SccpListener listener) {
            sccpListener = listener;
        }

        @Override
        public void send(SccpDataMessage msg) throws IOException {
            // we check here that no messages go from TCAP previewMode

            fail("No message must go from TCAP previewMode");
        }

        @Override
        public void registerManagementEventListener(SccpManagementEventListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void deregisterManagementEventListener(SccpManagementEventListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void coordRequest(int ssn) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public FastMap<Integer, NetworkIdState> getNetworkIdStateList() {
            return new FastMap<Integer, NetworkIdState>();
        }

        @Override
        public ExecutorCongestionMonitor[] getExecutorCongestionMonitorList() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void send(SccpNoticeMessage message) throws IOException {
            // TODO Auto-generated method stub
            
        }

		@Override
		public SccpStack getSccpStack() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void updateSPCongestion(Integer ssn, Integer congestionLevel) {
			// TODO Auto-generated method stub
			
		}
    }

    private class TCAPListenerHarness implements TCListener {

        private void opComponents(Component[] comList) {
            if (comList == null)
                return;

            for (Component comp : comList) {
                EventType et = null;
                switch (comp.getType()) {
                case InvokeNotLast:
                    et = EventType.InvokeNotLast;
                    break;
                case InvokeLast:
                    et = EventType.InvokeLast;
                    break;
                case ReturnResultNotLast:
                    et = EventType.ReturnResult;
                    break;
                case ReturnResultLast:
                    et = EventType.ReturnResultLast;
                    break;
                case ReturnError:
                    et = EventType.ReturnError;
                    break;
                case Reject:
                    et = EventType.Reject;
                    break;
                }

                if (et != null) {
                    TestEvent te = TestEvent.createReceivedEvent(et, comp, sequence++);
                    observerdEvents.add(te);
                }
            }
        }

        @Override
        public void onTCUni(TCUniIndication ind) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.Uni, ind, sequence++);
            observerdEvents.add(te);

            opComponents(ind.getComponents());
        }

        @Override
        public void onTCQuery(TCQueryIndication ind) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, ind, sequence++);
            observerdEvents.add(te);

            opComponents(ind.getComponents());
        }

        @Override
        public void onTCConversation(TCConversationIndication ind) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.Continue, ind, sequence++);
            observerdEvents.add(te);

            opComponents(ind.getComponents());
        }

        @Override
        public void onTCResponse(TCResponseIndication ind) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.End, ind, sequence++);
            observerdEvents.add(te);

            opComponents(ind.getComponents());
        }

        @Override
        public void onTCUserAbort(TCUserAbortIndication ind) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.UAbort, ind, sequence++);
            observerdEvents.add(te);
        }

        @Override
        public void onTCPAbort(TCPAbortIndication ind) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, ind, sequence++);
            observerdEvents.add(te);
        }

        @Override
        public void onTCNotice(TCNoticeIndication ind) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.Notice, ind, sequence++);
            observerdEvents.add(te);
        }

        @Override
        public void onDialogReleased(Dialog d) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRelease, d, sequence++);
            observerdEvents.add(te);
        }

        @Override
        public void onInvokeTimeout(Invoke tcInvokeRequest) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, tcInvokeRequest, sequence++);
            observerdEvents.add(te);
        }

        @Override
        public void onDialogTimeout(Dialog d) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, d, sequence++);
            observerdEvents.add(te);
        }

    }
}
