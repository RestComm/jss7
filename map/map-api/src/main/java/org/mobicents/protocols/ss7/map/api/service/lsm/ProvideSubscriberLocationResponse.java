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

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * ProvideSubscriberLocation-Res ::= SEQUENCE { locationEstimate Ext-GeographicalInformation, ageOfLocationEstimate [0]
 * AgeOfLocationInformation OPTIONAL, extensionContainer [1] ExtensionContainer OPTIONAL, ... , add-LocationEstimate [2]
 * Add-GeographicalInformation OPTIONAL, deferredmt-lrResponseIndicator [3] NULL OPTIONAL, geranPositioningData [4]
 * PositioningDataInformation OPTIONAL, utranPositioningData [5] UtranPositioningDataInfo OPTIONAL, cellIdOrSai [6]
 * CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL, sai-Present [7] NULL OPTIONAL, accuracyFulfilmentIndicator [8]
 * AccuracyFulfilmentIndicator OPTIONAL, velocityEstimate [9] VelocityEstimate OPTIONAL, mo-lrShortCircuitIndicator [10] NULL
 * OPTIONAL, geranGANSSpositioningData [11] GeranGANSSpositioningData OPTIONAL, utranGANSSpositioningData [12]
 * UtranGANSSpositioningData OPTIONAL, targetServingNodeForHandover [13] ServingNodeAddress OPTIONAL }
 *
 * -- if deferredmt-lrResponseIndicator is set, locationEstimate is ignored.
 *
 * -- the add-LocationEstimate parameter shall not be sent to a node that did not indicate the -- geographic shapes supported in
 * the ProvideSubscriberLocation-Arg -- The locationEstimate and the add-locationEstimate parameters shall not be sent if -- the
 * supportedGADShapes parameter has been received in ProvideSubscriberLocation-Arg -- and the shape encoded in locationEstimate
 * or add-LocationEstimate is not marked -- as supported in supportedGADShapes. In such a case ProvideSubscriberLocation --
 * shall be rejected with error FacilityNotSupported with additional indication -- shapeOfLocationEstimateNotSupported. --
 * sai-Present indicates that the cellIdOrSai parameter contains a Service Area Identity.
 *
 *
 * @author amit bhayani
 *
 */
public interface ProvideSubscriberLocationResponse extends LsmMessage {

    /**
     * Ext-GeographicalInformation ::= OCTET STRING (SIZE (1..maxExt-GeographicalInformation)) -- Refers to geographical
     * Information defined in 3GPP TS 23.032. -- This is composed of 1 or more octets with an internal structure according to --
     * 3GPP TS 23.032 -- Octet 1: Type of shape, only the following shapes in 3GPP TS 23.032 are allowed: -- (a) Ellipsoid point
     * with uncertainty circle -- (b) Ellipsoid point with uncertainty ellipse -- (c) Ellipsoid point with altitude and
     * uncertainty ellipsoid -- (d) Ellipsoid Arc -- (e) Ellipsoid Point -- Any other value in octet 1 shall be treated as
     * invalid -- Octets 2 to 8 for case (a) – Ellipsoid point with uncertainty circle -- Degrees of Latitude 3 octets --
     * Degrees of Longitude 3 octets -- Uncertainty code 1 octet -- Octets 2 to 11 for case (b) – Ellipsoid point with
     * uncertainty ellipse: -- Degrees of Latitude 3 octets -- Degrees of Longitude 3 octets -- Uncertainty semi-major axis 1
     * octet -- Uncertainty semi-minor axis 1 octet -- Angle of major axis 1 octet -- Confidence 1 octet -- Octets 2 to 14 for
     * case (c) – Ellipsoid point with altitude and uncertainty ellipsoid -- Degrees of Latitude 3 octets -- Degrees of
     * Longitude 3 octets -- Altitude 2 octets -- Uncertainty semi-major axis 1 octet -- Uncertainty semi-minor axis 1 octet --
     * Angle of major axis 1 octet -- Uncertainty altitude 1 octet -- Confidence 1 octet -- Octets 2 to 13 for case (d) –
     * Ellipsoid Arc -- Degrees of Latitude 3 octets -- Degrees of Longitude 3 octets -- Inner radius 2 octets -- Uncertainty
     * radius 1 octet -- Offset angle 1 octet -- Included angle 1 octet -- Confidence 1 octet -- Octets 2 to 7 for case (e) –
     * Ellipsoid Point -- Degrees of Latitude 3 octets -- Degrees of Longitude 3 octets -- -- An Ext-GeographicalInformation
     * parameter comprising more than one octet and -- containing any other shape or an incorrect number of octets or coding
     * according -- to 3GPP TS 23.032 shall be treated as invalid data by a receiver. -- -- An Ext-GeographicalInformation
     * parameter comprising one octet shall be discarded -- by the receiver if an Add-GeographicalInformation parameter is
     * received -- in the same message. -- -- An Ext-GeographicalInformation parameter comprising one octet shall be treated as
     * -- invalid data by the receiver if an Add-GeographicalInformation parameter is not -- received in the same message.
     *
     *
     * maxExt-GeographicalInformation INTEGER ::= 20 -- the maximum length allows for further shapes in 3GPP TS 23.032 to be
     * included in later -- versions of 3GPP TS 29.002
     *
     * @return
     */
    ExtGeographicalInformation getLocationEstimate();

