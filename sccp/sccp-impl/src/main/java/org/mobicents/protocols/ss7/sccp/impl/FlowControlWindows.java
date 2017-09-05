package org.mobicents.protocols.ss7.sccp.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCauseValue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class FlowControlWindows {
    protected final Logger logger;
    protected ReentrantLock windowLock;

    private byte sendFlowControlWindowStartNumber = 0;
    private int sendFlowControlWindowSize = 127;

    private byte receiveFlowControlWindowStartNumber = 0;
    private int receiveFlowControlWindowSize = 127;

    private byte lastReceiveSequenceNumberReceived;
    private byte lastReceiveSequenceNumberSent;

    private byte nextSendSequenceNumber = 0;
    private byte nextReceiveSequenceNumber = 0;

    public FlowControlWindows(String name, ReentrantLock windowLock) {
        this.logger = Logger.getLogger(FlowControlWindows.class.getCanonicalName() + "-" + name);
        this.windowLock = windowLock;
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
            Range allowedReceiveFlowControlNumbers = Range.rangeCountAfter(receiveFlowControlWindowStartNumber, receiveFlowControlWindowSize);
            if (sendSequenceNumber == nextReceiveSequenceNumber
                    && allowedReceiveFlowControlNumbers.contains(sendSequenceNumber)) {
                incrementReceiveSequenceNumber();
            } else {
                needReset = true;
                cause = ResetCauseValue.MESSAGE_OUT_OF_ORDER_INCORRECT_PS;
            }
            allowedReceiveFlowControlNumbers = Range.rangeCountAfter(receiveFlowControlWindowStartNumber, receiveFlowControlWindowSize);
            endReached = !allowedReceiveFlowControlNumbers.contains(nextReceiveSequenceNumber);
        }

        if (Range.rangeFromTo(lastReceiveSequenceNumberReceived, nextSendSequenceNumber).contains(receiveSequenceNumber)) {
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

    /*
    private boolean isAkNeeded() {
        try {
            windowLock.lock();
            double exhaustionPercentage = (receiveFlowControlWindowSize < 50) ? 0.1 : 0.5;
            Range receiveFlowControlNumbers = getReceiveFlowControlWindowRange();
            return !receiveFlowControlNumbers.isInTheFirstPart(exhaustionPercentage, nextReceiveSequenceNumber);
        } finally {
            windowLock.unlock();
        }
    }
     */

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
        Range allowedSendFlowControlNumbers = Range.rangeCountAfter(sendFlowControlWindowStartNumber, sendFlowControlWindowSize);

        if (!allowedSendFlowControlNumbers.contains(sendSequenceNumber)) {
            throw new IllegalStateException("P(S) is larger than send window end");
        }
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("returned P(S) = %d", nextSendSequenceNumber));
        }
        return sendSequenceNumber;
    }

    public synchronized boolean sendSequenceWindowExhausted() {
        return !Range.rangeCountAfter(sendFlowControlWindowStartNumber, sendFlowControlWindowSize).contains(nextSendSequenceNumber);
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
        sendFlowControlWindowStartNumber = 0;
        nextSendSequenceNumber = sendFlowControlWindowStartNumber;
    }

    public void reloadReceiveSequenceNumber() {
        nextReceiveSequenceNumber = receiveFlowControlWindowStartNumber;
    }

    public static class Range {
        private static final int MAX = 128;
        private final int start;
        private final int end;
        private final Integer size;

        private Range(int start, int end) {
            this.start = start;
            this.end = end;
            this.size = null;
        }

        private Range(int start, int end, int size) {
            this.start = start;
            this.end = end;
            this.size = size;
        }

        public boolean contains(int number) {
            if (end < 0 || (size != null && size == 0)) {
                return false;
            }
            if (number == start || number == end) {
                return true;
            }

            if (end < start) {
                return start < number && number < MAX || 0 <= number && number < end;
            } else {
                return start < number && number < end;
            }
        }

        public static Range rangeFromTo(int start, int end) {
            return new Range(start, end);
        }

        public static Range rangeCountAfter(int start, int howFar) {
            return new Range(start, (start + howFar - 1) % MAX, howFar);
        }

        public boolean isInTheFirstPart(double percentage, byte number) {
            if (size == null) {
                throw new IllegalArgumentException("Not implemented");
            }
            if (percentage > 1 || percentage < 0) {
                throw new IllegalArgumentException();
            }
            return rangeCountAfter(start, (int) (size * percentage)).contains(number);
        }

        @Override
        public String toString() {
            return "Range[" + start + ", " + end + "], size=" + size +
                    '}';
        }
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

        /*
        public boolean isEndReached() {
            return endReached;
        }
        */

        public boolean isAkNeeded() {
            return endReached;
        }

        public ResetCauseValue getResetCause() {
            return resetCause;
        }
    }
}
