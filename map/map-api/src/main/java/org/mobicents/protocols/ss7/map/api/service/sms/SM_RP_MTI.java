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

package org.mobicents.protocols.ss7.map.api.service.sms;

/**
 *
 * SM-RP-MTI ::= INTEGER (0..10)
 *	-- 0 SMS Deliver 
 *	-- 1 SMS Status Report
 *	-- other values are reserved for future use and shall be discarded if
 *	-- received
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum SM_RP_MTI {

	SMS_Deliver(0), 
	SMS_Status_Report(1); 

	private int code;

	private SM_RP_MTI(int code) {
		this.code = code;
	}


	public int getCode () {
		return this.code;
	}

	public static SM_RP_MTI getInstance(int code) {
		switch (code) {
		case 0:
			return SMS_Deliver;
		case 1:
			return SMS_Status_Report;
		default:
			return null;
		}
	}	

}
