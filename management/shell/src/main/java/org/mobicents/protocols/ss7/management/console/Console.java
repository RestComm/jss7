package org.mobicents.protocols.ss7.management.console;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 
 * @author amit bhayani
 *
 */
public class Console {

    private final OutputStream out;
    private final PrintWriter pw;
    private final InputStream in;
    private final Scanner scanner;

    private ConsoleListener consoleListener = null;

    public Console(InputStream in, OutputStream out,
            ConsoleListener consoleListener) {

        // Input
        this.in = in;
        this.scanner = new Scanner(in);

        // Output
        this.out = out;
        this.pw = new PrintWriter(this.out);

        // Listener
        this.consoleListener = consoleListener;

    }

    public void start() {

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (this.consoleListener != null) {
                this.consoleListener.commandEntered(line);
            }

        }
    }

    public void write(String text) {
        pw.write(text);
        pw.flush();
    }

}
