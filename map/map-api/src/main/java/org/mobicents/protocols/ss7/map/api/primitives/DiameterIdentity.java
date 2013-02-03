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

/**
*
DiameterIdentity ::= OCTET STRING (SIZE(9..55))
-- content of DiameterIdentity is defined in IETF RFC 3588 [139]

IETF RFC 3588:
The DiameterIdentity format is derived from the OctetString AVP
      Base Format.

         DiameterIdentity  = FQDN

      DiameterIdentity value is used to uniquely identify a Diameter
      node for purposes of duplicate connection and routing loop
      detection.

      The contents of the string MUST be the FQDN of the Diameter node.
      If multiple Diameter nodes run on the same host, each Diameter
      node MUST be assigned a unique DiameterIdentity.  If a Diameter
      node can be identified by several FQDNs, a single FQDN should be
      picked at startup, and used as the only DiameterIdentity for that
      node, whatever the connection it is sent on.

FQDN=fully qualified domain name

* 
* @author sergey vetyutnev
* 
*/
public interface DiameterIdentity {

	public byte[] getData();

	// TODO: add implementing of internal structure (?)

}
