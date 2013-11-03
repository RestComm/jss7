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
 price [4] OCTET STRING (SIZE(4))
 *
 * -- Price is BCD encoded. The digit indicating hundreds of thousands occupies bits 0-3 of the -- first octet, and the digit
 * indicating tens of thousands occupies bits 4-7 of the first octet. -- The digit indicating thousands occupies bits 0-3 of the
 * second octet, whilst the digit -- indicating hundreds occupies bits 4-7 of the second octet. The digit indicating tens
 * occupies -- bits 0-3 of the third octet, and the digit indicating 0 to 9 occupies bits 4-7 of the third -- octet. The tenths
 * digit occupies bits 0-3 of the fourth octet, and the hundredths digit -- occupies bits 4-7 of the fourth octet. -- -- For the
 * encoding of digits in an octet, refer to the timeAndtimezone parameter
 *
 * -- The Definition of range of constants follows numOfInfoItems INTEGER ::= 4
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface VariablePartPrice extends Serializable {

    byte[] getData();

    double getPrice();

    int getPriceIntegerPart();

    int getPriceHundredthPart();

}