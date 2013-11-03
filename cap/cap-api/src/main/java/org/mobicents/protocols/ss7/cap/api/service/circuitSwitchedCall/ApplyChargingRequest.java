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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.primitives.AChChargingAddress;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;

/**
 * <p>
 * This is the instruction from the gsmSCF to the gsmSSF to start or continue monitoring the call duration
 * </p>
 * <p>
 * See also {@link ApplyChargingReportRequest}
 * </p>
 *
 * applyCharging {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT ApplyChargingArg {bound} RETURN RESULT FALSE ERRORS
 * {missingParameter | unexpectedComponentSequence | unexpectedParameter | unexpectedDataValue | parameterOutOfRange |
 * systemFailure | taskRefused | unknownLegID | unknownCSID} CODE opcode-applyCharging} -- Direction: gsmSCF -> gsmSSF, Timer:
 * Tac -- This operation is used for interacting from the gsmSCF with the gsmSSF charging mechanisms. -- The ApplyChargingReport
 * operation provides the feedback from the gsmSSF to the gsmSCF.
 *
 * ApplyChargingArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { aChBillingChargingCharacteristics [0]
 * AChBillingChargingCharacteristics {bound}, partyToCharge [2] SendingSideID DEFAULT sendingSideID : leg1, extensions [3]
 * Extensions {bound} OPTIONAL, aChChargingAddress [50] AChChargingAddress {bound} DEFAULT legID:sendingSideID:leg1, ... }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ApplyChargingRequest extends CircuitSwitchedCallMessage {

    CAMELAChBillingChargingCharacteristics getAChBillingChargingCharacteristics();

    SendingSideID getPartyToCharge();

    CAPExtensions getExtensions();

    AChChargingAddress getAChChargingAddress();

}