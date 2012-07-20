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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

/**
 * 

CallOutcome ::= ENUMERATED {
	success		(0),
	failure		(1),
	busy			(2),
	...}
	--	exception handling: 
	--  reception of values 3-10 shall be mapped to 'success'
	--  reception of values 11-20 shall be mapped to 'failure'
	--  reception of values > 20 shall be mapped to 'busy'

 * 
 * @author sergey vetyutnev
 * 
 */
public enum CallOutcome {
	success(0), 
	failure(1), 
	busy(2);

	private int code;

	private CallOutcome(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static CallOutcome getInstance(int code) {

		if (code == 0 || code >= 3 && code <= 10)
			return CallOutcome.success;
		else if (code == 1 || code >= 11 && code <= 20)
			return CallOutcome.failure;
		else
			return CallOutcome.busy;
	}
}
