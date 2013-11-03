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

package org.mobicents.protocols.ss7.map.api.service.lsm;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 * LCS-QoS ::= SEQUENCE { horizontal-accuracy [0] Horizontal-Accuracy OPTIONAL, verticalCoordinateRequest [1] NULL OPTIONAL,
 * vertical-accuracy [2] Vertical-Accuracy OPTIONAL, responseTime [3] ResponseTime OPTIONAL, extensionContainer [4]
 * ExtensionContainer OPTIONAL, ...}
 *
 * @author amit bhayani
 *
 */
public interface LCSQoS extends Serializable {

    /**
     * Horizontal-Accuracy ::= OCTET STRING (SIZE (1)) -- bit 8 = 0 -- bits 7-1 = 7 bit Uncertainty Code defined in 3GPP TS
     * 23.032. The horizontal location -- error should be less than the error indicated by the uncertainty code with 67% --
     * confidence.
     *
     * @return
     */
    Integer getHorizontalAccuracy();

    /**
     * NULL
     *
     * @return
     */
    boolean getVerticalCoordinateRequest();

    /**
     * Vertical-Accuracy ::= OCTET STRING (SIZE (1)) -- bit 8 = 0 -- bits 7-1 = 7 bit Vertical Uncertainty Code defined in 3GPP
     * TS 23.032. -- The vertical location error should be less than the error indicated -- by the uncertainty code with 67%
     * confidence.
     *
     * @return
     */
    Integer getVerticalAccuracy();

    ResponseTime getResponseTime();

    MAPExtensionContainer getExtensionContainer();
}
