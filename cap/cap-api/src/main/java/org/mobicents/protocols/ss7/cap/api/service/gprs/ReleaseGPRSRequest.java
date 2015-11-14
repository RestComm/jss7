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

package org.mobicents.protocols.ss7.cap.api.service.gprs;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

/**
 *
 releaseGPRS {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT ReleaseGPRSArg {bound} RETURN RESULT FALSE ERRORS
 * {missingParameter | taskRefused | unknownPDPID} CODE opcode-releaseGPRS} -- Direction: gsmSCF -> gprsSSF, Timer: Trg -- This
 * operation is used to tear down an existing GPRS session or PDP Context at any phase.
 *
 * ReleaseGPRSArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { gprsCause [0] GPRSCause {bound}, pDPID [1] PDPID OPTIONAL, ... }
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ReleaseGPRSRequest extends GprsMessage {

    GPRSCause getGPRSCause();

    PDPID getPDPID();

}