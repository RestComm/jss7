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

import org.mobicents.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;

/**
 *
 fCIBCCCAMELsequence1 [0] SEQUENCE { freeFormatData [0] OCTET STRING (SIZE( bound.&minFCIBillingChargingDataLength ..
 * bound.&maxFCIBillingChargingDataLength)), partyToCharge [1] SendingSideID DEFAULT sendingSideID: leg1, appendFreeFormatData
 * [2] AppendFreeFormatData DEFAULT overwrite, ... }
 *
 * minFCIBillingChargingDataLength ::= 1 maxFCIBillingChargingDataLength ::= 160
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface FCIBCCCAMELsequence1 extends Serializable {

    FreeFormatData getFreeFormatData();

    SendingSideID getPartyToCharge();

    AppendFreeFormatData getAppendFreeFormatData();

}