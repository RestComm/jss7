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

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;

/**
 *
 Add-GeographicalInformation ::= OCTET STRING (SIZE (1..91)) -- Refers to geographical Information defined in 3GPP TS 23.032.
 * -- This is composed of 1 or more octets with an internal structure according to -- 3GPP TS 23.032 -- Octet 1: Type of shape,
 * all the shapes defined in 3GPP TS 23.032 are allowed: -- Octets 2 to n (where n is the total number of octets necessary to
 * encode the shape -- according to 3GPP TS 23.032) are used to encode the shape itself in accordance with the -- encoding
 * defined in 3GPP TS 23.032 -- -- An Add-GeographicalInformation parameter, whether valid or invalid, received -- together with
 * a valid Ext-GeographicalInformation parameter in the same message -- shall be discarded. -- -- An Add-GeographicalInformation
 * parameter containing any shape not defined in -- 3GPP TS 23.032 or an incorrect number of octets or coding according to --
 * 3GPP TS 23.032 shall be treated as invalid data by a receiver if not received -- together with a valid
 * Ext-GeographicalInformation parameter in the same message.
 *
 * maxAdd-GeographicalInformation INTEGER ::= 91 -- the maximum length allows support for all the shapes currently defined in
 * 3GPP TS 23.032
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
