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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UUData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;

/**
 *
 CAP V4: InitialDPArgExtension {PARAMETERS-BOUND : bound} ::= SEQUENCE { gmscAddress [0] ISDN-AddressString OPTIONAL,
 * forwardingDestinationNumber [1] CalledPartyNumber {bound} OPTIONAL, ms-Classmark2 [2] MS-Classmark2 OPTIONAL, iMEI [3] IMEI
 * OPTIONAL, supportedCamelPhases [4] SupportedCamelPhases OPTIONAL, offeredCamel4Functionalities [5]
 * OfferedCamel4Functionalities OPTIONAL, bearerCapability2 [6] BearerCapability {bound} OPTIONAL, ext-basicServiceCode2 [7]
 * Ext-BasicServiceCode OPTIONAL, highLayerCompatibility2 [8] HighLayerCompatibility OPTIONAL, lowLayerCompatibility [9]
 * LowLayerCompatibility {bound} OPTIONAL, lowLayerCompatibility2 [10] LowLayerCompatibility {bound} OPTIONAL, ...,
 * enhancedDialledServicesAllowed [11] NULL OPTIONAL, uu-Data [12] UU-Data OPTIONAL }
 *
 * CAP V2: InitialDPArgExtension ::= SEQUENCE { naCarrierInformation [0] NACarrierInformation OPTIONAL, gmscAddress [1]
 * ISDN-AddressString OPTIONAL, ... }
 *
 * -- If iPSSPCapabilities is not present then this denotes that a colocated gsmSRF is not -- supported by the gsmSSF. If
 * present, then the gsmSSF supports a colocated gsmSRF capable -- of playing announcements via elementaryMessageIDs and
 * variableMessages, the playing of -- tones and the collection of DTMF digits. Other supported capabilities are explicitly --
 * detailed in the IPSSPCapabilities parameter itself. -- Carrier is included at the discretion of the gsmSSF operator.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InitialDPArgExtension extends Serializable {

    ISDNAddressString getGmscAddress();

    CalledPartyNumberCap getForwardingDestinationNumber();

    MSClassmark2 getMSClassmark2();

    IMEI getIMEI();

    SupportedCamelPhases getSupportedCamelPhases();

    OfferedCamel4Functionalities getOfferedCamel4Functionalities();

    BearerCapability getBearerCapability2();

    ExtBasicServiceCode getExtBasicServiceCode2();

    HighLayerCompatibilityInap getHighLayerCompatibility2();

    LowLayerCompatibility getLowLayerCompatibility();

    LowLayerCompatibility getLowLayerCompatibility2();

    boolean getEnhancedDialledServicesAllowed();

    UUData getUUData();

}