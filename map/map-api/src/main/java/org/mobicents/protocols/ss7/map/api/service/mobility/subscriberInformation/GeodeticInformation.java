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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.io.Serializable;

/**
 *
 GeodeticInformation ::= OCTET STRING (SIZE (10)) -- Refers to Calling Geodetic Location defined in Q.763 (1999). -- Only the
 * description of an ellipsoid point with uncertainty circle -- as specified in Q.763 (1999) is allowed to be used -- The
 * internal structure according to Q.763 (1999) is as follows: -- Screening and presentation indicators 1 octet -- Type of shape
 * (ellipsoid point with uncertainty circle) 1 octet -- Degrees of Latitude 3 octets -- Degrees of Longitude 3 octets --
 * Uncertainty code 1 octet -- Confidence 1 octet
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GeodeticInformation extends Serializable {

    byte[] getData();

    int getScreeningAndPresentationIndicators();

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

    int getConfidence();

}
