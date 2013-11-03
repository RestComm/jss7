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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.inap.api.isup.CalledPartyNumberInap;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartyNumberInap;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.CauseInap;
import org.mobicents.protocols.ss7.inap.api.isup.Digits;
import org.mobicents.protocols.ss7.inap.api.isup.ForwardCallIndicatorsInap;
import org.mobicents.protocols.ss7.inap.api.isup.ForwardGVNSInap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.api.isup.ISDNAccessRelatedInformationInap;
import org.mobicents.protocols.ss7.inap.api.isup.LocationNumberInap;
import org.mobicents.protocols.ss7.inap.api.isup.OriginalCalledPartyIDInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectingPartyIDInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.api.primitives.BearerCapability;
import org.mobicents.protocols.ss7.inap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.inap.api.primitives.INAPExtensions;
import org.mobicents.protocols.ss7.inap.api.primitives.TerminalType;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.GenericNumbers;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.INServiceCompatibilityIndication;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPAvailable;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.USIInformation;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.USIServiceIndicator;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;

/**
*

*** CS1: ***
InitialDP ::= OPERATION
ARGUMENT
    InitialDPArg
ERRORS {
    MissingCustomerRecord,
    MissingParameter,
    SystemFailure,
    TaskRefused,
    UnexpectedComponentSequence,
    UnexpectedDataValue,
    UnexpectedParameter
}
-- Direction: SSF -> SCF, Timer: Tidp
-- This operation is used after a TDP to indicate request for service.

InitialDPArg ::= SEQUENCE {
    serviceKey                      [0] ServiceKey,
    calledPartyNumber               [2] CalledPartyNumber OPTIONAL,
    callingPartyNumber              [3] CallingPartyNumber OPTIONAL,
    callingPartysCategory           [5] CallingPartysCategory OPTIONAL,
    cGEncountered                   [7] CGEncountered OPTIONAL,
    iPSSPCapabilities               [8] IPSSPCapabilities OPTIONAL,
    iPAvailable                     [9] IPAvailable OPTIONAL,
    locationNumber                  [10] LocationNumber OPTIONAL,
    originalCalledPartyID           [12] OriginalCalledPartyID OPTIONAL,
    extensions                      [15] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
    highLayerCompatibility          [23] HighLayerCompatibility OPTIONAL,
    serviceInteractionIndicators    [24] ServiceInteractionIndicatorsOPTIONAL,
    additionalCallingPartyNumber    [25] AdditionalCallingPartyNumberOPTIONAL,
    forwardCallIndicators           [26] ForwardCallIndicators OPTIONAL,
    bearerCapability                [27] BearerCapability OPTIONAL,
    eventTypeBCSM                   [28] EventTypeBCSM OPTIONAL,
    redirectingPartyID              [29] RedirectingPartyID OPTIONAL,
    redirectionInformation          [30] RedirectionInformation OPTIONAL
    -- ...
}
-- OPTIONAL for iPSSPCapabilities, iPAvailable, cGEncountered denotes network operator specific use.
-- OPTIONAL for callingPartyNumber, and callingPartysCategory refer to Clause 7 for the trigger detection
-- point processing rules to specify when these parameters are included in the message.
-- The following parameters shall be recognized by the SCF upon reception of InitialDP:
-- dialledDigits [1] CalledPartyNumber OPTIONAL,
-- callingPartyBusinessGroupID [4] CallingPartyBusinessGroupID OPTIONAL,
-- callingPartySubaddress [6] CallingPartySubaddress OPTIONAL,
-- miscCallInfo [11] MiscCallInfo OPTIONAL,
-- serviceProfileIdentifier [13] ServiceProfileIdentifier OPTIONAL,
-- terminalType [14] TerminalType OPTIONAL
-- These parameters shall be ignored by the SCF and not lead to any error procedures.
-- These parameters shall not be sent by a SSF following this ETS.
-- For details on the coding of these parameters refer to ITU-T Recommendation Q.1218 [12].

*** CS2: ***
initialDP {PARAMETERS-BOUND : bound} OPERATION ::= {
    ARGUMENT InitialDPArg {bound}
RETURN RESULT FALSE
ERRORS {
    missingCustomerRecord |
    missingParameter |
    parameterOutOfRange |
    systemFailure |
    taskRefused |
    unexpectedComponentSequence |
    unexpectedDataValue |
    unexpectedParameter}
CODE
    opcode-initialDP
}
-- Direction: SSF -> SCF, Timer: Tidp
-- This operation is used after a TDP to indicate request for service.
InitialDPArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
    serviceKey                     [0] ServiceKey ,
    calledPartyNumber              [2] CalledPartyNumber {bound} OPTIONAL,
    callingPartyNumber             [3] CallingPartyNumber {bound} OPTIONAL,
    callingPartyBusinessGroupID    [4] CallingPartyBusinessGroupID OPTIONAL,
    callingPartysCategory          [5] CallingPartysCategory OPTIONAL,
    cGEncountered                  [7] CGEncountered OPTIONAL,
    iPSSPCapabilities              [8] IPSSPCapabilities {bound} OPTIONAL,
    iPAvailable                    [9] IPAvailable {bound} OPTIONAL,
    locationNumber                 [10] LocationNumber {bound} OPTIONAL,
    originalCalledPartyID          [12] OriginalCalledPartyID {bound} OPTIONAL,
    terminalType                   [14] TerminalType OPTIONAL,
    extensions                     [15] SEQUENCE SIZE(1..bound.&numOfExtensions) OF ExtensionField {bound} OPTIONAL,
    highLayerCompatibility         [23] HighLayerCompatibility OPTIONAL,
    serviceInteractionIndicators   [24] ServiceInteractionIndicators {bound} OPTIONAL,
    additionalCallingPartyNumber   [25] AdditionalCallingPartyNumber {bound} OPTIONAL,
    forwardCallIndicators          [26] ForwardCallIndicators OPTIONAL,
    bearerCapability               [27] BearerCapability {bound} OPTIONAL,
    eventTypeBCSM                  [28] EventTypeBCSM OPTIONAL,
    redirectingPartyID             [29] RedirectingPartyID {bound} OPTIONAL,
    redirectionInformation         [30] RedirectionInformation OPTIONAL,
    cause                          [17] Cause {bound} OPTIONAL,
    iSDNAccessRelatedInformation      [21] ISDNAccessRelatedInformation OPTIONAL,
    iNServiceCompatibilityIndication  [22] INServiceCompatibilityIndication {bound} OPTIONAL,
    genericNumbers                    [31] GenericNumbers {bound} OPTIONAL,
    serviceInteractionIndicatorsTwo   [32] ServiceInteractionIndicatorsTwo OPTIONAL,
    forwardGVNS                       [33] ForwardGVNS {bound} OPTIONAL,
    createdCallSegmentAssociation     [34] CSAID {bound} OPTIONAL,
    uSIServiceIndicator            [35] USIServiceIndicator {bound} OPTIONAL,
    uSIInformation                 [36] USIInformation {bound} OPTIONAL,
    carrier                        [37] Carrier OPTIONAL,
    iMSI                           [50] IMSI OPTIONAL,
    subscriberState                [51] SubscriberState OPTIONAL,
    locationInformation            [52] LocationInformation OPTIONAL,
    ext-basicServiceCode           [53] Ext-BasicServiceCode OPTIONAL,
    callReferenceNumber            [54] CallReferenceNumber OPTIONAL,
    mscAddress                     [55] ISDN-AddressString OPTIONAL,
    calledPartyBCDNumber           [56] CalledPartyBCDNumber OPTIONAL,
    ...
}
-- OPTIONAL for iPSSPCapabilities, iPAvailable, cGEncountered, and miscCallInfo denotes network
-- operator specific use.
-- OPTIONAL for callingPartyNumber, and callingPartysCategory refer to clause 18 for the trigger
-- detection point processing rules to specify when these parameters are included in the message.

CSAID {PARAMETERS-BOUND : bound} ::= INTEGER (1..bound.&numOfCSAs)
-- Indicates the SSF CSA identifier

*
*
* @author sergey vetyutnev
*
*/
public interface InitialDPRequest {

