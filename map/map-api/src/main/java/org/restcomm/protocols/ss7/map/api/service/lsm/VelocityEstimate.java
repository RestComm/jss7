/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import java.io.Serializable;

/**
 *
 VelocityEstimate ::= OCTET STRING (SIZE (4..7)) -- Refers to Velocity description defined in 3GPP TS 23.032. -- This is
 * composed of 4 or more octets with an internal structure according to -- 3GPP TS 23.032 -- Octet 1: Type of velocity, only the
 * following types in 3GPP TS 23.032 are allowed: -- (a) Horizontal Velocity -- (b) Horizontal with Vertical Velocity -- (c)
 * Horizontal Velocity with Uncertainty -- (d) Horizontal with Vertical Velocity and Uncertainty -- For types Horizontal with
 * Vertical Velocity and Horizontal with Vertical Velocity -- and Uncertainty, the direction of the Vertical Speed is also
 * included in Octet 1 -- Any other value in octet 1 shall be treated as invalid -- Octets 2 to 4 for case (a) Horizontal
 * velocity: -- Bearing 1 octet -- Horizontal Speed 2 octets -- Octets 2 to 5 for case (b) Horizontal with Vertical Velocity: --
 * Bearing 1 octet -- Horizontal Speed 2 octets -- Vertical Speed 1 octet -- Octets 2 to 5 for case (c) Horizontal velocity with
 * Uncertainty: -- Bearing 1 octet -- Horizontal Speed 2 octets -- Uncertainty Speed 1 octet -- Octets 2 to 7 for case (d)
 * Horizontal with Vertical Velocity and Uncertainty: -- Bearing 1 octet -- Horizontal Speed 2 octets -- Vertical Speed 1 octet
 * -- Horizontal Uncertainty Speed 1 octet -- Vertical Uncertainty Speed 1 octet
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface VelocityEstimate extends Serializable {

    byte[] getData();

    VelocityType getVelocityType();

    /**
     * @return speed in 1 kilometer per hour
     */
    int getHorizontalSpeed();

    /**
     * @return speed in degrees measured clockwise from North
     */
    int getBearing();

    /**
     * @return speed in 1 kilometer per hour. Positive value means upward speed, negative means downward speed
     */
    int getVerticalSpeed();

    /**
     * @return Uncertainty speed in 1 kilometer per hour
     */
    int getUncertaintyHorizontalSpeed();

    /**
     * @return Uncertainty speed in 1 kilometer per hour
     */
    int getUncertaintyVerticalSpeed();

}
