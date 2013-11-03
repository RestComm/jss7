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
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;

/**
 *
 splitLeg {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT SplitLegArg {bound} RETURN RESULT TRUE ERRORS {missingParameter
 * | unexpectedComponentSequence | unexpectedParameter | unexpectedDataValue | systemFailure | taskRefused | unknownLegID} CODE
 * opcode-splitLeg} -- Direction: gsmSCF -> gsmSSF, Timer Tsl -- This operation is used by the gsmSCF to separate a leg from its
 * source call segment and -- place it in a new call segment within the same call segment association.
 *
 * SplitLegArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { legToBeSplit [0] LegID, newCallSegment [1] CallSegmentID {bound}
 * OPTIONAL, extensions [2] Extensions {bound} OPTIONAL, ... }
 *
 * CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..127)
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