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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.io.Serializable;

import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;

/**
<code>
LocationInformationGPRS ::= SEQUENCE {
  cellGlobalIdOrServiceAreaIdOrLAI    [0] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
  routeingAreaIdentity                [1] RAIdentity OPTIONAL,
  geographicalInformation             [2] GeographicalInformation OPTIONAL,
  sgsn-Number                         [3] ISDN-AddressString OPTIONAL,
  selectedLSAIdentity                 [4] LSAIdentity OPTIONAL,
  extensionContainer                  [5] ExtensionContainer OPTIONAL,
  ...,
  sai-Present                         [6] NULL OPTIONAL,
  geodeticInformation                 [7] GeodeticInformation OPTIONAL,
  currentLocationRetrieved            [8] NULL OPTIONAL,
  ageOfLocationInformation            [9] AgeOfLocationInformation OPTIONAL
}
-- sai-Present indicates that the cellGlobalIdOrServiceAreaIdOrLAI parameter contains
-- a Service Area Identity.
-- currentLocationRetrieved shall be present if the location information
-- was retrieved after successful paging.
</code>
 *
 *
 *
 * @author amit bhayani
 *
 */
public interface LocationInformationGPRS extends Serializable {

    CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI();

    RAIdentity getRouteingAreaIdentity();

    GeographicalInformation getGeographicalInformation();

    ISDNAddressString getSGSNNumber();

    LSAIdentity getLSAIdentity();

    MAPExtensionContainer getExtensionContainer();

    boolean isSaiPresent();

    GeodeticInformation getGeodeticInformation();

    boolean isCurrentLocationRetrieved();

    Integer getAgeOfLocationInformation();

}
