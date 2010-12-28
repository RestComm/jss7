package org.mobicents.ss7.management.console;

import java.io.InputStream;
import java.io.PrintStream;

/**
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
