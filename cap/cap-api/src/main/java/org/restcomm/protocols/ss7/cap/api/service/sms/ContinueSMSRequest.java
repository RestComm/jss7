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

/**
*
continueSMS OPERATION ::= {
RETURN RESULT FALSE
ALWAYS RESPONDS FALSE
CODE opcode-continueSMS}
-- Direction: gsmSCF -> smsSSF, Timer: Tcuesms
-- This operation is used to request the smsSSF to proceed with
-- Short Message processing at the DP at which it previously suspended
-- Short Message processing to await gsmSCF instructions (i.e. proceed
-- to the next Point in Association in the SMS FSM). The smsSSF
-- continues SMS processing without substituting new data from the gsmSCF.

*
*
*
* @author sergey vetyutnev
*
*/
public interface ContinueSMSRequest extends SmsMessage {

}
