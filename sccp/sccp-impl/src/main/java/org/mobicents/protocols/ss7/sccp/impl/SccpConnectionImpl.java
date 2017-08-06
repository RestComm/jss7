package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;

public class SccpConnectionImpl extends BaseSccpConnectionImpl implements SccpConnection {

    public SccpConnectionImpl(int localSsn, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(stack.newSls(), localSsn, protocol, stack, sccpRoutingControl);
    }

    protected void callListenerOnData(byte[] data) {
        listener.onData(this, data);
    }
}
