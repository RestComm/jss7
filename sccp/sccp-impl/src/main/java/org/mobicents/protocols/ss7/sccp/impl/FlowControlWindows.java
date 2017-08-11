package org.mobicents.protocols.ss7.sccp.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCauseValue;

import java.util.HashSet;
import java.util.Set;

public class FlowControlWindows {
    protected final Logger logger;

    private byte sendFlowControlWindowStartNumber = 0;
    private int sendFlowControlWindowSize = 127;

    private byte receiveFlowControlWindowStartNumber = 0;
    private int receiveFlowControlWindowSize = 127;

    private byte lastReceiveSequenceNumberReceived;
    private byte lastReceiveSequenceNumberSent;

    private byte nextSendSequenceNumber = 0;
    private byte nextReceiveSequenceNumber = 0;

    public FlowControlWindows(String name) {
        this.logger = Logger.getLogger(FlowControlWindows.class.getCanonicalName() + "-" + name);
    }

    private Set<Byte> getAllowedReceiveFlowControlNumbers() {
        Set<Byte> numbers = new HashSet<>();
        int number = receiveFlowControlWindowStartNumber;
        for (int i = 0; i < receiveFlowControlWindowSize; i++) {
            numbers.add((byte)number);
            number++;
            if (number > 127) {
                number = 0;
            }
        }
        return numbers;
    }

    private Set<Byte> getAllowedSendFlowControlNumbers() {
        Set<Byte> numbers = new HashSet<>();
        int number = sendFlowControlWindowStartNumber;
        for (int i = 0; i < sendFlowControlWindowSize; i++) {
            numbers.add((byte)number);
            number++;
            if (number > 127) {
                number = 0;
            }
        }
        return numbers;
    }

    public synchronized void setSendCredit(int credit) {
        if (credit > 127) {
            throw new IllegalArgumentException("Window size is too high");
        }
        sendFlowControlWindowStartNumber = 0;
        sendFlowControlWindowSize = credit;
    }

    public synchronized void setReceiveCredit(int credit) {
        if (credit > 127) {
            throw new IllegalArgumentException("Window size is too high");
        }
        this.receiveFlowControlWindowStartNumber = 0;
        this.receiveFlowControlWindowSize = credit;
    }

    public synchronized SendSequenceNumberHandlingResult handleSequenceNumbers(Byte sendSequenceNumber, byte receiveSequenceNumber) {
        boolean needReset = false;
        boolean endReached = false;
        ResetCauseValue cause = null;

        if (sendSequenceNumber != null) {
            Set<Byte> allowedReceiveFlowControlNumbers = getAllowedReceiveFlowControlNumbers();
            if (sendSequenceNumber == nextReceiveSequenceNumber
                    && allowedReceiveFlowControlNumbers.contains(sendSequenceNumber)) {
                incrementReceiveSequenceNumber();
            } else {
                needReset = true;
                cause = ResetCauseValue.MESSAGE_OUT_OF_ORDER_INCORRECT_PS;
            }
            allowedReceiveFlowControlNumbers = getAllowedReceiveFlowControlNumbers();
            endReached = !allowedReceiveFlowControlNumbers.contains(nextReceiveSequenceNumber);
        }

        if ((receiveSequenceNumber >= lastReceiveSequenceNumberReceived)
                && (receiveSequenceNumber <= nextSendSequenceNumber)) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("P(S) was eq %d, set value to %d", nextSendSequenceNumber, receiveSequenceNumber));
            }
            sendFlowControlWindowStartNumber = receiveSequenceNumber;
        } else {
            needReset = true;
            cause = ResetCauseValue.MESSAGE_OUT_OF_ORDER_INCORRECT_PR;
        }
        lastReceiveSequenceNumberReceived = receiveSequenceNumber;

        return new SendSequenceNumberHandlingResult(needReset, cause, endReached);
    }

    public synchronized byte getLastReceiveSequenceNumberSent() {
//        receiveFlowControlWindowStart = lastReceiveSequenceNumberSent;
        return lastReceiveSequenceNumberSent;
    }

    public synchronized int getSendCredit() {
        return sendFlowControlWindowSize;
    }

    public synchronized int getReceiveCredit() {
        return receiveFlowControlWindowSize;
    }

    public synchronized byte getSendSequenceNumber() {
        byte sendSequenceNumber = nextSendSequenceNumber;
        if (!getAllowedSendFlowControlNumbers().contains(sendSequenceNumber)) {
            throw new IllegalStateException("P(S) is larger than send window end");
        }
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("returned P(S) = %d", nextSendSequenceNumber));
        }
        return sendSequenceNumber;
    }

    public synchronized boolean sendSequenceWindowExhausted() {
        return !getAllowedSendFlowControlNumbers().contains(nextSendSequenceNumber);
    }

    public synchronized byte getReceiveSequenceNumber() {
        byte receiveSequenceNumber = nextReceiveSequenceNumber;
        lastReceiveSequenceNumberSent = receiveSequenceNumber;
        return receiveSequenceNumber;
    }

    public synchronized void incrementSendSequenceNumber() {
        if (nextSendSequenceNumber + 1 > 127) {
            nextSendSequenceNumber = 0;
        }
        nextSendSequenceNumber++;
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("set P(S) = ", nextSendSequenceNumber));
        }
    }

    private synchronized void incrementReceiveSequenceNumber() {
        if (nextReceiveSequenceNumber + 1 > 127) {
            nextReceiveSequenceNumber = 0;
        }
        nextReceiveSequenceNumber++;
    }

    public void reloadSendSequenceNumber() {
        nextSendSequenceNumber = sendFlowControlWindowStartNumber;
    }

    public void reloadReceiveSequenceNumber() {
        nextReceiveSequenceNumber = receiveFlowControlWindowStartNumber;
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
