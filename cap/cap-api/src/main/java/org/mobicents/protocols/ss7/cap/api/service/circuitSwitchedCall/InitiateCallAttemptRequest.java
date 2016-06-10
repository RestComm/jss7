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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;

/**
 *
<code>
initiateCallAttempt {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT InitiateCallAttemptArg {bound}
  RESULT InitiateCallAttemptRes {bound}
  ERRORS {
    missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter
  }
  CODE opcode-initiateCallAttempt}
-- Direction: gsmSCF -> gsmSSF, Timer T ica
-- This operation is used to instruct the gsmSSF to create a new call to a call party using the
-- address information provided by the gsmSCF.

InitiateCallAttemptArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  destinationRoutingAddress  [0] DestinationRoutingAddress {bound},
  extensions                 [4] Extensions {bound} OPTIONAL,
  legToBeCreated             [5] LegID OPTIONAL,
  newCallSegment             [6] CallSegmentID {bound} OPTIONAL,
  callingPartyNumber         [30] CallingPartyNumber {bound} OPTIONAL,
  callReferenceNumber        [51] CallReferenceNumber OPTIONAL,
  gsmSCFAddress              [52] ISDN-AddressString OPTIONAL,
  suppress-T-CSI             [53] NULL OPTIONAL,
  ...
}

CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..127)
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InitiateCallAttemptRequest extends CircuitSwitchedCallMessage {

    DestinationRoutingAddress getDestinationRoutingAddress();

    CAPExtensions getExtensions();

    LegID getLegToBeCreated();

    Integer getNewCallSegment();

    CallingPartyNumberCap getCallingPartyNumber();

    CallReferenceNumber getCallReferenceNumber();

    ISDNAddressString getGsmSCFAddress();

    boolean getSuppressTCsi();

}