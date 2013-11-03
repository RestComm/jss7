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
 * Start time:12:17:32 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:17:32 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CircuitAssigmentMap extends ISUPParameter {
    int _PARAMETER_CODE = 0x25;

    /**
     * See Q.763 3.69 Map type : 1544 kbit/s digital path map format (64 kbit/s base rate)
     */
    int _MAP_TYPE_1544 = 1;

    /**
     * See Q.763 3.69 Map type : 2048 kbit/s digital path map format (64 kbit/s base rate)
     */
    int _MAP_TYPE_2048 = 2;

    int getMapType();

    void setMapType(int mapType);

    int getMapFormat();

    void setMapFormat(int mapFormat);

    /**
     * Enables circuit
     *
     * @param circuitNumber - index of circuit - must be number <1,31>
     * @throws IllegalArgumentException - when number is not in range
     */
    void enableCircuit(int circuitNumber) throws IllegalArgumentException;

    /**
     * Disables circuit
     *
     * @param circuitNumber - index of circuit - must be number <1,31>
     * @throws IllegalArgumentException - when number is not in range
     */
    void disableCircuit(int circuitNumber) throws IllegalArgumentException;

    boolean isCircuitEnabled(int circuitNumber) throws IllegalArgumentException;

}
