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

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;

/**
 * LCS-ClientID ::= SEQUENCE { lcsClientType [0] LCSClientType, lcsClientExternalID [1] LCSClientExternalID OPTIONAL,
 * lcsClientDialedByMS [2] AddressString OPTIONAL, lcsClientInternalID [3] LCSClientInternalID OPTIONAL, lcsClientName [4]
 * LCSClientName OPTIONAL, ..., lcsAPN [5] APN OPTIONAL, lcsRequestorID [6] LCSRequestorID OPTIONAL }
 *
 * @author amit bhayani
 *
 */
public interface LCSClientID extends Serializable {
    LCSClientType getLCSClientType();

    LCSClientExternalID getLCSClientExternalID();

    AddressString getLCSClientDialedByMS();

    LCSClientInternalID getLCSClientInternalID();

    LCSClientName getLCSClientName();

    APN getLCSAPN();

    LCSRequestorID getLCSRequestorID();

}