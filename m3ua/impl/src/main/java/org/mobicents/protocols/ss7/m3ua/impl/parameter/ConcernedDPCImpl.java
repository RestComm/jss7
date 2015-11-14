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
