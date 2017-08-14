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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.CLOSED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.CR_RECEIVED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.CR_SENT;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED_SEND_WINDOW_EXHAUSTED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.NEW;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.RSR_RECEIVED;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.RSR_SENT;

public abstract class BaseSccpConnectionImpl {
    private static final int SLEEP_DELAY = 15;

    protected final Logger logger;
    protected SccpStackImpl stack;
    protected SccpRoutingControl sccpRoutingControl;
    protected SccpListener listener;

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
    private ReentrantLock sendLock = new ReentrantLock();
    private int remoteDpc;
    private FlowControlWindows windows;

    private List<byte[]> buffer = new ArrayList<>();
    private boolean appendNextMessage = false;

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
        return state == ESTABLISHED || state == ESTABLISHED_SEND_WINDOW_EXHAUSTED;
    }

    private void sendData(byte[] data, boolean moreData) throws Exception {
        if (protocolClass.getProtocolClass() == 2) {
            SccpConnDt1MessageImpl msg = new SccpConnDt1MessageImpl(255, sls, localSsn);
            msg.setDestinationLocalReferenceNumber(remoteReference);
            msg.setSegmentingReassembling(new SegmentingReassemblingImpl(moreData));
            msg.setUserData(data);

            msg.setSourceLocalReferenceNumber(localReference);
            sendMessage(msg);
        } else {
            if (state != ESTABLISHED) {
                int totalDelay = 0;
                while (totalDelay <= stack.getSendTimeout() && state != ESTABLISHED) {
                    Thread.sleep(SLEEP_DELAY);
                    totalDelay += SLEEP_DELAY;
                }
                if (state != ESTABLISHED) {
                    throw new IllegalStateException("Send timeout reached due to state " + state);
                }
            }

            SccpConnDt2MessageImpl msg = new SccpConnDt2MessageImpl(255, sls, localSsn);
            msg.setDestinationLocalReferenceNumber(remoteReference);
            msg.setSequencingSegmenting(new SequencingSegmentingImpl(windows.getSendSequenceNumber(), windows.getReceiveSequenceNumber(), moreData));

            windows.incrementSendSequenceNumber();

            msg.setUserData(data);

            msg.setSourceLocalReferenceNumber(localReference);
            sendMessage(msg);
            if (windows.sendSequenceWindowExhausted()) {
                setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
            }
        }
    }

    public void send(byte[] data) throws Exception {
        try {
            sendLock.lock();

            if (data.length <= 255) {
                sendData(data, false);
            } else {
                int chunks = (int) Math.ceil(data.length / 255.0);
                int pos = 0;
                List<byte[]> chunkData = new ArrayList<>();
                for (int i = 0; i < chunks; i++) {
                    int copyBytes;
                    if (i != chunks - 1) {
                        copyBytes = 255;
                        chunkData.add(Arrays.copyOfRange(data, pos, pos + 255));
                    } else {
                        copyBytes = data.length - i * 255;
                        chunkData.add(Arrays.copyOfRange(data, pos, pos + copyBytes));
                    }

                    pos += copyBytes;
                }
                for (int i = 0; i < chunkData.size(); i++) {
                    sendData(chunkData.get(i), i != chunkData.size() - 1);
                }
            }

        } finally {
            sendLock.unlock();
        }
    }

    public SccpConnectionState getState() {
        return state;
    }

    public Credit getSendCredit() {
        return new CreditImpl(windows.getSendCredit());
    }

    public Credit getReceiveCredit() {
        return new CreditImpl(windows.getReceiveCredit());
    }

    public void reset(ResetCause reason) throws Exception {
        SccpConnRsrMessageImpl rsr = new SccpConnRsrMessageImpl(sls, localSsn);
        rsr.setSourceLocalReferenceNumber(localReference);
        rsr.setDestinationLocalReferenceNumber(remoteReference);
        rsr.setResetCause(reason);
        state = RSR_SENT;
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
        iasProcess.stopTimer();
        iarProcess.stopTimer();
    }

    public void refuse(RefusalCause reason, byte[] data) throws Exception {

        SccpConnCrefMessageImpl cref = new SccpConnCrefMessageImpl(sls, localSsn);
        cref.setDestinationLocalReferenceNumber(remoteReference);
        cref.setRefusalCause(reason);
        cref.setUserData(data);
//        ans.setOutgoingDpc();
        sendMessage(cref);
        setState(CLOSED);
    }

    public void confirm(SccpAddress respondingAddress, Credit credit) throws Exception {
        if (state != CR_RECEIVED) {
            throw new IllegalStateException("confirm");
        }
        SccpConnCcMessageImpl message = new SccpConnCcMessageImpl(sls, localSsn);
        message.setSourceLocalReferenceNumber(localReference);
        message.setDestinationLocalReferenceNumber(remoteReference);
        message.setProtocolClass(protocolClass);
        message.setCalledPartyAddress(respondingAddress);
        if (protocolClass.getProtocolClass() == 3) {
            message.setCredit(credit);
            windows.setReceiveCredit(message.getCredit().getValue());
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
                    || this.state == NEW && state == CLOSED
                    || this.state == CR_RECEIVED && state == ESTABLISHED
                    || this.state == CR_RECEIVED && state == CLOSED
                    || this.state == CR_SENT && state == ESTABLISHED
                    || this.state == CR_SENT && state == CLOSED
                    || this.state == ESTABLISHED && state == CLOSED
                    || this.state == ESTABLISHED && state == RSR_SENT
                    || this.state == ESTABLISHED && state == RSR_RECEIVED
                    || this.state == ESTABLISHED && state == ESTABLISHED_SEND_WINDOW_EXHAUSTED
                    || this.state == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == ESTABLISHED
                    || this.state == RSR_SENT && state == ESTABLISHED
                    || this.state == RSR_SENT && state == CLOSED
                    || this.state == RSR_RECEIVED && state == ESTABLISHED
                    || this.state == RSR_RECEIVED && state == CLOSED
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
                windows.setReceiveCredit(message.getCredit().getValue());
            }

            if (message.getCalledPartyAddress() == null) {
                throw new IOException("Message to send must have filled CalledPartyAddress field");
            }

            sendMessage(message);
            connEstProcess.startTimer();

            setState(CR_SENT);

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
            windows.setSendCredit(message.getCredit().getValue());
        }
        setState(CR_RECEIVED);
    }

    protected void handleCCMessage(SccpConnCcMessageImpl message) {
        remoteReference = message.getSourceLocalReferenceNumber();
        connEstProcess.stopTimer();
        if (message.getCredit() != null) {
            windows.setSendCredit(message.getCredit().getValue());
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
        setState(RSR_RECEIVED);

        SccpConnRscMessageImpl rsc = new SccpConnRscMessageImpl(sls, localSsn);
        rsc.setDestinationLocalReferenceNumber(remoteReference);
        rsc.setSourceLocalReferenceNumber(localReference);
        sendMessage(rsc);

        setState(SccpConnectionState.ESTABLISHED);
    }

    private void handleData(byte[] data, boolean moreData) {
        if (!moreData && !appendNextMessage) {
            callListenerOnData(data);
        } else if (moreData) {
            appendNextMessage = true;
            buffer.add(data);
        } else if (!moreData && appendNextMessage) {
            buffer.add(data);
            appendNextMessage = false;

            int totalLength = 0;
            for (int i = 0; i < buffer.size(); i++) {
                totalLength += buffer.get(i).length;
            }
            byte[] allData = new byte[totalLength];
            int pos = 0;
            for (int i = 0; i < buffer.size(); i++) {
                System.arraycopy(buffer.get(i),0, allData,pos         , buffer.get(i).length);
                pos += buffer.get(i).length;
            }

            callListenerOnData(allData);
            buffer.clear();
        }
    }

    protected abstract void callListenerOnData(byte[] data);

    public synchronized void handleDT1Message(SccpConnDt1MessageImpl msg) throws Exception {
        if (state != ESTABLISHED && state != ESTABLISHED_SEND_WINDOW_EXHAUSTED) {
            logger.error(state + " Message discarded " + msg);
            return;
        }

        handleData(msg.getUserData(), msg.getSegmentingReassembling().isMoreData());
    }

    public synchronized void handleDT2Message(SccpConnDt2MessageImpl msg) throws Exception {
        if (state != ESTABLISHED && state != ESTABLISHED_SEND_WINDOW_EXHAUSTED) {
            logger.error(state + " Message discarded " + msg);
            return;
        }
        FlowControlWindows.SendSequenceNumberHandlingResult result = windows.handleSequenceNumbers(
                (byte)msg.getSequencingSegmenting().getSendSequenceNumber(),
                (byte)msg.getSequencingSegmenting().getReceiveSequenceNumber());
        if (result.isResetNeeded()) {
            reset(new ResetCauseImpl(result.getResetCause()));
            return;
        }
        if (result.isEndReached()) {
            SccpConnAkMessageImpl msgAk = new SccpConnAkMessageImpl(sls, localSsn);
            msgAk.setDestinationLocalReferenceNumber(remoteReference);
            msgAk.setReceiveSequenceNumber(new ReceiveSequenceNumberImpl(windows.getLastReceiveSequenceNumberSent()));
            msgAk.setCredit(getSendCredit());

            msgAk.setSourceLocalReferenceNumber(localReference);

            windows.reloadReceiveSequenceNumber();

            sendMessage(msgAk);
        }

        handleData(msg.getUserData(), msg.getSequencingSegmenting().isMoreData());
        return;
    }

    public synchronized void handleAkMessage(SccpConnAkMessageImpl msg) throws Exception {

        FlowControlWindows.SendSequenceNumberHandlingResult result = windows.handleSequenceNumbers(null, (byte)msg.getReceiveSequenceNumber().getValue());
        if (result.isResetNeeded()) {
            reset(new ResetCauseImpl(result.getResetCause()));
        } else {
            windows.reloadSendSequenceNumber();
            if (!windows.sendSequenceWindowExhausted()) {
                setState(ESTABLISHED);
            }
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
