/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
