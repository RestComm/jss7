/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.inap.api.primitives;

/**
 *
 <code>
MonitorMode ::= ENUMERATED {
  interrupted (0),
  notifyAndContinue (1),
  transparent (2)
}
-- Indicates the event is relayed and/or processed by the SSP.
-- If this parameter is used in the context of charging events,
-- the following definitions apply for the handling of charging events:
-- Interrupted means that the SSF notifies the SCF of the charging event using
-- EventNotificationCharging, does not process the event but discard it.
-- NotifyAndContinue means that SSF notifies the SCF of the charging event using
-- EventNotificationCharging, and continues processing the event or signal without
-- waiting for SCF instructions.
-- Transparent means that the SSF does not notify the SCF of the event.
-- This value is used to end the monitoring of a previously requested charging event.
-- Previously requested charging events are monitored
-- until ended by a transparent monitor mode, or until the end of the connection configuration.
-- For the use of this parameter in the context of BCSM events refer to clause 18.
</code>
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
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }

}
