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

import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;

/**
 *
 sendChargingInformation {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT SendChargingInformationArg {bound} RETURN RESULT
 * FALSE ERRORS {missingParameter | unexpectedComponentSequence | unexpectedParameter | parameterOutOfRange | systemFailure |
 * taskRefused | unexpectedDataValue | unknownLegID} CODE opcode-sendChargingInformation} -- Direction: gsmSCF -> gsmSSF, Timer:
 * Tsci -- This operation is used to instruct the gsmSSF on the charging information to send by the gsmSSF. -- The charging
 * information can either be sent back by means of signalling or internal -- if the gsmSSF is located in the local exchange. In
 * the local exchange -- this information may be used to update the charge meter or to create a standard call record.
 *
 * SendChargingInformationArg {PARAMETERS-BOUND : bound}::= SEQUENCE { sCIBillingChargingCharacteristics [0]
 * SCIBillingChargingCharacteristics {bound}, partyToCharge [1] SendingSideID, extensions [2] Extensions {bound} OPTIONAL, ... }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendChargingInformationRequest extends CircuitSwitchedCallMessage {

    SCIBillingChargingCharacteristics getSCIBillingChargingCharacteristics();

    SendingSideID getPartyToCharge();

    CAPExtensions getExtensions();

}