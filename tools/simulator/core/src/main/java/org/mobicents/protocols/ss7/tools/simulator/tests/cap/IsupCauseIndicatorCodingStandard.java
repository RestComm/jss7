/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

/**
 *
 * IsupCauseIndicatorCodingStandard ::= ENUMERATED { ITUT (0), IOS_IEC(1),
 * national(2), specific(3); }
 *
 * @author mnowa
 *
 *@see Q.850
 */
public enum IsupCauseIndicatorCodingStandard {
    ITUT(0), IOS_IEC(1), national(2), specific(3);

    private int code;

    private IsupCauseIndicatorCodingStandard(int code) {
        this.code = code;
    }

    public static IsupCauseIndicatorCodingStandard getInstance(int code) {
        switch (code) {
            case 0:
                return ITUT;
            case 1:
                return IOS_IEC;
            case 2:
                return national;
            case 3:
                return specific;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }
}
