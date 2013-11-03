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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;

/**
 *
 connect {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT ConnectArg {bound} RETURN RESULT FALSE ERRORS {missingParameter |
 * parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter |
 * unknownLegID} CODE opcode-connect} -- Direction: gsmSCF-> gsmSSF, Timer: Tcon -- This operation is used to request the gsmSSF
 * to perform the call processing actions -- to route or forward a call to a specified destination.
 *
 * ConnectArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { destinationRoutingAddress [0] DestinationRoutingAddress {bound},
 * alertingPattern [1] AlertingPattern OPTIONAL, originalCalledPartyID [6] OriginalCalledPartyID {bound} OPTIONAL, extensions
 * [10] Extensions {bound} OPTIONAL, carrier [11] Carrier {bound} OPTIONAL, callingPartysCategory [28] CallingPartysCategory
 * OPTIONAL, redirectingPartyID [29] RedirectingPartyID {bound} OPTIONAL, redirectionInformation [30] RedirectionInformation
 * OPTIONAL, genericNumbers [14] GenericNumbers {bound} OPTIONAL, serviceInteractionIndicatorsTwo [15]
 * ServiceInteractionIndicatorsTwo OPTIONAL, chargeNumber [19] ChargeNumber {bound} OPTIONAL, legToBeConnected [21] LegID
 * OPTIONAL, cug-Interlock [31] CUG-Interlock OPTIONAL, cug-OutgoingAccess [32] NULL OPTIONAL, suppressionOfAnnouncement [55]
 * SuppressionOfAnnouncement OPTIONAL, oCSIApplicable [56] OCSIApplicable OPTIONAL, naOliInfo [57] NAOliInfo OPTIONAL,
 * bor-InterrogationRequested [58] NULL OPTIONAL, ... } -- na-Info is included at the discretion of the gsmSCF operator.
 *
 * SuppressionOfAnnouncement ::= NULL
 *
 * OCSIApplicable ::= NULL -- Indicates that the Originating CAMEL Subscription Information, if present, shall be -- applied on
 * the outgoing call leg created with a Connect operation. For the use of this -- parameter see 3GPP TS 23.078 [7].
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ConnectRequest extends CircuitSwitchedCallMessage {

    DestinationRoutingAddress getDestinationRoutingAddress();

    AlertingPatternCap getAlertingPattern();

    OriginalCalledNumberCap getOriginalCalledPartyID();

    CAPExtensions getExtensions();

    Carrier getCarrier();

    CallingPartysCategoryInap getCallingPartysCategory();

    RedirectingPartyIDCap getRedirectingPartyID();

    RedirectionInformationInap getRedirectionInformation();

    ArrayList<GenericNumberCap> getGenericNumbers();

    ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo();

    LocationNumberCap getChargeNumber();

    LegID getLegToBeConnected();

    CUGInterlock getCUGInterlock();

    boolean getCugOutgoingAccess();

    boolean getSuppressionOfAnnouncement();

    boolean getOCSIApplicable();

    NAOliInfo getNAOliInfo();

    boolean getBorInterrogationRequested();

}