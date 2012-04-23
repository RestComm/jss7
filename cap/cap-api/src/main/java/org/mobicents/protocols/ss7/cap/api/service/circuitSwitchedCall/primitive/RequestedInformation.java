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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.primitives.DateAndTime;

/**
*

RequestedInformationTypeList ::= SEQUENCE SIZE (1.. numOfInfoItems) OF RequestedInformationType

RequestedInformation {PARAMETERS-BOUND : bound} ::= SEQUENCE {
requestedInformationType [0] RequestedInformationType,
requestedInformationValue [1] RequestedInformationValue {bound},
...
}

RequestedInformationValue {PARAMETERS-BOUND : bound} ::= CHOICE {
callAttemptElapsedTimeValue [0] INTEGER (0..255),
callStopTimeValue [1] DateAndTime,
callConnectedElapsedTimeValue [2] Integer4,
releaseCauseValue [30] Cause {bound}
}
-- The callAttemptElapsedTimeValue is specified in seconds. The unit for the
-- callConnectedElapsedTimeValue is 100 milliseconds
 
* 
* @author sergey vetyutnev
* 
*/
public interface RequestedInformation {
	
	public RequestedInformationType getRequestedInformationType();

	public Integer getCallAttemptElapsedTimeValue();

	public DateAndTime getCallStopTimeValue();

	public Integer getCallConnectedElapsedTimeValue();

	public CauseCap getReleaseCauseValue();

}
