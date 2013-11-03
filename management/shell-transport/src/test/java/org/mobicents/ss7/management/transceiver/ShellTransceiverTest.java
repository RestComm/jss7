/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.ss7.management.transceiver;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;

import javolution.util.FastSet;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ShellTransceiverTest {
    private InetAddress localhost;

    private int serverPort = 9084;
    private int clientPort = 9085;

    private Server server;
    private Client client;

    public ShellTransceiverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() throws UnknownHostException, IOException {
        localhost = InetAddress.getLocalHost();

        client = new Client(localhost, clientPort);
        server = new Server(localhost, serverPort);

    }

    @AfterTest
    public void tearDown() {
    }

    private Message createMessage(ChannelProvider provider, String text) {
        Message msg = provider.getMessageFactory().createMessage(text);
        return msg;
    }

    /**
     * Test of connect method, of class M3UASocket.
     */
    @Test
    @SuppressWarnings("static-access")
    public void testSockets() throws Exception {
        System.out.println("Starting server");
        server.start();
        Thread.currentThread().sleep(100);

        System.out.println("Connecting to server");
        client.connect(localhost, serverPort);
        System.out.println("Client connected");

        client.start();

        Thread.currentThread().sleep(3000);

        client.stop();
        server.stop();

        assertEquals("Hello world", server.getReceivedMessage());
        assertEquals("Hello world", client.getReceivedMessage());
    }

    private class Client implements Runnable {
        private ChannelProvider provider;
        private ShellChannel channel;
        private ChannelSelector selector;
        private ChannelSelectionKey skey;
        private volatile boolean started = false;

        private String rxMessage = "";
        private String[] txMessage = new String[] { "Hello", " ", "world" };

        public Client(InetAddress address, int port) throws IOException {
            provider = ChannelProvider.provider();
            channel = provider.openChannel();
            channel.bind(new InetSocketAddress(address, port));

            selector = provider.openSelector();
            skey = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

        @SuppressWarnings("static-access")
        public void connect(InetAddress endpoint, int port) throws IOException {
            channel.connect(new InetSocketAddress(endpoint, port));
            if (channel.isConnectionPending()) {
                while (!channel.isConnected()) {
                    channel.finishConnect();
                    try {
                        Thread.currentThread().sleep(10);
                    } catch (Exception e) {
                    }
                }
            }
        }

        public void start() {
            started = true;
            new Thread(this).start();
        }

        public void stop() throws IOException {
            started = false;
        }

        public String getReceivedMessage() {
            return rxMessage;
        }

        public void run() {
            int i = 0;
            while (started) {
                try {
                    FastSet<ChannelSelectionKey> keys = selector.selectNow();

                    for (FastSet.Record record = keys.head(), end = keys.tail(); (record = record.getNext()) != end;) {
                        ChannelSelectionKey key = (ChannelSelectionKey) keys.valueOf(record);
                        ShellChannel chan = (ShellChannel) key.channel();
                        if (key.isReadable()) {
                            Message msg = (Message) chan.receive();
                            System.out.println("Receive message " + msg);
                            rxMessage += msg.toString();
                        }

                        if (key.isWritable() && i < txMessage.length) {
                            chan.send(createMessage(this.provider, txMessage[i++]));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                skey.cancel();
                selector.close();
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Server implements Runnable {

        private ChannelProvider provider;
        private ShellServerChannel serverChannel;
        private ShellChannel channel;
        private ChannelSelector selector;
        private ChannelSelectionKey skey;

        private volatile boolean started = false;

        private String rxMessage = "";
        private String[] txMessage = new String[] { "Hello", " ", "world" };

        public Server(InetAddress address, int port) throws IOException {
            provider = ChannelProvider.provider();
            serverChannel = provider.openServerChannel();
            serverChannel.bind(new InetSocketAddress(address, port));

            selector = provider.openSelector();
            skey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        }

        public void start() {
            started = true;
            new Thread(this).start();
        }

        public void stop() throws IOException {
            started = false;
        }

        public String getReceivedMessage() {
            return rxMessage;
        }

        private void accept() throws IOException {
            channel = serverChannel.accept();
            skey.cancel();
            skey = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

        public void run() {
            int i = 0;
            while (started) {
                try {
                    FastSet<ChannelSelectionKey> keys = selector.selectNow();

                    for (FastSet.Record record = keys.head(), end = keys.tail(); (record = record.getNext()) != end;) {
                        ChannelSelectionKey key = (ChannelSelectionKey) keys.valueOf(record);

                        if (key.isAcceptable()) {
                            ShellServerChannel chan = (ShellServerChannel) key.channel();
                            accept();
                        } else if (key.isReadable()) {
                            ShellChannel chan = (ShellChannel) key.channel();
                            Message msg = (Message) chan.receive();
                            System.out.println("Receive " + msg);
                            rxMessage += msg.toString();
                        } else if (key.isWritable() && i < txMessage.length) {
                            ShellChannel chan = (ShellChannel) key.channel();
                            chan.send(createMessage(this.provider, txMessage[i++]));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
        }
    }
}
