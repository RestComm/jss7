package org.restcomm.protocols.ss7.sccp.impl;

import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;

public class SccpConnectionImpl extends SccpConnectionWithCouplingImpl implements SccpConnection {

    public SccpConnectionImpl(int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(stack.newSls(), localSsn, localReference, protocol, stack, sccpRoutingControl);
    }

    public void receiveMessage(SccpConnMessage message) throws Exception {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SccpConnection[");

        fillSccpConnectionFields(sb);
        sb.append("]");

        return sb.toString();
    }

    protected void fillSccpConnectionFields(StringBuilder sb) {
        LocalReference lr = getLocalReference();
        LocalReference rr = getRemoteReference();
        ProtocolClass pClass = getProtocolClass();

        sb.append("localReference=");
        if (lr != null)
            sb.append(lr.getValue());
        else
            sb.append("null");
        sb.append(", remoteReference=");
        if (rr != null)
            sb.append(rr.getValue());
        else
            sb.append("null");
        sb.append(", localSsn=");
        sb.append(getLocalSsn());
        sb.append(", remoteSsn=");
        sb.append(remoteSsn);
        sb.append(", remoteDpc=");
        sb.append(remoteDpc);
        sb.append(", state=");
        sb.append(getState());
        sb.append(", protocolClass=");
        if (pClass != null)
            sb.append(getProtocolClass().getProtocolClass());
        else
            sb.append("null");
        if (isAwaitSegments())
            sb.append(", awaitSegments");
        if (isCouplingEnabled())
            sb.append(", couplingEnabled");
    }
}
