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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 * 

T-BcsmTriggerDetectionPoint ::= ENUMERATED {
	termAttemptAuthorized (12),
	... ,
	tBusy (13),
	tNoAnswer (14)}
	-- exception handling:
	-- For T-BcsmCamelTDPData sequences containing this parameter with any other
	-- value than the ones listed above, the receiver shall ignore the whole
	-- T-BcsmCamelTDPData sequence.

 * 
 * @author sergey vetyutnev
 * 
 */
public enum TBcsmTriggerDetectionPoint {
	termAttemptAuthorized(12), 
	tBusy(13), 
	tNoAnswer(14);

	private int code;

	private TBcsmTriggerDetectionPoint(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static TBcsmTriggerDetectionPoint getInstance(int code) {
		switch (code) {
		case 12:
			return TBcsmTriggerDetectionPoint.termAttemptAuthorized;
		case 13:
			return TBcsmTriggerDetectionPoint.tBusy;
		case 14:
			return TBcsmTriggerDetectionPoint.tNoAnswer;
		default:
			return null;
		}
	}
}
