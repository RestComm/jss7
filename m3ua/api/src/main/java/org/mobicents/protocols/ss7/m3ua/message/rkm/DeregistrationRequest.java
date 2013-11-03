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

package org.mobicents.protocols.ss7.m3ua.message.rkm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * The DEREG REQ message is sent by an ASP to indicate to a remote M3UA peer that it wishes to deregister a given Routing Key.
 * Typically, an ASP would send this message to an SGP and expects to receive a DEREG RSP message in return with the associated
 * Routing Context value.
 *
 * @author amit bhayani
 *
 */
public interface DeregistrationRequest extends M3UAMessage {

    /**
     * The Routing Context parameter contains (a list of) integers indexing the Application Server traffic that the sending ASP
     * is currently registered to receive from the SGP but now wishes to deregister.
     *
     * @return
     */
    RoutingContext getRoutingContext();

    void setRoutingContext(RoutingContext rc);
}
