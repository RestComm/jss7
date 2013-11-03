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
 MonitorMode ::= ENUMERATED { interrupted (0), notifyAndContinue (1), transparent (2) } -- Indicates the event is relayed
 * and/or processed by the SSP. -- Transparent means that the gsmSSF or gprsSSF does not notify the gsmSCF of the event. -- For
 * the use of this parameter refer to the procedure descriptions in clause 11. -- For the RequestNotificationCharging operation,
 * 'interrupted' shall not be used in MonitorMode.
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum MonitorMode {

    interrupted(0), notifyAndContinue(1), transparent(2);

    private int code;

    private MonitorMode(int code) {
        this.code = code;
    }

    public static MonitorMode getInstance(int code) {
        switch (code) {
            case 0:
                return MonitorMode.interrupted;
            case 1:
                return MonitorMode.notifyAndContinue;
            case 2:
                return MonitorMode.transparent;
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
