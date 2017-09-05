package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnAkMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCrMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReceiveSequenceNumberImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ResetCauseImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SequencingSegmentingImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpConnMessage;
import org.mobicents.protocols.ss7.sccp.parameter.Credit;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.CR_RECEIVED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED_SEND_WINDOW_EXHAUSTED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.NEW;

public class SccpConnectionWithFlowControlImpl extends SccpConnectionImpl implements SccpConnection {

    protected FlowControlWindows windows;

    private boolean overloaded;

    public SccpConnectionWithFlowControlImpl(int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(localSsn, localReference, protocol, stack, sccpRoutingControl);
        if (protocol.getProtocolClass() != 3) {
            logger.error("Using connection class for non-supported protocol class 2");
            throw new IllegalArgumentException();
        }
        this.windows = new FlowControlWindows(stack.name, connectionLock);
    }

    public void establish(SccpConnCrMessage message) throws IOException {
        super.establish(message);
        windows.setReceiveCredit(message.getCredit().getValue());
    }

    public void confirm(SccpAddress respondingAddress, Credit credit, byte[] data) throws Exception {
        if (getState() != CR_RECEIVED) {
            logger.error(String.format("Trying to confirm connection in non-compatible state %s", getState()));
            throw new IllegalStateException(String.format("Trying to confirm connection in non-compatible state %s", getState()));
        }
        windows.setReceiveCredit(credit.getValue());

        super.confirm(respondingAddress, credit, data);
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
            SccpConnDt2MessageImpl dt = (SccpConnDt2MessageImpl) message;
            boolean moreData = dt.getSequencingSegmenting().isMoreData();
            byte sendSequenceNumber = windows.getSendSequenceNumber();
            dt.setSequencingSegmenting(new SequencingSegmentingImpl(sendSequenceNumber, windows.getReceiveSequenceNumber(), moreData));

            windows.incrementSendSequenceNumber();
            if (windows.sendSequenceWindowExhausted()) {
                setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    protected void prepareMessageForSending(SccpConnItMessageImpl message) {
        message.setCredit(getReceiveCredit());
        message.setSequencingSegmenting(new SequencingSegmentingImpl(windows.getSendSequenceNumber(),
                windows.getReceiveSequenceNumber(), lastMoreDataSent));
    }

    protected void receiveMessage(SccpConnMessage message) throws Exception {
        try {
            connectionLock.lock();
            super.receiveMessage(message);

            if (message instanceof SccpConnCrMessageImpl) {
                SccpConnCrMessageImpl cr = (SccpConnCrMessageImpl) message;
                if (cr.getCredit() != null) {
                    windows.setSendCredit(cr.getCredit().getValue());
                }

            } else if (message instanceof SccpConnCcMessageImpl) {
                SccpConnCcMessageImpl cc = (SccpConnCcMessageImpl) message;
                if (cc.getCredit() != null) {
                    windows.setSendCredit(cc.getCredit().getValue());
                }

            } else if (message instanceof SccpConnAkMessageImpl) {
                handleAkMessage((SccpConnAkMessageImpl) message);
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

        FlowControlWindows.SendSequenceNumberHandlingResult result = windows.handleSequenceNumbers(
                (byte)((SccpConnDt2MessageImpl)msg).getSequencingSegmenting().getSendSequenceNumber(),
                (byte)((SccpConnDt2MessageImpl)msg).getSequencingSegmenting().getReceiveSequenceNumber());
        if (result.isResetNeeded()) {
            reset(new ResetCauseImpl(result.getResetCause()));
            return;
        }
        if (result.isAkNeeded()) {
            sendAk();
        }

        super.receiveDataMessage(msg);
    }

    protected void sendAk() throws Exception {
        sendAk(getSendCredit());
    }

    protected void sendAk(Credit credit) throws Exception {

        SccpConnAkMessageImpl msgAk = new SccpConnAkMessageImpl(getSls(), getLocalSsn());
        msgAk.setDestinationLocalReferenceNumber(getRemoteReference());
        msgAk.setReceiveSequenceNumber(new ReceiveSequenceNumberImpl(windows.getReceiveSequenceNumber()));
        msgAk.setCredit(credit);

        msgAk.setSourceLocalReferenceNumber(getLocalReference());

        sendMessage(msgAk);
        windows.reloadReceiveSequenceNumber(); //TODO2
    }

    private void handleAkMessage(SccpConnAkMessageImpl msg) throws Exception {

        FlowControlWindows.SendSequenceNumberHandlingResult result = windows.handleSequenceNumbers(null, (byte)msg.getReceiveSequenceNumber().getValue());
        if (result.isResetNeeded()) {
            reset(new ResetCauseImpl(result.getResetCause()));
        } else {
            windows.reloadSendSequenceNumber();
            windows.setSendCredit(msg.getCredit().getValue());
            if (!windows.sendSequenceWindowExhausted()) {
                setState(ESTABLISHED);
            } else {
                setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
            }
        }
    }

    protected void setConnectionLock(ReentrantLock lock) {
        if (getState() != NEW) {
            throw new IllegalStateException();
        }
        super.setConnectionLock(lock);
        this.windows.windowLock = lock;
    }

    public Credit getSendCredit() {
        return new CreditImpl(windows.getSendCredit());
    }

    public Credit getReceiveCredit() {
        return new CreditImpl(windows.getReceiveCredit());
    }
}
