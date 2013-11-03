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
 * Defines valid message types.
 *
 * @author kulikov
 */
public interface MessageType {
    // Management (MGMT) Messages
    int ERROR = 0;
    int NOTIFY = 1;

    String S_ERROR = "ERROR";
    String S_NOTIFY = "NOTIFY";

    // Transfer Messages
    int PAYLOAD = 1;
    String S_PAYLOAD = "DATA";

    // SS7 Signalling Network Management (SSNM) Messages
    int DESTINATION_UNAVAILABLE = 1;
    int DESTINATION_AVAILABLE = 2;
    int DESTINATION_STATE_AUDIT = 3;
    int SIGNALING_CONGESTION = 4;
    int DESTINATION_USER_PART_UNAVAILABLE = 5;
    int DESTINATION_RESTRICTED = 6;

    String S_DESTINATION_UNAVAILABLE = "DUNA";
    String S_DESTINATION_AVAILABLE = "DAVA";
    String S_DESTINATION_STATE_AUDIT = "DAUD";
    String S_SIGNALING_CONGESTION = "SCON";
    String S_DESTINATION_USER_PART_UNAVAILABLE = "DUPU";
    String S_DESTINATION_RESTRICTED = "DRST";

    // ASP State Maintenance (ASPSM) Messages
    int ASP_UP = 1;
    int ASP_DOWN = 2;
    int HEARTBEAT = 3;
    int ASP_UP_ACK = 4;
    int ASP_DOWN_ACK = 5;
    int HEARTBEAT_ACK = 6;

    String S_ASP_UP = "ASPUP";
    String S_ASP_DOWN = "ASPDOWN";
    String S_HEARTBEAT = "HEARTBEAT";
    String S_ASP_UP_ACK = "ASPUPACK";
    String S_ASP_DOWN_ACK = "ASPDOWNACK";
    String S_HEARTBEAT_ACK = "HEARTBEATACK";

    // ASP Traffic Maintenance (ASPTM) Messages
    int ASP_ACTIVE = 1;
    int ASP_INACTIVE = 2;
    int ASP_ACTIVE_ACK = 3;
    int ASP_INACTIVE_ACK = 4;

    String S_ASP_ACTIVE = "ASPACTIVE";
    String S_ASP_INACTIVE = "ASPINACTIVE";
    String S_ASP_ACTIVE_ACK = "ASPACTIVEACK";
    String S_ASP_INACTIVE_ACK = "ASPINACTIVEACK";

    // Routing Key Management (RKM) Messages
    int REG_REQUEST = 1;
    int REG_RESPONSE = 2;
    int DEREG_REQUEST = 3;
    int DEREG_RESPONSE = 4;

    String S_REG_REQUEST = "REGREQ";
    String S_REG_RESPONSE = "REGRSP";
    String S_DEREG_REQUEST = "DEREGREQ";
    String S_DEREG_RESPONSE = "DEREGRESP";
}
