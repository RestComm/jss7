package org.restcomm.protocols.ss7.sccp.impl;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.SccpConnectionState;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCrefMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnErrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRlcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRlsdMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRscMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRsrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCause;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CLOSED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CONNECTION_INITIATED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CR_RECEIVED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.DISCONNECT_INITIATED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED_SEND_WINDOW_EXHAUSTED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.NEW;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_PROPAGATED_VIA_COUPLED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_RECEIVED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_RECEIVED_WILL_PROPAGATE;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_SENT;

abstract class SccpConnectionBaseImpl {

    protected final Logger logger;
    protected SccpStackImpl stack;
    protected SccpRoutingControl sccpRoutingControl;
    protected ReentrantLock connectionLock = new ReentrantLock();
    protected Integer remoteSsn;
    protected Integer remoteDpc;
    protected boolean lastMoreDataSent;

    private SccpConnectionState state;
    private int sls;
    private int localSsn;
    private ProtocolClass protocolClass;
    private LocalReference localReference;
    private LocalReference remoteReference;

    public SccpConnectionBaseImpl(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        this.stack = stack;
        this.sccpRoutingControl = sccpRoutingControl;
        this.sls = sls;
        this.localSsn = localSsn;
        this.protocolClass = protocol;
        this.localReference = localReference;
        this.state = NEW;
        this.logger = Logger.getLogger(SccpConnectionBaseImpl.class.getCanonicalName() + "-" + localReference + "-" + stack.name);
    }

