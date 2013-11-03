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
 MidCallControlInfo ::= SEQUENCE { minimumNumberOfDigits [0] INTEGER (1..30) DEFAULT 1, maximumNumberOfDigits [1] INTEGER
 * (1..30) DEFAULT 30, endOfReplyDigit [2] OCTET STRING (SIZE (1..2)) OPTIONAL, cancelDigit [3] OCTET STRING (SIZE (1..2))
 * OPTIONAL, startDigit [4] OCTET STRING (SIZE (1..2)) OPTIONAL, interDigitTimeout [6] INTEGER (1..127) DEFAULT 10, ... } -- --
 * - minimumNumberOfDigits specifies the minumum number of digits that shall be collected -- - maximumNumberOfDigits specifies
 * the maximum number of digits that shall be collected -- - endOfReplyDigit specifies the digit string that denotes the end of
 * the digits -- to be collected. -- - cancelDigit specifies the digit string that indicates that the input shall -- be erased
 * and digit collection shall start afresh. -- - startDigit specifies the digit string that denotes the start of the digits --
 * to be collected. -- - interDigitTimeout specifies the maximum duration in seconds between successive -- digits. -- --
 * endOfReplyDigit, cancelDigit and startDigit shall contain digits in the range 0..9, '*' and '#' -- only. The collected digits
 * string, reported to the gsmSCF, shall include the endOfReplyDigit and -- the startDigit, if present. -- -- endOfReplyDigit,
 * cancelDigit and startDigit shall be encoded as BCD digits. Each octet shall -- contain one BCD digit, in the 4 least
 * significant bits of each octet. -- The following encoding shall be used for the over-decadic digits: 1011 (*), 1100 (#).
 *
 * @author sergey vetyutnev
 *
 */
public interface MidCallControlInfo extends Serializable {

    int getMinimumNumberOfDigits();

    int getMaximumNumberOfDigits();

    String getEndOfReplyDigit();

    String getCancelDigit();

    String getStartDigit();

    int getInterDigitTimeout();

}