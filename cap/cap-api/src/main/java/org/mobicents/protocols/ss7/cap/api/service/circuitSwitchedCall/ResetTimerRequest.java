/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;

/**
 *
<code>
resetTimer {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT ResetTimerArg {bound}
  RETURN RESULT FALSE
  ERRORS {missingParameter | parameterOutOfRange | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter | unknownCSID}
  CODE opcode-resetTimer
}
-- Direction: gsmSCF -> gsmSSF, Timer: Trt
-- This operation is used to request the gsmSSF to refresh an application timer in the gsmSSF.

ResetTimerArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  timerID        [0] TimerID DEFAULT tssf,
  timervalue     [1] TimerValue,
  extensions     [2] Extensions {bound} OPTIONAL,
  callSegmentID  [3] CallSegmentID {bound} OPTIONAL,
  ...
}

TimerID ::= ENUMERATED { tssf (0) }
-- Indicates the timer to be reset. TimerValue ::= Integer4
-- Indicates the timer value (in seconds).

CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..bound.&numOfCSs)
numOfCSs ::= 127
</code>
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