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
 * Protocol class (contains class data (0-3) and "return message on error" option for connectionless classes)
 *
 * The "protocol class" parameter field is a one-octet parameter and is structured as follows: Bits 1-4 indicating protocol
 * class are coded as follows: 4321 0000 class 0 0001 class 1 0010 class 2 0011 class 3
 *
 * @author baranowb
 * @author kulikov
 */
public interface ProtocolClass extends Parameter {

    int PARAMETER_CODE = 0x05;

    int HANDLING_RET_ERR = 0x08;

    /**
     * The value of protocol class.
     *
     * @return protocol class code
     */
    int getProtocolClass();

    /**
     * Gets a "return message on error" flag
     *
     * @return
     */
    boolean getReturnMessageOnError();

    /**
     * Clear a flag "ReturnMessageOnError"
     */
    void clearReturnMessageOnError();
}
