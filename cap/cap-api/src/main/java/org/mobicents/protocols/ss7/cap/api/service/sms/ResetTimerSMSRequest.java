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

import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;

/**
*

resetTimerSMS {PARAMETERS-BOUND : bound} OPERATION ::= { 
 ARGUMENT  ResetTimerSMSArg {bound} 
 RETURN RESULT FALSE 
 ERRORS   {missingParameter | 
     parameterOutOfRange | 
     taskRefused | 
     unexpectedComponentSequence | 
     unexpectedDataValue | 
     unexpectedParameter} 
 CODE   opcode-resetTimerSMS} 
-- Direction: gsmSCF -> smsSSF, Timer: T  
rtsms 
-- This operation is used to request the smsSSF to refresh an application 
-- timer in the smsSSF. 
 
ResetTimerSMSArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { 
 timerID        [0] TimerID DEFAULT tssf, 
 timervalue       [1] TimerValue, 
 extensions       [2] Extensions {bound}       OPTIONAL, 
 ... 
 } 

TimerValue ::= Integer4
-- Indicates the timer value (in seconds).

* 
* @author sergey vetyutnev
* 
*/
public interface ResetTimerSMSRequest extends SmsMessage {

	public TimerID getTimerID();

	public int getTimerValue();

	public CAPExtensions getExtensions();

}
