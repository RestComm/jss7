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

package org.mobicents.protocols.ss7.map.api.service.lsm;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * SubscriberLocationReport-Res ::= SEQUENCE { extensionContainer ExtensionContainer OPTIONAL, ..., na-ESRK [0]
 * ISDN-AddressString OPTIONAL, na-ESRD [1] ISDN-AddressString OPTIONAL } -- na-ESRK and na-ESRD are mutually exclusive -- --
 * exception handling -- receipt of both na-ESRK and na-ESRD shall be treated the same as a return error
 *
 *
 * @author amit bhayani
 *
 */
public interface SubscriberLocationReportResponse extends LsmMessage {

    MAPExtensionContainer getExtensionContainer();

    ISDNAddressString getNaESRK();

    ISDNAddressString getNaESRD();
}
