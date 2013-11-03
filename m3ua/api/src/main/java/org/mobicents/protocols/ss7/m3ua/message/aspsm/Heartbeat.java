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

package org.mobicents.protocols.ss7.m3ua.message.aspsm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.HeartbeatData;

/**
 * The BEAT message is optionally used to ensure that the M3UA peers are still available to each other. It is recommended for
 * use when the M3UA runs over a transport layer other than the SCTP, which has its own heartbeat.
 *
 * @author amit bhayani
 *
 */
public interface Heartbeat extends M3UAMessage {
    HeartbeatData getHeartbeatData();

    void setHeartbeatData(HeartbeatData hrBtData);
}
