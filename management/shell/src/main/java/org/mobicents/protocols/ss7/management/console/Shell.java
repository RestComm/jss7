package org.mobicents.protocols.ss7.management.console;

public class Shell {

    public static void main(String args[]) {
        Console ss7Cli = new Console(System.in, System.out);

        // TODO : Oleg what about command line arguments?

        ss7Cli.start();
    }

}
