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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

/*
 * ISDN-SubaddressString ::=
 * OCTET STRING (SIZE (1..maxISDN-SubaddressLength))
 * -- This type is used to represent ISDN subaddresses.
 * -- It is composed of
 * -- a) one octet for type of subaddress and odd/even indicator.
 * -- b) 20 octets for subaddress information.
 * -- a) The first octet includes a one bit extension indicator, a
 * -- 3 bits type of subaddress and a one bit odd/even indicator,
 * -- encoded as follows:
 * -- bit 8: 1 (no extension)
 * -- bits 765: type of subaddress
 * -- 000 NSAP (X.213/ISO 8348 AD2)
 * -- 010 User Specified
 * -- All other values are reserved
 * -- bit 4: odd/even indicator
 * -- 0 even number of address signals
 * -- 1 odd number of address signals
 * -- The odd/even indicator is used when the type of subaddress
 * -- is "user specified" and the coding is BCD.
 * -- bits 321: 000 (unused)
 * -- b) Subaddress information.
 * -- The NSAP X.213/ISO8348AD2 address shall be formatted as specified
 * -- by octet 4 which contains the Authority and Format Identifier
 * -- (AFI). The encoding is made according to the "preferred binary
 * -- encoding" as defined in X.213/ISO834AD2. For the definition
 * -- of this type of subaddress, see ITU-T Rec I.334.
 * -- For User-specific subaddress, this field is encoded according
 * -- to the user specification, subject to a maximum length of 20
 * -- octets. When interworking with X.25 networks BCD coding should
 * -- be applied.
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public interface ISDNSubaddressString {
	public byte[] getData();
}