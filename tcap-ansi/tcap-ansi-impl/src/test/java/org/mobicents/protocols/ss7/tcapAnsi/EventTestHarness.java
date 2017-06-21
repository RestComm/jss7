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

package org.mobicents.protocols.ss7.tcapAnsi;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCListener;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;

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

    protected ApplicationContext acn;
    protected UserInformation ui;

    protected PAbortCause pAbortCauseType;
    protected RejectProblem rejectProblem;

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
        ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
        // UI is optional!
        TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.dialog, true);
        tcbr.setApplicationContextName(acn);
        this.dialog.send(tcbr);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Begin, tcbr, sequence++));
    }

    public void sendContinue(boolean addingInv) throws TCAPSendException, TCAPException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send CONTINUE");
        // send end
        TCConversationRequest con = this.tcapProvider.getDialogPrimitiveFactory().createConversation(dialog, true);
        if (acn != null) {
            con.setApplicationContextName(acn);
            acn = null;
        }
        if (ui != null) {
            con.setUserInformation(ui);
            ui = null;
        }

        if (addingInv && acn == null && ui == null) {
            // no dialog patch - we are adding Invoke primitive
            Invoke inv = TcapFactory.createComponentInvoke();
            inv.setInvokeId(this.dialog.getNewInvokeId());
            inv.setNotLast(true);
            OperationCode oc = TcapFactory.createOperationCode();
            oc.setNationalOperationCode(10L);
            inv.setOperationCode(oc);
            Parameter p = TcapFactory.createParameter();
            p.setData(new byte[] { 1, 2, 3, 4, 5 });
            p.setPrimitive(false);
            p.setTagClass(Tag.CLASS_PRIVATE);
            p.setTag(Parameter._TAG_SEQUENCE);
            inv.setParameter(p);
            dialog.sendComponent(inv);
        }

        dialog.send(con);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Continue, con, sequence++));

    }

    public void sendEnd(boolean addingInv) throws TCAPSendException, TCAPException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send END");
        // send end
        TCResponseRequest end = this.tcapProvider.getDialogPrimitiveFactory().createResponse(dialog);
//        end.setTermination(terminationType);
        if (acn != null) {
            end.setApplicationContextName(acn);
            acn = null;
        }
        if (ui != null) {
            end.setUserInformation(ui);
            ui = null;
        }

        if (addingInv && acn == null && ui == null) {
            // no dialog patch - we are adding Invoke primitive
            Invoke inv = TcapFactory.createComponentInvoke();
            inv.setInvokeId(this.dialog.getNewInvokeId());
            inv.setNotLast(true);
            OperationCode oc = TcapFactory.createOperationCode();
            oc.setNationalOperationCode(10L);
            inv.setOperationCode(oc);
            Parameter p = TcapFactory.createParameter();
            p.setData(new byte[] { 1, 2, 3, 4, 5 });
            p.setPrimitive(false);
            p.setTagClass(Tag.CLASS_PRIVATE);
            p.setTag(Parameter._TAG_SEQUENCE);
            inv.setParameter(p);
            dialog.sendComponent(inv);
        }

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.End, end, sequence++));
        dialog.send(end);

    }

    public void sendAbort(ApplicationContext acn, UserInformationElement ui) throws TCAPSendException {

        System.err.println(this + " T[" + System.currentTimeMillis() + "]send ABORT");
        TCUserAbortRequest abort = this.tcapProvider.getDialogPrimitiveFactory().createUAbort(dialog);
        if (acn != null) {
            abort.setApplicationContextName(acn);
        }
        if (ui != null) {
            abort.setUserAbortInformation(ui);
        }
//        abort.setDialogServiceUserType(type);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UAbort, abort, sequence++));
        this.dialog.send(abort);

    }

    public void sendUni() throws TCAPException, TCAPSendException {
        ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

        // create some INVOKE
        Invoke invoke = cpFactory.createTCInvokeRequestNotLast(InvokeClass.Class4);
        invoke.setInvokeId(this.dialog.getNewInvokeId());
        OperationCode oc = cpFactory.createOperationCode();
        oc.setNationalOperationCode(12L);
        invoke.setOperationCode(oc);
        // no parameter
        this.dialog.sendComponent(invoke);

        System.err.println(this + " T[" + System.currentTimeMillis() + "]send UNI");
        ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
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

    public void onTCQuery(TCQueryIndication ind) {
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

    public void onTCConversation(TCConversationIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onContinue");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Continue, ind, sequence++);
        this.observerdEvents.add(te);
        if (ind.getApplicationContextName() != null) {
//            this.acn = ind.getApplicationContextName();
        }

        if (ind.getUserInformation() != null) {
            this.ui = ind.getUserInformation();
        }
    }

    public void onTCResponse(TCResponseIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onEnd");
        TestEvent te = TestEvent.createReceivedEvent(EventType.End, ind, sequence++);
        this.observerdEvents.add(te);

        Component[] compp = ind.getComponents();
        if (compp != null && compp.length > 0) {
            if (compp[0] instanceof Reject) {
                Reject rej = (Reject) compp[0];
                this.rejectProblem = rej.getProblem();
            }
        }

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
            assertEquals(observerdEvents.get(index), expectedEvents.get(index), "Received event does not match, index[" + index + "]");
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
