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

package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * Protocl data parameter.
 *
 * @author kulikov
 */
public interface ProtocolData extends Parameter {
    /**
     * Gets origination point code.
     *
     * @return point code value in decimal format.
     */
    int getOpc();

    /**
     * Gets destination point code
     *
     * @return point code value in decimal format
     */
    int getDpc();

    /**
     * Gets the service indicator.
     *
     * @return service indicator value.
     */
    int getSI();

    /**
     * Gets the network indicator.
     *
     * @return the network indicator value.
     */
    int getNI();

    /**
     * Gets the message priority.
     *
     * @return message priority value.
     */
    int getMP();

    /**
     * Gets the signaling link selection.
     *
     * @return the signaling link selection value
     */
    int getSLS();

    /**
     * Gets the payload of message.
     *
     * @return binary message.
     */
    byte[] getData();
}
