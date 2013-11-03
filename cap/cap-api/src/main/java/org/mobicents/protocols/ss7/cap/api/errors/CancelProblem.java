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

package org.mobicents.protocols.ss7.cap.api.errors;

/**
 *
 PARAMETER SEQUENCE { problem [0] ENUMERATED { unknownOperation (0), tooLate (1), operationNotCancellable (2) },
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum CancelProblem {
    unknownOperation(0), tooLate(1), operationNotCancellable(2);

    private int code;

    private CancelProblem(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static CancelProblem getInstance(int code) {
        switch (code) {
            case 0:
                return CancelProblem.unknownOperation;
            case 1:
                return CancelProblem.tooLate;
            case 2:
                return CancelProblem.operationNotCancellable;
            default:
                return null;
        }
    }
}
