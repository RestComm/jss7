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

import org.mobicents.protocols.ss7.cap.api.primitives.Burst;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LegOrCallSegment;

/**
 *
 playTone {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT PlayToneArg {bound} RETURN RESULT FALSE ERRORS {missingParameter
 * | parameterOutOfRange | systemFailure | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter |
 * unknownLegID | unknownCSID} CODE opcode-playTone} -- Direction: gsmSCF -> gsmSSF, Timer: Tpt -- This operation is used to
 * play tones to either a leg or a call segment using -- the MSC's tone generator.
 *
 * PlayToneArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { legOrCallSegment [0] LegOrCallSegment {bound}, bursts [1] Burst,
 * extensions [2] Extensions {bound} OPTIONAL, ... }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PlayToneRequest extends CircuitSwitchedCallMessage {

    LegOrCallSegment getLegOrCallSegment();

    Burst getBursts();

    CAPExtensions getExtensions();

}