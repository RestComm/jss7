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

import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;

/**
 *
 establishTemporaryConnection {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT EstablishTemporaryConnectionArg {bound}
 * RETURN RESULT FALSE ERRORS {eTCFailed | missingParameter | systemFailure | taskRefused | unexpectedComponentSequence |
 * unexpectedDataValue | unexpectedParameter | unknownCSID} CODE opcode-establishTemporaryConnection} -- Direction: gsmSCF ->
 * gsmSSF, Timer: T etc -- This operation is used to create a connection to a resource for a limited period -- of time (e.g. to
 * play an announcement, to collect user information); it implies -- the use of the assist procedure. Refer to clause 11 for a
 * description of the -- procedures associated with this operation.
 *
 * EstablishTemporaryConnectionArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { assistingSSPIPRoutingAddress [0]
 * AssistingSSPIPRoutingAddress {bound}, correlationID [1] CorrelationID {bound} OPTIONAL, scfID [3] ScfID {bound} OPTIONAL,
 * extensions [4] Extensions {bound} OPTIONAL, carrier [5] Carrier {bound} OPTIONAL, serviceInteractionIndicatorsTwo [6]
 * ServiceInteractionIndicatorsTwo OPTIONAL, callSegmentID [7] CallSegmentID {bound} OPTIONAL, naOliInfo [50] NAOliInfo
 * OPTIONAL, chargeNumber [51] ChargeNumber {bound} OPTIONAL, ..., originalCalledPartyID [52] OriginalCalledPartyID {bound}
 * OPTIONAL, callingPartyNumber [53] CallingPartyNumber {bound} OPTIONAL }
 *
 * CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..127)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface EstablishTemporaryConnectionRequest extends CircuitSwitchedCallMessage {

    /**
     * Use Digits.getGenericNumber() for AssistingSSPIPRoutingAddress
     *
     * @return
     */
    Digits getAssistingSSPIPRoutingAddress();

    /**
     * Use Digits.getGenericDigits() for CorrelationID
     *
     * @return
     */
    Digits getCorrelationID();

    ScfID getScfID();

    CAPExtensions getExtensions();

    Carrier getCarrier();

    ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo();

    Integer getCallSegmentID();

    NAOliInfo getNAOliInfo();

    LocationNumberCap getChargeNumber();

    OriginalCalledNumberCap getOriginalCalledPartyID();

    CallingPartyNumberCap getCallingPartyNumber();

}