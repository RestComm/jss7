/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
     * TC-NOTICE is received for outgoing message has not been delivered to a peer because of network issue. When Dialog
     * initiating state (TC-BEGIN has been sent) onDialogReject() will be delivered instead of onDialogNotice()
     */
    MessageCannotBeDeliveredToThePeer(0);

    private int code;

    private MAPNoticeProblemDiagnostic(int code) {
        this.code = code;
    }
}
