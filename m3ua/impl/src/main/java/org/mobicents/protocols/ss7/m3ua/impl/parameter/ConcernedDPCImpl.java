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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.ConcernedDPC;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 *
 */
public class ConcernedDPCImpl extends ParameterImpl implements ConcernedDPC {

    private int pointCode;

    protected ConcernedDPCImpl(int pointCode) {
        this.pointCode = pointCode;
        this.tag = Parameter.Concerned_Destination;
    }

    protected ConcernedDPCImpl(byte[] data) {
        // data[0] is reserved

        this.pointCode = 0;
        this.pointCode |= data[1] & 0xFF;
        this.pointCode <<= 8;
        this.pointCode |= data[2] & 0xFF;
        this.pointCode <<= 8;
        this.pointCode |= data[3] & 0xFF;
        this.tag = Parameter.Concerned_Destination;
    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        // reserved
        data[0] = 0;

        // DPC
        data[1] = (byte) (pointCode >>> 16);
        data[2] = (byte) (pointCode >>> 8);
        data[3] = (byte) (pointCode);

        return data;
    }

    @Override
    public String toString() {
        return String.format("ConcernedDPC dpc=%d", pointCode);
    }

    public int getPointCode() {
        return this.pointCode;
    }

}
