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
