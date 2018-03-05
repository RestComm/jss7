package org.restcomm.protocols.ss7.sccp.impl;

import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpConnectionState;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnAkMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRsrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CR_RECEIVED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED_SEND_WINDOW_EXHAUSTED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.NEW;

public class SccpConnectionWithFlowControlImpl extends SccpConnectionImpl implements SccpConnection {

    protected SccpFlowControl flow;

    private boolean overloaded;

    public SccpConnectionWithFlowControlImpl(int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(localSsn, localReference, protocol, stack, sccpRoutingControl);
        if (protocol.getProtocolClass() != 3) {
            logger.error("Using connection class for non-supported protocol class 2");
            throw new IllegalArgumentException();
        }
    }

    public void establish(SccpConnCrMessage message) throws IOException {
        this.flow = newSccpFlowControl(message.getCredit());
        super.establish(message);
    }

    public void confirm(SccpAddress respondingAddress, Credit credit, byte[] data) throws Exception {
        if (getState() != CR_RECEIVED) {
            logger.error(String.format("Trying to confirm connection in non-compatible state %s", getState()));
            throw new IllegalStateException(String.format("Trying to confirm connection in non-compatible state %s", getState()));
        }
        this.flow = newSccpFlowControl(credit);

        super.confirm(respondingAddress, credit, data);
    }

    protected SccpFlowControl newSccpFlowControl(Credit credit) {
        return new SccpFlowControl(stack.name, credit.getValue());
    }

    public void setOverloaded(boolean overloaded) throws Exception {
        try {
            connectionLock.lock();

            if (this.overloaded == overloaded) {
                return;
            }
            if (overloaded) {
                sendAk(new CreditImpl(0));
            } else {
                sendAk();
            }
            this.overloaded = overloaded;

        } finally {
            connectionLock.unlock();
        }
    }

    protected void prepareMessageForSending(SccpConnSegmentableMessageImpl message) {
        if (message instanceof SccpConnDt2MessageImpl) {
            SccpConnDt2MessageImpl dt2 = (SccpConnDt2MessageImpl) message;

            flow.initializeMessageNumbering(dt2);
            flow.checkOutputMessageNumbering(dt2);

            if (!flow.isAuthorizedToTransmitAnotherMessage()) {
                setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
            }

        } else {
            throw new IllegalArgumentException();
        }
    }

    protected void prepareMessageForSending(SccpConnItMessageImpl it) {
        it.setCredit(new CreditImpl(flow.getReceiveCredit()));

        flow.initializeMessageNumbering(it);
        flow.checkOutputMessageNumbering(it);

        if (!flow.isAuthorizedToTransmitAnotherMessage()) {
            setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
        }
    }

    protected void receiveMessage(SccpConnMessage message) throws Exception {
        try {
            connectionLock.lock();
            super.receiveMessage(message);

            if (message instanceof SccpConnCcMessageImpl) {
                SccpConnCcMessageImpl cc = (SccpConnCcMessageImpl) message;
                if (cc.getCredit() != null) {
                    this.flow = newSccpFlowControl(cc.getCredit());
                }

            } else if (message instanceof SccpConnAkMessageImpl) {
                handleAkMessage((SccpConnAkMessageImpl) message);
            } else if (message instanceof SccpConnRsrMessageImpl) {
                flow.reinitialize();
            }
        } finally {
            connectionLock.unlock();
        }
    }

    protected void receiveDataMessage(SccpConnSegmentableMessageImpl msg) throws Exception {
        if (!isAvailable()) {
            logger.error(getState() + " Message discarded " + msg);
            return;
        }
        if (!(msg instanceof SccpConnDt2MessageImpl)) {
            logger.error("Using protocol class 3, DT1 message discarded " + msg);
            return;
        }

        SccpConnDt2MessageImpl dt2 = (SccpConnDt2MessageImpl) msg;

        boolean correctNumbering = flow.checkInputMessageNumbering(this, dt2.getSequencingSegmenting().getSendSequenceNumber(),
                dt2.getSequencingSegmenting().getReceiveSequenceNumber());

        if (flow.isAkSendCriterion(dt2)) {
            sendAk();
        }
        if (correctNumbering) {
            super.receiveDataMessage(msg);
        } else {
            logger.error(String.format("Message %s was discarded due to incorrect sequence numbers", msg.toString()));
        }

        if (flow.isAuthorizedToTransmitAnotherMessage()) {
            setState(ESTABLISHED);
        } else {
            setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
        }
    }

    protected void sendAk() throws Exception {
        sendAk(new CreditImpl(flow.getMaximumWindowSize()));
    }

    protected void sendAk(Credit credit) throws Exception {
        SccpConnAkMessageImpl msg = new SccpConnAkMessageImpl(0, 0);
        msg.setDestinationLocalReferenceNumber(getRemoteReference());
        msg.setSourceLocalReferenceNumber(getLocalReference());
        msg.setCredit(credit);

        flow.setReceiveCredit(credit.getValue());
        flow.initializeMessageNumbering(msg);

        sendMessage(msg);
    }

    private void handleAkMessage(SccpConnAkMessageImpl msg) throws Exception {


        flow.checkInputMessageNumbering(this, msg.getReceiveSequenceNumber().getNumber());
        flow.setSendCredit(msg.getCredit().getValue());

        if (flow.isAuthorizedToTransmitAnotherMessage()) {
            setState(ESTABLISHED);
        } else {
            setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
        }
    }

    public void reset(ResetCause reason) throws Exception {
        super.reset(reason);
        flow.reinitialize();
    }

    protected void setConnectionLock(ReentrantLock lock) {
        if (getState() != NEW) {
            throw new IllegalStateException();
        }
        super.setConnectionLock(lock);
    }

    protected boolean isCanSendData() {
        try {
            connectionLock.lock();

            SccpConnectionState oldState = getState();
            if (oldState == ESTABLISHED_SEND_WINDOW_EXHAUSTED && flow.isAuthorizedToTransmitAnotherMessage()) {
                setState(ESTABLISHED);
            }
            return getState() == ESTABLISHED;

        } finally {
            connectionLock.unlock();
        }
    }

    public Credit getSendCredit() {
        return new CreditImpl(flow.getSendCredit());
    }

    public Credit getReceiveCredit() {
        return new CreditImpl(flow.getReceiveCredit());
    }

    protected boolean isPreemptiveAck() {
        return flow.isPreemptiveAk();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ConnectionWithFlowControl[");

        fillSccpConnectionFields(sb);
        if (overloaded)
            sb.append(", overloaded");
        sb.append("]");

        return sb.toString();
    }
}
