package org.mobicents.protocols.ss7.management.console;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.mobicents.ss7.management.transceiver.Message;
import org.mobicents.ss7.management.transceiver.MessageFactory;

public class ConsoleListenerImpl implements ConsoleListener {

    // TODO : We still have to work on formatter and how text on CLI should be
    // displayed

    private String address = "127.0.0.1";
    private int port = 3435;

    protected Console console;
    private Client client = null;

    private MessageFactory messageFactory = new MessageFactory();

    public ConsoleListenerImpl() {
        client = new Client();
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    public void commandEntered(String consoleInput) {
        System.out.println(consoleInput);
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
                    this.console.setPrefix(Shell.CLI_PREFIX + "(" + address
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
            //Send everything to Server as per Oleg
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
