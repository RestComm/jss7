/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class NetworkAppearanceImpl extends ParameterImpl implements
        NetworkAppearance {

    private static final long UNSIGNED_INT_MAX_VALUE = 0xFFFFFFFF;
    private long value;

    protected NetworkAppearanceImpl(long value) {
        this.value = value;
        this.tag = Parameter.Network_Appearance;
    }

    protected NetworkAppearanceImpl(byte[] data) {
        this.value = 0;
        this.value |= data[0] & 0xFF;
        this.value <<= 8;
        this.value |= data[1] & 0xFF;
        this.value <<= 8;
        this.value |= data[2] & 0xFF;
        this.value <<= 8;
        this.value |= data[3] & 0xFF;
        this.tag = Parameter.Network_Appearance;
    }

    public long getNetApp() {
        return value;
    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        data[0] = (byte) (value >>> 24);
        data[1] = (byte) (value >>> 16);
        data[2] = (byte) (value >>> 8);
        data[3] = (byte) (value);

        return data;
    }

    @Override
    public String toString() {
        return String.format("NetworkAppearance = %d", value);
    }

}
