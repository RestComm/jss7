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

package org.mobicents.protocols.ss7.m3ua.message.asptm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * The ASP Active Ack message is used to acknowledge an ASP Active message received from a remote M3UA peer.
 *
 * @author amit bhayani
 *
 */
public interface ASPActiveAck extends M3UAMessage {

    /**
     * The Traffic Mode Type parameter identifies the traffic mode of operation of the ASP within an AS. Optional
     *
     * @return
     */
    TrafficModeType getTrafficModeType();

    void setTrafficModeType(TrafficModeType mode);

    /**
     * The optional Routing Context parameter contains (a list of) integers indexing the Application Server traffic that the
     * sending ASP is configured/registered to receive.
     *
     * @return
     */
    RoutingContext getRoutingContext();

    void setRoutingContext(RoutingContext rc);

    InfoString getInfoString();

    void setInfoString(InfoString str);
}
