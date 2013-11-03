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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 PDN-GW-Identity ::= SEQUENCE { pdn-gw-ipv4-Address [0] PDP-Address OPTIONAL, pdn-gw-ipv6-Address [1] PDP-Address OPTIONAL,
 * pdn-gw-name [2] FQDN OPTIONAL, extensionContainer [3] ExtensionContainer OPTIONAL, ... }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PDNGWIdentity extends Serializable {

    PDPAddress getPdnGwIpv4Address();

    PDPAddress getPdnGwIpv6Address();

    FQDN getPdnGwName();

    MAPExtensionContainer getExtensionContainer();

}
