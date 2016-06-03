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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;

/**
 *
<code>
splitLeg {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT SplitLegArg {bound}
  RETURN RESULT TRUE
  ERRORS {missingParameter | unexpectedComponentSequence | unexpectedParameter | unexpectedDataValue | systemFailure | taskRefused | unknownLegID}
  CODE opcode-splitLeg
}
-- Direction: gsmSCF -> gsmSSF, Timer Tsl
-- This operation is used by the gsmSCF to separate a leg from its source call segment and
-- place it in a new call segment within the same call segment association.

SplitLegArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  legToBeSplit    [0] LegID,
  newCallSegment  [1] CallSegmentID {bound} OPTIONAL,
  extensions      [2] Extensions {bound} OPTIONAL,
  ...
}

CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..127)
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SplitLegRequest extends CircuitSwitchedCallMessage {

    LegID getLegToBeSplit();

    Integer getNewCallSegment();

    CAPExtensions getExtensions();

}