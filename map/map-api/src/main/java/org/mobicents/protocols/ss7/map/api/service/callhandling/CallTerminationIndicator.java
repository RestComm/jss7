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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

/**
 *
 CallTerminationIndicator ::= ENUMERATED { terminateCallActivityReferred (0), terminateAllCallActivities (1), ...} --
 * exception handling: -- reception of values 2-10 shall be mapped to ' terminateCallActivityReferred ' -- reception of values >
 * 10 shall be mapped to ' terminateAllCallActivities '
 *
 * -- In MSCs not supporting linkage of all call activities, any value received shall -- be interpreted as '
 * terminateCallActivityReferred '
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum CallTerminationIndicator {
    terminateCallActivityReferred(0), terminateAllCallActivities(1);

    private int code;

    private CallTerminationIndicator(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static CallTerminationIndicator getInstance(int code) {
        if (code == 0 || code >= 2 && code <= 10)
            return CallTerminationIndicator.terminateCallActivityReferred;
        else
            return CallTerminationIndicator.terminateAllCallActivities;
    }
}
