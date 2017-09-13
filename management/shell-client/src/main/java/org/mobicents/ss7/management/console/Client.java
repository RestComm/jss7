/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.ss7.management.console;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.mobicents.ss7.management.transceiver.ChannelProvider;
import org.mobicents.ss7.management.transceiver.Message;
import org.mobicents.ss7.management.transceiver.MessageFactory;
import org.mobicents.ss7.management.transceiver.ShellChannel;

/**
 *
 * @author amit bhayani
 *
 */
public class Client {

    private ChannelProvider provider;
    private ShellChannel channel;
    private MessageFactory messageFactory;

    private boolean isConnected = false;

    private boolean wrote = false;

    public Client() {
        provider = ChannelProvider.provider();
        messageFactory = provider.createMessageFactory();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean isChannelConnected() {
        return channel.isConnected();
    }

    public void connect(InetSocketAddress endpoint) throws IOException {

        channel = provider.openChannel();
        // channel.bind(new InetSocketAddress(address, port));

        channel.connect(endpoint);
        if (channel.isConnectionPending()) {
            while (!channel.isConnected()) {
                channel.finishConnect();
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            }
        }
        this.isConnected = true;
    }

    public Message run(Message outgoing) throws IOException {

        if (!this.isConnected) {
            return messageFactory.createMessage("Not yet connected");
        }

        int count = 30;
        wrote = false;

        // Wait for 300 secs to get message
        while (count > 0) {
            if (!channel.isConnected()) {
                stop();
                throw new IOException("Channel closed by server");
            }

            if (!wrote) {
                if (outgoing != null) {
                    channel.send(outgoing);
                    channel.doWrite();
                }
                wrote = true;
            } else {
                channel.doRead();
                Message msg = (Message) channel.receive();
                if (msg != null) {
                    return msg;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            count--;
        } // end of while
        throw new IOException("No response from server");

    }

    protected void stop() {
        this.isConnected = false;

        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
            }
            channel = null;
        }
    }
}