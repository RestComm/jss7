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

package org.mobicents.protocols.ss7.map.api.smstpdu;

/**
 * The TP-Validity-Period-Format is a 2-bit field, located within bit no 3 and 4 of the first octet of SMS-SUBMIT, and to be
 * given the following values: bit4 bit3 0 0 TP-VP field not present 1 0 TP-VP field present - relative format 0 1 TP-VP field
 * present - enhanced format 1 1 TP-VP field present - absolute format
 *
 * @author sergey vetyutnev
 *
 */
public enum ValidityPeriodFormat {

    fieldNotPresent(0), fieldPresentEnhancedFormat(1), fieldPresentRelativeFormat(2), fieldPresentAbsoluteFormat(3);

    private int code;

    private ValidityPeriodFormat(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ValidityPeriodFormat getInstance(int code) {
        switch (code) {
            case 0:
                return fieldNotPresent;
            case 1:
                return fieldPresentEnhancedFormat;
            case 2:
                return fieldPresentRelativeFormat;
            default:
                return fieldPresentAbsoluteFormat;
        }
    }
}
