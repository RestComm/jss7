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

package org.mobicents.protocols.ss7.tcap;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

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
        Invoke invoke = cpFactory.createTCInvokeRequest(InvokeClass.Class1);
        invoke.setInvokeId(this.dialog.getNewInvokeId());
        OperationCode oc = cpFactory.createOperationCode();
        oc.setLocalOperationCode(new Long(12));
        invoke.setOperationCode(oc);
        // no parameter
        this.dialog.sendComponent(invoke);

        // create a second INVOKE for which we will test linkedId
        invoke = cpFactory.createTCInvokeRequest(InvokeClass.Class1);
        invoke.setInvokeId(this.dialog.getNewInvokeId());
        oc = cpFactory.createOperationCode();
        oc.setLocalOperationCode(new Long(13));
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
        ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
        // UI is optional!
        TCBeginRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createBegin(this.dialog);
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

        Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(12l);

        OperationCode oc = TcapFactory.createOperationCode();

        oc.setLocalOperationCode(59L);
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
        // System.err.println(this+" T["+System.currentTimeMillis()+"]send BEGIN");
        // ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
        // TCBeginRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createBegin(this.dialog);
        // tcbr.setApplicationContextName(acn);
        //
        // GlobalTitle gt = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL,
        // "93702994006");
        // ((DialogImpl) this.dialog).setRemoteAddress(new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt,
        // 8));

        for (Long invokeId : lstInvokeId) {
            Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
            invoke.setInvokeId(invokeId);
            OperationCode opCode = this.tcapProvider.getComponentPrimitiveFactory().createOperationCode();
            opCode.setLocalOperationCode(0L);
            invoke.setOperationCode(opCode);
            this.dialog.sendComponent(invoke);
        }

        // this.observerdEvents.add(TestEvent.createSentEvent(EventType.Begin, tcbr, sequence++));
        // this.dialog.send(tcbr);

        this.sendBegin();
    }
}
