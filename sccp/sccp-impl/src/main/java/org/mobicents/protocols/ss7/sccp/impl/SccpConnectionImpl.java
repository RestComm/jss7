package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnMessage;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;

public class SccpConnectionImpl extends SccpConnectionWithCouplingImpl implements SccpConnection {

    public SccpConnectionImpl(int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(stack.newSls(), localSsn, localReference, protocol, stack, sccpRoutingControl);
    }

    protected void receiveMessage(SccpConnMessage message) throws Exception {
        try {
            connectionLock.lock();
            super.receiveMessage(message);

        } finally {
            connectionLock.unlock();
        }
    }

    protected void sendMessage(SccpConnMessage message) throws Exception {
        try {
            connectionLock.lock();
            super.sendMessage(message);

        } finally {
            connectionLock.unlock();
        }
    }

    protected void callListenerOnData(byte[] data) {
        SccpListener listener = getListener();
        if (listener != null) { // when listener is absent it's handled by SccpRoutingControl
            listener.onData(this, data);
        }
    }

    protected void prepareMessageForSending(SccpConnSegmentableMessageImpl message) {
        // not needed for protocol class 2
    }

    protected void prepareMessageForSending(SccpConnItMessageImpl message) {
        // not needed for protocol class 2
    }

    public static class ConnectionNotAvailableException extends IllegalStateException {
        public ConnectionNotAvailableException(String message) {
            super(message);
        }
    }
}
