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

/**
 *
 */

package org.mobicents.protocols.ss7.tcapAnsi.api.tc.component;

/**
 * State of operation, see Q.771 3.1.5
 *
 * @author baranowb
 *
 */
public enum OperationState {

    // Initial and End state
    Idle,
    // Once passed to sendComponent
    Pending,
    // On Begin,Continue or End is sent and Result-NL received
    Sent,
    // On Result-L or TC-U-ERROR
    Reject_W,
    // On TC-L-REJECT
    Reject_P;

}
