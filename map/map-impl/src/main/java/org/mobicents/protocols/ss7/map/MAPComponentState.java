/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
