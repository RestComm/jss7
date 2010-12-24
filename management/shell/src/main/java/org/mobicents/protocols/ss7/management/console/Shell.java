package org.mobicents.protocols.ss7.management.console;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Shell {

    private static final OutputStream out = System.out;
    private static final InputStream in = System.in;
    private static final Scanner scanner = new Scanner(in);

    private ShellListener shellListener = null;

    public static void main(String args[]) {
        Shell ss7Cli = new Shell();
        
        // TODO : Oleg what about command line arguments?
        
        ss7Cli.beginCli();
    }

    public void beginCli() {

        while (true) {
            String line = scanner.nextLine();
            this.shellListener.commandEntered(line, out);

        }
    }

}
