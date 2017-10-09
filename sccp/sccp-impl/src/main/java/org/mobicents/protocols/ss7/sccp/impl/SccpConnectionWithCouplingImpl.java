package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRlcMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRlsdMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRscMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRsrMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnMessage;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;

import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.RSR_PROPAGATED_VIA_COUPLED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.RSR_RECEIVED_WILL_PROPAGATE;

/*
 * Is inherited by both protocol class 2 and 3 implementations. Skips execution when connection isn't coupled
 */
public abstract class SccpConnectionWithCouplingImpl extends SccpConnectionWithSegmentingImpl {
    protected SccpConnectionWithCouplingImpl nextConn;

    private boolean couplingEnabled;

    public SccpConnectionWithCouplingImpl(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(sls, localSsn, localReference, protocol, stack, sccpRoutingControl);
    }

    protected void receiveMessage(SccpConnMessage message) throws Exception {
        super.receiveMessage(message);
        if (couplingEnabled) {
            if (message instanceof SccpConnCcMessageImpl) {
                SccpConnCcMessageImpl cc = (SccpConnCcMessageImpl) message;
                nextConn.confirm(cc.getCalledPartyAddress(), cc.getCredit(), cc.getUserData());
            } else if (message instanceof SccpConnRlsdMessageImpl) {
                SccpConnRlsdMessageImpl rlsd = (SccpConnRlsdMessageImpl) message;
                nextConn.disconnect(rlsd.getReleaseCause(), rlsd.getUserData());

            } else if (message instanceof SccpConnRlcMessageImpl) {
                SccpConnRlcMessageImpl copy = new SccpConnRlcMessageImpl(nextConn.getSls(), nextConn.getLocalSsn());
                copy.setSourceLocalReferenceNumber(nextConn.getLocalReference());
                copy.setDestinationLocalReferenceNumber(nextConn.getRemoteReference());
                copy.setOutgoingDpc(nextConn.getRemoteDpc());

                stack.removeConnection(getLocalReference());
                nextConn.sendMessage(copy);
                stack.removeConnection(nextConn.getLocalReference());
            } else if (message instanceof SccpConnRsrMessageImpl) {
                SccpConnRsrMessageImpl rsr = (SccpConnRsrMessageImpl) message;
                setState(RSR_RECEIVED_WILL_PROPAGATE);
                nextConn.reset(rsr.getResetCause());
                setState(RSR_PROPAGATED_VIA_COUPLED);

            } else if (message instanceof SccpConnRscMessageImpl) {
                SccpConnRscMessageImpl copy = new SccpConnRscMessageImpl(nextConn.getSls(), nextConn.getLocalSsn());
                copy.setSourceLocalReferenceNumber(nextConn.getLocalReference());
                copy.setDestinationLocalReferenceNumber(nextConn.getRemoteReference());
                copy.setOutgoingDpc(nextConn.getRemoteDpc());

                nextConn.sendMessage(copy);
                setState(ESTABLISHED);
                nextConn.setState(ESTABLISHED);
            }
        }
    }

    protected void receiveDataMessage(SccpConnSegmentableMessageImpl msg) throws Exception {
        super.receiveDataMessage(msg);
        if (couplingEnabled) {
            nextConn.send(msg.getUserData());
        }
    }

    protected void confirmReset() throws Exception {
        if (!couplingEnabled) {
            super.confirmReset();
        }
    }

    public void enableCoupling(SccpConnectionWithCouplingImpl nextConn) {
        if (nextConn == null || getState() != SccpConnectionState.NEW || nextConn.getState() != SccpConnectionState.NEW) {
            throw new IllegalArgumentException();
        }
        this.couplingEnabled = true;
        this.nextConn = nextConn;
        setConnectionLock(nextConn.connectionLock);
        if (!nextConn.couplingEnabled && nextConn.nextConn != this) {
            nextConn.enableCoupling(this);
        }

    }

    public boolean isCouplingEnabled() {
        return couplingEnabled;
    }
}
