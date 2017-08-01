package org.mobicents.protocols.ss7.sccp.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCauseValue;

public class FlowControlWindows {
    protected final Logger logger;
    private int credit = 0;

    private byte sendFlowControlWindowStart = 0;
    private byte sendFlowControlWindowEnd = 127;

    private byte receiveFlowControlWindowStart = 0;
    private byte receiveFlowControlWindowEnd = 127;

    private byte lastReceiveSequenceNumberReceived;
    private byte lastReceiveSequenceNumberSent;

    public FlowControlWindows(String name) {
        this.logger = Logger.getLogger(FlowControlWindows.class.getCanonicalName() + "-" + name);
    }

    public synchronized void setCredit(int credit) {
        if (credit > 128) {
            throw new IllegalArgumentException("Window size is too high");
        }
        this.credit = credit;
        this.sendFlowControlWindowEnd = (byte)(credit - 1);
        this.receiveFlowControlWindowEnd = (byte)(credit - 1);
    }

    public synchronized SendSequenceNumberHandlingResult handleSequenceNumbers(Byte sendSequenceNumber, byte receiveSequenceNumber) {
        boolean needReset = false;
        boolean endReached = false;
        ResetCauseValue cause = null;

        if (sendSequenceNumber != null) {
            if (sendSequenceNumber == receiveFlowControlWindowStart
                    && sendSequenceNumber <= receiveFlowControlWindowEnd) {
                incrementReceiveSequenceNumber();
            } else {
                needReset = true;
                cause = ResetCauseValue.MESSAGE_OUT_OF_ORDER_INCORRECT_PS;
            }
            endReached = sendSequenceNumber == receiveFlowControlWindowEnd;
        }

        if ((receiveSequenceNumber >= lastReceiveSequenceNumberReceived)
                && (receiveSequenceNumber <= sendFlowControlWindowStart)) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("P(S) was eq %d, set value to %d", sendFlowControlWindowStart, receiveSequenceNumber));
            }
            sendFlowControlWindowStart = receiveSequenceNumber;
        } else {
            needReset = true;
            cause = ResetCauseValue.MESSAGE_OUT_OF_ORDER_INCORRECT_PR;
        }
        lastReceiveSequenceNumberReceived = receiveSequenceNumber;

        return new SendSequenceNumberHandlingResult(needReset, cause, endReached);
    }



    public synchronized byte getLastReceiveSequenceNumberSent() {
        receiveFlowControlWindowStart = lastReceiveSequenceNumberSent;
        return lastReceiveSequenceNumberSent;
    }

    public synchronized int getCredit() {
        return credit;
    }

    public synchronized byte getSendSequenceNumber() {
        byte sendSequenceNumber = sendFlowControlWindowStart;
        if (sendSequenceNumber > sendFlowControlWindowEnd) {
            throw new IllegalStateException("P(S) is larger than send window end");
        }
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("returned P(S) = %d", sendFlowControlWindowStart));
        }
        return sendSequenceNumber;
    }

    public synchronized boolean sendSequenceWindowExhausted() {
        return sendFlowControlWindowStart > sendFlowControlWindowEnd;
    }

    public synchronized byte getReceiveSequenceNumber() {
        byte receiveSequenceNumber = receiveFlowControlWindowStart;
        lastReceiveSequenceNumberSent = receiveSequenceNumber;
        return receiveSequenceNumber;
    }

    public synchronized void incrementSendSequenceNumber() {
        if (sendFlowControlWindowStart + 1 > 127) {
            sendFlowControlWindowStart = 0;
        }
        sendFlowControlWindowStart++;
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("set P(S) = ", sendFlowControlWindowStart));
        }
    }

    private synchronized void incrementReceiveSequenceNumber() {
        if (receiveFlowControlWindowStart + 1 > 127) {
            receiveFlowControlWindowStart = 0;
        }
        receiveFlowControlWindowStart++;
    }

    public static class SendSequenceNumberHandlingResult {
        private boolean needReset;
        private ResetCauseValue resetCause;
        private boolean endReached;

        public SendSequenceNumberHandlingResult(boolean needReset, ResetCauseValue resetCause, boolean endReached) {
            this.needReset = needReset;
            this.resetCause = resetCause;
            this.endReached = endReached;
        }

        public boolean isResetNeeded() {
            return needReset;
        }

        public boolean isEndReached() {
            return endReached;
        }

        public ResetCauseValue getResetCause() {
            return resetCause;
        }
    }
}
