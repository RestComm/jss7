/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;

/**
 *
 MAP V3:
 *
 * subscriberLocationReport OPERATION ::= { --Timer m ARGUMENT SubscriberLocationReport-Arg RESULT SubscriberLocationReport-Res
 * ERRORS { systemFailure | dataMissing | resourceLimitation | unexpectedDataValue | unknownSubscriber |
 * unauthorizedRequestingNetwork | unknownOrUnreachableLCSClient} CODE local:86 }
 *
 * SubscriberLocationReport-Arg ::= SEQUENCE { lcs-Event LCS-Event, lcs-ClientID LCS-ClientID, lcsLocationInfo LCSLocationInfo,
 * msisdn [0] ISDN-AddressString OPTIONAL, imsi [1] IMSI OPTIONAL, imei [2] IMEI OPTIONAL, na-ESRD [3] ISDN-AddressString
 * OPTIONAL, na-ESRK [4] ISDN-AddressString OPTIONAL, locationEstimate [5] Ext-GeographicalInformation OPTIONAL,
 * ageOfLocationEstimate [6] AgeOfLocationInformation OPTIONAL, slr-ArgExtensionContainer [7] SLR-ArgExtensionContainer
 * OPTIONAL, ... , add-LocationEstimate [8] Add-GeographicalInformation OPTIONAL, deferredmt-lrData [9] Deferredmt-lrData
 * OPTIONAL, lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL, geranPositioningData [11] PositioningDataInformation
 * OPTIONAL, utranPositioningData [12] UtranPositioningDataInfo OPTIONAL, cellIdOrSai [13] CellGlobalIdOrServiceAreaIdOrLAI
 * OPTIONAL, h-gmlc-Address [14] GSN-Address OPTIONAL, lcsServiceTypeID [15] LCSServiceTypeID OPTIONAL, sai-Present [17] NULL
 * OPTIONAL, pseudonymIndicator [18] NULL OPTIONAL, accuracyFulfilmentIndicator [19] AccuracyFulfilmentIndicator OPTIONAL,
 * velocityEstimate [20] VelocityEstimate OPTIONAL, sequenceNumber [21] SequenceNumber OPTIONAL, periodicLDRInfo [22]
 * PeriodicLDRInfo OPTIONAL, mo-lrShortCircuitIndicator [23] NULL OPTIONAL, geranGANSSpositioningData [24]
 * GeranGANSSpositioningData OPTIONAL, utranGANSSpositioningData [25] UtranGANSSpositioningData OPTIONAL,
 * targetServingNodeForHandover [26] ServingNodeAddress OPTIONAL }
 *
 * -- one of msisdn or imsi is mandatory -- a location estimate that is valid for the locationEstimate parameter should -- be
 * transferred in this parameter in preference to the add-LocationEstimate. -- the deferredmt-lrData parameter shall be included
 * if and only if the lcs-Event -- indicates a deferredmt-lrResponse. -- if the lcs-Event indicates a deferredmt-lrResponse then
 * the locationEstimate -- and the add-locationEstimate parameters shall not be sent if the -- supportedGADShapes parameter had
 * been received in ProvideSubscriberLocation-Arg -- and the shape encoded in locationEstimate or add-LocationEstimate was not
 * marked -- as supported in supportedGADShapes. In such a case terminationCause -- in deferredmt-lrData shall be present with
 * value -- shapeOfLocationEstimateNotSupported. -- If a lcs event indicates deferred mt-lr response, the lcs-Reference number
 * shall be -- included. -- sai-Present indicates that the cellIdOrSai parameter contains a Service Area Identity.
 *
 * SequenceNumber ::= INTEGER (1..8639999)
 *
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface SubscriberLocationReportRequest extends LsmMessage {

    LCSEvent getLCSEvent();

    LCSClientID getLCSClientID();

    LCSLocationInfo getLCSLocationInfo();

    ISDNAddressString getMSISDN();

    IMSI getIMSI();

    IMEI getIMEI();

    ISDNAddressString getNaESRD();

    ISDNAddressString getNaESRK();

    ExtGeographicalInformation getLocationEstimate();

    /**
     * AgeOfLocationInformation ::= INTEGER (0..32767) -- the value represents the elapsed time in minutes since the last --
     * network contact of the mobile station (i.e. the actuality of the -- location information). -- value "0" indicates that
     * the MS is currently in contact with the -- network -- value "32767" indicates that the location information is at least
     * -- 32767 minutes old
     *
     * @return
     */
    Integer getAgeOfLocationEstimate();

    SLRArgExtensionContainer getSLRArgExtensionContainer();

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

    DeferredmtlrData getDeferredmtlrData();

    /**
     * LCS-ReferenceNumber::= OCTET STRING (SIZE(1))
     *
     * @return
     */
    Integer getLCSReferenceNumber();

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

    CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI();

    /**
     * GSN-Address ::= OCTET STRING (SIZE (5..17)) -- Octets are coded according to TS 3GPP TS 23.003 [17]
     *
     * @return
     */
    GSNAddress getHGMLCAddress();

    /**
     * LCSServiceTypeID ::= INTEGER (0..127) -- the integer values 0-63 are reserved for Standard LCS service types -- the
     * integer values 64-127 are reserved for Non Standard LCS service types
     *
     * @return
     */
    Integer getLCSServiceTypeID();

    boolean getSaiPresent();

    boolean getPseudonymIndicator();

    AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator();

    VelocityEstimate getVelocityEstimate();

    Integer getSequenceNumber();

    PeriodicLDRInfo getPeriodicLDRInfo();

    boolean getMoLrShortCircuitIndicator();

    GeranGANSSpositioningData getGeranGANSSpositioningData();

    UtranGANSSpositioningData getUtranGANSSpositioningData();

    ServingNodeAddress getTargetServingNodeForHandover();

}
