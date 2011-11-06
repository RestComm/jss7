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

package org.mobicents.protocols.ss7.map.api.service.subscriberInformation;

/**
*
NotReachableReason ::= ENUMERATED {
	msPurged (0),
	imsiDetached (1),
	restrictedArea (2),
	notRegistered (3)}
* 
* @author sergey vetyutnev
* 
*/
public enum NotReachableReason {
	msPurged (0),
	imsiDetached (1),
	restrictedArea (2),
	notRegistered (3);

	private int code;

	private NotReachableReason(int code) {
		this.code = code;
	}

	public static NotReachableReason getInstance(int code) {
		switch (code) {
		case 0:
			return NotReachableReason.msPurged;
		case 1:
			return NotReachableReason.imsiDetached;
		case 2:
			return NotReachableReason.restrictedArea;
		case 3:
			return NotReachableReason.notRegistered;
		default:
			return null;
		}
	}

	public int getCode() {
		return this.code;
	}
}
