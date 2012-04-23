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
 * CAP-U-ABORT-REASON
 * 
 */
public enum CAPUserAbortReason {
	// shall be set when any other reasons above do not apply
	no_reason_given(1), 
	// shall be set when application timer (e.g. Tssf) is expired.
	application_timer_expired(2), 
	// shall be set when received signal is not allowed in CAP procedures.
	// For example, when a class 4 operation is received from the 
	// gsmSCF and the operation is not allowed in gsmSSF FSM. 
	// (gsmSSF FSM cannot continue state transition). (e.g. ReleaseCall 
	// operation received in Waiting for End of Temporary Connection 
	// state.) 
	not_allowed_procedures(3), 
	// shall be set when abnormal procedures occur at entity action.
	abnormal_processing(4), 
	// shall be set when requested resource is unavailable due to 
	// congestion at TC user (CAP) level.
	congestion(5),
	// shall be set if the received destinationReference is unknown or
	// for a known destination Reference the received originationReference
	// does not match with the stored originationReference.
	invalid_reference(6),
	// shall be set when the destinationReference or the
	// originationReference is absent in the received message but is
	// required to be present according to the procedures in
	// subclause 14.1.7.
	missing_reference(7),
	// shall be used by the gprsSSF to indicate to the gsmSCF that a
	// specific instance already has a TC dialogue open. This error
	// cause is typically obtained when both the gsmSCF and gprsSSF
	// open a new dialogue at the same time.
	overlapping_dialogue(8);

	private int code;

	private CAPUserAbortReason(int code) {
		this.code = code;
	}

	public static CAPUserAbortReason getInstance(int code) {
		switch (code) {
		case 1:
			return CAPUserAbortReason.no_reason_given;
		case 2:
			return CAPUserAbortReason.application_timer_expired;
		case 3:
			return CAPUserAbortReason.not_allowed_procedures;
		case 4:
			return CAPUserAbortReason.abnormal_processing;
		case 5:
			return CAPUserAbortReason.congestion;
		case 6:
			return CAPUserAbortReason.invalid_reference;
		case 7:
			return CAPUserAbortReason.missing_reference;
		case 8:
			return CAPUserAbortReason.overlapping_dialogue;
		}
		
		return no_reason_given;
	}
	
	public int getCode() {
		return code;
	}
}
