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

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;

/**
 *
 LocationInformation ::= SEQUENCE { ageOfLocationInformation AgeOfLocationInformation OPTIONAL, geographicalInformation [0]
 * GeographicalInformation OPTIONAL, vlr-number [1] ISDN-AddressString OPTIONAL, locationNumber [2] LocationNumber OPTIONAL,
 * cellGlobalIdOrServiceAreaIdOrLAI [3] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL, extensionContainer [4] ExtensionContainer
 * OPTIONAL, ... , selectedLSA-Id [5] LSAIdentity OPTIONAL, msc-Number [6] ISDN-AddressString OPTIONAL, geodeticInformation [7]
 * GeodeticInformation OPTIONAL, currentLocationRetrieved [8] NULL OPTIONAL, sai-Present [9] NULL OPTIONAL,
 * locationInformationEPS [10] LocationInformationEPS OPTIONAL, userCSGInformation [11] UserCSGInformation OPTIONAL } --
 * sai-Present indicates that the cellGlobalIdOrServiceAreaIdOrLAI parameter contains -- a Service Area Identity. --
 * currentLocationRetrieved shall be present -- if the location information were retrieved after a successfull paging. -- if the
 * locationinformationEPS IE is present then the cellGlobalIdOrServiceAreaIdOrLAI IE, -- the ageOfLocationInformation IE, the
 * geographicalInformation IE, the geodeticInformation IE -- and the currentLocationRetrieved IE (outside the
 * locationInformationEPS IE) shall be -- absent. -- UserCSGInformation contains the CSG ID, Access mode, and the CSG Membership
 * Indication in -- the case the Access mode is Hybrid Mode.
 *
 * AgeOfLocationInformation ::= INTEGER (0..32767) -- the value represents the elapsed time in minutes since the last -- network
 * contact of the mobile station (i.e. the actuality of the -- location information). -- value 0 indicates that the MS is
 * currently in contact with the -- network -- value 32767 indicates that the location information is at least -- 32767 minutes
 * old
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface LocationInformation extends Serializable {

    Integer getAgeOfLocationInformation();

    GeographicalInformation getGeographicalInformation();

    ISDNAddressString getVlrNumber();

    LocationNumberMap getLocationNumber();

    CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI();

    MAPExtensionContainer getExtensionContainer();

    LSAIdentity getSelectedLSAId();

    ISDNAddressString getMscNumber();

    GeodeticInformation getGeodeticInformation();

    boolean getCurrentLocationRetrieved();

    boolean getSaiPresent();

    LocationInformationEPS getLocationInformationEPS();

    UserCSGInformation getUserCSGInformation();

}
