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
Ext-GeographicalInformation ::= OCTET STRING (SIZE (1..maxExt-GeographicalInformation))
-- Refers to geographical Information defined in 3GPP TS 23.032.
-- This is composed of 1 or more octets with an internal structure according to
-- 3GPP TS 23.032

-- Octet 1: Type of shape, only the following shapes in 3GPP TS 23.032 are allowed:
-- (a) Ellipsoid point with uncertainty circle
-- (b) Ellipsoid point with uncertainty ellipse
-- (c) Ellipsoid point with altitude and uncertainty ellipsoid
-- (d) Ellipsoid Arc
-- (e) Ellipsoid Point
-- Any other value in octet 1 shall be treated as invalid

-- Octets 2 to 8 for case (a) Ellipsoid point with uncertainty circle
-- Degrees of Latitude 3 octets
-- Degrees of Longitude 3 octets
-- Uncertainty code 1 octet

-- Octets 2 to 11 for case (b) Ellipsoid point with uncertainty ellipse:
-- Degrees of Latitude 3 octets
-- Degrees of Longitude 3 octets
-- Uncertainty semi-major axis 1 octet
-- Uncertainty semi-minor axis 1 octet
-- Angle of major axis 1 octet
-- Confidence 1 octet

-- Octets 2 to 14 for case (c) Ellipsoid point with altitude and uncertainty ellipsoid
-- Degrees of Latitude 3 octets
-- Degrees of Longitude 3 octets
-- Altitude 2 octets
-- Uncertainty semi-major axis 1 octet
-- Uncertainty semi-minor axis 1 octet
-- Angle of major axis 1 octet
-- Uncertainty altitude 1 octet
-- Confidence 1 octet

-- Octets 2 to 13 for case (d) Ellipsoid Arc
-- Degrees of Latitude 3 octets
-- Degrees of Longitude 3 octets
-- Inner radius 2 octets
-- Uncertainty radius 1 octet
-- Offset angle 1 octet
-- Included angle 1 octet
-- Confidence 1 octet

-- Octets 2 to 7 for case (e) Ellipsoid Point
-- Degrees of Latitude 3 octets
-- Degrees of Longitude 3 octets
--
-- An Ext-GeographicalInformation parameter comprising more than one octet and
-- containing any other shape or an incorrect number of octets or coding according
-- to 3GPP TS 23.032 shall be treated as invalid data by a receiver.
--
-- An Ext-GeographicalInformation parameter comprising one octet shall be discarded
-- by the receiver if an Add-GeographicalInformation parameter is received
-- in the same message.
--
-- An Ext-GeographicalInformation parameter comprising one octet shall be treated as
-- invalid data by the receiver if an Add-GeographicalInformation parameter is not
-- received in the same message.

maxExt-GeographicalInformation INTEGER ::= 20
-- the maximum length allows for further shapes in 3GPP TS 23.032 to be included in later
-- versions of 3GPP TS 29.002
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ExtGeographicalInformation extends Serializable {

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

}
