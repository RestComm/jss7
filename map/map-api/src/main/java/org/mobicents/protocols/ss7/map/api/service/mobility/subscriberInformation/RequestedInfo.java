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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * RequestedInfo ::= SEQUENCE { locationInformation [0] NULL OPTIONAL, subscriberState [1] NULL OPTIONAL, extensionContainer [2]
 * ExtensionContainer OPTIONAL, ..., currentLocation [3] NULL OPTIONAL, requestedDomain [4] DomainType OPTIONAL, imei [6] NULL
 * OPTIONAL, ms-classmark [5] NULL OPTIONAL, mnpRequestedInfo [7] NULL OPTIONAL } --currentLocation shall be absent if
 * locationInformation is absent
 *
 *
 * @author abhayani
 *
 */
public interface RequestedInfo extends Serializable {
    boolean getLocationInformation();

    boolean getSubscriberState();

    MAPExtensionContainer getExtensionContainer();

    boolean getCurrentLocation();

    DomainType getRequestedDomain();

    boolean getImei();

    boolean getMsClassmark();

    boolean getMnpRequestedInfo();

}