    int getServiceKey();

    CalledPartyNumberInap getCalledPartyNumber();

    CallingPartyNumberInap getCallingPartyNumber();

    CallingPartyBusinessGroupID getCallingPartyBusinessGroupID();

    CallingPartysCategoryInap getCallingPartysCategory();

    CGEncountered getCGEncountered();

    IPSSPCapabilities getIPSSPCapabilities();

    IPAvailable getIPAvailable();

    LocationNumberInap getLocationNumber();

    OriginalCalledPartyIDInap getOriginalCalledPartyID();

    TerminalType getTerminalType();

    INAPExtensions getExtensions();

    HighLayerCompatibilityInap getHighLayerCompatibility();

    ServiceInteractionIndicators getServiceInteractionIndicators();

    INServiceCompatibilityIndication getINServiceCompatibilityIndication();

    /**
     * Use Digits.getGenericNumber() for AdditionalCallingPartyNumber
     *
     * @return
     */
    Digits getAdditionalCallingPartyNumber();

    ForwardCallIndicatorsInap getForwardCallIndicators();

    BearerCapability getBearerCapability();

    EventTypeBCSM getEventTypeBCSM();

    RedirectingPartyIDInap getRedirectingPartyID();

    RedirectionInformationInap getRedirectionInformation();

    CauseInap getCause();

    ISDNAccessRelatedInformationInap getISDNAccessRelatedInformation();

    GenericNumbers getGenericNumbers();

    ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo();

    ForwardGVNSInap getForwardGVNS();

    Integer getCreatedCallSegmentAssociation();

    USIServiceIndicator getUSIServiceIndicator();

    USIInformation getUSIInformation();

    Carrier getCarrier();

    IMSI getIMSI();

    SubscriberState getSubscriberState();

    LocationInformation getLocationInformation();

    ExtBasicServiceCode getExtBasicServiceCode();

    CallReferenceNumber getCallReferenceNumber();

    ISDNAddressString getMscAddress();

    CalledPartyBCDNumber getCalledPartyBCDNumber();

}
