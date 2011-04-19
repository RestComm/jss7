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

/**
 * Listener interface for receiving {@link String} data from {@link Console}.
 * The class that is interested in processing command implements this interface,
 * and the {@link Console} object is registered with instance of this class,
 * using the setConsole method.
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
    public void commandEntered(String consoleInput);

    /**
     * Set the {@link Console} for this listnere
     * 
     * @param console
     */
    public void setConsole(Console console);

}
