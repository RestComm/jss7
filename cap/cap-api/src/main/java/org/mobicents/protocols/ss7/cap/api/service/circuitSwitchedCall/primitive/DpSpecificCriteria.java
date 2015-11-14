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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
 *
<code>
DpSpecificCriteria {PARAMETERS-BOUND : bound}::= CHOICE {
  applicationTimer       [1] ApplicationTimer,
  midCallControlInfo     [2] MidCallControlInfo,
  dpSpecificCriteriaAlt  [3] DpSpecificCriteriaAlt {bound}
}
-- Exception handling: reception of DpSpecificCriteriaAlt shall be treated like
-- reception of no DpSpecificCriteria.
-- The gsmSCF may set a timer in the gsmSSF for the No_Answer event.
-- If the user does not answer the call within the allotted time,
-- then the gsmSSF reports the event to the gsmSCF.
-- The gsmSCF may define a criterion for the detection of DTMF digits during a call.
-- The gsmSCF may define other criteria in the dpSpecificCriteriaAlt alternative
-- in future releases.

ApplicationTimer ::=INTEGER (0..2047)
-- Used by the gsmSCF to set a timer in the gsmSSF. The timer is in seconds.
</code>
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
