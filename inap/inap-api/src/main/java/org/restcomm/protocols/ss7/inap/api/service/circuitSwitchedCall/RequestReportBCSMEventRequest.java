/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.inap.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.inap.api.primitives.INAPExtensions;

/**
*
<code>
*** CS1: ***
RequestReportBCSMEvent ::= OPERATION
  ARGUMENT RequestReportBCSMEventArg
  ERRORS { MissingParameter, SystemFailure, TaskRefused, UnexpectedComponentSequence, UnexpectedDataValue, UnexpectedParameter
}
-- Direction: SCF -> SSF, Timer: Trrb
-- This operation is used to request the SSF to monitor for a call-related event (e.g., BCSM events
-- such as busy or no answer), then send a notification back to the SCF when the event is
-- detected.

*** CS2: ***
requestReportBCSMEvent {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT RequestReportBCSMEventArg {bound}
  RETURN RESULT FALSE
  ERRORS {missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter | unknownLegID}
  CODE opcode-requestReportBCSMEvent
}
-- Direction: SCF -> SSF, Timer: Trrb
-- This operation is used to request the SSF to monitor for a call-related event
-- (e.g. BCSM events such as busy or no answer), then send a notification back to the SCF when
-- the event is detected.
-- NOTE:
-- Every EDP must be explicitly armed by the SCF via a RequestReportBCSMEvent operation. No
-- implicit arming of EDPs at the SSF after reception of any operation (different from
-- RequestReportBCSMEvent) from the SCF is allowed.

RequestReportBCSMEventArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  bcsmEvents [0] SEQUENCE SIZE(1..bound.&numOfBCSMEvents) OF BCSMEvent {bound},
  extensions [2] SEQUENCE SIZE(1..bound.&numOfExtensions) OF ExtensionField {bound} OPTIONAL,
  ...
}
-- Indicates the BCSM related events for notification.
</code>
*
*
* @author sergey vetyutnev
*
*/
public interface RequestReportBCSMEventRequest {

    ArrayList<BCSMEvent> getBCSMEvents();

    INAPExtensions getExtensions();

}
