/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.ss7;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.ss7.linkset.oam.LinksetManager;
import org.mobicents.ss7.management.console.Subject;
import org.mobicents.ss7.management.transceiver.ChannelProvider;
import org.mobicents.ss7.management.transceiver.ChannelSelectionKey;
import org.mobicents.ss7.management.transceiver.ChannelSelector;
import org.mobicents.ss7.management.transceiver.Message;
import org.mobicents.ss7.management.transceiver.MessageFactory;
import org.mobicents.ss7.management.transceiver.ShellChannel;
import org.mobicents.ss7.management.transceiver.ShellServerChannel;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class ShellExecutor implements Runnable {

    Logger logger = Logger.getLogger(ShellExecutor.class);

    private ChannelProvider provider;
    private ShellServerChannel serverChannel;
    private ShellChannel channel;
    private ChannelSelector selector;
    private ChannelSelectionKey skey;

    private MessageFactory messageFactory = null;

    private String rxMessage = "";
    private String txMessage = "";

    private LinksetManager linksetManager = null;

    private LinksetListenerImpl linksetListenerImpl = null;

    private volatile boolean started = false;

    public ShellExecutor(InetAddress address, int port) throws IOException {
        provider = ChannelProvider.open();
        serverChannel = provider.openServerChannel();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(address,
                port);
        serverChannel.bind(inetSocketAddress);

        selector = provider.openSelector();
        skey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        messageFactory = new MessageFactory();

        this.logger.info(String.format("ShellExecutor listening at %s",
                inetSocketAddress));
    }

    public void start() {
        linksetListenerImpl = new LinksetListenerImpl(this.linksetManager);

        this.started = true;
        new Thread(this).start();
    }

    public void stop() {
        this.started = false;
    }

    protected LinksetManager getLinksetManager() {
        return linksetManager;
    }

    protected void setLinksetManager(LinksetManager linksetManager) {
        this.linksetManager = linksetManager;
    }

    public String getReceivedMessage() {
        return rxMessage;
    }

    public void run() {

        while (started) {
            try {
                FastList<ChannelSelectionKey> keys = selector.selectNow();

                for (FastList.Node<ChannelSelectionKey> n = keys.head(), end = keys
                        .tail(); (n = n.getNext()) != end;) {

                    ChannelSelectionKey key = n.getValue();

                    if (key.isAcceptable()) {
                        accept();
                    } else if (key.isReadable()) {
                        ShellChannel chan = (ShellChannel) key.channel();
                        Message msg = (Message) chan.receive();

                        if (msg != null) {
                            System.out.println("Receive " + msg);
                            rxMessage = msg.toString();

                            if (rxMessage.compareTo("disconnect") == 0) {
                                this.txMessage = "Bye";
                                chan.send(messageFactory
                                        .createMessage(txMessage));

                            } else {
                                String[] options = rxMessage.split(" ");
                                Subject subject = Subject
                                        .getSubject(options[0]);
                                if (subject == null) {
                                    chan.send(messageFactory
                                            .createMessage("Invalid Command"));
                                } else {
                                    // Nullify examined options
                                    options[0] = null;

                                    switch (subject) {
                                    case LINKSET:
                                        this.txMessage = this.linksetListenerImpl
                                                .execute(options);
                                        chan.send(messageFactory
                                                .createMessage(this.txMessage));
                                        break;
                                    }
                                }
                            } // if (rxMessage.compareTo("disconnect")
                        } // if (msg != null)

                        // TODO Handle message

                        rxMessage = "";

                    } else if (key.isWritable() && txMessage.length() > 0) {

                        if (this.txMessage.compareTo("Bye") == 0) {
                            this.closeChannel();
                        } 
                        
//                        else {
//
//                            ShellChannel chan = (ShellChannel) key.channel();
//                            System.out.println("Sending " + txMessage);
//                            chan.send(messageFactory.createMessage(txMessage));
//                        }

                        this.txMessage = "";
                    }
                }
            } catch (IOException e) {
                logger.error("Error while operating on ChannelSelectionKey", e);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        try {
            skey.cancel();
            if (channel != null) {
                channel.close();
            }
            serverChannel.close();
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.logger.info("Stopped ShellExecutor service");
    }

    private void accept() throws IOException {
        channel = serverChannel.accept();
        skey.cancel();
        skey = channel.register(selector, SelectionKey.OP_READ
                | SelectionKey.OP_WRITE);
        System.out.println("Client connected");
    }

    private void closeChannel() throws IOException {
        if (channel != null) {
            try {
                this.channel.close();
            } catch (IOException e) {
                logger.error("Error closing channel", e);
            }
        }
        skey.cancel();
        skey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

}