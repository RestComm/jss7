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
 DiameterIdentity ::= OCTET STRING (SIZE(9..55)) -- content of DiameterIdentity is defined in IETF RFC 3588 [139]
 *
 * IETF RFC 3588: The DiameterIdentity format is derived from the OctetString AVP Base Format.
 *
 * DiameterIdentity = FQDN
 *
 * DiameterIdentity value is used to uniquely identify a Diameter node for purposes of duplicate connection and routing loop
 * detection.
 *
 * The contents of the string MUST be the FQDN of the Diameter node. If multiple Diameter nodes run on the same host, each
 * Diameter node MUST be assigned a unique DiameterIdentity. If a Diameter node can be identified by several FQDNs, a single
 * FQDN should be picked at startup, and used as the only DiameterIdentity for that node, whatever the connection it is sent on.
 *
 * FQDN=fully qualified domain name
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface DiameterIdentity extends Serializable {

    byte[] getData();

    // TODO: add implementing of internal structure (?)

}
