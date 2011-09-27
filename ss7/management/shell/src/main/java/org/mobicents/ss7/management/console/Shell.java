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

import java.io.InputStream;
import java.io.PrintStream;

/**
 * <p>
 * This class represents the Command Line Interface for managing the Mobicents
 * SS7 stack.
 * </p>
 * 
 *
 * @author amit bhayani
 * 
 */
public class Shell {

    Version version = Version.instance;

    public final String WELCOME_MESSAGE = version.toString()
            + "\n"
            + "This is free software, with components licensed under the GNU General Public License\n"
            + "version 2 and other licenses. For further details visit http://mobicents.org\n"
            + "=========================================================================";

    public static final String CONNECTED_MESSAGE = "Connected to %s currently running on %s";

    public static final String CLI_PREFIX = "mobicents";
    public static final String CLI_POSTFIX = ">";

    private final ConsoleListener listener;
    private final Console console;

    private static final InputStream in = System.in;
    private static final PrintStream out = System.out;

    private void showCliHelp() {
        System.out.println(version.toString());
        System.out.println("Usage: SS7 [OPTIONS]");
        System.out.println("Valid Options");
        System.out.println("-v           Display version number and exit");
        System.out.println("-h           This help screen");
    }

    public Shell() throws Exception {
        listener = new ConsoleListenerImpl();
        console = new Console(in, out, listener, CLI_PREFIX + CLI_POSTFIX);
    }

    public static void main(String args[]) throws Exception {
        Shell shell = new Shell();
        shell.start(args);
    }

    private void start(String args[]) throws Exception {

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

        console.start();
    }

}
