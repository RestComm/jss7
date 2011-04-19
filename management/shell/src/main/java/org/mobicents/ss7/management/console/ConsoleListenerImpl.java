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

/**
 * 
 * @author amit bhayani
 * 
 */
public class ConsoleListenerImpl implements ConsoleListener {

    // TODO : We still have to work on formatter and how text on CLI should be
    // displayed

    private String address = "127.0.0.1";
    private int port = 3435;

    protected Console console;
    private Client client = null;

    private MessageFactory messageFactory = ChannelProvider.provider()
            .getMessageFactory();

    public ConsoleListenerImpl() {
        client = new Client();
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    public void commandEntered(String consoleInput) {
        if (consoleInput.compareTo("exit") == 0) {
            if (this.client.isConnected()) {
                this.console
                        .write("Still connected to server. Disconnect first");
                return;
            }
            console.stop();
        } else if (consoleInput.startsWith("ss7 connect")) {

            if (client.isConnected()) {
                console.write("Already connected");
                return;
            }

            String[] commands = consoleInput.split(" ");
            if (commands.length == 2) {
                // use default
                try {
                    client.connect(new InetSocketAddress(address, port));
                    console.setPrefix(Shell.CLI_PREFIX + "(local)"
                            + Shell.CLI_POSTFIX);
                } catch (IOException e) {
                    console.write(e.getMessage());
                }
            } else if (commands.length == 4) {
                address = commands[2];
                port = Integer.parseInt(commands[3]);

                try {
                    client.connect(new InetSocketAddress(address, port));
                    this.console.setPrefix(Shell.CLI_PREFIX + "(" + address +":" + port 
                            + ")" + Shell.CLI_POSTFIX);
                } catch (IOException e) {
                    console.write(e.getMessage());
                }
            } else {
                console.write("Invalid command.");
            }
        } else if (consoleInput.startsWith("ss7 disconnect")) {
            if (!client.isConnected()) {
                console.write("Already disconnected");
                return;
            }
            sendMessage("disconnect");

        } else {
            // Send everything to Server as per Oleg
            sendMessage(consoleInput);
        }
    }

    private void sendMessage(String text) {
        Message outgoing = messageFactory.createMessage(text);
        try {
            Message incoming = this.client.run(outgoing);
            if (incoming != null) {
                this.console.write(incoming.toString());
            } else {
                this.console.write("No response from server");
            }
        } catch (IOException e) {
            this.console.write(e.getMessage());
            this.console.stop();
        }

        if (text.compareTo("disconnect") == 0) {
            this.client.stop();
            this.console.setPrefix(Shell.CLI_PREFIX + Shell.CLI_POSTFIX);
        }
    }

}
