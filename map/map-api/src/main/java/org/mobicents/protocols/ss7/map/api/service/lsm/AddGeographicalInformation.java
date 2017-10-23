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

package org.mobicents.protocols.ss7.map.api.service.lsm;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;

/**
 *
<code>
Add-GeographicalInformation ::= OCTET STRING (SIZE (1..91))
-- Refers to geographical Information defined in 3GPP TS 23.032.
-- This is composed of 1 or more octets with an internal structure according to
-- 3GPP TS 23.032
-- Octet 1: Type of shape, all the shapes defined in 3GPP TS 23.032 are allowed:
-- Octets 2 to n (where n is the total number of octets necessary to encode the shape
-- according to 3GPP TS 23.032) are used to encode the shape itself in accordance with the
-- encoding defined in 3GPP TS 23.032
--
-- An Add-GeographicalInformation parameter, whether valid or invalid, received
-- together with a valid Ext-GeographicalInformation parameter in the same message
-- shall be discarded.
--
-- An Add-GeographicalInformation parameter containing any shape not defined in
-- 3GPP TS 23.032 or an incorrect number of octets or coding according to
-- 3GPP TS 23.032 shall be treated as invalid data by a receiver if not received
-- together with a valid Ext-GeographicalInformation parameter in the same message.

maxAdd-GeographicalInformation INTEGER ::= 91
-- the maximum length allows support for all the shapes currently defined in 3GPP TS 23.032
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AddGeographicalInformation extends Serializable {

    byte[] getData();

    TypeOfShape getTypeOfShape();

    /**
     * @return Latitude value in degrees (-90 ... 90)
     */
    double getLatitude();

    /**
     * @return Longitude value in degrees (-180 ... 180)
     */
    double getLongitude();

    /**
     * @return Uncertainty value in meters
     */
    double getUncertainty();

    /**
     * @return Uncertainty value in meters
     */
    double getUncertaintySemiMajorAxis();

    /**
     * @return Uncertainty value in meters
     */
    double getUncertaintySemiMinorAxis();

    /**
     * @return Angle value in degrees
     */
    double getAngleOfMajorAxis();

    int getConfidence();

    /**
     * @return Altitude value in meters
     */
    int getAltitude();

    /**
     * @return Uncertainty value in meters
     */
    double getUncertaintyAltitude();

    /**
     * @return Radius value in meters
     */
    int getInnerRadius();

    /**
     * @return Uncertainty value in meters
     */
    double getUncertaintyRadius();

    /**
     * @return Angle value in degrees
     */
    double getOffsetAngle();

    /**
     * @return Angle value in degrees
     */
    double getIncludedAngle();

    // TODO: add processing missed: TypeOfShape.Polygon, TypeOfShape.EllipsoidPointWithAltitude

}
