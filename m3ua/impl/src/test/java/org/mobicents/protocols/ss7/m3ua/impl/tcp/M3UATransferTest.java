/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */ 
package org.mobicents.protocols.ss7.m3ua.impl.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;
import org.mobicents.protocols.ss7.m3ua.M3UAServerChannel;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;

import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class M3UATransferTest {

    private InetAddress localhost;
    
    private int serverPort = 6082;
    private int clientPort = 6083;
    
    private Server server;
    private Client client;
        
    public M3UATransferTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws UnknownHostException, IOException {
        localhost = InetAddress.getLocalHost();
        
        client = new Client(localhost, clientPort);
        server = new Server(localhost, serverPort);
        
    }

    @After
    public void tearDown() {
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


    private M3UAMessage createMessage(M3UAProvider provider, String text) {
        PayloadData msg = (PayloadData) provider.getMessageFactory().createMessage(MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
        
        ProtocolData protocolData = provider.getParameterFactory().createProtocolData(1408, 14150, 0, 0, 0, 0, text.getBytes());
        msg.setData(protocolData);
        
        return msg;
    }
    
    private class Client implements Runnable {
        private M3UAProvider provider;
        private M3UAChannel channel;
        private M3UASelector selector;
        private M3UASelectionKey skey;
        private volatile boolean started = false;
        
        private String rxMessage = "";        
        private String[] txMessage = new String[]{"Hello", " ", "world"};
        
        public Client(InetAddress address, int port) throws IOException {
            provider = TcpProvider.provider();
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
                    Collection<M3UASelectionKey> keys = selector.selectNow();
                    for (M3UASelectionKey key : keys) {
                        M3UAChannel chan = (M3UAChannel) key.channel();
                        if (key.isReadable()) {
                            PayloadData msg = (PayloadData) chan.receive();
                            System.out.println("Receive message " + msg);
                            rxMessage += new String(msg.getData().getData());
                        }
                        
                        if (key.isWritable() && i < txMessage.length) {
                            chan.send(createMessage(provider, txMessage[i++]));
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
        
        private M3UAProvider provider;
        private M3UAServerChannel serverChannel;
        private M3UAChannel channel;
        private M3UASelector selector;
        private M3UASelectionKey skey;
        
        private volatile boolean started = false;
        
        private String rxMessage = "";        
        private String[] txMessage = new String[]{"Hello", " ", "world"};
        
        public Server(InetAddress address, int port) throws IOException {
            provider = TcpProvider.provider();
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
                    Collection<M3UASelectionKey> keys = selector.selectNow();
                    for (M3UASelectionKey key : keys) {
                        if (key.isAcceptable()) {
                            M3UAServerChannel chan = (M3UAServerChannel) key.channel();
                            accept();
                        } else if (key.isReadable()) {
                            M3UAChannel chan = (M3UAChannel) key.channel();
                            PayloadData msg = (PayloadData) chan.receive();
                            System.out.println("Receive " + msg);
                            rxMessage += new String(msg.getData().getData());
                        } else if (key.isWritable() && i < txMessage.length) {
                            M3UAChannel chan = (M3UAChannel) key.channel();
                            chan.send(createMessage(provider, txMessage[i++]));
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