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

import java.util.Arrays;

import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AffectedPointCodeImpl extends ParameterImpl implements
        AffectedPointCode {

    private byte[] value;
    private int[] pointCodes;
    private short[] masks;

    protected AffectedPointCodeImpl(byte[] value) {
        this.tag = Parameter.Affected_Point_Code;

        int count = 0;
        int arrSize = 0;
        pointCodes = new int[(value.length / 4)];
        masks = new short[(value.length / 4)];

        while (count < value.length) {
            masks[arrSize] = value[count++];

            pointCodes[arrSize] = 0;
            pointCodes[arrSize] |= value[count++] & 0xFF;
            pointCodes[arrSize] <<= 8;
            pointCodes[arrSize] |= value[count++] & 0xFF;
            pointCodes[arrSize] <<= 8;
            pointCodes[arrSize++] |= value[count++] & 0xFF;
        }
        this.value = value;
    }

    protected AffectedPointCodeImpl(int[] pointCodes, short[] masks) {
        this.tag = Parameter.Affected_Point_Code;
        this.pointCodes = pointCodes;
        this.masks = masks;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;

        this.value = new byte[(pointCodes.length * 4)];

        int count = 0;
        int arrSize = 0;
        // encode routing context
        while (count < value.length) {
            value[count++] = (byte) (masks[arrSize]);

            value[count++] = (byte) (pointCodes[arrSize] >>> 16);
            value[count++] = (byte) (pointCodes[arrSize] >>> 8);
            value[count++] = (byte) (pointCodes[arrSize++]);
        }
    }

    @Override
    protected byte[] getValue() {
        return this.value;
    }

    public short[] getMasks() {
        return this.masks;
    }

    public int[] getPointCodes() {
        return this.pointCodes;
    }

    @Override
    public String toString() {
        return String
                .format("AffectedPointCode pointCode=%s mask=%s",
                        Arrays.toString(this.pointCodes), Arrays
                                .toString(this.masks));
    }

}
