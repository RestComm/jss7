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

package org.mobicents.protocols.ss7.sccp.parameter;

/**
 * Release cause values for connection-oriented message
 */
public enum ReleaseCauseValue {

    // ITU and ANSI
    // in ITU version 0x1 is END_USER_CONGESTION, in ANSI it's END_USER_BUSY
    END_USER_ORIGINATED(0x0), ITU_END_USER_CONGESTION_ANSI_END_USER_BUSY(0x1), END_USER_FAILURE(0x2), SCCP_USER_ORIGINATED(0x3),
            REMOTE_PROCEDURE_ERROR(0x4), INCONSISTENT_CONNECTION_DATA(0x5), ACCESS_FAULURE(0x6), ACCESS_CONGESTION(0x7),
            SUBSYSTEM_FAILURE(0x8), SUBSYSTEM_CONGESTION(0x9), MTP_FAILURE(0xA), NETWORK_CONGESTION(0xB),
            EXPIRATION_OF_RESET_TIMER(0xC), EXPIRATION_OF_RECEIVE_INACTIVITY_TIMER(0xD), UNQUALIFIED(0xF), SCCP_FAILURE(0x10);

    private int code;

    private ReleaseCauseValue(int code) {
        this.code = code;
    }

    public int getValue() {
        return this.code;
    }

    public static ReleaseCauseValue getInstance(int code) {
        switch (code) {

        case 0x0:
            return END_USER_ORIGINATED;
        case 0x1:
            return ITU_END_USER_CONGESTION_ANSI_END_USER_BUSY;
        case 0x2:
            return END_USER_FAILURE;
        case 0x3:
            return SCCP_USER_ORIGINATED;
        case 0x4:
            return REMOTE_PROCEDURE_ERROR;
        case 0x5:
            return INCONSISTENT_CONNECTION_DATA;
        case 0x6:
            return ACCESS_FAULURE;
        case 0x7:
            return ACCESS_CONGESTION;
        case 0x8:
            return SUBSYSTEM_FAILURE;
        case 0x9:
            return SUBSYSTEM_CONGESTION;
        case 0xA:
            return MTP_FAILURE;
        case 0xB:
            return NETWORK_CONGESTION;
        case 0xC:
            return EXPIRATION_OF_RESET_TIMER;
        case 0xD:
            return EXPIRATION_OF_RECEIVE_INACTIVITY_TIMER;
        case 0xF:
            return UNQUALIFIED;
        case 0x10:
            return SCCP_FAILURE;

        default:
            return UNQUALIFIED;
        }
    }
}
