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

import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;

/**
 *
 assistRequestInstructions {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT AssistRequestInstructionsArg {bound} RETURN
 * RESULT FALSE ERRORS {missingCustomerRecord | missingParameter | systemFailure | taskRefused | unexpectedComponentSequence |
 * unexpectedDataValue | unexpectedParameter} CODE opcode-assistRequestInstructions} -- Direction: gsmSSF -> gsmSCF or gsmSRF ->
 * gsmSCF, Timer: Tari -- This operation is used when there is an assist procedure and may be -- sent by the gsmSSF or gsmSRF to
 * the gsmSCF. This operation is sent by the -- assisting gsmSSF to gsmSCF, when the initiating gsmSSF has set up a connection
 * to -- the gsmSRF or to the assisting gsmSSF as a result of receiving an -- EstablishTemporaryConnection from -- the gsmSCF.
 * -- Refer to clause 11 for a description of the procedures associated with this operation.
 *
 * AssistRequestInstructionsArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { correlationID [0] CorrelationID {bound},
 * iPSSPCapabilities [2] IPSSPCapabilities {bound}, extensions [3] Extensions {bound} OPTIONAL, ... } -- OPTIONAL denotes
 * network operator specific use. The value of the correlationID may be the -- Called Party Number supplied by the initiating
 * gsmSSF.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AssistRequestInstructionsRequest extends CircuitSwitchedCallMessage {

    /**
     * Use Digits.getGenericNumber() for CorrelationID
     *
     * @return
     */
    Digits getCorrelationID();

    IPSSPCapabilities getIPSSPCapabilities();

    CAPExtensions getExtensions();
}