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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
 *
 time [2] OCTET STRING (SIZE(2)), -- HH: MM, BCD coded
 *
 * -- Indicates the variable part of the message. Time is BCD encoded. -- The most significant hours digit occupies bits 0-3 of
 * the first octet, and the least -- significant digit occupies bits 4-7 of the first octet. The most significant minutes digit
 * -- occupies bits 0-3 of the second octet, and the least significant digit occupies bits 4-7 -- of the second octet.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface VariablePartTime extends Serializable {

    byte[] getData();

    int getHour();

    int getMinute();

}