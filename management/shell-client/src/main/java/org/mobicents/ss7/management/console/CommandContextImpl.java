/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
import java.util.Collection;

import org.mobicents.ss7.management.transceiver.ChannelProvider;
import org.mobicents.ss7.management.transceiver.Message;
import org.mobicents.ss7.management.transceiver.MessageFactory;

/**
 * @author amit bhayani
 *
 */
public class CommandContextImpl implements CommandContext {

    public static final String CONNECTED_AUTHENTICATING_MESSAGE = "Authenticating against configured security realm";
    public static final String CONNECTED_AUTHENTICATION_FAILED = "Authentication failed";

    public static final String CLOSING_CONNECTION_MESSAGE = "Closing this connection";

    private MessageFactory messageFactory = ChannelProvider.provider().createMessageFactory();

    private String prompt;
    private String prefix;

    private boolean terminated = false;
    private Console console;
    private Client client = null;

    /** the default controller host */
    private String defaultControllerHost = "127.0.0.1";

    /** the default controller port */
    private int defaultControllerPort = 3435;

    /** the host of the controller */
    private String controllerHost;

    /** the port of the controller */
    private int controllerPort = -1;

    /** the command line specified username */
    private String username;

    /** the command line specified password */
    private String password;

    /**
     *
     */
    public CommandContextImpl() {
        this.client = new Client();
        try {
            this.console = new ConsoleImpl(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Set the prefix. Adds this prefix on next line after reading current line of data
     *
     * @param prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.prompt = this.prefix + Shell.CLI_POSTFIX;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#isConnected()
     */
    @Override
    public boolean isControllerConnected() {
        return this.client.isConnected();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#printLine(java.lang .String)
     */
    @Override
    public void printLine(String message) {
        console.print(message);
        console.printNewLine();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#printColumns(java .util.Collection)
     */
    @Override
    public void printColumns(Collection<String> col) {
        console.printColumns(col);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#clearScreen()
     */
    @Override
    public void clearScreen() {
        console.clearScreen();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#terminateSession()
     */
    @Override
    public void terminateSession() {
        this.terminated = true;
        this.console.stop();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#isTerminated()
     */
    @Override
    public boolean isTerminated() {
        return this.terminated;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#connectController (java.lang.String, int)
     */
    @Override
    public void connectController(String host, int port) {

        try {
            if (host == null && port == -1) {
                try {
                    this.controllerHost = this.defaultControllerHost;
                    this.controllerPort = this.defaultControllerPort;

                    client.connect(new InetSocketAddress(this.controllerHost, this.controllerPort));
                    this.prompt = this.prefix + "(local)" + Shell.CLI_POSTFIX;
                } catch (IOException e) {
                    this.printLine(e.getMessage());
                }
            } else {
                this.controllerHost = host;
                this.controllerPort = port;

                client.connect(new InetSocketAddress(this.controllerHost, this.controllerPort));
                this.prompt = this.prefix + "(" + host + ":" + port + ")" + Shell.CLI_POSTFIX;
            }

            Message incomingFirstMessage = this.client.run(null);
            String mesage = incomingFirstMessage.toString();

            this.printLine(mesage);
            if (mesage.contains(CONNECTED_AUTHENTICATING_MESSAGE)) {
                username = this.console.readLine("Username:");
                this.client.run(messageFactory.createMessage(username));

                password = this.console.readLine("Password:", '*');
                Message message = this.client.run(messageFactory.createMessage(password));
                mesage = message.toString();
                if (mesage.equals(CONNECTED_AUTHENTICATION_FAILED)) {
                    this.printLine(message.toString());
                    this.disconnectController();
                }
            } else if (mesage.contains(CLOSING_CONNECTION_MESSAGE)) {
                this.disconnectController();
            }
        } catch (Exception e) {
            this.printLine(e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#disconnectController ()
     */
    @Override
    public void disconnectController() {
        this.client.stop();
        this.prompt = this.prefix + Shell.CLI_POSTFIX;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#getDefaultControllerHost ()
     */
    @Override
    public String getDefaultControllerHost() {
        return this.defaultControllerHost;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#getDefaultControllerPort ()
     */
    @Override
    public int getDefaultControllerPort() {
        return this.defaultControllerPort;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#getControllerHost()
     */
    @Override
    public String getControllerHost() {
        return this.controllerHost;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#getControllerPort()
     */
    @Override
    public int getControllerPort() {
        return this.controllerPort;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandContext#getHistory()
     */
    @Override
    public CommandHistory getHistory() {
        return this.console.getHistory();
    }

    @Override
    public void sendMessage(String text) {
        Message outgoing = messageFactory.createMessage(text);
        try {
            Message incoming = this.client.run(outgoing);
            if (incoming != null) {
                this.printLine(incoming.toString());
            } else {
                this.printLine("No response from server");
            }
        } catch (IOException e) {
            this.printLine(e.getMessage());
            this.disconnectController();
        }
    }

    protected String readLine() {
        return this.console.readLine(this.prompt);
    }

}