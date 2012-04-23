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

package org.mobicents.protocols.ss7.cap.api;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public enum CAPServerRole {
	// GSM Service Switching Function
	gsmSSF(0),
	// GSM Assisting Service Switching Function
	assistingGsmSSF(1),
	// GSM Specialized Resource Function
	gsmSRF(2),
	// Short Message Service Service Switching Function
	smsSSF(3),
	// GPRS Service Switching Function
	gprsSSF(4),
	// GSM Service Control Function
	gsmSCF(10);

	private int code;

	private CAPServerRole(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
