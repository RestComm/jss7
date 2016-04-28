/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.sccp;

import java.io.IOException;
import java.io.Serializable;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;

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
     * Registers listener for some SSN. This is an equivalent of N-STATE request with User status==UIS (user in service)
     *
     * @param listener
     */
    void registerSccpListener(int ssn, SccpListener listener);

    /**
     * Removes listener for some SSN. This is an equivalent of N-STATE request with User status==UOS (user out of service)
     *
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
     * @param msgNetworkId
     * @return
     */
    int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId);

    /**
     * Request of N-COORD when the originating user is requesting permission to go out-of-service
     *
     * @param ssn
     */
    void coordRequest(int ssn);

    /**
     * The collection of netwokIds that are marked as prohibited or congested.
     *
     * @return The collection of pairs: netwokId value - NetworkIdState (prohibited / congested state)
     */
    FastMap<Integer, NetworkIdState> getNetworkIdStateList();

    /**
     * @return ExecutorCongestionMonitor list that are responsible for measuring of congestion of the thread Executor that
     *         processes incoming messages at mtp3 levels
     */
    ExecutorCongestionMonitor[] getExecutorCongestionMonitorList();

}
