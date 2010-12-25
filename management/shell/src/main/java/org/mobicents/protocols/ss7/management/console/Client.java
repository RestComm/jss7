package org.mobicents.protocols.ss7.management.console;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

import javolution.util.FastList;

import org.mobicents.ss7.management.transceiver.ChannelProvider;
import org.mobicents.ss7.management.transceiver.ChannelSelectionKey;
import org.mobicents.ss7.management.transceiver.ChannelSelector;
import org.mobicents.ss7.management.transceiver.Message;
import org.mobicents.ss7.management.transceiver.ShellChannel;

public class Client {

    private ChannelProvider provider;
    private ShellChannel channel;
    private ChannelSelector selector;
    private ChannelSelectionKey skey;

    private boolean isConnected = false;
    
    private boolean wrote = false;

    public Client() {
        provider = ChannelProvider.open();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect(InetSocketAddress endpoint) throws IOException {

        channel = provider.openChannel();
        // channel.bind(new InetSocketAddress(address, port));

        selector = provider.openSelector();
        skey = channel.register(selector, SelectionKey.OP_READ
                | SelectionKey.OP_WRITE);

        channel.connect(endpoint);
        if (channel.isConnectionPending()) {
            while (!channel.isConnected()) {
                channel.finishConnect();
                try {
                    Thread.currentThread().sleep(1);
                } catch (Exception e) {
                }
            }
        }
        this.isConnected = true;
    }

    public Message run(Message outgoing) throws IOException {

        if (!this.isConnected) {
            throw new IllegalStateException("Not yet connected");
        }

        int count = 3;
        wrote = false;

        //Wait for 3 secs to get message 
        while (count > 0) {
            FastList<ChannelSelectionKey> keys = selector.selectNow();

            for (FastList.Node<ChannelSelectionKey> n = keys.head(), end = keys
                    .tail(); (n = n.getNext()) != end;) {
                ChannelSelectionKey key = n.getValue();
                ShellChannel chan = (ShellChannel) key.channel();

                if (!wrote && key.isWritable()) {
                    chan.send(outgoing);
                    wrote = true;
                }

                if (key.isReadable()) {
                    Message msg = (Message) chan.receive();
                    return msg;
                }
            }// End of For loop

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            count--;
        }//end of while
        return null;

    }

    protected void stop() {
        this.isConnected = false;

        if (skey != null) {
            skey.cancel();
            skey = null;
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
            }
            selector = null;
        }

        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
            }
            channel = null;
        }
    }
}
