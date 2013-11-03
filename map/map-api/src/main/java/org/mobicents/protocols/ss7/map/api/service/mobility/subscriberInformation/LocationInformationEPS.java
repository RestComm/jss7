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

import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 * LocationInformationEPS ::= SEQUENCE { e-utranCellGlobalIdentity [0] E-UTRAN-CGI OPTIONAL, trackingAreaIdentity [1] TA-Id
 * OPTIONAL, extensionContainer [2] ExtensionContainer OPTIONAL, geographicalInformation [3] GeographicalInformation OPTIONAL,
 * geodeticInformation [4] GeodeticInformation OPTIONAL, currentLocationRetrieved [5] NULL OPTIONAL, ageOfLocationInformation
 * [6] AgeOfLocationInformation OPTIONAL, ..., mme-Name [7] DiameterIdentity OPTIONAL } -- currentLocationRetrieved shall be
 * present if the location information -- was retrieved after successful paging.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface LocationInformationEPS extends Serializable {

    EUtranCgi getEUtranCellGlobalIdentity();

    TAId getTrackingAreaIdentity();

    MAPExtensionContainer getExtensionContainer();

    GeographicalInformation getGeographicalInformation();

    GeodeticInformation getGeodeticInformation();

    boolean getCurrentLocationRetrieved();

    Integer getAgeOfLocationInformation();

    DiameterIdentity getMmeName();

}
