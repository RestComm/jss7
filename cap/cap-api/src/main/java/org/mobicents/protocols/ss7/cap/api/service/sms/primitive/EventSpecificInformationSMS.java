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

package org.mobicents.protocols.ss7.cap.api.service.sms.primitive;

import org.mobicents.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiSms.OSmsSubmissionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiSms.TSmsDeliverySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfo;

/**
*

EventSpecificInformationSMS ::= CHOICE {
o-smsFailureSpecificInfo [0] SEQUENCE {
failureCause [0] MO-SMSCause OPTIONAL,
...
},
o-smsSubmissionSpecificInfo [1] SEQUENCE {
-- no specific info defined
...
},
t-smsFailureSpecificInfo [2] SEQUENCE {
failureCause [0] MT-SMSCause OPTIONAL,
...
},
t-smsDeliverySpecificInfo [3] SEQUENCE {
-- no specific info defined
...
} }

* 
* @author sergey vetyutnev
* 
*/
public interface EventSpecificInformationSMS {

	public OSmsFailureSpecificInfo getOSmsFailureSpecificInfo();

	public OSmsSubmissionSpecificInfo getOSmsSubmissionSpecificInfo();

	public TSmsFailureSpecificInfo getTSmsFailureSpecificInfo();

	public TSmsDeliverySpecificInfo getTSmsDeliverySpecificInfo();

}
