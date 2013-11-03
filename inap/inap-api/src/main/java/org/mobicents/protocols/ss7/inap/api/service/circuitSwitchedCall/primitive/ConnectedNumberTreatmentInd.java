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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

/**
*
ConnectedNumberTreatmentInd ::= ENUMERATED {
noINImpact (0),
presentationRestricted (1),
presentCalledINNumber (2),
presentCalledINNumberRestricted (3)
}
-- The default is as specified in EN 301 070-1.
*
* @author sergey vetyutnev
*
*/
public enum ConnectedNumberTreatmentInd {
    noINImpact(0), presentationRestricted(1), presentCalledINNumber(2), presentCallINNumberRestricted(3);

    private int code;

    private ConnectedNumberTreatmentInd(int code) {
        this.code = code;
    }

    public static ConnectedNumberTreatmentInd getInstance(int code) {
        switch (code) {
            case 0:
                return ConnectedNumberTreatmentInd.noINImpact;
            case 1:
                return ConnectedNumberTreatmentInd.presentationRestricted;
            case 2:
                return ConnectedNumberTreatmentInd.presentCalledINNumber;
            case 3:
                return ConnectedNumberTreatmentInd.presentCallINNumberRestricted;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }
}
