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

package org.mobicents.protocols.ss7.cap.api.dialog;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public enum CAPGeneralAbortReason {
	// Received data is absent or unrecognizable
	BadReceivedData(0),
	// TCAP Dialog is destroyed when CapDialog is alive and not shutting down
	TcapDialogDestroyedData(1),
	// Application context name does not supported
	ACNNotSupported(2),
	// Other part has been refused the Dialog with AARE apdu with
	// abortReason=null or abortReason=no-reason-given
	DialogRefused(3),
	// Other part has been refused the Dialog with AARE apdu with
	// abortReason=null or abortReason=NoCommonDialogPortion
	NoCommonDialogPortionReceived(4),
	// User abort, CAPUserAbortReason is present in the message
	UserSpecific(5);

	private int code;

	private CAPGeneralAbortReason(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
