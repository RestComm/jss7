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

package org.mobicents.protocols.ss7.map.api.primitives;

import java.io.Serializable;

/**
 *
 Time ::= OCTET STRING (SIZE (4)) -- Octets are coded according to IETF RFC 3588 [139]
 *
 * The Time format is derived from the OctetString AVP Base Format. The string MUST contain four octets, in the same format as
 * the first four bytes are in the NTP timestamp format. The NTP Timestamp format is defined in chapter 3 of [SNTP].
 *
 * This represents the number of seconds since 0h on 1 January 1900 with respect to the Coordinated Universal Time (UTC).
 *
 * On 6h 28m 16s UTC, 7 February 2036 the time value will overflow. SNTP [SNTP] describes a procedure to extend the time to
 * 2104. This procedure MUST be supported by all DIAMETER nodes.
 *
 * For "NTP Timestamp Format" please refer rfc2030.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface Time extends Serializable {

    byte[] getData();

    /**
     * Returns the value in UTC
     *
     */
    int getYear();

    /**
     * Returns the value in UTC
     *
     */
    int getMonth();

    /**
     * Returns the value in UTC
     *
     */
    int getDay();

    /**
     * Returns the value in UTC
     *
     */
    int getHour();

    /**
     * Returns the value in UTC
     *
     */
    int getMinute();

    /**
     * Returns the value in UTC
     *
     */
    int getSecond();

}
