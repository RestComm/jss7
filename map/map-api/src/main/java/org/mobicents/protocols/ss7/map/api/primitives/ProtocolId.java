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

package org.mobicents.protocols.ss7.map.api.primitives;

/**
 * ProtocolId ::= ENUMERATED { gsm-0408 (1), gsm-0806 (2), gsm-BSSMAP (3), -- Value 3 is reserved and must not be used
 * ets-300102-1 (4)}
 *
 * @author cristian veliscu
 *
 */
public enum ProtocolId {
    gsm_0408(1), gsm_0806(2), gsm_BSSMAP(3), ets_300102_1(4);

    private int code;

    private ProtocolId(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ProtocolId getProtocolId(int code) {
        switch (code) {
            case 1:
                return gsm_0408;
            case 2:
                return gsm_0806;
            case 3:
                return gsm_BSSMAP;
            case 4:
                return ets_300102_1;
            default:
                return null;
        }
    }
}