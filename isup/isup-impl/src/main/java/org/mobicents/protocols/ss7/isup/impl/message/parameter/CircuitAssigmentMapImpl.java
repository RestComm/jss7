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

/**
 * Start time:12:20:07 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitAssigmentMap;

/**
 * Start time:12:20:07 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CircuitAssigmentMapImpl extends AbstractISUPParameter implements CircuitAssigmentMap {

    private static final int _CIRCUIT_ENABLED = 0x01;

    private int mapType = 0;

    private int mapFormat = 0;

    public CircuitAssigmentMapImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public CircuitAssigmentMapImpl(int mapType, int mapFormat) {
        super();
        this.mapType = mapType;
        this.mapFormat = mapFormat;
    }

    public CircuitAssigmentMapImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 5) {
            throw new ParameterException("byte[] must  not be null and length must  be 5");
        }

        this.mapType = b[0] & 0x3F;
        this.mapFormat = b[1];
        this.mapFormat |= b[2] << 8;
        this.mapFormat |= b[3] << 16;
        this.mapFormat |= (b[4] & 0x7F) << 24;

        return 5;
    }

    public byte[] encode() throws ParameterException {

        byte[] b = new byte[5];
        b[0] = (byte) (this.mapType & 0x3F);
        b[1] = (byte) this.mapFormat;
        b[2] = (byte) (this.mapFormat >> 8);
        b[3] = (byte) (this.mapFormat >> 16);
        b[4] = (byte) ((this.mapFormat >> 24) & 0x7F);
        return b;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public int getMapFormat() {
        return mapFormat;
    }

    public void setMapFormat(int mapFormat) {
        this.mapFormat = mapFormat;
    }

    /**
     * Enables circuit
     *
     * @param circuitNumber - index of circuit - must be number <1,31>
     * @throws IllegalArgumentException - when number is not in range
     */
    public void enableCircuit(int circuitNumber) throws IllegalArgumentException {
        if (circuitNumber < 1 || circuitNumber > 31) {
            throw new IllegalArgumentException("Cicruit number is out of range[" + circuitNumber + "] <1,31>");
        }

        this.mapFormat |= _CIRCUIT_ENABLED << (circuitNumber - 1);
    }

    /**
     * Disables circuit
     *
     * @param circuitNumber - index of circuit - must be number <1,31>
     * @throws IllegalArgumentException - when number is not in range
     */
    public void disableCircuit(int circuitNumber) throws IllegalArgumentException {
        if (circuitNumber < 1 || circuitNumber > 31) {
            throw new IllegalArgumentException("Cicruit number is out of range[" + circuitNumber + "] <1,31>");
        }
        this.mapFormat &= 0xFFFFFFFE << (circuitNumber - 1);
    }

    public boolean isCircuitEnabled(int circuitNumber) throws IllegalArgumentException {
        if (circuitNumber < 1 || circuitNumber > 31) {
            throw new IllegalArgumentException("Cicruit number is out of range[" + circuitNumber + "] <1,31>");
        }

        return ((this.mapFormat >> (circuitNumber - 1)) & 0x01) == _CIRCUIT_ENABLED;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
