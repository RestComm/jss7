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

/**
 *
 
RequestedInformationType ::= ENUMERATED {
callAttemptElapsedTime (0),
callStopTime (1),
callConnectedElapsedTime (2),
releaseCause (30)
}
 
 * 
 * @author sergey vetyutnev
 * 
 */
public enum RequestedInformationType {

	callAttemptElapsedTime (0),
	callStopTime (1),
	callConnectedElapsedTime (2),
	releaseCause (30);
	
	private int code;

	private RequestedInformationType(int code) {
		this.code = code;
	}

	public static RequestedInformationType getInstance(int code) {
		switch (code) {
		case 0:
			return RequestedInformationType.callAttemptElapsedTime;
		case 1:
			return RequestedInformationType.callStopTime;
		case 2:
			return RequestedInformationType.callConnectedElapsedTime;
		case 30:
			return RequestedInformationType.releaseCause;
		}

		return null;
	}

	public int getCode() {
		return code;
	}
}
