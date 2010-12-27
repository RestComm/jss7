package org.mobicents.ss7.management.console;

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

    private String prefix;

    private boolean stopped = false;

    private ConsoleListener consoleListener = null;

    public Console(InputStream in, OutputStream out,
            ConsoleListener consoleListener, String prefix) {
        // prefix
        this.prefix = prefix;

        // Input
        this.in = in;
        this.scanner = new Scanner(this.in);

        // Output
        this.out = out;
        this.pw = new PrintWriter(this.out);

        // Listener
        this.consoleListener = consoleListener;
        this.consoleListener.setConsole(this);

    }

    public void start() {
        addPrefix();
        while (!stopped && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (this.consoleListener != null) {
                this.consoleListener.commandEntered(line);
            }

            if (!this.stopped) {
                addPrefix();
            }
        }
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void addPrefix() {
        if (this.prefix != null) {
            this.write("\n");
            this.write(prefix);
        }
    }

    public void stop() {
        this.stopped = true;
        this.scanner.close();
    }

    public void write(String text) {
        pw.write(text);
        pw.flush();
    }

}
