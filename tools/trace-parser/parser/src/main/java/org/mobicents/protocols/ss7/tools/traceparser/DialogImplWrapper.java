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

package org.mobicents.protocols.ss7.tools.traceparser;

import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.DialogImpl;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DialogImplWrapper extends DialogImpl {

    private int acnValue;
    private int acnVersion;
    protected int curOpcOrig;
    protected int curOpc;

    public DialogImplWrapper(SccpAddress localAddress, SccpAddress remoteAddress, Long origTransactionId, boolean structured,
            ScheduledExecutorService executor, TCAPProviderImpl provider, int seqControl) {
        super(localAddress, remoteAddress, origTransactionId, structured, executor, provider, seqControl, true);
    }

    public int getAcnValue() {
        return acnValue;
    }

    public int getAcnVersion() {
        return acnVersion;
    }

    public void setAcnValue(int v) {
        acnValue = v;
    }

    public void setAcnVersion(int v) {
        acnVersion = v;
    }

    public void SetStateActive() {
        this.setState(TRPseudoState.Active);
    }

    private HashMap<Long, Invoke> invLst = new HashMap<Long, Invoke>();

    @Override
    protected Component[] processOperationsState(Component[] components) {

        if (components == null)
            return null;

        for (Component c : components) {
            // adding 1 to InvokeId: excluding 0 value
            c.setInvokeId(c.getInvokeId() + 1);

            boolean rev = false;
            if (curOpc == this.curOpcOrig) {
                if (c.getType() != ComponentType.Invoke)
                    rev = true;
            } else {
                if (c.getType() == ComponentType.Invoke)
                    rev = true;
            }
            if (rev) {
                c.setInvokeId(-c.getInvokeId());
            }

            switch (c.getType()) {
                case Invoke:
                    invLst.put(c.getInvokeId(), (Invoke) c);
                    break;

                case ReturnResult:
                case ReturnResultLast:
                    Return rr = (Return) c;
                    Invoke inv = invLst.get(c.getInvokeId());
                    if (rr.getOperationCode() == null) {
                        if (inv != null) {
                            rr.setOperationCode(inv.getOperationCode());
                        }
                    }
                    break;
            }
        }

        return components;
    }

    @Override
    public void processAbort(TCAbortMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) {
        super.processAbort(msg, localAddress2, remoteAddress2);
    }

    @Override
    public void processEnd(TCEndMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) {
        super.processEnd(msg, localAddress2, remoteAddress2);
    }

    @Override
    public void processContinue(TCContinueMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) {
        super.processContinue(msg, localAddress2, remoteAddress2);
    }

    @Override
    public void processBegin(TCBeginMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) {
        super.processBegin(msg, localAddress2, remoteAddress2);
    }

    public void send(TCUserAbortRequest event) throws TCAPSendException {
    }
}
