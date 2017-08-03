package org.mobicents.protocols.ss7.sccp.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnAkMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCrMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCrefMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnDt1MessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRlcMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRlsdMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRscMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRsrMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReceiveSequenceNumberImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ResetCauseImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentingReassemblingImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SequencingSegmentingImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpConnMessage;
import org.mobicents.protocols.ss7.sccp.parameter.Credit;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.RefusalCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.CLOSED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.CR_RECEIVED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.CR_SENT;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.NEW;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.RSR_RECEIVED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.RSR_SENT;

public class BaseSccpConnectionImpl {
    private static final int SLEEP_DELAY = 15;

    protected final Logger logger;
    protected SccpStackImpl stack;
    protected SccpRoutingControl sccpRoutingControl;

    private SccpConnectionState state;
    private int sls;
    private int localSsn;
    private ProtocolClass protocolClass;
    private LocalReference localReference;
    private LocalReference remoteReference;

    private ConnEstProcess connEstProcess;
    private IasProcess iasProcess;
    private IarProcess iarProcess;
    private RelProcess relProcess;
    private RepeatRelProcess repeatRelProcess;
    private IntProcess intProcess;
    private GuardProcess guardProcess;
    private ResetProcess resetProcess;
    private ReassemblyProcess reassemblyProcess;

    private ReentrantLock connectionLock = new ReentrantLock();
    private int remoteDpc;
    private SccpListener listener;
    private FlowControlWindows windows;

