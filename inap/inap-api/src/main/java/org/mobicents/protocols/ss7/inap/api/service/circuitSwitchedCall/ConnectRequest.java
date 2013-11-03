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

import org.mobicents.protocols.ss7.inap.api.isup.BackwardGVNSInap;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartyNumberInap;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.Digits;
import org.mobicents.protocols.ss7.inap.api.isup.ForwardCallIndicatorsInap;
import org.mobicents.protocols.ss7.inap.api.isup.ForwardGVNSInap;
import org.mobicents.protocols.ss7.inap.api.isup.ISDNAccessRelatedInformationInap;
import org.mobicents.protocols.ss7.inap.api.isup.LocationNumberInap;
import org.mobicents.protocols.ss7.inap.api.isup.OriginalCalledPartyIDInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectingPartyIDInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.api.primitives.BearerCapability;
import org.mobicents.protocols.ss7.inap.api.primitives.INAPExtensions;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DisplayInformation;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.Entry;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.GenericNumbers;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ScfID;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;

/**
*

*** CS1: ***
Connect ::= OPERATION
    ARGUMENT ConnectArg
    ERRORS { MissingParameter, SystemFailure, TaskRefused, UnexpectedComponentSequence, UnexpectedDataValue, UnexpectedParameter}

ConnectArg ::= SEQUENCE {
  destinationRoutingAddress       [0] DestinationRoutingAddress,
  alertingPattern                 [1] AlertingPattern OPTIONAL,
  correlationID                   [2] CorrelationID OPTIONAL,
  cutAndPaste                     [3] CutAndPaste OPTIONAL,
  originalCalledPartyID           [6] OriginalCalledPartyID OPTIONAL,
  routeList                       [7] RouteList OPTIONAL,
  scfID                           [8] ScfID OPTIONAL,
  extensions                      [10] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
  serviceInteractionIndicators    [26] ServiceInteractionIndicators OPTIONAL,
  callingPartyNumber              [27] CallingPartyNumber OPTIONAL,
  callingPartysCategory           [28] CallingPartysCategory OPTIONAL,
  redirectingPartyID              [29] RedirectingPartyID OPTIONAL,
  redirectionInformation          [30] RedirectionInformation OPTIONAL
  ...
}
-- For alerting pattern, OPTIONAL denotes that this parameter only applies if SSF is the
-- terminating local exchange for the subscriber.

*** CS2: ***
connect {PARAMETERS-BOUND : bound} OPERATION ::= {
    ARGUMENT ConnectArg {bound}
    RETURN RESULT FALSE
    ERRORS {
        missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter
    }
    CODE opcode-connect
}
-- Direction: SCF -> SSF, Timer: Tcon
-- This operation is used to request the SSF to perform the call processing actions to route or
-- forward a call to a specified destination. To do so, the SSF may or may not use destination
-- information from the calling party (e.g. dialed digits) and existing call setup information
-- (e.g. route index to a list of trunk groups), depending on the information provided by the SCF.
-- - When address information is only included in the Connect operation, call processing resumes at
-- the Analyzed_Information PIC in the O-BCSM.
-- - When address information and routing information is included, call processing resumes at the
-- Select_Route PIC.

ConnectArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
    destinationRoutingAddress        [0] DestinationRoutingAddress {bound},
    alertingPattern                  [1] AlertingPattern OPTIONAL,
    correlationID                    [2] CorrelationID {bound} OPTIONAL,
    cutAndPaste                      [3] CutAndPaste OPTIONAL,
    iSDNAccessRelatedInformation     [5] ISDNAccessRelatedInformation{bound} OPTIONAL,
    originalCalledPartyID            [6] OriginalCalledPartyID {bound} OPTIONAL,
    routeList                        [7] RouteList {bound} OPTIONAL,
    scfID                            [8] ScfID {bound} OPTIONAL,
    extensions                       [10] SEQUENCE SIZE(1..bound.&numOfExtensions) OF ExtensionField {bound} OPTIONAL,
    carrier                          [11] Carrier{bound} OPTIONAL,
    serviceInteractionIndicators     [26] ServiceInteractionIndicators {bound} OPTIONAL,
    callingPartyNumber               [27] CallingPartyNumber {bound} OPTIONAL,
    callingPartysCategory            [28] CallingPartysCategory OPTIONAL,
    redirectingPartyID               [29] RedirectingPartyID {bound} OPTIONAL,
    redirectionInformation           [30] RedirectionInformation OPTIONAL,
    displayInformation               [12] DisplayInformation {bound} OPTIONAL,
    forwardCallIndicators            [13] ForwardCallIndicators OPTIONAL,
    genericNumbers                   [14] GenericNumbers {bound} OPTIONAL,
    serviceInteractionIndicatorsTwo  [15] ServiceInteractionIndicatorsTwo OPTIONAL,
    iNServiceCompatibilityResponse   [16] INServiceCompatibilityResponse OPTIONAL,
    forwardGVNS                      [17] ForwardGVNS {bound} OPTIONAL,
    backwardGVNS                     [18] BackwardGVNS {bound} OPTIONAL,
    callSegmentID                    [20] CallSegmentID {bound} OPTIONAL,
    legToBeCreated                   [21] LegID OPTIONAL,
    locationNumber                   [50] LocationNumber {bound} OPTIONAL,
    bearerCapability                 [51] BearerCapability {bound} OPTIONAL,
    suppressionOfAnnouncement        [55] SuppressionOfAnnouncement OPTIONAL,
    oCSIApplicable                   [56] OCSIApplicable OPTIONAL,
    ...
}

CorrelationID {PARAMETERS-BOUND : bound} ::= Digits {bound}
-- used by SCF for correlation with a previous operation.
-- Refer to clause 18 for a description of the procedures associated with this parameter.

CutAndPaste ::= INTEGER (0..22)
-- Indicates the number of digits to be deleted.

INServiceCompatibilityResponse ::= Entry

CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..bound.&numOfCSs)

SuppressionOfAnnouncement ::= NULL
OCSIApplicable ::= NULL
*
*
* @author sergey vetyutnev
*
*/
public interface ConnectRequest {

    DestinationRoutingAddress getDestinationRoutingAddress();

    AlertingPattern getAlertingPattern();

    Digits getCorrelationID();

    Integer getCutAndPaste();

    ISDNAccessRelatedInformationInap getISDNAccessRelatedInformation();

    OriginalCalledPartyIDInap getOriginalCalledPartyID();

    RouteList getRouteListID();

    ScfID getScfID();

    INAPExtensions getExtensions();

    Carrier getCarrier();

    ServiceInteractionIndicators getServiceInteractionIndicators();

    CallingPartyNumberInap getCallingPartyNumber();

    CallingPartysCategoryInap getCallingPartysCategory();

    RedirectingPartyIDInap getRedirectingPartyID();

    RedirectionInformationInap getRedirectionInformation();

    DisplayInformation getDisplayInformation();

    ForwardCallIndicatorsInap getForwardCallIndicators();

    GenericNumbers getGenericNumbers();

    ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo();

    Entry getINServiceCompatibilityResponse();

    ForwardGVNSInap getForwardGVNS();

    BackwardGVNSInap getBackwardGVNS();

    Integer getCallSegmentID();

    LegID getLegToBeCreated();

    LocationNumberInap getLocationNumber();

    BearerCapability getBearerCapability();

    boolean getSuppressionOfAnnouncement();

    boolean getOCSIApplicable();

}
