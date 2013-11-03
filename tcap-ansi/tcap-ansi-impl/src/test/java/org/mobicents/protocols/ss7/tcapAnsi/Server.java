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

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;

/**
 * @author baranowb
 *
 */
public class Server extends EventTestHarness {

    private Component[] components;

    /**
     * @param stack
     * @param thisAddress
     * @param remoteAddress
     */
    public Server(TCAPStack stack, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super(stack, thisAddress, remoteAddress);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onTCQuery(TCQueryIndication ind) {
        // TODO Auto-generated method stub
        super.onTCQuery(ind);
        this.components = ind.getComponents();
    }

    @Override
    public void sendContinue(boolean addingInv) throws TCAPSendException, TCAPException {

        Component[] comps = components;
        if (comps == null || comps.length != 2) {
            throw new TCAPSendException("Bad comps!");
        }
        Component c = comps[0];
        if (c.getType() != ComponentType.InvokeNotLast) {
            throw new TCAPSendException("Bad type: " + c.getType());
        }
        // lets kill this Invoke - sending ReturnResultLast
        Invoke invoke = (Invoke) c;
        ReturnResultLast rrlast = this.tcapProvider.getComponentPrimitiveFactory().createTCResultLastRequest();
        rrlast.setCorrelationId(invoke.getInvokeId());
        // we need not set operationCode here because of no Parameter is sent and ReturnResultLast will not carry
        // ReturnResultLast value
        // rrlast.setOperationCode(invoke.getOperationCode());
        super.dialog.sendComponent(rrlast);

        c = comps[1];
        if (c.getType() != ComponentType.InvokeLast) {
            throw new TCAPSendException("Bad type: " + c.getType());
        }

        // lets kill this Invoke - sending Invoke with linkedId
        invoke = (Invoke) c;
        Invoke invoke2 = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast(InvokeClass.Class1);
        invoke2.setInvokeId(this.dialog.getNewInvokeId());
        invoke2.setCorrelationId(invoke.getInvokeId());
        OperationCode oc = this.tcapProvider.getComponentPrimitiveFactory().createOperationCode();
        oc.setPrivateOperationCode(14L);
        invoke2.setOperationCode(oc);
        // no parameter
        this.dialog.sendComponent(invoke2);

        super.sendContinue(addingInv);
    }

    public void sendContinue2() throws TCAPSendException, TCAPException {
        super.sendContinue(false);
    }
}
