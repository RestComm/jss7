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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.inap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.inap.api.primitives.INAPExtensions;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;

/**
*
<code>
*** CS1: ***
EventReportBCSM ::= OPERATION
ARGUMENT EventReportBCSMArg
-- Direction: SSF -> SCF, Timer: Terb
-- This operation is used to notify the SCF of a call-related event (e.g., BCSM events such as
-- busy or no answer) previously requested by the SCF in a RequestReportBCSMEvent operation.

*** CS2: ***
eventReportBCSM {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT EventReportBCSMArg {bound}
  RETURN RESULT FALSE
  ALWAYS RESPONDS FALSE
  CODE opcode-eventReportBCSM
}
-- Direction: SSF -> SCF, Timer: Terb
-- This operation is used to notify the SCF of a call-related event (e.g. BCSM events such as busy
-- or no answer) previously requested by the SCF in a RequestReportBCSMEvent operation.

EventReportBCSMArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  eventTypeBCSM                [0] EventTypeBCSM,
  eventSpecificInformationBCSM [2] EventSpecificInformationBCSM {bound} OPTIONAL,
  legID                        [3] LegID OPTIONAL,
  miscCallInfo                 [4] MiscCallInfo DEFAULT {messageType request},
  extensions                   [5] SEQUENCE SIZE(1..bound.&numOfExtensions) OF ExtensionField {bound} OPTIONAL,
  ...
}
</code>
*
*
* @author sergey vetyutnev
*
*/
public interface EventReportBCSMRequest {

    EventTypeBCSM getEventTypeBCSM();

    EventSpecificInformationBCSM getEventSpecificInformationBCSM();

    LegID getLegID();

    MiscCallInfo getMiscCallInfo();

    INAPExtensions getExtensions();

}
