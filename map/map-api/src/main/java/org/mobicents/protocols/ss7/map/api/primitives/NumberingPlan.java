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
 *
 * -- 0000 unknown -- 0001 ISDN/Telephony Numbering Plan (Rec CCITT E.164) -- 0010 spare -- 0011 data numbering plan (CCITT Rec
 * X.121) -- 0100 telex numbering plan (CCITT Rec F.69) -- 0101 spare -- 0110 land mobile numbering plan (CCITT Rec E.212) --
 * 0111 spare -- 1000 national numbering plan -- 1001 private numbering plan -- 1111 reserved for extension -- all other values
 * are reserved.
 *
 * @author amit bhayani
 *
 */
public enum NumberingPlan {

    unknown(0), ISDN(1), spare_2(2), data(3), telex(4), spare_5(5), land_mobile(6), spare_7(7), national(8), private_plan(9), reserved(
            15);

    private int indicator;

    private NumberingPlan(int indicator) {
        this.indicator = indicator;
    }

    public int getIndicator() {
        return indicator;
    }

    public static NumberingPlan getInstance(int indication) {
        switch (indication) {
            case 0:
                return unknown;
            case 1:
                return ISDN;
            case 2:
                return spare_2;
            case 3:
                return data;
            case 4:
                return telex;
            case 5:
                return spare_5;
            case 6:
                return land_mobile;
            case 7:
                return spare_7;
            case 8:
                return national;
            case 9:
                return private_plan;
            case 15:
                return reserved;
            default:
                return null;
        }
    }

}
