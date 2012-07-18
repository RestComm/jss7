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

NotificationToMSUser ::= ENUMERATED {
	notifyLocationAllowed	(0),
	notifyAndVerify-LocationAllowedIfNoResponse	(1),
	notifyAndVerify-LocationNotAllowedIfNoResponse	(2),
	...,
	locationNotAllowed (3) }
-- exception handling:
-- At reception of any other value than the ones listed the receiver shall ignore
-- NotificationToMSUser.

 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public enum NotificationToMSUser {
	notifyLocationAllowed(0), 
	notifyAndVerifyLocationAllowedIfNoResponse(1), 
	notifyAndVerifyLocationNotAllowedIfNoResponse(2), 
	locationNotAllowed(3);

	private int code;

	private NotificationToMSUser(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static NotificationToMSUser getInstance(int code) {
		switch (code) {
		case 0:
			return NotificationToMSUser.notifyLocationAllowed;
		case 1:
			return NotificationToMSUser.notifyAndVerifyLocationAllowedIfNoResponse;
		case 2:
			return NotificationToMSUser.notifyAndVerifyLocationNotAllowedIfNoResponse;
		case 3:
			return NotificationToMSUser.locationNotAllowed;
		default:
			return null;
		}
	}
}

