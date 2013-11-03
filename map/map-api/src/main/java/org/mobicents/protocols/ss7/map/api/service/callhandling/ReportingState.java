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
 ReportingState ::= ENUMERATED { stopMonitoring (0), startMonitoring (1), ...} -- exception handling: -- reception of values
 * 2-10 shall be mapped to 'stopMonitoring' -- reception of values > 10 shall be mapped to 'startMonitoring'
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum ReportingState {
    stopMonitoring(0), startMonitoring(1);

    private int code;

    private ReportingState(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ReportingState getInstance(int code) {
        if (code == 0 || code >= 2 && code <= 10)
            return ReportingState.stopMonitoring;
        else
            return ReportingState.startMonitoring;
    }
}
