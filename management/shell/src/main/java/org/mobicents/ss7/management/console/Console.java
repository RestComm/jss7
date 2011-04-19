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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * <p>
 * Represents the character-based console device associated with current Java
 * virtual machine.
 * </p>
 * 
 * <p>
 * The console instance is created by passing {@link InputStream} and
 * {@link OutputStream} from which this console reads and writes data to. The
 * console blocks till next line of data is available. Once the line is read
 * from {@link InputStream}, it calls {@link ConsoleListener}
 * </p>
 * 
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

    /**
     * Starts this console. Will block till next line of data is available
     */
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

    /**
     * Set the prefix. Adds this prefix on next line after reading current line
     * of data
     * 
     * @param prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void addPrefix() {
        if (this.prefix != null) {
            this.write("\n");
            this.write(prefix);
        }
    }

    /**
     * Stop the console
     */
    public void stop() {
        this.stopped = true;
        this.scanner.close();
    }

    /**
     * Write the passed text to {@link OutputStream} referenced by this console
     * 
     * @param text
     */
    public void write(String text) {
        pw.write(text);
        pw.flush();
    }

}
