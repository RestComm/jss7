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

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCListener;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;

/**
 * Simple example demonstrates how to use TCAP Stack
 *
 * @author Amit Bhayani
 *
 */
public class ClientTest implements TCListener {
    // encoded Application Context Name
    public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };
    private TCAPProvider tcapProvider;
    private Dialog clientDialog;

    ClientTest() throws NamingException {

        InitialContext ctx = new InitialContext();
        try {
            String providerJndiName = "java:/mobicents/ss7/tcap";
            this.tcapProvider = ((TCAPProvider) ctx.lookup(providerJndiName));
        } finally {
            ctx.close();
        }

        this.tcapProvider.addTCListener(this);
    }

    public void sendInvoke() throws TCAPException, TCAPSendException {
        SccpAddress localAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
        SccpAddress remoteAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);

        clientDialog = this.tcapProvider.getNewDialog(localAddress, remoteAddress);
        ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

        // create some INVOKE
        Invoke invoke = cpFactory.createTCInvokeRequestNotLast();
        invoke.setInvokeId(this.clientDialog.getNewInvokeId());
        OperationCode oc = cpFactory.createOperationCode();
        oc.setNationalOperationCode(12L);
        invoke.setOperationCode(oc);
        // no parameter
        this.clientDialog.sendComponent(invoke);
        ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
        // UI is optional!
        TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.clientDialog, true);
        tcbr.setApplicationContextName(acn);
        this.clientDialog.send(tcbr);
    }

    public void onDialogReleased(Dialog d) {
    }

    public void onInvokeTimeout(Invoke tcInvokeRequest) {
    }

    public void onDialogTimeout(Dialog d) {
        d.keepAlive();
    }

    public void onTCQuery(TCQueryIndication ind) {
    }

    public void onTCConversation(TCConversationIndication ind) {
        // send end
        TCResponseRequest end = this.tcapProvider.getDialogPrimitiveFactory().createResponse(ind.getDialog());
//        end.setTermination(TerminationType.Basic);
        try {
            ind.getDialog().send(end);
        } catch (TCAPSendException e) {
            throw new RuntimeException(e);
        }
    }

    public void onTCResponse(TCResponseIndication ind) {
        // should not happen, in this scenario, we send data.
    }

    public void onTCUni(TCUniIndication ind) {
        // not going to happen
    }

    public void onTCPAbort(TCPAbortIndication ind) {
        // TODO Auto-generated method stub
    }

    public void onTCUserAbort(TCUserAbortIndication ind) {
        // TODO Auto-generated method stub
    }

    public void onTCNotice(TCNoticeIndication ind) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {

        try {
            ClientTest c = new ClientTest();
            c.sendInvoke();
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TCAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TCAPSendException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
