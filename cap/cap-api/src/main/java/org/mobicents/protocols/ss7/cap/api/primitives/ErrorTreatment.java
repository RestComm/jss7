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

package org.mobicents.protocols.ss7.cap.api.primitives;

/**
 *
 ErrorTreatment ::= ENUMERATED { stdErrorAndInfo (0), help (1), repeatPrompt (2) } -- stdErrorAndInfomeans returning the
 * 'ImproperCallerResponse' error in the event of an error -- condition during collection of user info.
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum ErrorTreatment {

    stdErrorAndInfo(0), help(1), repeatPrompt(2);

    private int code;

    private ErrorTreatment(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ErrorTreatment getInstance(int code) {
        switch (code) {
            case 0:
                return ErrorTreatment.stdErrorAndInfo;
            case 1:
                return ErrorTreatment.help;
            case 2:
                return ErrorTreatment.repeatPrompt;
            default:
                return null;
        }
    }
}
