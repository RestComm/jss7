package org.mobicents.protocols.ss7.sccp.impl.parameter;

import org.mobicents.protocols.ss7.sccp.parameter.SequenceNumber;

public class SequenceNumberImpl implements SequenceNumber {

    public static final int DIVISOR = 128;

    private final byte value;

    // to allow to do number.increment() at method beginning and still have 0 as a first received value
    private boolean nextValueIsZero;

    public SequenceNumberImpl(int value) {
        this(value, false);
    }

    public SequenceNumberImpl(int value, boolean truncate) {
        this(value, truncate, false);
    }

    public SequenceNumberImpl(int value, boolean truncate, boolean nextValueIsZero) {
        if (!truncate) {
            if ((value > SequenceNumber.MAX_VALUE || value < MIN_VALUE)) {
                throw new IllegalArgumentException();
            }
            this.value = (byte) value;
        } else {
            this.value = (byte) (value % DIVISOR);
        }
        if (nextValueIsZero) {
            this.nextValueIsZero = nextValueIsZero;
        }
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public SequenceNumber nextNumber() {
        SequenceNumber newValue;
        if (nextValueIsZero) {
            newValue = new SequenceNumberImpl(0);
        } else {
            if (value + 1 > MAX_VALUE) {
                newValue = new SequenceNumberImpl(0);
            } else {
                newValue = new SequenceNumberImpl(value + 1);
            }
        }
        return newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SequenceNumberImpl that = (SequenceNumberImpl) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SequenceNumber [value=");
        sb.append(this.value);
        sb.append("]");

        return sb.toString();
    }
}
