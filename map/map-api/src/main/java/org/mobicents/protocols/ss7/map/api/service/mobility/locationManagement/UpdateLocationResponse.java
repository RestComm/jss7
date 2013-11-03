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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * MAP V3: UpdateLocationRes ::= SEQUENCE { hlr-Number ISDN-AddressString, extensionContainer ExtensionContainer OPTIONAL, ...,
 * add-Capability NULL OPTIONAL, pagingArea-Capability [0]NULL OPTIONAL }
 *
 * MAP V2: UpdateLocationRes ::= CHOICE { hlr-Number ISDN-AddressString, -- hlr-Number must not be used in version greater 1
 * extensibleUpdateLocationRes ExtensibleUpdateLocationRes} -- extensibleUpdateLocationRes must not be used in version 1
 *
 * ExtensibleUpdateLocationRes ::= SEQUENCE { hlr-Number ISDN-AddressString, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface UpdateLocationResponse extends MobilityMessage {

    ISDNAddressString getHlrNumber();

    MAPExtensionContainer getExtensionContainer();

    boolean getAddCapability();

    boolean getPagingAreaCapability();

    long getMapProtocolVersion();

}
