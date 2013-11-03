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

import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;

/**
 *
 disconnectLeg {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT DisconnectLegArg {bound}
   RETURN RESULT TRUE
   ERRORS {
     missingParameter | systemFailure | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter | unknownLegID
   }
   CODE opcode-disconnectLeg}
-- Direction: gsmSCF -> gsmSSF, Timer Tdl
-- This operation is used by the gsmSCF to release a specific leg associated with the call and
-- retain any other legs not specified in the DisconnectLeg. Refer to clause 11 for a description
-- of the procedures associated with this operation.

DisconnectLegArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  legToBeReleased   [0] LegID,
  releaseCause      [1] Cause {bound} OPTIONAL,
  extensions        [2] Extensions {bound} OPTIONAL,
  ...
}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface DisconnectLegRequest extends CircuitSwitchedCallMessage {

    LegID getLegToBeReleased();

    CauseCap getReleaseCause();

    CAPExtensions getExtensions();

}