package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.sccp.impl.message.MessageUtil;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnAkMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnDt1MessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnMessage;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.RefusalCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCause;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCause;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.ss7.scheduler.Task;

import java.util.concurrent.ConcurrentLinkedQueue;

import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;

abstract class SccpConnectionWithTransmitQueueImpl extends SccpConnectionBaseImpl {
    private static final int SLEEP_DELAY = 15;
    private static final int OUTGOING_SIZE_LIMIT = 10000;

    private MessageSender messageSender;
    // queue for DT1/DT2 and AK messages
    private final ConcurrentLinkedQueue<SccpConnMessage> outgoing = new ConcurrentLinkedQueue<SccpConnMessage>();

    public SccpConnectionWithTransmitQueueImpl(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(sls, localSsn, localReference, protocol, stack, sccpRoutingControl);
        messageSender = new MessageSender(this.stack.scheduler);
    }

    protected void sendMessage(SccpConnMessage message) throws Exception {
        if (stack.state != SccpStackImpl.State.RUNNING) {
            logger.error("Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING");
            return;
        }
        if (!(message instanceof SccpConnSegmentableMessageImpl)) {
            super.sendMessage(message);

        } else {
            if (MessageUtil.getDln(message) == null) {
                logger.error(String.format("Message doesn't have DLN set: ", message));
                throw new IllegalStateException();
            }
            if (outgoing.size() > OUTGOING_SIZE_LIMIT) {
                logger.error(String.format("Outgoing messages queue overloaded, already reached the limit %d", OUTGOING_SIZE_LIMIT));
                throw new IllegalStateException(String.format("Outgoing messages queue overloaded, already reached the limit %d", OUTGOING_SIZE_LIMIT));
            }
            this.outgoing.add(message);
            this.messageSender.submit();
        }
    }

    public void reset(ResetCause reason) throws Exception {
        super.reset(reason);
        clearTransmitQueue();
    }

    public void disconnect(ReleaseCause reason, byte[] data) throws Exception {
        super.disconnect(reason, data);
        clearTransmitQueue();
    }

    public void refuse(RefusalCause reason, byte[] data) throws Exception {
        super.refuse(reason, data);
        clearTransmitQueue();
    }

    protected int getTransmitQueueSize() {
        return outgoing.size();
    }

    private void clearTransmitQueue() {
        outgoing.clear();
    }

    private class MessageSender extends Task {
        public MessageSender(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return Scheduler.L4WRITE_QUEUE;
        }

        public void submit() {
            scheduler.submit(this, getQueueNumber());
        }

        public long perform() {
            SccpConnMessage message;
            while (!outgoing.isEmpty() && getState() == ESTABLISHED) {
                message = outgoing.poll();

                try {
                    SccpConnectionWithTransmitQueueImpl.super.sendMessage(message);

                } catch (Exception e) {
                    // log here Exceptions from MTP3 level
                    logger.error("IOException when sending the message to MTP3 level: " + e.getMessage(), e);
                    throw new RuntimeException(e);
                }

            }
            if (!outgoing.isEmpty()) {
                try {
                    Thread.sleep(SLEEP_DELAY);
                } catch (InterruptedException e) {
                    logger.error(e);
                    throw new RuntimeException(e);
                }
                submit();
            }

            return 0;
        }
    }
}
