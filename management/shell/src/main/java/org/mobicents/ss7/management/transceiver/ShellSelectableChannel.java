package org.mobicents.ss7.management.transceiver;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public abstract class ShellSelectableChannel {

    //underlying network channel
    protected SelectableChannel channel;

    public ShellSelectionKey register(ChannelSelector selector, int ops) throws ClosedChannelException {
        SelectionKey k = channel.register(selector.selector, ops);
        ShellSelectionKey key = new ShellSelectionKey(this, k);
        k.attach(key);
        return key;
    }
}
