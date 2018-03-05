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

package org.restcomm.protocols.ss7.cap.api.service.sms;

import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfo;

/**
 *
 eventReportSMS {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT EventReportSMSArg {bound}
   RETURN RESULT FALSE
   ALWAYS RESPONDS FALSE
   CODE opcode-eventReportSMS
 }
 -- Direction: gsmSSF or gprsSSF -> gsmSCF, Timer: Terbsms
 -- This operation is used to notify the gsmSCF of a Short Message related event (FSM events
 -- such as submission, delivery or failure) previously requested by the gsmSCF in a
 -- RequestReportSMSEvent operation.

 EventReportSMSArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
   eventTypeSMS                [0] EventTypeSMS,
   eventSpecificInformationSMS [1] EventSpecificInformationSMS OPTIONAL,
   miscCallInfo                [2] MiscCallInfo DEFAULT {messageType request},
   extensions                  [10] Extensions {bound} OPTIONAL,
   ...
}
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface EventReportSMSRequest extends SmsMessage {

    EventTypeSMS getEventTypeSMS();

    EventSpecificInformationSMS getEventSpecificInformationSMS();

    MiscCallInfo getMiscCallInfo();

    CAPExtensions getExtensions();

}