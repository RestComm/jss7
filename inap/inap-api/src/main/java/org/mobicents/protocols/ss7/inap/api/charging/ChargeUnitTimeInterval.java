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

package org.mobicents.protocols.ss7.inap.api.charging;

import java.io.Serializable;

/**
*
<code>
ChargeUnitTimeInterval ::= OCTET STRING (SIZE(2))
-- The ChargeUnitTimeInterval is binary coded and has the value range from 0 to 35997. It begins with 200 milliseconds and
-- then in steps of 50 milliseconds.
-- the LSBit is the least significant bit of the first octet
-- the MSBit is the most significant bit of the last octet
-- the coding of the ChargeUnitTimeInterval is the following:
-- 0 : no periodic metering
-- 1 : 200 msec
-- 2 : 250 msec
-- ..
-- 35997 : 30 minutes
-- All other values are spare.
</code>
*
* @author sergey vetyutnev
*
*/
public interface ChargeUnitTimeInterval extends Serializable {

    int getData();

}
