package org.mobicents.protocols.ss7.sccp.parameter;

public interface SequenceNumber {
    int MIN_VALUE = 0;
    int MAX_VALUE = 127;

    int getValue();
    SequenceNumber increment();
}
