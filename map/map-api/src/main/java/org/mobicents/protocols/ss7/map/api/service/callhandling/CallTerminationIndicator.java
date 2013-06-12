/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
