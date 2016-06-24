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

package org.mobicents.protocols.ss7.map.api.primitives;

import java.io.Serializable;

/**
 * <code>
AddressString ::= OCTET STRING (SIZE (1..maxAddressLength))
-- This type is used to represent a number for addressing
-- purposes. It is composed of
-- a) one octet for nature of address, and numbering plan indicator.
-- b) digits of an address encoded as TBCD-String.
-- a) The first octet includes a one bit extension indicator, a
-- 3 bits nature of address indicator and a 4 bits numbering
-- plan indicator, encoded as follows:
-- bit 8: 1 (no extension)
-- bits 765: nature of address indicator
-- 000 unknown
-- 001 international number
-- 010 national significant number
-- 011 network specific number
-- 100 subscriber number
-- 101 reserved
-- 110 abbreviated number
-- 111 reserved for extension

-- bits 4321: numbering plan indicator
-- 0000 unknown
-- 0001 ISDN/Telephony Numbering Plan (Rec CCITT E.164)
-- 0010 spare
-- 0011 data numbering plan (CCITT Rec X.121)
-- 0100 telex numbering plan (CCITT Rec F.69)
-- 0101 spare
-- 0110 land mobile numbering plan (CCITT Rec E.212)
-- 0111 spare
-- 1000 national numbering plan
-- 1001 private numbering plan
-- 1111 reserved for extension
-- all other values are reserved.
-- b) The following octets representing digits of an address
-- encoded as a TBCD-STRING.

TBCD-STRING ::= OCTET STRING
-- This type (Telephony Binary Coded Decimal String) is used to
-- represent several digits from 0 through 9, *, #, a, b, c, two
-- digits per octet, each digit encoded 0000 to 1001 (0 to 9),
-- 1010 (*), 1011 (#), 1100 (a), 1101 (b) or 1110 (c); 1111 used
-- as filler when there is an odd number of digits.
-- bits 8765 of octet n encoding digit 2n -- bits 4321 of octet n encoding digit 2(n-1) +1
 * </code>
 *
 * @author amit bhayani
 *
 */
public interface AddressString extends Serializable {

    AddressNature getAddressNature();

    NumberingPlan getNumberingPlan();

    String getAddress();

}
