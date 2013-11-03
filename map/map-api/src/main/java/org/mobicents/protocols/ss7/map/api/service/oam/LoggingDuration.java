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

package org.mobicents.protocols.ss7.map.api.service.oam;

/**
 *
 LoggingDuration ::= ENUMERATED { d600sec (0), d1200sec (1), d2400sec (2), d3600sec (3), d5400sec (4), d7200sec (5)}
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum LoggingDuration {
    d600sec(0), d1200sec(1), d2400sec(2), d3600sec(3), d5400sec(4), d7200sec(5);

    private int code;

    private LoggingDuration(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static LoggingDuration getInstance(int code) {
        switch (code) {
            case 0:
                return LoggingDuration.d600sec;
            case 1:
                return LoggingDuration.d1200sec;
            case 2:
                return LoggingDuration.d2400sec;
            case 3:
                return LoggingDuration.d3600sec;
            case 4:
                return LoggingDuration.d5400sec;
            case 5:
                return LoggingDuration.d7200sec;
            default:
                return null;
        }
    }
}
