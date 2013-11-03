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

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

/**
 *
 entityReleasedGPRS {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT EntityReleasedGPRSArg {bound} RETURN RESULT TRUE
 * ERRORS {missingParameter | taskRefused | unknownPDPID} CODE opcode-entityReleasedGPRS} -- Direction: gprsSSF -> gsmSCF,
 * Timer: Terg -- This operation is used when the GPRS Session is detached or a PDP Context is diconnected and -- the associated
 * event is not armed for reporting. -- The usage of this operation is independent of the functional entity that initiates the
 * Detach -- or PDP Context Disconnection and is independent of the cause of the Detach or PDP Context -- Disconnect.
 *
 * EntityReleasedGPRSArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { gPRSCause [0] GPRSCause {bound}, pDPID [1] PDPID OPTIONAL,
 * ... }
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface EntityReleasedGPRSRequest extends GprsMessage {

    GPRSCause getGPRSCause();

    PDPID getPDPID();

}