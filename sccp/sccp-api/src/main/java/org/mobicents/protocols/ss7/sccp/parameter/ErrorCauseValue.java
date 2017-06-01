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
 * Error cause values for connection-oriented message
 */
public enum ErrorCauseValue {
    // ITU and ANSI
    LRN_MISMATCH_UNASSIGNED_DESTINATION_LRN(0x0), LRN_MISMATCH_INCONSISTENT_SOURCE_LRN(0x1), POINT_CODE_MISMATCH(0x2),
            SERVICE_CLASS_MISMATCH(0x3), UNQUALIFIED(0x4);

    private int code;

    private ErrorCauseValue(int code) {
        this.code = code;
    }

    public int getValue() {
        return this.code;
    }

    public static ErrorCauseValue getInstance(int code) {
        switch (code) {

        case 0x0:
            return LRN_MISMATCH_UNASSIGNED_DESTINATION_LRN;
        case 0x1:
            return LRN_MISMATCH_INCONSISTENT_SOURCE_LRN;
        case 0x2:
            return POINT_CODE_MISMATCH;
        case 0x3:
            return SERVICE_CLASS_MISMATCH;
        case 0x4:
            return UNQUALIFIED;

        default:
            return UNQUALIFIED;
        }
    }
}
