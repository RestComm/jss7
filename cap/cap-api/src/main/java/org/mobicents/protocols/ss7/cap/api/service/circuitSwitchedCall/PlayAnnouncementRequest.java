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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;

/**
 *
<code>
playAnnouncement { PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT PlayAnnouncementArg {bound}
  RETURN RESULT FALSE
  ERRORS {canceled | missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
          unexpectedDataValue | unexpectedParameter | unavailableResource | unknownCSID}
  LINKED {specializedResourceReport}
  CODE opcode-playAnnouncement
}
-- Direction: gsmSCF -> gsmSRF, Timer: T pa
-- This operation is to be used after Establish Temporary Connection (assist procedure
-- with a second gsmSSF) or a Connect to Resource (no assist) operation. It may be used
-- for inband interaction with a mobile station, or for interaction with an ISDN user.
-- In the former case, the gsmSRF is usually collocated with the gsmSSF for standard
-- tones (congestion tone...) or standard announcements.
-- In the latter case, the gsmSRF is always collocated with the gsmSSF in the switch.
-- Any error is returned to the gsmSCF. The timer associated with this operation must
-- be of a sufficient duration to allow its linked operation to be correctly correlated.

PlayAnnouncementArg {PARAMETERS-BOUND : bound}::= SEQUENCE {
  informationToSend                        [0] InformationToSend {bound},
  disconnectFromIPForbidden                [1] BOOLEAN DEFAULT TRUE,
  requestAnnouncementCompleteNotification  [2] BOOLEAN DEFAULT TRUE,
  extensions                               [3] Extensions {bound} OPTIONAL,
  callSegmentID                            [5] CallSegmentID {bound} OPTIONAL,
  requestAnnouncementStartedNotification   [51] BOOLEAN DEFAULT FALSE,
  ...
}

CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..bound.&numOfCSs) numOfCSs ::= 127
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PlayAnnouncementRequest extends CircuitSwitchedCallMessage {

    InformationToSend getInformationToSend();

    Boolean getDisconnectFromIPForbidden();

    Boolean getRequestAnnouncementCompleteNotification();

    CAPExtensions getExtensions();

    Integer getCallSegmentID();

    Boolean getRequestAnnouncementStartedNotification();

}
