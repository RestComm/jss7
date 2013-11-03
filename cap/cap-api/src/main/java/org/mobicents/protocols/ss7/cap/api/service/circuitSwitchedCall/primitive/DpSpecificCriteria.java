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

/**
 *
 DpSpecificCriteria {PARAMETERS-BOUND : bound}::= CHOICE { applicationTimer [1] ApplicationTimer, midCallControlInfo [2]
 * MidCallControlInfo, dpSpecificCriteriaAlt [3] DpSpecificCriteriaAlt {bound} } -- Exception handling: reception of
 * DpSpecificCriteriaAlt shall be treated like -- reception of no DpSpecificCriteria. -- The gsmSCF may set a timer in the
 * gsmSSF for the No_Answer event. -- If the user does not answer the call within the allotted time, -- then the gsmSSF reports
 * the event to the gsmSCF. -- The gsmSCF may define a criterion for the detection of DTMF digits during a call. -- The gsmSCF
 * may define other criteria in the dpSpecificCriteriaAlt alternative -- in future releases.
 *
 * ApplicationTimer ::=INTEGER (0..2047) -- Used by the gsmSCF to set a timer in the gsmSSF. The timer is in seconds.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface DpSpecificCriteria extends Serializable {

    Integer getApplicationTimer();

    MidCallControlInfo getMidCallControlInfo();

    DpSpecificCriteriaAlt getDpSpecificCriteriaAlt();

}