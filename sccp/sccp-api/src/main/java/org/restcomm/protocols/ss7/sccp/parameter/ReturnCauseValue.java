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

package org.restcomm.protocols.ss7.sccp.parameter;

/**
 * Return cause values for connectionless message
 *
 * @author baranowb
 * @author sergey vetyutnev
 */
public enum ReturnCauseValue {

    // ITU
    NO_TRANSLATION_FOR_NATURE(0x0), NO_TRANSLATION_FOR_ADDRESS(0x1), SUBSYSTEM_CONGESTION(0x2), SUBSYSTEM_FAILURE(0x3), UNEQUIPED_USER(0x4), MTP_FAILURE(0x5), NETWORK_CONGESTION(
            0x6), UNQALIFIED(0x7), ERR_IN_MSG_TRANSPORT(0x8), ERR_IN_LOCAL_PROCESSING(0x9), CANNOT_REASEMBLE(0xA), SCCP_FAILURE(0xB), HOP_COUNTER_VIOLATION(0xC), SEG_NOT_SUPPORTED(
            0xD), SEG_FAILURE(0xE),

    // ANSI
    MESSAGE_CHANGE_FAILURE(0xF7), INVALID_INS_ROUTING_REQUEST(0xF8), INVALID_ISNI_ROUTING_REQUEST(0xF9), UNAUTHORIZED_MESSAGE(0xFA), MESSAGE_INCOMPATIBILITY(0xFB), CANNOT_PERFORM_ISNI_CONSTRAINED_ROUTING(
            0xFC), REDUNDANT_ISNI_CONSTRAINED_ROUTING_INFORMATION(0xFD), UNABLE_TO_PERFORM_ISNI_IDENTIFICATION(0xFE);

    private int code;

    private ReturnCauseValue(int code) {
        this.code = code;
    }

    public int getValue() {
        return this.code;
    }

    public static ReturnCauseValue getInstance(int code) {
        switch (code) {

        // ITU
        case 0x0:
            return NO_TRANSLATION_FOR_NATURE;
        case 0x1:
            return NO_TRANSLATION_FOR_ADDRESS;
        case 0x2:
            return SUBSYSTEM_CONGESTION;
        case 0x3:
            return SUBSYSTEM_FAILURE;
        case 0x4:
            return UNEQUIPED_USER;
        case 0x5:
            return MTP_FAILURE;
        case 0x6:
            return NETWORK_CONGESTION;
        case 0x7:
            return UNQALIFIED;
        case 0x8:
            return ERR_IN_MSG_TRANSPORT;
        case 0x9:
            return ERR_IN_LOCAL_PROCESSING;
        case 0xA:
            return CANNOT_REASEMBLE;
        case 0xB:
            return SCCP_FAILURE;
        case 0xC:
            return HOP_COUNTER_VIOLATION;
        case 0xD:
            return SEG_NOT_SUPPORTED;
        case 0xE:
            return SEG_FAILURE;

            // ANSI
        case 0xF7:
            return MESSAGE_CHANGE_FAILURE;
        case 0xF8:
            return INVALID_INS_ROUTING_REQUEST;
        case 0xF9:
            return INVALID_ISNI_ROUTING_REQUEST;
        case 0xFA:
            return UNAUTHORIZED_MESSAGE;
        case 0xFB:
            return MESSAGE_INCOMPATIBILITY;
        case 0xFC:
            return CANNOT_PERFORM_ISNI_CONSTRAINED_ROUTING;
        case 0xFD:
            return REDUNDANT_ISNI_CONSTRAINED_ROUTING_INFORMATION;
        case 0xFE:
            return UNABLE_TO_PERFORM_ISNI_IDENTIFICATION;

        default:
            return UNQALIFIED;

        }
    }
}
