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

import org.mobicents.protocols.ss7.cap.api.gap.GapCriteria;
import org.mobicents.protocols.ss7.cap.api.gap.GapIndicators;
import org.mobicents.protocols.ss7.cap.api.gap.GapTreatment;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ControlType;

/**
 *
<code>
callGap {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT CallGapArg {bound}
  RETURN RESULT FALSE
  ALWAYS RESPONDS FALSE
  CODE opcode-callGap
}
-- Direction: gsmSCF -> gsmSSF, Timer: T cg
-- This operation is used to request the gsmSSF to reduce the rate at which specific service
-- requests are sent to the gsmSCF.

CallGapArg {PARAMETERS-BOUND : bound}::= SEQUENCE {
  gapCriteria    [0] GapCriteria {bound},
  gapIndicators  [1] GapIndicators,
  controlType    [2] ControlType OPTIONAL
  gapTreatment   [3] GapTreatment {bound} OPTIONAL
  extensions     [4] Extensions {bound} OPTIONAL,
  ...
}
-- OPTIONAL denotes network operator optional. If gapTreatment is not present, then the gsmSSF will
-- use a default treatment depending on network operator implementation.
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CallGapRequest extends CircuitSwitchedCallMessage {

    GapCriteria getGapCriteria();

    GapIndicators getGapIndicators();

    ControlType getControlType();

    GapTreatment getGapTreatment();

    CAPExtensions getExtensions();

}