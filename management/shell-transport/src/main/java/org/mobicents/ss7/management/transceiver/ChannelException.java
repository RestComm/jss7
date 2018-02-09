package org.mobicents.ss7.management.transceiver;

import java.io.IOException;

public class ChannelException extends IOException {

    private static final long serialVersionUID = 1L;

    private ChannelSelectionKey key;

    public ChannelException(ChannelSelectionKey key, String message) {
        super(message);
        this.key = key;
    }

    public ChannelSelectionKey getKey() {
        return key;
    }
}
