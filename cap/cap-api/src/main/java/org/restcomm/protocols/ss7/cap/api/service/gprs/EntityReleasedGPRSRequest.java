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

package org.restcomm.protocols.ss7.cap.api.service.gprs;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

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