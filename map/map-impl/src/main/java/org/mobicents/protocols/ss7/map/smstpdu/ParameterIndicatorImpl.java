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

package org.mobicents.protocols.ss7.map.smstpdu;

import org.mobicents.protocols.ss7.map.api.smstpdu.ParameterIndicator;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ParameterIndicatorImpl implements ParameterIndicator {

    private static int _MASK_TP_UDL = 0x04;
    private static int _MASK_TP_DCS = 0x02;
    private static int _MASK_TP_PID = 0x01;

    private int code;

    public ParameterIndicatorImpl(int code) {
        this.code = code;
    }

    public ParameterIndicatorImpl(boolean TP_UDLPresence, boolean getTP_DCSPresence, boolean getTP_PIDPresence) {
        this.code = (TP_UDLPresence ? _MASK_TP_UDL : 0) + (getTP_DCSPresence ? _MASK_TP_DCS : 0)
                + (getTP_PIDPresence ? _MASK_TP_PID : 0);
    }

    public int getCode() {
        return code;
    }

    public boolean getTP_UDLPresence() {
        if ((this.code & _MASK_TP_UDL) != 0)
            return true;
        else
            return false;
    }

    public boolean getTP_DCSPresence() {
        if ((this.code & _MASK_TP_DCS) != 0)
            return true;
        else
            return false;
    }

    public boolean getTP_PIDPresence() {
        if ((this.code & _MASK_TP_PID) != 0)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("TP-Parameter-Indicator [");

        if (this.getTP_UDLPresence())
            sb.append(" TP_UDL");
        if (this.getTP_DCSPresence())
            sb.append(" TP_DCS");
        if (this.getTP_PIDPresence())
            sb.append(" TP_PID");
        sb.append("]");

        return sb.toString();
    }
}
