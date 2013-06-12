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
public interface MidCallControlInfo {

    int getMinimumNumberOfDigits();

    int getMaximumNumberOfDigits();

    String getEndOfReplyDigit();

    String getCancelDigit();

    String getStartDigit();

    int getInterDigitTimeout();

}
