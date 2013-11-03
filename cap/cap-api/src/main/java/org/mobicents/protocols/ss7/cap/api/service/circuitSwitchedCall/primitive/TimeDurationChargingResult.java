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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.primitives.AChChargingAddress;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;

/**
 *
 timeDurationChargingResult [0] SEQUENCE { partyToCharge [0] ReceivingSideID, timeInformation [1] TimeInformation, legActive
 * [2] BOOLEAN DEFAULT TRUE, callLegReleasedAtTcpExpiry [3] NULL OPTIONAL, extensions [4] Extensions {bound} OPTIONAL,
 * aChChargingAddress [5] AChChargingAddress {bound} DEFAULT legID:receivingSideID:leg1,
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TimeDurationChargingResult extends Serializable {

    ReceivingSideID getPartyToCharge();

    TimeInformation getTimeInformation();

    boolean getLegActive();

    boolean getCallLegReleasedAtTcpExpiry();

    CAPExtensions getExtensions();

    AChChargingAddress getAChChargingAddress();

}