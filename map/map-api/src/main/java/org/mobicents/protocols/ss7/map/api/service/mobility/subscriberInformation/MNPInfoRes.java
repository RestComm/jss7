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
