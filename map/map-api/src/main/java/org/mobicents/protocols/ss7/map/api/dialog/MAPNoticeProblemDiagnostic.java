/*
 * TeleStax, Open Source Cloud Communications  
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.api.dialog;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public enum MAPNoticeProblemDiagnostic {
	// we do not use following reasons now because we deliver local and remote Reject primitives
	// to a MAP-USER in components array
	// AbnormalEventDetectedByThePeer(0), ResponseRejectedByThePeer(1),
	// AbnormalEventReceivedFromThePeer(2),

	/**
	 * TC-NOTICE is received for outgoing message has not been delivered to a peer
	 * because of network issue.
	 * When Dialog initiating state (TC-BEGIN has been sent)
	 * onDialogReject() will be delivered instead of onDialogNotice() 
	 */
	MessageCannotBeDeliveredToThePeer(0);

	private int code;

	private MAPNoticeProblemDiagnostic(int code) {
		this.code = code;
	}
}