    public BaseSccpConnectionImpl(int sls, int localSsn, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        this.stack = stack;
        this.windows = new FlowControlWindows(stack.name);
        this.sccpRoutingControl = sccpRoutingControl;
        this.sls = sls;
        this.localSsn = localSsn;
        this.protocolClass = protocol;
        this.state = NEW;
        this.logger = Logger.getLogger(BaseSccpConnectionImpl.class.getCanonicalName() + "-" + stack.name);
        this.listener = stack.sccpProvider.getSccpListener(localSsn);

        connEstProcess = new ConnEstProcess();
        iasProcess = new IasProcess();
        iarProcess = new IarProcess();
        relProcess = new RelProcess();
        repeatRelProcess = new RepeatRelProcess();
        intProcess = new IntProcess();
        guardProcess = new GuardProcess();
        resetProcess = new ResetProcess();
        reassemblyProcess = new ReassemblyProcess();
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

    public void setLocalReference(LocalReference localReference) {
        this.localReference = localReference;
    }

    public void setRemoteReference(LocalReference remoteReference) {
        this.remoteReference = remoteReference;
    }

    public boolean isAvailable() {
        return false;
    }

    public void send(byte[] data) throws Exception {
        if (protocolClass.getProtocolClass() == 2) {
            if (state != ESTABLISHED ) {
                int totalDelay = 0;
                while (totalDelay <= stack.getSendTimeout() && state != ESTABLISHED) {
                    Thread.sleep(SLEEP_DELAY);
                    totalDelay += SLEEP_DELAY;
                }
                if (state != ESTABLISHED) {
                    throw new IllegalStateException("Send timeout reached ");
                }
            }
        } else {
            if (state != ESTABLISHED || windows.sendSequenceWindowExhausted()) {
                int totalDelay = 0;
                while (totalDelay <= stack.getSendTimeout() && (state != ESTABLISHED || windows.sendSequenceWindowExhausted())) {
                    Thread.sleep(SLEEP_DELAY);
                    totalDelay += SLEEP_DELAY;
                }
                if ((state != ESTABLISHED) || windows.sendSequenceWindowExhausted()) {
                    throw new IllegalStateException("Send timeout reached ");
                }
            }
        }

        synchronized (this) {
            if (protocolClass.getProtocolClass() == 2) {
                SccpConnDt1MessageImpl msg = new SccpConnDt1MessageImpl(255, sls, localSsn);
                msg.setDestinationLocalReferenceNumber(remoteReference);
                msg.setSegmentingReassembling(new SegmentingReassemblingImpl(false));
                msg.setUserData(data);

                msg.setSourceLocalReferenceNumber(localReference);
                sendMessage(msg);
            } else {
                SccpConnDt2MessageImpl msg = new SccpConnDt2MessageImpl(255, sls, localSsn);
                msg.setDestinationLocalReferenceNumber(remoteReference);
                msg.setSequencingSegmenting(new SequencingSegmentingImpl(windows.getSendSequenceNumber(), windows.getReceiveSequenceNumber(), false));

                windows.incrementSendSequenceNumber();

                msg.setUserData(data);

                msg.setSourceLocalReferenceNumber(localReference);
                sendMessage(msg);
            }
        }
    }

    public SccpConnectionState getState() {
        return state;
    }

    public Credit getCredit() {
        return new CreditImpl(windows.getCredit());
    }

    public void reset(ResetCause reason) throws Exception {
        if (state != SccpConnectionState.ESTABLISHED) {
            throw new IllegalStateException("reset");
        }
        SccpConnRsrMessageImpl rsr = new SccpConnRsrMessageImpl(sls, localSsn);
        rsr.setSourceLocalReferenceNumber(localReference);
        rsr.setDestinationLocalReferenceNumber(remoteReference);
        rsr.setResetCause(reason);
        state = SccpConnectionState.RSR_SENT;
        sendMessage(rsr);
        resetProcess.startTimer();
    }

    public void disconnect(ReleaseCause reason, byte[] data) throws Exception {
        if (state != SccpConnectionState.ESTABLISHED) {
            throw new IllegalStateException("disconnect");
        }

        SccpConnRlsdMessageImpl rlsd = new SccpConnRlsdMessageImpl(sls, localSsn);
        rlsd.setDestinationLocalReferenceNumber(remoteReference);
        rlsd.setReleaseCause(reason);
        rlsd.setSourceLocalReferenceNumber(localReference);
        rlsd.setUserData(data);
        sendMessage(rlsd);
        relProcess.startTimer();
    }

    public void disconnect(RefusalCause reason, byte[] data) throws Exception {

        SccpConnCrefMessageImpl cref = new SccpConnCrefMessageImpl(sls, localSsn);
        cref.setDestinationLocalReferenceNumber(remoteReference);
        cref.setRefusalCause(reason);
        cref.setUserData(data);
//        ans.setOutgoingDpc();
        sendMessage(cref);
    }

    public void confirm(SccpAddress respondingAddress) throws Exception {
        if (state != SccpConnectionState.CR_RECEIVED) {
            throw new IllegalStateException("confirm");
        }
        SccpConnCcMessageImpl message = new SccpConnCcMessageImpl(sls, localSsn);
        message.setSourceLocalReferenceNumber(localReference);
        message.setDestinationLocalReferenceNumber(remoteReference);
        message.setProtocolClass(protocolClass);
        message.setCalledPartyAddress(respondingAddress);
        if (protocolClass.getProtocolClass() == 3) {
            message.setCredit(new CreditImpl(1));
            windows.setCredit(message.getCredit().getValue());
        }

        sendMessage(message);

        this.state = SccpConnectionState.ESTABLISHED;
    }

    private void sendMessage(SccpConnMessage message) throws Exception {
        if (stack.state != SccpStackImpl.State.RUNNING) {
            logger.error("Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING");
            return;
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
        synchronized (this) {
            if (!(this.state == NEW && state == CR_SENT
                    || this.state == NEW && state == CR_RECEIVED
                    || this.state == CR_RECEIVED && state == ESTABLISHED
                    || this.state == CR_SENT && state == ESTABLISHED
                    || this.state == ESTABLISHED && state == CLOSED
                    || this.state == ESTABLISHED && state == RSR_SENT
                    || this.state == ESTABLISHED && state == RSR_RECEIVED
                    || this.state == RSR_SENT && state == ESTABLISHED
                    || this.state == RSR_RECEIVED && state == ESTABLISHED
            )) {
                throw new IllegalStateException("state change error");
            }
            this.state = state;
        }
    }

    public void establish(SccpConnCrMessage message) throws IOException {
        try {
            message.setSourceLocalReferenceNumber(localReference);
            if (protocolClass.getProtocolClass() == 3) {
                message.setCredit(new CreditImpl(1));
                windows.setCredit(message.getCredit().getValue());
            }

            if (message.getCalledPartyAddress() == null) {
                throw new IOException("Message to send must have filled CalledPartyAddress field");
            }

            sendMessage(message);
            connEstProcess.startTimer();

            setState(SccpConnectionState.CR_SENT);

        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void setRemoteDpc(int remoteDpc) {
        this.remoteDpc = remoteDpc;
    }

    public int getRemoteDpc() {
        return remoteDpc;
    }

    public SccpListener getListener() {
        return listener;
    }

    public void setListener(SccpListener listener) {
        this.listener = listener;
    }

    protected void stopTimers() {
        connEstProcess.stopTimer();
        iasProcess.stopTimer();
        iarProcess.stopTimer();
        relProcess.stopTimer();
        repeatRelProcess.stopTimer();
        intProcess.stopTimer();
        guardProcess.stopTimer();
        resetProcess.stopTimer();
        reassemblyProcess.stopTimer();
    }

    protected void handleCRMessage(SccpConnCrMessageImpl message) {
        remoteReference = message.getSourceLocalReferenceNumber();
        remoteDpc = message.getIncomingOpc();
        if (message.getCredit() != null) {
            windows.setCredit(message.getCredit().getValue());
        }
        setState(SccpConnectionState.CR_RECEIVED);
    }

    protected void handleCCMessage(SccpConnCcMessageImpl message) {
        remoteReference = message.getSourceLocalReferenceNumber();
        connEstProcess.stopTimer();
        if (message.getCredit() != null) {
            windows.setCredit(message.getCredit().getValue());
        }
        setState(SccpConnectionState.ESTABLISHED);
    }

    protected void handleRSCMessage(SccpConnRscMessageImpl msg) {
        resetProcess.stopTimer();
        setState(SccpConnectionState.ESTABLISHED);
    }

    protected void handleRLCMessage(SccpConnRlcMessageImpl msg) {
        stack.removeConnection(localReference);
    }

    protected void handleRSRMessage(SccpConnRsrMessageImpl msg) throws Exception {
        setState(SccpConnectionState.RSR_RECEIVED);

        SccpConnRscMessageImpl rsc = new SccpConnRscMessageImpl(sls, localSsn);
        rsc.setDestinationLocalReferenceNumber(remoteReference);
        rsc.setSourceLocalReferenceNumber(localReference);
        sendMessage(rsc);

        setState(SccpConnectionState.ESTABLISHED);
    }

    public synchronized boolean handleDT1Message(SccpConnDt1MessageImpl msg) throws Exception {
        if (state != ESTABLISHED) {
            logger.error(state + " Message discarded " + msg);
            return false;
        }

        return true;
    }

    public synchronized boolean handleDT2Message(SccpConnDt2MessageImpl msg) throws Exception {
        if (state != ESTABLISHED) {
            logger.error(state + " Message discarded " + msg);
            return false;
        }
        FlowControlWindows.SendSequenceNumberHandlingResult result = windows.handleSequenceNumbers(
                (byte)msg.getSequencingSegmenting().getSendSequenceNumber(),
                (byte)msg.getSequencingSegmenting().getReceiveSequenceNumber());
        if (result.isResetNeeded()) {
            reset(new ResetCauseImpl(result.getResetCause()));
            return false;
        }
        if (result.isEndReached()) {
            SccpConnAkMessageImpl msgAk = new SccpConnAkMessageImpl(sls, localSsn);
            msgAk.setDestinationLocalReferenceNumber(remoteReference);
            msgAk.setReceiveSequenceNumber(new ReceiveSequenceNumberImpl(windows.getLastReceiveSequenceNumberSent()));
            msgAk.setCredit(getCredit());

            msgAk.setSourceLocalReferenceNumber(localReference);

            sendMessage(msgAk);
        }

        return true;
    }

    public synchronized void handleAkMessage(SccpConnAkMessageImpl msg) throws Exception {
        FlowControlWindows.SendSequenceNumberHandlingResult result = windows.handleSequenceNumbers(null, (byte)msg.getReceiveSequenceNumber().getValue());
        if (result.isResetNeeded()) {
            reset(new ResetCauseImpl(result.getResetCause()));
        }
    }

    private class ConnEstProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getConnEstTimerDelay();
        }

        @Override
        public void run() {
            try {
                connectionLock.lock();

                disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), new byte[]{});

            } catch (Exception e) {
                logger.error(e);
            } finally {
                connectionLock.unlock();
            }
        }
    }

