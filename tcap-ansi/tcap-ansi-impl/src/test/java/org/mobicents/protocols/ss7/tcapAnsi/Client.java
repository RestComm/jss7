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

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.DialogImpl;
import org.mobicents.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class Client extends EventTestHarness {

    /**
     * @param stack
     * @param thisAddress
     * @param remoteAddress
     */
    public Client(TCAPStack stack, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super(stack, thisAddress, remoteAddress);

    }

    @Override
    public void sendBegin() throws TCAPException, TCAPSendException {
        ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

        // create some INVOKE
        Invoke invoke = cpFactory.createTCInvokeRequestNotLast(InvokeClass.Class1);
        invoke.setInvokeId(this.dialog.getNewInvokeId());
        OperationCode oc = cpFactory.createOperationCode();
        oc.setPrivateOperationCode(12L);
        invoke.setOperationCode(oc);
        // no parameter
        this.dialog.sendComponent(invoke);

        // create a second INVOKE for which we will test linkedId
        invoke = cpFactory.createTCInvokeRequestLast(InvokeClass.Class1);
        invoke.setInvokeId(this.dialog.getNewInvokeId());
        oc = cpFactory.createOperationCode();
        oc.setPrivateOperationCode(13L);
        invoke.setOperationCode(oc);
        // no parameter
        this.dialog.sendComponent(invoke);

        super.sendBegin();
    }

    public void sendBegin2() throws TCAPException, TCAPSendException {
        super.sendBegin();
    }

    public void sendBeginUnreachableAddress(boolean returnMessageOnError) throws TCAPException, TCAPSendException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send BEGIN");
        ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
        // UI is optional!
        TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.dialog, true);
        tcbr.setApplicationContextName(acn);

        GlobalTitle gt = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "93702994006");
        ((DialogImpl) this.dialog).setRemoteAddress(new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 8));
        tcbr.setReturnMessageOnError(returnMessageOnError);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Begin, tcbr, sequence++));
        this.dialog.send(tcbr);
    }

    public void releaseDialog() {
        if (this.dialog != null)
            this.dialog.release();
        this.dialog = null;
    }

    public DialogImpl getCurDialog() {
        return (DialogImpl) this.dialog;
    }

    public Invoke createNewInvoke() {

        Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast();
        invoke.setInvokeId(12l);

        OperationCode oc = TcapFactory.createOperationCode();

        oc.setPrivateOperationCode(59L);
        invoke.setOperationCode(oc);

        Parameter p1 = TcapFactory.createParameter();
        p1.setTagClass(Tag.CLASS_UNIVERSAL);
        p1.setTag(Tag.STRING_OCTET);
        p1.setData(new byte[] { 0x0F });

        Parameter p2 = TcapFactory.createParameter();
        p2.setTagClass(Tag.CLASS_UNIVERSAL);
        p2.setTag(Tag.STRING_OCTET);
        p2.setData(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
                0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 });

        Parameter pm = TcapFactory.createParameter();
        pm.setTagClass(Tag.CLASS_UNIVERSAL);
        pm.setTag(Tag.SEQUENCE);
        pm.setParameters(new Parameter[] { p1, p2 });
        invoke.setParameter(pm);

        return invoke;
    }

    public void sendInvokeSet(Long[] lstInvokeId) throws Exception {

        for (Long invokeId : lstInvokeId) {
            Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast();
            invoke.setInvokeId(invokeId);
            OperationCode opCode = this.tcapProvider.getComponentPrimitiveFactory().createOperationCode();
            opCode.setPrivateOperationCode(0L);
            invoke.setOperationCode(opCode);
            this.dialog.sendComponent(invoke);
        }

        this.sendBegin();
    }
}
