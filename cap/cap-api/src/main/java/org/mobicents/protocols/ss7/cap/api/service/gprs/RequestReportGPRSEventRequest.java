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

package org.mobicents.protocols.ss7.cap.api.service.gprs;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

/**
 *
 requestReportGPRSEvent {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT RequestReportGPRSEventArg {bound} RETURN RESULT
 * FALSE ERRORS {missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
 * unexpectedDataValue | unexpectedParameter | unknownPDPID} CODE opcode-requestReportGPRSEvent} -- Direction: gsmSCF ->
 * gprsSSF, Timer: Trrqe -- This operation is used to request the gprsSSF to monitor for an event (e.g., GPRS events -- such as
 * attach or PDP Context activiation), then send a notification back to the -- gsmSCF when the event is detected.
 *
 * RequestReportGPRSEventArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { gPRSEvent [0] SEQUENCE SIZE (1..10) OF GPRSEvent, pDPID
 * [1] PDPID OPTIONAL, ... } -- Indicates the GPRS related events for notification.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface RequestReportGPRSEventRequest extends GprsMessage {

    ArrayList<GPRSEvent> getGPRSEvent();

    PDPID getPDPID();

}