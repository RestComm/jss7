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

package org.mobicents.protocols.ss7.map.api.service.oam;

/**
 *
 LoggingInterval ::= ENUMERATED { d1dot28 (0), d2dot56 (1), d5dot12 (2), d10dot24 (3), d20dot48 (4), d30dot72 (5), d40dot96
 * (6), d61dot44 (7)}
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum LoggingInterval {
    d1dot28(0), d2dot56(1), d5dot12(2), d10dot24(3), d20dot48(4), d30dot72(5), d40dot96(6), d61dot44(7);

    private int code;

    private LoggingInterval(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static LoggingInterval getInstance(int code) {
        switch (code) {
            case 0:
                return LoggingInterval.d1dot28;
            case 1:
                return LoggingInterval.d2dot56;
            case 2:
                return LoggingInterval.d5dot12;
            case 3:
                return LoggingInterval.d10dot24;
            case 4:
                return LoggingInterval.d20dot48;
            case 5:
                return LoggingInterval.d30dot72;
            case 6:
                return LoggingInterval.d40dot96;
            case 7:
                return LoggingInterval.d61dot44;
            default:
                return null;
        }
    }
}
