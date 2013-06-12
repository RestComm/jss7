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
