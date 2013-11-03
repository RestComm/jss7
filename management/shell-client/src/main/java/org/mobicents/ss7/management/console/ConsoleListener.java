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
 * Listener interface for receiving {@link String} data from {@link Console}. The class that is interested in processing command
 * implements this interface, and the {@link Console} object is registered with instance of this class, using the setConsole
 * method.
 *
 * @author amit bhayani
 *
 */
public interface ConsoleListener {

    /**
     * Invoked when {@link Console} reads next line of data
     *
     * @param consoleInput
     */
    void commandEntered(String consoleInput);

    /**
     * Set the {@link Console} for this listnere
     *
     * @param console
     */
    void setConsole(Console console);

}
