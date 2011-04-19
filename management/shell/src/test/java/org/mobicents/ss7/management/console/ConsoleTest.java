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

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.ss7.management.console.Console;
import org.mobicents.ss7.management.console.ConsoleListener;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ConsoleTest {

    ByteArrayInputStream in = null;
    ByteArrayOutputStream out = null;
    Console console = null;
    ConsoleListener listener = null;

    public ConsoleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConsole() throws IOException {

        String message = "This is some message";
        in = new ByteArrayInputStream(message.getBytes());

        out = new ByteArrayOutputStream();

        listener = new TestConsoleListener(out);

        console = new Console(in, out, listener, null);
        console.start();

        assertEquals("Sent message should be equal to received", message,
                new String(out.toByteArray()));
    }

    private class TestConsoleListener implements ConsoleListener {

        ByteArrayOutputStream out;
        Console console;

        TestConsoleListener(ByteArrayOutputStream out) {
            this.out = out;
        }

        public void commandEntered(String consoleInput) {

            System.out.println(consoleInput);

            this.console.write(consoleInput);

        }

        public void setConsole(Console console) {
            this.console = console;
        }
    }

}
