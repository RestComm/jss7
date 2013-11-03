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

package org.mobicents.protocols.ss7.map.api.service.oam;

/**
 *
 TraceDepth ::= ENUMERATED { minimum (0), medium (1), maximum (2), ...} -- The value medium is applicable only for RNC. For
 * other network elements, if value medium -- is received, value minimum shall be applied.
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum TraceDepth {
    minimum(0), medium(1), maximum(2);

    private int code;

    private TraceDepth(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static TraceDepth getInstance(int code) {
        switch (code) {
            case 0:
                return TraceDepth.minimum;
            case 1:
                return TraceDepth.medium;
            case 2:
                return TraceDepth.maximum;
            default:
                return null;
        }
    }
}
