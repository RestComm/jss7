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
package org.mobicents.ss7.management.console;

/**
 * <p>
 * This class represents the Command Line Interface for managing the Mobicents SS7 stack.
 * </p>
 *
 *
 * @author amit bhayani
 *
 */
public class Shell {

    Version version = Version.instance;

    public final String WELCOME_MESSAGE = version.toString();

    public static final String CONNECTED_MESSAGE = "Connected to %s currently running on %s";

    public final String prefix;
    public static final String CLI_POSTFIX = ">";

    private void showCliHelp() {
        System.out.println(version.toString());
        System.out.println("Usage: SS7 [OPTIONS]");
        System.out.println("Valid Options");
        System.out.println("-v           Display version number and exit");
        System.out.println("-h           This help screen");
    }

    public Shell() {
        prefix = version.getProperty("prefix");
    }

    public static void main(String[] args) throws Exception {
        Shell shell = new Shell();
        shell.start(args);
    }

    private void start(String[] args) throws Exception {

        // Take care of Cmd Line arguments
        if (args != null && args.length > 0) {

            String cmd = args[0];

            if (cmd.compareTo("-v") == 0) {
                System.out.println(version.toString());
                System.exit(0);
            }

            if (cmd.compareTo("-h") == 0) {
                this.showCliHelp();
                System.exit(0);
            }

        }

        System.out.println(WELCOME_MESSAGE);

        CommandContextImpl commandContext = new CommandContextImpl();
        commandContext.setPrefix(this.prefix);

        // consoleListener = new ConsoleListenerImpl(this.prefix,
        // commandContext);
        // console = new ConsoleImpl(commandContext);
        // consoleListener.setConsole(console);
        // console.start();
        String line;
        // console.pushToConsole(ANSIColors.GREEN_TEXT());
        while ((!commandContext.isTerminated() && ((line = commandContext.readLine()) != null))) {
            line = line.trim();

            if (line.equals("")) {
                continue;
            }
            // if (line.equalsIgnoreCase("password")) {
            // line = console.read("password: ", Character.valueOf((char) 0));
            // console.pushToConsole("password typed:" + line + "\n");
            //
            // }
            if (line.equals("clear") || line.equals("cls")) {
                commandContext.clearScreen();
                continue;
            }

            // this.consoleListener.commandEntered(line);

            CommandHandler commandHandler = null;
            for (CommandHandler commandHandlerTemp : ConsoleImpl.commandHandlerList) {
                if (commandHandlerTemp.handles(line)) {
                    commandHandler = commandHandlerTemp;
                    break;
                }
            }

            if (commandHandler != null) {

                if (!commandHandler.isAvailable(commandContext)) {
                    continue;
                }

                commandHandler.handle(commandContext, line);
            } else {
                commandContext.printLine("Unexpected command \"" + line + "\"");
            }

        }
    }
}
