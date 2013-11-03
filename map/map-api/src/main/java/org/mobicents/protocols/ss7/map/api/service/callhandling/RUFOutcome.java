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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

/**
 *
 RUF-Outcome ::= ENUMERATED{ accepted (0), rejected (1), noResponseFromFreeMS (2), -- T4 Expiry noResponseFromBusyMS (3), --
 * T10 Expiry udubFromFreeMS (4), udubFromBusyMS (5), ...} -- exception handling: -- reception of values 6-20 shall be mapped to
 * 'accepted' -- reception of values 21-30 shall be mapped to 'rejected' -- reception of values 31-40 shall be mapped to
 * 'noResponseFromFreeMS' -- reception of values 41-50 shall be mapped to 'noResponseFromBusyMS' -- reception of values 51-60
 * shall be mapped to 'udubFromFreeMS' -- reception of values > 60 shall be mapped to 'udubFromBusyMS'
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum RUFOutcome {
    accepted(0), rejected(1), noResponseFromFreeMS(2), // -- T4 Expiry
    noResponseFromBusyMS(3), // -- T10 Expiry
    udubFromFreeMS(4), udubFromBusyMS(5);

    private int code;

    private RUFOutcome(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static RUFOutcome getInstance(int code) {
        if (code == 0 || code >= 6 && code <= 20)
            return RUFOutcome.accepted;
        else if (code == 1 || code >= 21 && code <= 30)
            return RUFOutcome.rejected;
        else if (code == 2 || code >= 31 && code <= 40)
            return RUFOutcome.rejected;
        else if (code == 3 || code >= 41 && code <= 50)
            return RUFOutcome.rejected;
        else if (code == 4 || code >= 51 && code <= 60)
            return RUFOutcome.rejected;
        else
            return RUFOutcome.udubFromBusyMS;
    }
}