    /**
     * PositioningDataInformation ::= OCTET STRING (SIZE (2..maxPositioningDataInformation)) -- Refers to the Positioning Data
     * defined in 3GPP TS 49.031. -- This is composed of 2 or more octets with an internal structure according to -- 3GPP TS
     * 49.031.
     *
     * maxPositioningDataInformation INTEGER ::= 10
     *
     * @return
     */
    PositioningDataInformation getGeranPositioningData();

    /**
     * UtranPositioningDataInfo ::= OCTET STRING (SIZE (3..maxUtranPositioningDataInfo)) -- Refers to the Position Data defined
     * in 3GPP TS 25.413. -- This is composed of the positioningDataDiscriminator and the positioningDataSet -- included in
     * positionData as defined in 3GPP TS 25.413.
     *
     * maxUtranPositioningDataInfo INTEGER ::= 11
     *
     * @return
     */
    UtranPositioningDataInfo getUtranPositioningData();

    /**
     * AgeOfLocationInformation ::= INTEGER (0..32767) -- the value represents the elapsed time in minutes since the last --
     * network contact of the mobile station (i.e. the actuality of the -- location information). -- value “0” indicates that
     * the MS is currently in contact with the -- network -- value “32767” indicates that the location information is at least
     * -- 32767 minutes old
     *
     * @return
     */
    Integer getAgeOfLocationEstimate();

    /**
     * Add-GeographicalInformation ::= OCTET STRING (SIZE (1..maxAdd-GeographicalInformation)) -- Refers to geographical
     * Information defined in 3GPP TS 23.032. -- This is composed of 1 or more octets with an internal structure according to --
     * 3GPP TS 23.032 -- Octet 1: Type of shape, all the shapes defined in 3GPP TS 23.032 are allowed: -- Octets 2 to n (where n
     * is the total number of octets necessary to encode the shape -- according to 3GPP TS 23.032) are used to encode the shape
     * itself in accordance with the -- encoding defined in 3GPP TS 23.032 -- -- An Add-GeographicalInformation parameter,
     * whether valid or invalid, received -- together with a valid Ext-GeographicalInformation parameter in the same message --
     * shall be discarded. -- -- An Add-GeographicalInformation parameter containing any shape not defined in -- 3GPP TS 23.032
     * or an incorrect number of octets or coding according to -- 3GPP TS 23.032 shall be treated as invalid data by a receiver
     * if not received -- together with a valid Ext-GeographicalInformation parameter in the same message.
     *
     *
     * maxAdd-GeographicalInformation INTEGER ::= 91 -- the maximum length allows support for all the shapes currently defined
     * in 3GPP TS 23.032
     *
     * @return
     */
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
