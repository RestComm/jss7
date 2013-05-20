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
