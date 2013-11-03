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
 AccessNetworkSignalInfo ::= SEQUENCE { accessNetworkProtocolId AccessNetworkProtocolId, signalInfo LongSignalInfo, --
 * Information about the internal structure is given in clause 7.6.9.1
 *
 * extensionContainer ExtensionContainer OPTIONAL, ...}
 *
 * LongSignalInfo ::= OCTET STRING (SIZE (1..maxLongSignalInfoLength))
 *
 * maxLongSignalInfoLength INTEGER ::= 2560 -- This Named Value represents the maximum number of octets which is available -- to
 * carry a single instance of the LongSignalInfo data type using -- White Book SCCP with the maximum number of segments. -- It
 * takes account of the octets used by the lower layers of the protocol, and -- other information elements which may be included
 * in the same component.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AccessNetworkSignalInfo extends Serializable {

    AccessNetworkProtocolId getAccessNetworkProtocolId();

    byte[] getSignalInfo();

    MAPExtensionContainer getExtensionContainer();

}
