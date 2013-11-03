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

package org.mobicents.protocols.ss7.map.api.service.lsm;

/**
 * LCSClientType ::= ENUMERATED { emergencyServices (0), valueAddedServices (1), plmnOperatorServices (2),
 * lawfulInterceptServices (3), ... } -- exception handling: -- unrecognized values may be ignored if the LCS client uses the
 * privacy override -- otherwise, an unrecognized value shall be treated as unexpected data by a receiver -- a return error
 * shall then be returned if received in a MAP invoke
 *
 * @author amit bhayani
 *
 */
public enum LCSClientType {

    emergencyServices(0), valueAddedServices(1), plmnOperatorServices(2), lawfulInterceptServices(3);

    private final int type;

    private LCSClientType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public static LCSClientType getLCSClientType(int type) {
        switch (type) {
            case 0:
                return emergencyServices;
            case 1:
                return valueAddedServices;
            case 2:
                return plmnOperatorServices;
            case 3:
                return lawfulInterceptServices;
            default:
                return null;
        }
    }
}
