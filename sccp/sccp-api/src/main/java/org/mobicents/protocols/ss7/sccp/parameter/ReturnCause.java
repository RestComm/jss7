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

package org.mobicents.protocols.ss7.sccp.parameter;

/**
 * Return cause values parameter for connectionless message
 *
 * @author baranowb
 * @author sergey vetyutnev
 */
public interface ReturnCause extends Parameter {

    int PARAMETER_CODE = 0xB;

    // public static final int NO_TRANSLATION_FOR_NATURE = 0x0;
    // public static final int NO_TRANSLATION_FOR_ADDRESS = 0x1;
    // public static final int SUBSYSTEM_CONGESTION = 0x2;
    // public static final int SUBSYSTEM_FAILURE = 0x3;
    // public static final int UNEQUIPED_USER = 0x4;
    // public static final int MTP_FAILURE = 0x5;
    // public static final int NETWORK_CONGESTION = 0x6;
    // public static final int UNQALIFIED = 0x7;
    // public static final int ERR_IN_MSG_TRANSPORT = 0x8;
    // public static final int ERR_IN_LOCAL_PROCESSING = 0x9;
    // public static final int CANNOT_REASEMBLE = 0xA;
    // public static final int SCCP_FAILURE = 0xB;
    // public static final int HOP_COUNTER_VIOLATION = 0xC;
    // public static final int SEG_NOT_SUPPORTED = 0xD;
    // public static final int SEG_FAILURE = 0xE;

    /**
     * Gets the value of this parameter.
     *
     * @return the value of this parameter.
     */
    ReturnCauseValue getValue();
}
