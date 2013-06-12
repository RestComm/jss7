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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

import org.mobicents.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PdpContextchangeOfPositionSpecificInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;

/**
 *
 GPRSEventSpecificInformation {PARAMETERS-BOUND : bound} ::= CHOICE { attachChangeOfPositionSpecificInformation [0] SEQUENCE {
 * locationInformationGPRS [0] LocationInformationGPRS OPTIONAL, ... }, pdp-ContextchangeOfPositionSpecificInformation [1]
 * SEQUENCE { accessPointName [0] AccessPointName {bound} OPTIONAL, chargingID [1] GPRSChargingID OPTIONAL,
 * locationInformationGPRS [2] LocationInformationGPRS OPTIONAL, endUserAddress [3] EndUserAddress {bound} OPTIONAL,
 * qualityOfService [4] QualityOfService OPTIONAL, timeAndTimeZone [5] TimeAndTimezone {bound} OPTIONAL, ..., gGSNAddress [6]
 * GSN-Address OPTIONAL }, detachSpecificInformation [2] SEQUENCE { initiatingEntity [0] InitiatingEntity OPTIONAL, ...,
 * routeingAreaUpdate [1] NULL OPTIONAL }, disconnectSpecificInformation [3] SEQUENCE { initiatingEntity [0] InitiatingEntity
 * OPTIONAL, ..., routeingAreaUpdate [1] NULL OPTIONAL }, pDPContextEstablishmentSpecificInformation [4] SEQUENCE {
 * accessPointName [0] AccessPointName {bound} OPTIONAL, endUserAddress [1] EndUserAddress {bound} OPTIONAL, qualityOfService
 * [2] QualityOfService OPTIONAL, locationInformationGPRS [3] LocationInformationGPRS OPTIONAL, timeAndTimeZone [4]
 * TimeAndTimezone {bound} OPTIONAL, pDPInitiationType [5] PDPInitiationType OPTIONAL, ..., secondaryPDP-context [6] NULL
 * OPTIONAL }, pDPContextEstablishmentAcknowledgementSpecificInformation [5] SEQUENCE { accessPointName [0] AccessPointName
 * {bound} OPTIONAL, chargingID [1] GPRSChargingID OPTIONAL, endUserAddress [2] EndUserAddress {bound} OPTIONAL,
 * qualityOfService [3] QualityOfService OPTIONAL, locationInformationGPRS [4] LocationInformationGPRS OPTIONAL, timeAndTimeZone
 * [5] TimeAndTimezone {bound} OPTIONAL, ..., gGSNAddress [6] GSN-Address OPTIONAL } }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GPRSEventSpecificInformation {

    LocationInformationGPRS getLocationInformationGPRS();

    PdpContextchangeOfPositionSpecificInformation getPdpContextchangeOfPositionSpecificInformation();

    DetachSpecificInformation getDetachSpecificInformation();

    DisconnectSpecificInformation getDisconnectSpecificInformation();

    PDPContextEstablishmentSpecificInformation getPDPContextEstablishmentSpecificInformation();

    PDPContextEstablishmentAcknowledgementSpecificInformation getPDPContextEstablishmentAcknowledgementSpecificInformation();

}
