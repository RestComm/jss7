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
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;

/**
 *
 resetTimer {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT ResetTimerArg {bound} RETURN RESULT FALSE ERRORS
 * {missingParameter | parameterOutOfRange | taskRefused | unexpectedComponentSequence | unexpectedDataValue |
 * unexpectedParameter | unknownCSID} CODE opcode-resetTimer} -- Direction: gsmSCF -> gsmSSF, Timer: Trt -- This operation is
 * used to request the gsmSSF to refresh an application timer in the gsmSSF.
 *
 * ResetTimerArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { timerID [0] TimerID DEFAULT tssf, timervalue [1] TimerValue,
 * extensions [2] Extensions {bound} OPTIONAL, callSegmentID [3] CallSegmentID {bound} OPTIONAL, ... }
 *
 * TimerID ::= ENUMERATED { tssf (0) } -- Indicates the timer to be reset. TimerValue ::= Integer4 -- Indicates the timer value
 * (in seconds).
 *
 * CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..bound.&numOfCSs) numOfCSs ::= 127
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ResetTimerRequest extends CircuitSwitchedCallMessage {

    TimerID getTimerID();

    int getTimerValue();

    CAPExtensions getExtensions();

    Integer getCallSegmentID();

}