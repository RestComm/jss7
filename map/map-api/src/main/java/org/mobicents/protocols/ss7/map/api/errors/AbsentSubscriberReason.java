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

package org.mobicents.protocols.ss7.map.api.errors;

/**
 *
 * AbsentSubscriberReason ::= ENUMERATED { imsiDetach (0), restrictedArea (1), noPageResponse (2), ... , purgedMS (3)} --
 * exception handling: at reception of other values than the ones listed the -- AbsentSubscriberReason shall be ignored. -- The
 * AbsentSubscriberReason: purgedMS is defined for the Super-Charger feature -- (see TS 23.116). If this value is received in a
 * Provide Roaming Number response -- it shall be mapped to the AbsentSubscriberReason: imsiDetach in the Send Routeing --
 * Information response
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum AbsentSubscriberReason {
    imsiDetach(0), restrictedArea(1), noPageResponse(2), purgedMS(3);

    private int code;

    private AbsentSubscriberReason(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AbsentSubscriberReason getInstance(int code) {
        switch (code) {
            case 0:
                return imsiDetach;
            case 1:
                return restrictedArea;
            case 2:
                return noPageResponse;
            case 3:
                return purgedMS;
            default:
                return null;
        }
    }

}