    protected void receiveMessage(SccpConnMessage message) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Rx : SCCP message %s", message.toString()));
        }

        if (message instanceof SccpConnCrMessageImpl) {
            SccpConnCrMessageImpl cr = (SccpConnCrMessageImpl) message;
            remoteReference = cr.getSourceLocalReferenceNumber();
            if (cr.getCallingPartyAddress() != null && cr.getCallingPartyAddress().getSignalingPointCode() != 0) {
                remoteDpc = cr.getCallingPartyAddress().getSignalingPointCode();
            } else {
                if (cr.getIncomingOpc() != -1) {
                    remoteDpc = cr.getIncomingOpc();
                } else {
                    // when both users are on the same stack
                    remoteDpc = cr.getCalledPartyAddress().getSignalingPointCode();
                }
            }
            setState(CR_RECEIVED);

        } else if (message instanceof SccpConnCcMessageImpl) {
            SccpConnCcMessageImpl cc = (SccpConnCcMessageImpl) message;
            remoteReference = cc.getSourceLocalReferenceNumber();
            if (cc.getIncomingDpc() != -1) {
                remoteDpc = cc.getIncomingOpc();
            }
            setState(SccpConnectionState.ESTABLISHED);

        } else if (message instanceof SccpConnRscMessageImpl) {
            setState(SccpConnectionState.ESTABLISHED);

        } else if (message instanceof SccpConnRsrMessageImpl) {
            confirmReset();
        } else if (message instanceof SccpConnRlsdMessageImpl) {
            confirmRelease();
        }
    }

    protected void confirmRelease() throws Exception {
        SccpConnRlcMessageImpl rlc = new SccpConnRlcMessageImpl(sls, localSsn);
        rlc.setSourceLocalReferenceNumber(localReference);
        rlc.setDestinationLocalReferenceNumber(remoteReference);
        rlc.setOutgoingDpc(remoteDpc);
        sendMessage(rlc);
    }

    protected void confirmReset() throws Exception {
        setState(RSR_RECEIVED);

        SccpConnRscMessageImpl rsc = new SccpConnRscMessageImpl(sls, localSsn);
        rsc.setDestinationLocalReferenceNumber(remoteReference);
        rsc.setSourceLocalReferenceNumber(localReference);
        sendMessage(rsc);

        setState(SccpConnectionState.ESTABLISHED);
    }

    protected void sendErr(ErrorCause cause) throws Exception {
        SccpConnErrMessageImpl err = new SccpConnErrMessageImpl(sls, localSsn);
        err.setDestinationLocalReferenceNumber(remoteReference);
        err.setSourceLocalReferenceNumber(localReference);
        err.setErrorCause(cause);

        sendMessage(err);
    }

    protected void sendMessage(SccpConnMessage message) throws Exception {
        if (message instanceof SccpConnSegmentableMessageImpl) { // data message
            prepareMessageForSending((SccpConnSegmentableMessageImpl) message);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Tx : SCCP Message=%s", message.toString()));
        }
        try {
            this.sccpRoutingControl.routeMssgFromSccpUserConn(message);
        } catch (Exception e) {
            // log here Exceptions from MTP3 level
            logger.error("IOException when sending the message to MTP3 level: " + e.getMessage(), e);
            throw e;
        }
    }

    public void setState(SccpConnectionState state) {
        try {
            connectionLock.lock();
            if (!(this.state == NEW && state == CONNECTION_INITIATED
                    || this.state == NEW && state == CR_RECEIVED
                    || this.state == NEW && state == CLOSED
                    || this.state == CR_RECEIVED && state == ESTABLISHED
                    || this.state == CR_RECEIVED && state == CLOSED
                    || this.state == CONNECTION_INITIATED && state == ESTABLISHED
                    || this.state == CONNECTION_INITIATED && state == CLOSED
                    || this.state == ESTABLISHED && state == ESTABLISHED
                    || this.state == ESTABLISHED && state == CLOSED
                    || this.state == ESTABLISHED && state == RSR_SENT
                    || this.state == ESTABLISHED && state == RSR_RECEIVED
                    || this.state == ESTABLISHED && state == ESTABLISHED_SEND_WINDOW_EXHAUSTED
                    || this.state == ESTABLISHED && state == RSR_RECEIVED_WILL_PROPAGATE
                    || this.state == ESTABLISHED && state == DISCONNECT_INITIATED
                    || this.state == DISCONNECT_INITIATED && state == DISCONNECT_INITIATED // repeated RLSD
                    || this.state == DISCONNECT_INITIATED && state == CLOSED
                    || this.state == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == ESTABLISHED
                    || this.state == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == ESTABLISHED_SEND_WINDOW_EXHAUSTED
                    || this.state == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == RSR_RECEIVED
                    || this.state == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == CLOSED
                    || this.state == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == DISCONNECT_INITIATED
                    || this.state == RSR_SENT && state == ESTABLISHED
                    || this.state == RSR_SENT && state == CLOSED
                    || this.state == RSR_RECEIVED && state == ESTABLISHED
                    || this.state == RSR_RECEIVED_WILL_PROPAGATE && state == RSR_PROPAGATED_VIA_COUPLED
                    || this.state == RSR_PROPAGATED_VIA_COUPLED && state == ESTABLISHED
                    || this.state == RSR_RECEIVED && state == CLOSED
                    // when error happens during message routing connection becomes immediately closed
                    || this.state == CLOSED && state == CLOSED
            )) {
                logger.error(String.format("state change error: from %s to %s", this.state, state));
                throw new IllegalStateException(String.format("state change error: from %s to %s", this.state, state));
            }
            this.state = state;
        } finally {
            connectionLock.unlock();
        }
    }

    protected void checkLocalListener() throws IOException {
        if (stack.sccpProvider.getSccpListener(getLocalSsn()) == null) {

            logger.error(String.format("Attempting to establish connection but the SSN %d is not available", getLocalSsn()));
            throw new IOException(String.format(
                    "Attempting to establish connection but the SSN %d is not available", getLocalSsn()));
        }
    }

    public void establish(SccpConnCrMessage message) throws IOException {
        checkLocalListener();
        try {
            message.setSourceLocalReferenceNumber(getLocalReference());

            if (message.getCalledPartyAddress() == null) {
                logger.error("Message to send must have filled CalledPartyAddress field");
                throw new IOException("Message to send must have filled CalledPartyAddress field");
            }
            setState(CONNECTION_INITIATED);
            remoteSsn = message.getCalledPartyAddress().getSubsystemNumber();
            if (message.getCalledPartyAddress().getAddressIndicator().isPCPresent()) {
                remoteDpc = (message.getCalledPartyAddress().getSignalingPointCode());
            }

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Establishing connection to DPC=%d, SSN=%d", getRemoteDpc(), getRemoteSsn()));
            }

            sendMessage(message);

        } catch (Exception e) {
            logger.error(e);
            throw new IOException(e);
        }
    }

    public void reset(ResetCause reason) throws Exception {
        if (reason.getValue().isError()) {
            logger.warn(String.format("Resetting connection to DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(), getRemoteSsn(),
                    getRemoteReference(), reason));

        } else if (logger.isDebugEnabled()) {
            logger.debug(String.format("Resetting connection to DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(), getRemoteSsn(),
                    getRemoteReference(), reason));
        }
        SccpConnRsrMessageImpl rsr = new SccpConnRsrMessageImpl(getSls(), getLocalSsn());
        rsr.setSourceLocalReferenceNumber(getLocalReference());
        rsr.setDestinationLocalReferenceNumber(getRemoteReference());
        rsr.setResetCause(reason);
        setState(RSR_SENT);
        sendMessage(rsr);
    }

    public void resetSection(ResetCause reason) throws Exception {
        reset(reason);
    }

    public void disconnect(ReleaseCause reason, byte[] data) throws Exception {
        if (reason.getValue().isError()) {
            logger.warn(String.format("Disconnecting connection to DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(),
                    getRemoteSsn(), getRemoteReference(), reason));
        } else if (logger.isDebugEnabled()) {
            logger.debug(String.format("Disconnecting connection to DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(),
                    getRemoteSsn(), getRemoteReference(), reason));
        }

        SccpConnRlsdMessageImpl rlsd = new SccpConnRlsdMessageImpl(getSls(), getLocalSsn());
        rlsd.setDestinationLocalReferenceNumber(getRemoteReference());
        rlsd.setReleaseCause(reason);
        rlsd.setSourceLocalReferenceNumber(getLocalReference());
        rlsd.setUserData(data);
        SccpConnectionState prevState = state;
        try {
            setState(DISCONNECT_INITIATED);
            sendMessage(rlsd);
        } catch (Exception e) {
            state = prevState;
            throw e;
        }
    }

    public void refuse(RefusalCause reason, byte[] data) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Refusing connection from DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(),
                    getRemoteSsn(), getRemoteReference(), reason));
        }
        SccpConnCrefMessageImpl cref = new SccpConnCrefMessageImpl(getSls(), getLocalSsn());
        cref.setDestinationLocalReferenceNumber(getRemoteReference());
        cref.setSourceLocalReferenceNumber(getLocalReference());
        cref.setRefusalCause(reason);
        cref.setUserData(data);
        sendMessage(cref);
        stack.removeConnection(getLocalReference());
    }

    public void confirm(SccpAddress respondingAddress, Credit credit, byte[] data) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Confirming connection from DPC=%d, SSN=%d, DLR=%s", getRemoteDpc(),
                    getRemoteSsn(), getRemoteReference()));
        }
        if (getState() != CR_RECEIVED) {
            logger.error(String.format("Trying to confirm connection in non-compatible state %s", getState()));
            throw new IllegalStateException(String.format("Trying to confirm connection in non-compatible state %s", getState()));
        }
        SccpConnCcMessageImpl message = new SccpConnCcMessageImpl(getSls(), getLocalSsn());
        message.setSourceLocalReferenceNumber(getLocalReference());
        message.setDestinationLocalReferenceNumber(getRemoteReference());
        message.setProtocolClass(getProtocolClass());
        message.setCalledPartyAddress(respondingAddress);
        message.setUserData(data);
        message.setCredit(credit);

        sendMessage(message);

        setState(SccpConnectionState.ESTABLISHED);
    }

    protected void setConnectionLock(ReentrantLock lock) {
        if (getState() != NEW) {
            throw new IllegalStateException();
        }
        this.connectionLock = lock;
    }

    public Integer getRemoteDpc() {
        return remoteDpc;
    }

    public Integer getRemoteSsn() {
        // could be unknown i. e. null
        return remoteSsn;
    }

    public void setRemoteSsn(Integer val) {
        remoteSsn = val;
    }

    public SccpListener getListener() {
        return stack.sccpProvider.getSccpListener(localSsn);
    }

    public int getSls() {
        return sls;
    }

    public int getLocalSsn() {
        return localSsn;
    }

    public LocalReference getLocalReference() {
        return localReference;
    }

    public LocalReference getRemoteReference() {
        return remoteReference;
    }

    public SccpConnectionState getState() {
        return state;
    }

    public ProtocolClass getProtocolClass() {
        return protocolClass;
    }

    public boolean isAvailable() {
        return state == ESTABLISHED || state == ESTABLISHED_SEND_WINDOW_EXHAUSTED;
    }

    protected boolean isCanSendData() {
        return state == ESTABLISHED;
    }

    public Credit getSendCredit() {
        throw new IllegalArgumentException("sendCredit is supported only by flow control connection-oriented protocol class");
    }

    public Credit getReceiveCredit() {
        throw new IllegalArgumentException("receiveCredit is supported only by flow control connection-oriented protocol class");
    }

    protected abstract void prepareMessageForSending(SccpConnSegmentableMessageImpl message);
    protected abstract void prepareMessageForSending(SccpConnItMessageImpl message);
    protected abstract void callListenerOnData(byte[] data);
}
