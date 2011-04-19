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

import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ServiceIndicatorsImpl extends ParameterImpl implements ServiceIndicators {

    private short[] indicators;
    private byte[] value = null;

    protected ServiceIndicatorsImpl(short[] inds) {
        this.tag = Parameter.Service_Indicators;
        this.indicators = inds;
        this.encode();
    }

    protected ServiceIndicatorsImpl(byte[] value) {
        this.tag = Parameter.Service_Indicators;
        this.indicators = new short[value.length];
        for (int i = 0; i < value.length; i++) {
            this.indicators[i] = value[i];
        }
        this.value = value;
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[indicators.length];
        int count = 0;
        // encode routing context
        while (count < value.length) {
            value[count] = (byte) indicators[count++];
        }
    }

    @Override
    protected byte[] getValue() {
        return this.value;
    }

    public short[] getIndicators() {
        return this.indicators;
    }

    @Override
    public String toString() {
        return String.format("ServiceIndicators ids=%s", Arrays.toString(this.indicators));
    }

}
