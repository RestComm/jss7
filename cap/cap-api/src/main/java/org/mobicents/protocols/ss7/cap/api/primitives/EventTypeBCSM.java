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

package org.mobicents.protocols.ss7.cap.api.primitives;

/**
 *
 EventTypeBCSM ::= ENUMERATED { collectedInfo (2), analyzedInformation (3), routeSelectFailure (4), oCalledPartyBusy (5),
 * oNoAnswer (6), oAnswer (7), oMidCall (8), oDisconnect (9), oAbandon (10), termAttemptAuthorized (12), tBusy (13), tNoAnswer
 * (14), tAnswer (15), tMidCall (16), tDisconnect (17), tAbandon (18), oTermSeized (19), callAccepted (27), oChangeOfPosition
 * (50), tChangeOfPosition (51), ..., oServiceChange (52), tServiceChange (53) } -- Indicates the BCSM detection point event. --
 * Values analyzedInformation and termAttemptAuthorized may be used -- for TDPs only. -- Exception handling: reception of an
 * unrecognized value shall be treated -- like reception of no detection point.
 *
 * @author sergey vetyutnev
 *
 */
public enum EventTypeBCSM {
    collectedInfo(2), analyzedInformation(3), routeSelectFailure(4), oCalledPartyBusy(5), oNoAnswer(6), oAnswer(7), oMidCall(8), oDisconnect(
            9), oAbandon(10), termAttemptAuthorized(12), tBusy(13), tNoAnswer(14), tAnswer(15), tMidCall(16), tDisconnect(17), tAbandon(
            18), oTermSeized(19), callAccepted(27), oChangeOfPosition(50), tChangeOfPosition(51), oServiceChange(52), tServiceChange(
            53);

    private int code;

    private EventTypeBCSM(int code) {
        this.code = code;
    }

    public static EventTypeBCSM getInstance(int code) {
        switch (code) {
            case 2:
                return EventTypeBCSM.collectedInfo;
            case 3:
                return EventTypeBCSM.analyzedInformation;
            case 4:
                return EventTypeBCSM.routeSelectFailure;
            case 5:
                return EventTypeBCSM.oCalledPartyBusy;
            case 6:
                return EventTypeBCSM.oNoAnswer;
            case 7:
                return EventTypeBCSM.oAnswer;
            case 8:
                return EventTypeBCSM.oMidCall;
            case 9:
                return EventTypeBCSM.oDisconnect;
            case 10:
                return EventTypeBCSM.oAbandon;
            case 12:
                return EventTypeBCSM.termAttemptAuthorized;
            case 13:
                return EventTypeBCSM.tBusy;
            case 14:
                return EventTypeBCSM.tNoAnswer;
            case 15:
                return EventTypeBCSM.tAnswer;
            case 16:
                return EventTypeBCSM.tMidCall;
            case 17:
                return EventTypeBCSM.tDisconnect;
            case 18:
                return EventTypeBCSM.tAbandon;
            case 19:
                return EventTypeBCSM.oTermSeized;
            case 27:
                return EventTypeBCSM.callAccepted;
            case 50:
                return EventTypeBCSM.oChangeOfPosition;
            case 51:
                return EventTypeBCSM.tChangeOfPosition;
            case 52:
                return EventTypeBCSM.oServiceChange;
            case 53:
                return EventTypeBCSM.tServiceChange;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }
}
