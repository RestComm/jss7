package org.mobicents.ss7.management.transceiver;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public abstract class ShellSelectableChannel {

    //underlying network channel
    protected SelectableChannel channel;

    /**
     * (Non Java-doc.)
     * 
     * @see  org.mobicents.protocols.ss7.m3ua.M3UASelectableChannel#register(org.mobicents.protocols.ss7.m3ua.M3UASelector, int);
     */
    public ShellSelectionKey register(ShellSelector selector, int ops) throws ClosedChannelException {
        SelectionKey k = channel.register(selector.selector, ops);
        ShellSelectionKey key = new ShellSelectionKey(this, k);
        k.attach(key);
        return key;
    }
}
