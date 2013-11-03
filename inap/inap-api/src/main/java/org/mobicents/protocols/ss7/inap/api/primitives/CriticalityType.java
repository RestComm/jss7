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

package org.mobicents.protocols.ss7.inap.api.primitives;

/**
 *
 CriticalityType ::= ENUMERATED {ignore(0), abort(1)}
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum CriticalityType {
    ignore(0), abort(1);

    private int code;

    private CriticalityType(int code) {
        this.code = code;
    }

    public static CriticalityType getInstance(int code) {
        switch (code) {
            case 0:
                return CriticalityType.ignore;
            case 1:
                return CriticalityType.abort;
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
