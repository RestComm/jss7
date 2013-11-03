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

package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.Serializable;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;

/**
 *
 * @author kulikov
 */
public interface ParameterFactory extends Serializable {

    /**
     * Create SccpAddress parameter
     *
     * @param ri
     * @param dpc
     * @param gt
     * @param ssn
     * @return
     */
    SccpAddress createSccpAddress(RoutingIndicator ri, int dpc, GlobalTitle gt, int ssn);

    /**
     * Create Importance parameter.
     *
     * @param value the value of this parameter
     * @return parameter
     */
    Importance createImportance(int value);

    /**
     * Create hop counter parameter
     */
    HopCounter createHopCounter(int hopCount);

    // ReturnCause createReturnCause(int cause);
    // /**
    // * Creates protocol class parameter.
    // *
    // * @param value the value of the parameter
    // * @return parameter
    // */
    // ProtocolClass createProtocolClass(int pClass, boolean returnMessageOnError);
    // /**
    // * Create segemntation parameter
    // *
    // * @return parameter.
    // */
    // Segmentation createSegmentation();
}
