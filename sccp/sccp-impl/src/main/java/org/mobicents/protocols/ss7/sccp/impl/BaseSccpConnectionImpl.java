package org.mobicents.protocols.ss7.sccp.impl;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCrMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnCrefMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRlsdMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.RefusalCauseImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpConnMessage;
import org.mobicents.protocols.ss7.sccp.parameter.Credit;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.RefusalCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCause;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BaseSccpConnectionImpl {
    protected final Logger logger;

    protected SccpStackImpl stack;
    protected SccpRoutingControl sccpRoutingControl;

    private SccpConnectionState state;
    private int sls;
    private int localSsn;
    private ProtocolClass protocolClass;
    private LocalReference localReference;
    private LocalReference remoteReference;

    private IasProcess iasProcess;
    private IarProcess iarProcess;
    private RelProcess relProcess;
    private RepeatRelProcess repeatRelProcess;
    private IntProcess intProcess;
    private GuardProcess guardProcess;
    private ResetProcess resetProcess;
    private ReassemblyProcess reassemblyProcess;

    private ScheduledExecutorService executor;
    private ReentrantLock connectionLock = new ReentrantLock();
    private int remoteDpc;
    private SccpListener listener;

    public BaseSccpConnectionImpl(int sls, int localSsn, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        this.stack = stack;
        this.sccpRoutingControl = sccpRoutingControl;
        this.sls = sls;
        this.localSsn = localSsn;
        this.protocolClass = protocol;
        this.executor = Executors.newScheduledThreadPool(9, new DefaultThreadFactory(String.format("%s-Connection-%d-Thread",
                stack.getName(), localSsn)));
        this.state = SccpConnectionState.NEW;
        this.logger = Logger.getLogger(BaseSccpConnectionImpl.class.getCanonicalName() + "-" + stack.name);
        this.listener = stack.sccpProvider.getSccpListener(localSsn);

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

    public void send(byte[] data) {

    }

    public SccpConnectionState getState() {
        return state;
    }

    public Credit getCredit() {
        return null;
    }

    public void reset(ResetCause reason) throws Exception {

    }

    public void disconnect(ReleaseCause reason, byte[] data) throws Exception {
        if (state != SccpConnectionState.ESTABLISHED) {
            throw new IllegalStateException();
        }

        SccpConnRlsdMessageImpl rlsd = new SccpConnRlsdMessageImpl(sls, localSsn);
        rlsd.setDestinationLocalReferenceNumber(remoteReference);
        rlsd.setReleaseCause(reason);
        rlsd.setSourceLocalReferenceNumber(localReference);
        state = SccpConnectionState.CLOSED;
        send(rlsd);
        stack.removeConnection(localReference);
    }

    public void disconnect(RefusalCause reason, byte[] data) throws Exception {

        SccpConnCrefMessageImpl cref = new SccpConnCrefMessageImpl(sls, localSsn);
        cref.setDestinationLocalReferenceNumber(remoteReference);
        cref.setRefusalCause(reason);
//        ans.setOutgoingDpc();
        send(cref);
    }

    public void confirm(SccpAddress respondingAddress) throws Exception {
        SccpConnCcMessageImpl message = new SccpConnCcMessageImpl(sls, localSsn);
        message.setSourceLocalReferenceNumber(localReference);
        message.setDestinationLocalReferenceNumber(remoteReference);
        message.setProtocolClass(protocolClass);
        message.setCalledPartyAddress(respondingAddress);

        send(message);

        this.state = SccpConnectionState.ESTABLISHED;
    }

    private void send(SccpConnMessage message) throws Exception {
        if (stack.state != SccpStackImpl.State.RUNNING) {
            logger.error("Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING");
            return;
        }

        try {
            this.sccpRoutingControl.routeMssgFromSccpUser(message);
        } catch (Exception e) {
            // log here Exceptions from MTP3 level
            logger.error("IOException when sending the message to MTP3 level: " + e.getMessage(), e);
            throw e;
        }
    }

    public void setState(SccpConnectionState state) {
        this.state = state;
    }

    private void fail(RuntimeException t) throws RuntimeException {
        throw t;
    }

    public void establish(SccpConnCrMessage message) throws IOException {
        try {
            message.setSourceLocalReferenceNumber(localReference);

            if (stack.state != SccpStackImpl.State.RUNNING) {
                logger.error("Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING");
                return;
            }

            if (message.getCalledPartyAddress() == null) {
                throw new IOException("Message to send must have filled CalledPartyAddress field");
            }

            try {
                this.sccpRoutingControl.routeMssgFromSccpUser(message);
            } catch (Exception e) {
                // log here Exceptions from MTP3 level
                logger.error("IOException when sending the message to MTP3 level: " + e.getMessage(), e);
                throw e;
            }

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

    /*
     * Timer for waiting for connection confirm message
     */
    private class ConnEstProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getConnEstTimerDelay();
        }

        @Override
        public void run()   {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    /*
     * Timer for delay to send a message on a conn IT on a connection section when there are no messages to send
     */
    private class IasProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getIasTimerDelay();
        }

        @Override
        public void run()  {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    /*
     * Waiting to receive a message on a connection section
     */
    private class IarProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getIarTimerDelay();
        }

        @Override
        public void run()  {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    /*
     * Waiting for release complete message
     */
    private class RelProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getRelTimerDelay();
        }

        @Override
        public void run()  {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    /*
     * Waiting for release complete message; or to repeat sending released message after the initial T(rel) expiry
     */
    private class RepeatRelProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getRepeatRelTimerDelay();
        }

        @Override
        public void run()  {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    /*
     * Waiting for release complete message; or to release connection resources, freeze the LRN and alert a maintenance
     * function after the initial T(rel) expiry
     */
    private class IntProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getIntTimerDelay();
        }

        @Override
        public void run()  {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    /*
     * Waiting to resume normal procedure for temporary connection sections during the restart procedure (see 3.8)
     */
    private class GuardProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getGuardTimerDelay();
        }

        @Override
        public void run()  {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    /*
     * Waiting to release temporary connection section or alert maintenance function after reset request message is sent
     */
    private class ResetProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getResetTimerDelay();
        }

        @Override
        public void run()  {
            try {
                connectionLock.lock();

                // do smt...

            } finally {
                connectionLock.unlock();
            }
        }
    }

    /*
     * Waiting to receive all the segments of the remaining segments, single segmented message after receiving the first
     * segment
     */
    private class ReassemblyProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getReassemblyTimerDelay();
        }

        @Override
        public void run()  {
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
                    fail(new IllegalStateException());
                }
                this.future = executor.schedule(this, delay, TimeUnit.MILLISECONDS);

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
        public void run()  {
        }
    }
}
