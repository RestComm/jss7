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

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * MNPInfoRes ::= SEQUENCE { routeingNumber [0] RouteingNumber OPTIONAL, imsi [1] IMSI OPTIONAL, msisdn [2] ISDN-AddressString
 * OPTIONAL, numberPortabilityStatus [3] NumberPortabilityStatus OPTIONAL, extensionContainer [4] ExtensionContainer OPTIONAL,
 * ... } -- The IMSI parameter contains a generic IMSI, i.e. it is not tied necessarily to the -- Subscriber. MCC and MNC values
 * in this IMSI shall point to the Subscription Network of -- the Subscriber. See 3GPP TS 23.066 [108].
 *
 * @author amit bhayani
 *
 */
public interface MNPInfoRes extends Serializable {

    RouteingNumber getRouteingNumber();

    IMSI getIMSI();

    ISDNAddressString getMSISDN();

    NumberPortabilityStatus getNumberPortabilityStatus();

    MAPExtensionContainer getExtensionContainer();

}
