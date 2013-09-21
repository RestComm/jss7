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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.isup.Digits;

/**
 *
 VariablePart {PARAMETERS-BOUND : bound} ::= CHOICE { integer [0] Integer4, number [1] Digits {bound}, -- Generic digits time
 * [2] OCTET STRING (SIZE(2)), -- HH: MM, BCD coded date [3] OCTET STRING (SIZE(4)), -- YYYYMMDD, BCD coded price [4] OCTET
 * STRING (SIZE(4)) } -- Indicates the variable part of the message. Time is BCD encoded. -- The most significant hours digit
 * occupies bits 0-3 of the first octet, and the least -- significant digit occupies bits 4-7 of the first octet. The most
 * significant minutes digit -- occupies bits 0-3 of the second octet, and the least significant digit occupies bits 4-7 -- of
 * the second octet. -- -- Date is BCD encoded. The year digit indicating millenium occupies bits 0-3 of the first octet, -- and
 * the year digit indicating century occupies bits 4-7 of the first octet. The year digit -- indicating decade occupies bits 0-3
 * of the second octet, whilst the digit indicating the year -- within the decade occupies bits 4-7 of the second octet. -- The
 * most significant month digit occupies bits 0-3 of the third octet, and the least -- significant month digit occupies bits 4-7
 * of the third octet. The most significant day digit -- occupies bits 0-3 of the fourth octet, and the least significant day
 * digit occupies bits 4-7 -- of the fourth octet. -- Price is BCD encoded. The digit indicating hundreds of thousands occupies
 * bits 0-3 of the -- first octet, and the digit indicating tens of thousands occupies bits 4-7 of the first octet. -- The digit
 * indicating thousands occupies bits 0-3 of the second octet, whilst the digit -- indicating hundreds occupies bits 4-7 of the
 * second octet. The digit indicating tens occupies -- bits 0-3 of the third octet, and the digit indicating 0 to 9 occupies
 * bits 4-7 of the third -- octet. The tenths digit occupies bits 0-3 of the fourth octet, and the hundredths digit -- occupies
 * bits 4-7 of the fourth octet. -- -- For the encoding of digits in an octet, refer to the timeAndtimezone parameter
 *
 * -- The Definition of range of constants follows numOfInfoItems INTEGER ::= 4
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface VariablePart extends Serializable {

    Integer getInteger();

    Digits getNumber();

    VariablePartTime getTime();

    VariablePartDate getDate();

    VariablePartPrice getPrice();

}