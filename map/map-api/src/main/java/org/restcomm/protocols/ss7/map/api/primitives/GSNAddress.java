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

package org.restcomm.protocols.ss7.map.api.primitives;

import java.io.Serializable;

/**
 *
<code>
GSN-Address ::= OCTET STRING (SIZE (5..17))
-- Octets are coded according to TS 3GPP TS 23.003 [17]

GSN-Address:
Address Type - 2 bits
Address Length - 6 bits
Address - 4 to 16 octets

The GSN Address is composed of the following elements:
1) The Address Type, which is a fixed length code (of 2 bits) identifying the type of address that is used in the
Address field.
2) The Address Length, which is a fixed length code (of 6 bits) identifying the length of the Address field.
3) The Address, which is a variable length field which contains either an IPv4 address or an IPv6 address.
Address Type 0 and Address Length 4 are used when Address is an IPv4 address.
Address Type 1 and Address Length 16 are used when Address is an IPv6 address.
The IP v4 address structure is defined in RFC 791 [14].
The IP v6 address structure is defined in RFC 2373 [15].
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GSNAddress extends Serializable {

    byte[] getData();

    GSNAddressAddressType getGSNAddressAddressType();

    /**
     * @return the content of Address field (4 bytes for IPv4 and 16 bytes for
     *         IPv6) or null if GSNAddress contains bad data
     */
    byte[] getGSNAddressData();

}
