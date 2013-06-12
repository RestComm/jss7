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

package org.mobicents.protocols.ss7.cap.api.service.sms;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;

/**
 *
 requestReportSMSEvent {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT RequestReportSMSEventArg {bound} RETURN RESULT
 * FALSE ERRORS {missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
 * unexpectedDataValue | unexpectedParameter} CODE opcode-requestReportSMSEvent} -- Direction: gsmSCF -> gsmSSF or gprsSSF,
 * Timer: Trrbsms -- This operation is used to request the gsmSSF or gprsSSF to monitor for a -- Short Message related event
 * (FSM events such as submission, delivery or failure) -- and to send a notification to the gsmSCF when the event is detected.
 *
 * RequestReportSMSEventArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { sMSEvents [0] SEQUENCE SIZE (1..10) OF SMSEvent,
 * extensions [10] Extensions {bound} OPTIONAL, ... } -- Indicates the Short Message related events(s) for notification.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface RequestReportSMSEventRequest extends SmsMessage {

    ArrayList<SMSEvent> getSMSEvents();

    CAPExtensions getExtensions();

}
