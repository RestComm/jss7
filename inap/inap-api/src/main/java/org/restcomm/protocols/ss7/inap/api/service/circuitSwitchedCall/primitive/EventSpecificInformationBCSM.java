/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnalysedInfoSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.CallAccepted;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.FacilitySelectedAndAvailable;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.OAbandon;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.OAnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.OCalledPartyBusySpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.OMidCallSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.ONoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.OReAnswer;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.OSuspended;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.OTermSeizedSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.OrigAttemptAuthorized;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.TAbandon;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.TAnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.TBusySpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.TDisconnectSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.TMidCallSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.TNoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.TReAnswer;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.TSuspended;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.TerminationAttemptAuthorized;

/**
*
<code>
EventSpecificInformationBCSM {PARAMETERS-BOUND : bound} ::= CHOICE {
  collectedInfoSpecificInfo [0] SEQUENCE {
    calledPartynumber [0] CalledPartyNumber {bound},
    ...
  },
  analysedInfoSpecificInfo [1] SEQUENCE {
    calledPartynumber [0] CalledPartyNumber {bound},
    ...
  },
  routeSelectFailureSpecificInfo [2] SEQUENCE {
    failureCause [0] Cause {bound} OPTIONAL,
    ...
  },
  oCalledPartyBusySpecificInfo [3] SEQUENCE {
    busyCause [0] Cause {bound} OPTIONAL,
    ...
  },
  oNoAnswerSpecificInfo [4] SEQUENCE {
    -- no specific info defined --
    ...
  },
  oAnswerSpecificInfo [5] SEQUENCE {
    backwardGVNS [0] BackwardGVNS {bound} OPTIONAL,
    ...
  },
  oMidCallSpecificInfo [6] SEQUENCE {
    connectTime [0] Integer4 OPTIONAL,
    oMidCallInfo [1] MidCallInfo {bound} OPTIONAL,
    ...
  },
  oDisconnectSpecificInfo [7] SEQUENCE {
    releaseCause [0] Cause {bound} OPTIONAL,
    connectTime [1] Integer4 OPTIONAL,
    ...
  },
  tBusySpecificInfo [8] SEQUENCE {
    busyCause [0] Cause {bound} OPTIONAL,
    ...
  },
  tNoAnswerSpecificInfo [9] SEQUENCE {
    -- no specific info defined --
    ...
  },
  tAnswerSpecificInfo [10] SEQUENCE {
    -- no specific info defined --
    ...
  },
  tMidCallSpecificInfo [11] SEQUENCE {
    connectTime [0] Integer4 OPTIONAL,
    tMidCallInfo [1] MidCallInfo {bound} OPTIONAL,
    ...
  },
  tDisconnectSpecificInfo [12] SEQUENCE {
    releaseCause [0] Cause {bound} OPTIONAL,
    connectTime [1] Integer4 OPTIONAL,
    ...
  },
  oTermSeizedSpecificInfo [13] SEQUENCE {
    -- no specific info defined
    ...
  },
  oSuspended [14] SEQUENCE {
    -- no specific info defined
    ...
  },
  tSuspended [15] SEQUENCE {
    -- no specific info defined
    ...
  },
  origAttemptAuthorized [16] SEQUENCE {
    -- no specific info defined
    ...
  },
  oReAnswer [17] SEQUENCE {
    -- no specific info defined
    ...
  },
  tReAnswer [18] SEQUENCE {
    -- no specific info defined
    ...
  },
  facilitySelectedAndAvailable [19] SEQUENCE {
    -- no specific info defined
    ...
  },
  callAccepted [20] SEQUENCE {
    -- no specific info defined
    ...
  },
  oAbandon [21] SEQUENCE {
    abandonCause [0] Cause {bound} OPTIONAL,
    ...
  },
  tAbandon [22] SEQUENCE {
    abandonCause [0] Cause {bound} OPTIONAL,
    ...
  },
  terminationAttemptAuthorized [24] SEQUENCE {
    -- no specific info defined
    ...
  }
}
-- Indicates the call related information specific to the event.
-- The connectTime indicates the duration between the received answer indication
-- from the called party side
-- and the release of the connection for ODisconnect, OException, TDisconnect, or TException
-- or between
-- the received answer indication from the called party side and the time
-- of detection of the required mid call event.
-- The unit for the connectTime is 100 milliseconds.
</code>
*
*
* @author sergey vetyutnev
*
*/
public interface EventSpecificInformationBCSM extends Serializable {

    CollectedInfoSpecificInfo getCollectedInfoSpecificInfo();

    AnalysedInfoSpecificInfo getAnalysedInfoSpecificInfo();

    RouteSelectFailureSpecificInfo getRouteSelectFailureSpecificInfo();

    OCalledPartyBusySpecificInfo getOCalledPartyBusySpecificInfo();

    ONoAnswerSpecificInfo getONoAnswerSpecificInfo();

    OAnswerSpecificInfo getOAnswerSpecificInfo();

    OMidCallSpecificInfo getOMidCallSpecificInfo();

    ODisconnectSpecificInfo getODisconnectSpecificInfo();

    TBusySpecificInfo getTBusySpecificInfo();

    TNoAnswerSpecificInfo getTNoAnswerSpecificInfo();

    TAnswerSpecificInfo getTAnswerSpecificInfo();

    TMidCallSpecificInfo getTMidCallSpecificInfo();

    TDisconnectSpecificInfo getTDisconnectSpecificInfo();

    OTermSeizedSpecificInfo getOTermSeizedSpecificInfo();

    OSuspended getOSuspended();

    TSuspended getTSuspended();

    OrigAttemptAuthorized getOrigAttemptAuthorized();

    OReAnswer getOReAnswer();

    TReAnswer getTReAnswer();

    FacilitySelectedAndAvailable getFacilitySelectedAndAvailable();

    CallAccepted getCallAccepted();

    OAbandon getOAbandon();

    TAbandon getTAbandon();

    TerminationAttemptAuthorized getTerminationAttemptAuthorized();

}
