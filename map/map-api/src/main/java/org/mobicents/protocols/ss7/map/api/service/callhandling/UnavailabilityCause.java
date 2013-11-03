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
 * UnavailabilityCause ::= ENUMERATED { bearerServiceNotProvisioned (1), teleserviceNotProvisioned (2), absentSubscriber (3),
 * busySubscriber (4), callBarred (5), cug-Reject (6), ...}
 *
 * @author cristian veliscu
 *
 */
public enum UnavailabilityCause {
    bearerServiceNotProvisioned(1), teleserviceNotProvisioned(2), absentSubscriber(3), busySubscriber(4), callBarred(5), cugReject(
            6);

    private int code;

    private UnavailabilityCause(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static UnavailabilityCause getUnavailabilityCause(int code) {
        switch (code) {
            case 1:
                return bearerServiceNotProvisioned;
            case 2:
                return teleserviceNotProvisioned;
            case 3:
                return absentSubscriber;
            case 4:
                return busySubscriber;
            case 5:
                return callBarred;
            case 6:
                return cugReject;
            default:
                return null;
        }
    }
}