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
 CallOutcome ::= ENUMERATED { success (0), failure (1), busy (2), ...} -- exception handling: -- reception of values 3-10
 * shall be mapped to 'success' -- reception of values 11-20 shall be mapped to 'failure' -- reception of values > 20 shall be
 * mapped to 'busy'
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum CallOutcome {
    success(0), failure(1), busy(2);

    private int code;

    private CallOutcome(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static CallOutcome getInstance(int code) {

        if (code == 0 || code >= 3 && code <= 10)
            return CallOutcome.success;
        else if (code == 1 || code >= 11 && code <= 20)
            return CallOutcome.failure;
        else
            return CallOutcome.busy;
    }
}
