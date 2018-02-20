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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

import org.restcomm.protocols.ss7.map.api.MAPException;

/**
 *
<code>
APN ::= OCTET STRING (SIZE (2..63))
-- Octets are coded according to TS 3GPP TS 23.003 [17]

The encoding of the APN shall follow the Name Syntax defined in RFC 2181 [18], RFC 1035 [19] and RFC 1123 [20].
The APN consists of one or more labels. Each label is coded as a one octet length field followed by that number of
octets coded as 8 bit ASCII characters. Following RFC 1035 [19] the labels shall consist only of the alphabetic
characters (A-Z and a-z), digits (0-9) and the hyphen (-). Following RFC 1123 [20], the label shall begin and end with
either an alphabetic character or a digit. The case of alphabetic characters is not significant. The APN is not terminated
by a length byte of zero.
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface APN extends Serializable {

    byte[] getData();

    String getApn() throws MAPException;

}
