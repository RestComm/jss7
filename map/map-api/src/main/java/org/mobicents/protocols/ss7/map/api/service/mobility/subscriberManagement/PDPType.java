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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

/**
 *
<code>
PDP-Type ::= OCTET STRING (SIZE (2))
-- Octets are coded according to TS 3GPP TS 29.060 [105]
-- Only the values PPP, IPv4 and IPv6 are allowed for this parameter.

Byte 0:
bits 8-5: Spare  1 1 1 1
bits 4-1: PDP Type Organization
Byte 1:
PDP Type Number

PDP Type Organisation Values:
ETSI: 0
IETF: 1

ETSI defined PDP Type Values:
PPP: 1

IETF defined PDP Type Values:
IPv4 Address: PDP Type Number = HEX(21) = 33
IPv6 Address: PDP Type Number = HEX(57) = 87

For PPP the PDP Type Organisation is ETSI and the PDP Type Number is 1 and there shall be no address in the End
User Address IE. In this case the address is negotiated later as part of the PPP protocol.
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PDPType extends Serializable {

    byte[] getData();

    PDPTypeValue getPDPTypeValue();

}
