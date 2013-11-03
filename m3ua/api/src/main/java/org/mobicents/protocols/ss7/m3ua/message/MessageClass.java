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

package org.mobicents.protocols.ss7.m3ua.message;

/**
 * Defines the list of valid message classes.
 *
 * @author kulikov
 */
public interface MessageClass {
    int MANAGEMENT = 0;
    int TRANSFER_MESSAGES = 1;
    int SIGNALING_NETWORK_MANAGEMENT = 2;
    int ASP_STATE_MAINTENANCE = 3;
    int ASP_TRAFFIC_MAINTENANCE = 4;
    int ROUTING_KEY_MANAGEMENT = 9;

}
