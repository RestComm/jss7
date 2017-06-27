package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.mobicents.protocols.ss7.sccp.parameter.Credit;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.RefusalCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCause;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public class SccpConnectionImpl extends BaseSccpConnectionImpl implements SccpConnection {

    public SccpConnectionImpl(int localSsn, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(stack.newSls(), localSsn, protocol, stack, sccpRoutingControl);
    }
}
