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

package org.mobicents.protocols.ss7.cap.api.dialog;

/**
 * The result of checking if the CAPService can serve the given ApplicationContext AC_Serving - ApplicationContext can be served
 * AC_VersionIncorrect - ApplicationContext belongs to the MAPService serving zone but either not implemented or bad version. An
 * alternativeApplicationContext can be supplied AC_NotServing - ApplicationContext can not be served
 *
 * @author sergey vetyutnev
 *
 */
public enum ServingCheckResult {
    AC_Serving(0), AC_VersionIncorrect(1), AC_NotServing(2);

    private int code;

    private ServingCheckResult(int code) {
        this.code = code;
    }
}
