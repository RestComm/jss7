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

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
<code>
ProvideSubscriberLocation-Res ::= SEQUENCE {
  locationEstimate                 Ext-GeographicalInformation,
  ageOfLocationEstimate            [0] AgeOfLocationInformation OPTIONAL,
  extensionContainer               [1] ExtensionContainer OPTIONAL,
  ... ,
  add-LocationEstimate             [2] Add-GeographicalInformation OPTIONAL,
  deferredmt-lrResponseIndicator   [3] NULL OPTIONAL,
  geranPositioningData             [4] PositioningDataInformation OPTIONAL,
  utranPositioningData             [5] UtranPositioningDataInfo OPTIONAL,
  cellIdOrSai                      [6] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
  sai-Present                      [7] NULL OPTIONAL,
  accuracyFulfilmentIndicator      [8] AccuracyFulfilmentIndicator OPTIONAL,
  velocityEstimate                 [9] VelocityEstimate OPTIONAL,
  mo-lrShortCircuitIndicator       [10] NULL OPTIONAL,
  geranGANSSpositioningData        [11] GeranGANSSpositioningData OPTIONAL,
  utranGANSSpositioningData        [12] UtranGANSSpositioningData OPTIONAL,
  targetServingNodeForHandover     [13] ServingNodeAddress OPTIONAL
}

-- if deferredmt-lrResponseIndicator is set, locationEstimate is ignored.

-- the add-LocationEstimate parameter shall not be sent to a node that did not indicate the
-- geographic shapes supported in the ProvideSubscriberLocation-Arg
-- The locationEstimate and the add-locationEstimate parameters shall not be sent if
-- the supportedGADShapes parameter has been received in ProvideSubscriberLocation-Arg
-- and the shape encoded in locationEstimate or add-LocationEstimate is not marked
-- as supported in supportedGADShapes. In such a case ProvideSubscriberLocation
-- shall be rejected with error FacilityNotSupported with additional indication
-- shapeOfLocationEstimateNotSupported.
-- sai-Present indicates that the cellIdOrSai parameter contains a Service Area Identity.

AgeOfLocationInformation ::= INTEGER (0..32767)
-- the value represents the elapsed time in minutes since the last
--network contact of the mobile station (i.e. the actuality of the
-- location information).
-- value "0" indicates that the MS is currently in contact with the
-- network
-- value "32767" indicates that the location information is at least
-- 32767 minutes old

</code>
 *
 *
 * @author amit bhayani
 *
 */
public interface ProvideSubscriberLocationResponse extends LsmMessage {

    ExtGeographicalInformation getLocationEstimate();

    PositioningDataInformation getGeranPositioningData();

    UtranPositioningDataInfo getUtranPositioningData();

    Integer getAgeOfLocationEstimate();

    AddGeographicalInformation getAdditionalLocationEstimate();

    MAPExtensionContainer getExtensionContainer();

    boolean getDeferredMTLRResponseIndicator();

    CellGlobalIdOrServiceAreaIdOrLAI getCellIdOrSai();

    boolean getSaiPresent();

    AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator();

    VelocityEstimate getVelocityEstimate();

    boolean getMoLrShortCircuitIndicator();

    GeranGANSSpositioningData getGeranGANSSpositioningData();

    UtranGANSSpositioningData getUtranGANSSpositioningData();

    ServingNodeAddress getTargetServingNodeForHandover();

}
