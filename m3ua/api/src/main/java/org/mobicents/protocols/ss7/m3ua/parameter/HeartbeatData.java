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

package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The Heartbeat Data parameter contents are defined by the sending node. The Heartbeat Data could include, for example, a
 * Heartbeat Sequence Number and/or Timestamp. The receiver of a BEAT message does not process this field, as it is only of
 * significance to the sender. The receiver MUST respond with a BEAT Ack message.
 *
 * @author amit bhayani
 *
 */
public interface HeartbeatData extends Parameter {
    byte[] getData();

}
