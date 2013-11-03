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

package org.mobicents.protocols.ss7.map;

/**
 * @author amit bhayani
 *
 */
public enum MAPComponentState {
    // No activity associated with the ID.
    Idle,

    // An operation has been transmitted to the remote end, but no result has been received. The timer associated with
    // the operation invocation (with the value of "Timeout") is started when the transition from "Idle" to "Operation Sent"
    // occurs
    OperationPending,

    // The result has been received; TCAP is waiting for its possible rejection by the TC-user.
    WaitforReject,

    // Reject of the result has been requested by the TC-user, but no request for transmission has been issued.
    Rejectpending;
}
