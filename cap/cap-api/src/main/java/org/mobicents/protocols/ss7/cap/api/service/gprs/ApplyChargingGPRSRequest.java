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

package org.mobicents.protocols.ss7.cap.api.service.gprs;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

/**
 *
 applyChargingGPRS OPERATION ::= { ARGUMENT ApplyChargingGPRSArg RETURN RESULT FALSE ERRORS {missingParameter |
 * unexpectedComponentSequence | unexpectedParameter | unexpectedDataValue | parameterOutOfRange | systemFailure | taskRefused |
 * unknownPDPID} CODE opcode-applyChargingGPRS} -- Direction gsmSCF -> gprsSSF, Timer Tacg -- This operation is used for
 * interacting from the gsmSCF with the gprsSSF CSE-controlled -- GPRS session or PDP Context charging mechanism.
 *
 * ApplyChargingGPRSArg ::= SEQUENCE { chargingCharacteristics [0] ChargingCharacteristics, tariffSwitchInterval [1] INTEGER
 * (1..86400) OPTIONAL, pDPID [2] PDPID OPTIONAL, ... } -- tariffSwitchInterval is measured in 1 second units.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ApplyChargingGPRSRequest extends GprsMessage {

    ChargingCharacteristics getChargingCharacteristics();

    Integer getTariffSwitchInterval();

    PDPID getPDPID();

}