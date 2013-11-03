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

package org.mobicents.protocols.ss7.sccp;

import java.io.IOException;
import java.io.Serializable;

import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author Oleg Kulikov
 * @author baranowb
 */
public interface SccpProvider extends Serializable {

    /**
     * Gets the access to message factory.
     *
     * @return message factory.
     */
    MessageFactory getMessageFactory();

    /**
     * Gets the access to parameter factory.
     *
     * @return parameter factory
     */
    ParameterFactory getParameterFactory();

    /**
     * Register listener for some adddress.
     *
     * @param listener
     */
    void registerSccpListener(int ssn, SccpListener listener);

    /**
     * Removes listener
     */
    void deregisterSccpListener(int ssn);

    void registerManagementEventListener(SccpManagementEventListener listener);

    void deregisterManagementEventListener(SccpManagementEventListener listener);

    /**
     * Sends message.
     *
     * @param message Message to be sent
     * @throws IOException
     */
    void send(SccpDataMessage message) throws IOException;

    /**
     * Return the maximum length (in bytes) of the sccp message data
     *
     * @param calledPartyAddress
     * @param callingPartyAddress
     * @return
     */
    int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress);

}
