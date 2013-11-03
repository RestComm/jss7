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

package org.mobicents.protocols.ss7.cap.api.service.gprs;

import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;

/**
 *
 initialDPGPRS {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT InitialDPGPRSArg {bound} RETURN RESULT FALSE ERRORS
 * {missingCustomerRecord | missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
 * unexpectedDataValue | unexpectedParameter} CODE opcode-initialDPGPRS} -- Direction gprsSSF -> gsmSCF,Timer Tidpg -- This
 * operation is used by the gprsSSF when a trigger is detected at a DP in the GPRS state -- machines to request instructions
 * from the gsmSCF
 *
 * InitialDPGPRSArg {PARAMETERS-BOUND : bound}::= SEQUENCE { serviceKey [0] ServiceKey, gPRSEventType [1] GPRSEventType, mSISDN
 * [2] ISDN-AddressString, iMSI [3] IMSI, timeAndTimeZone [4] TimeAndTimezone {bound}, gPRSMSClass [5] GPRSMSClass OPTIONAL,
 * endUserAddress [6] EndUserAddress {bound} OPTIONAL, qualityOfService [7] QualityOfService OPTIONAL, accessPointName [8]
 * AccessPointName{bound} OPTIONAL, routeingAreaIdentity [9] RAIdentity OPTIONAL, chargingID [10] GPRSChargingID OPTIONAL,
 * sGSNCapabilities [11] SGSNCapabilities OPTIONAL, locationInformationGPRS [12] LocationInformationGPRS OPTIONAL,
 * pDPInitiationType [13] PDPInitiationType OPTIONAL, extensions [14] Extensions {bound} OPTIONAL, ..., gGSNAddress [15]
 * GSN-Address OPTIONAL, secondaryPDP-context [16] NULL OPTIONAL, iMEI [17] IMEI OPTIONAL } -- The RouteingAreaIdentity
 * parameter is not used. -- The receiving entity shall ignore RouteingAreaIdentity if received. -- The RouteingAreaIdentity is
 * conveyed in the LocationInformationGPRS parameter.
 *
 * ServiceKey ::= Integer4
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InitialDpGprsRequest extends GprsMessage {

    int getServiceKey();

    GPRSEventType getGPRSEventType();

    ISDNAddressString getMsisdn();

    IMSI getImsi();

    TimeAndTimezone getTimeAndTimezone();

    GPRSMSClass getGPRSMSClass();

    EndUserAddress getEndUserAddress();

    QualityOfService getQualityOfService();

    AccessPointName getAccessPointName();

    RAIdentity getRouteingAreaIdentity();

    GPRSChargingID getChargingID();

    SGSNCapabilities getSGSNCapabilities();

    LocationInformationGPRS getLocationInformationGPRS();

    PDPInitiationType getPDPInitiationType();

    CAPExtensions getExtensions();

    GSNAddress getGSNAddress();

    boolean getSecondaryPDPContext();

    IMEI getImei();

}