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

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

/**
 * Super class for event based tests. Has capabilities for testing if events are received and if so, if they were received in
 * proper order.
 *
 * @author baranowb
 *
 */
public abstract class EventTestHarness implements TCListener {
    public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };
    protected List<TestEvent> observerdEvents = new ArrayList<TestEvent>();

    protected Dialog dialog;
    protected TCAPStack stack;
    protected SccpAddress thisAddress;
    protected SccpAddress remoteAddress;

    protected TCAPProvider tcapProvider;
    protected ParameterFactory parameterFactory;
    protected int sequence = 0;

    protected ApplicationContextName acn;
    protected UserInformation ui;

    protected PAbortCauseType pAbortCauseType;

    /**
     * @param stack
     * @param thisAddress
     * @param remoteAddress
     */
    public EventTestHarness(final TCAPStack stack, final ParameterFactory parameterFactory, final SccpAddress thisAddress, final SccpAddress remoteAddress) {
        super();
        this.stack = stack;
        this.thisAddress = thisAddress;
        this.remoteAddress = remoteAddress;
        this.tcapProvider = this.stack.getProvider();
        this.tcapProvider.addTCListener(this);
        this.parameterFactory = parameterFactory;
    }

    public void startClientDialog() throws TCAPException {
        if (dialog != null) {
            throw new IllegalStateException("Dialog exists...");
        }
        dialog = this.tcapProvider.getNewDialog(thisAddress, remoteAddress);
    }

    public void startClientDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
        if (dialog != null) {
            throw new IllegalStateException("Dialog exists...");
        }
        dialog = this.tcapProvider.getNewDialog(localAddress, remoteAddress);
    }

    public void startUniDialog() throws TCAPException {
        if (dialog != null) {
            throw new IllegalStateException("Dialog exists...");
        }
        dialog = this.tcapProvider.getNewUnstructuredDialog(thisAddress, remoteAddress);
    }

    public void sendBegin() throws TCAPException, TCAPSendException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send BEGIN");
        ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
        // UI is optional!
        TCBeginRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createBegin(this.dialog);
        tcbr.setApplicationContextName(acn);
        this.dialog.send(tcbr);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Begin, tcbr, sequence++));
    }

    public void sendContinue() throws TCAPSendException, TCAPException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send CONTINUE");
        // send end
        TCContinueRequest con = this.tcapProvider.getDialogPrimitiveFactory().createContinue(dialog);
        if (acn != null) {
            con.setApplicationContextName(acn);
            acn = null;
        }
        if (ui != null) {
            con.setUserInformation(ui);
            ui = null;
        }
        dialog.send(con);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Continue, con, sequence++));

    }

    public void sendEnd(TerminationType terminationType) throws TCAPSendException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send END");
        // send end
        TCEndRequest end = this.tcapProvider.getDialogPrimitiveFactory().createEnd(dialog);
        end.setTermination(terminationType);
        if (acn != null) {
            end.setApplicationContextName(acn);
            acn = null;
        }
        if (ui != null) {
            end.setUserInformation(ui);
            ui = null;
        }
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.End, end, sequence++));
        dialog.send(end);

    }

    public void sendAbort(ApplicationContextName acn, UserInformation ui, DialogServiceUserType type) throws TCAPSendException {

        System.err.println(this + " T[" + System.currentTimeMillis() + "]send ABORT");
        TCUserAbortRequest abort = this.tcapProvider.getDialogPrimitiveFactory().createUAbort(dialog);
        if (acn != null) {
            abort.setApplicationContextName(acn);
        }
        if (ui != null) {
            abort.setUserInformation(ui);
        }
        abort.setDialogServiceUserType(type);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UAbort, abort, sequence++));
        this.dialog.send(abort);

    }

    public void sendUni() throws TCAPException, TCAPSendException {
        ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

        // create some INVOKE
        Invoke invoke = cpFactory.createTCInvokeRequest(InvokeClass.Class4);
        invoke.setInvokeId(this.dialog.getNewInvokeId());
        OperationCode oc = cpFactory.createOperationCode();
        oc.setLocalOperationCode(new Long(12));
        invoke.setOperationCode(oc);
        // no parameter
        this.dialog.sendComponent(invoke);

        System.err.println(this + " T[" + System.currentTimeMillis() + "]send UNI");
        ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
        TCUniRequest tcur = this.tcapProvider.getDialogPrimitiveFactory().createUni(this.dialog);
        tcur.setApplicationContextName(acn);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Uni, tcur, sequence++));
        this.dialog.send(tcur);
    }

    public void onTCUni(TCUniIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onUni");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Uni, ind, sequence++);
        this.observerdEvents.add(te);
        this.dialog = ind.getDialog();
    }

    public void onTCBegin(TCBeginIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onBegin");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, ind, sequence++);
        this.observerdEvents.add(te);
        this.dialog = ind.getDialog();

        if (ind.getApplicationContextName() != null) {
            this.acn = ind.getApplicationContextName();
        }

        if (ind.getUserInformation() != null) {
            this.ui = ind.getUserInformation();
        }
    }

    public void onTCContinue(TCContinueIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onContinue");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Continue, ind, sequence++);
        this.observerdEvents.add(te);
        if (ind.getApplicationContextName() != null) {
            this.acn = ind.getApplicationContextName();
        }

        if (ind.getUserInformation() != null) {
            this.ui = ind.getUserInformation();
        }
    }

    public void onTCEnd(TCEndIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onEnd");
        TestEvent te = TestEvent.createReceivedEvent(EventType.End, ind, sequence++);
        this.observerdEvents.add(te);

    }

    public void onTCUserAbort(TCUserAbortIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onUAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UAbort, ind, sequence++);
        this.observerdEvents.add(te);
    }

    public void onTCPAbort(TCPAbortIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onPAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, ind, sequence++);
        this.observerdEvents.add(te);

        pAbortCauseType = ind.getPAbortCause();
    }

    public void onDialogReleased(Dialog d) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onDialogReleased");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRelease, d, sequence++);
        this.observerdEvents.add(te);

    }

    public void onInvokeTimeout(Invoke tcInvokeRequest) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onInvokeTimeout");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, tcInvokeRequest, sequence++);
        this.observerdEvents.add(te);

    }

    public void onDialogTimeout(Dialog d) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onDialogTimeout");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, d, sequence++);
        this.observerdEvents.add(te);

    }

    public void onTCNotice(TCNoticeIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onNotice");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Notice, ind, sequence++);
        this.observerdEvents.add(te);
    }

    public List<TestEvent> getObserverdEvents() {
        return observerdEvents;
    }

    public void compareEvents(List<TestEvent> expectedEvents) {
        doCompareEvents(this.observerdEvents, expectedEvents);
    }

    public static void doCompareEvents(List<TestEvent> observerdEvents, List<TestEvent> expectedEvents) {

        if (expectedEvents.size() != observerdEvents.size()) {
            fail("Size of received events: " + observerdEvents.size() + ", does not equal expected events: "
                    + expectedEvents.size() + "\n" + doStringCompare(expectedEvents, observerdEvents));
        }

        for (int index = 0; index < expectedEvents.size(); index++) {
            assertEquals(observerdEvents.get(index), expectedEvents.get(index), "Received event does not match, index[" + index
                    + "]");
        }
    }

    protected static String doStringCompare(List lst1, List lst2) {
        StringBuilder sb = new StringBuilder();
        int size1 = lst1.size();
        int size2 = lst2.size();
        int count = size1;
        if (count < size2) {
            count = size2;
        }

        for (int index = 0; count > index; index++) {
            String s1 = size1 > index ? lst1.get(index).toString() : "NOP";
            String s2 = size2 > index ? lst2.get(index).toString() : "NOP";
            sb.append(s1).append(" - ").append(s2).append("\n");
        }
        return sb.toString();
    }

    public static void waitFor(long v) {
        try {
            Thread.currentThread().sleep(v);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