    private class IasProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getIasTimerDelay();
        }

        @Override
        public void run() {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    private class IarProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getIarTimerDelay();
        }

        @Override
        public void run() {
            try {
                connectionLock.lock();

                repeatRelProcess.stopTimer();
                stack.removeConnection(localReference);

            } finally {
                connectionLock.unlock();
            }
        }
    }

    private class RelProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getRelTimerDelay();
        }

        @Override
        public void run() {
            try {
                connectionLock.lock();

                disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), new byte[]{});
                intProcess.startTimer();
                repeatRelProcess.startTimer();

            } catch (Exception e) {
                logger.error(e);
            } finally {
                connectionLock.unlock();
            }
        }
    }

    private class RepeatRelProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getRepeatRelTimerDelay();
        }

        @Override
        public void run() {
            try {
                connectionLock.lock();

                disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), new byte[]{});
                repeatRelProcess.startTimer();

            } catch (Exception e) {
                logger.error(e);
            } finally {
                connectionLock.unlock();
            }
        }
    }

    private class IntProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getIntTimerDelay();
        }

        @Override
        public void run() {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    private class GuardProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getGuardTimerDelay();
        }

        @Override
        public void run() {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    private class ResetProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getResetTimerDelay();
        }

        @Override
        public void run() {
            try {
                connectionLock.lock();

                disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), new byte[]{});
                stack.removeConnection(localReference);

            } catch (Exception e) {
                logger.error(e);
            } finally {
                connectionLock.unlock();
            }
        }
    }

    private class ReassemblyProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getReassemblyTimerDelay();
        }

        @Override
        public void run() {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }


    private class BaseProcess implements Runnable {
        protected long delay = stack.getConnEstTimerDelay();
        private Future future;

        protected void startTimer() {
            try {
                connectionLock.lock();
                if (this.future != null) {
                    logger.error(new IllegalStateException());
                }
                this.future = stack.timerExecutors.schedule(this, delay, TimeUnit.MILLISECONDS);

            } finally {
                connectionLock.unlock();
            }
        }

        protected void stopTimer() {
            try {
                connectionLock.lock();
                if (this.future != null) {
                    this.future.cancel(false);
                    this.future = null;
                }

            } finally {
                connectionLock.unlock();
            }
        }

        @Override
        public void run() {
        }
    }
}
