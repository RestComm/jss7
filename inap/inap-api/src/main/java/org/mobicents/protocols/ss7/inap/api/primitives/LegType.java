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
 LegType ::= OCTET STRING (SIZE(1)) leg1 LegType ::= '01'H leg2 LegType ::= '02'H
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum LegType {
    leg1(1), leg2(2), leg3(3), leg4(4), leg5(5), leg6(6), leg7(7), leg8(8), leg9(
            9);

    private int code;

    private LegType(int code) {
        this.code = code;
    }

    public static LegType getInstance(int code) {
        switch (code) {
        case 1:
            return LegType.leg1;
        case 2:
            return LegType.leg2;
        case 3:
            return LegType.leg3;
        case 4:
            return LegType.leg4;
        case 5:
            return LegType.leg5;
        case 6:
            return LegType.leg6;
        case 7:
            return LegType.leg7;
        case 8:
            return LegType.leg8;
        case 9:
            return LegType.leg9;
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
