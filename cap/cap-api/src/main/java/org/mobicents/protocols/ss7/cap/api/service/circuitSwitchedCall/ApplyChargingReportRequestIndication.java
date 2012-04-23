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

import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;

/**
*
ApplyChargingReportArg {PARAMETERS-BOUND : bound} ::= CallResult {bound}

CallResult {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(
bound.&minCallResultLength .. bound.&maxCallResultLength))
(CONSTRAINED BY {-- shall be the result of the BER-encoded value of type -
CAMEL-CallResult {bound}})
-- The violation of the UserDefinedConstraint shall be handled as an ASN.1 syntax error.
-- This parameter provides the gsmSCF with the charging related information previously requested
-- using the ApplyCharging operation. This shall include the partyToCharge parameter as
-- received in the related ApplyCharging operation to correlate the result to the request

CAMEL-CallResult {PARAMETERS-BOUND : bound} ::= CHOICE {
timeDurationChargingResult [0] SEQUENCE {
partyToCharge [0] ReceivingSideID,
timeInformation [1] TimeInformation,
legActive [2] BOOLEAN DEFAULT TRUE,
callLegReleasedAtTcpExpiry [3] NULL OPTIONAL,
extensions [4] Extensions {bound} OPTIONAL,
aChChargingAddress [5] AChChargingAddress {bound} DEFAULT legID:receivingSideID:leg1,
...
}
}

* 
* @author sergey vetyutnev
* 
*/
public interface ApplyChargingReportRequestIndication extends CircuitSwitchedCallMessage {

	public TimeDurationChargingResult getTimeDurationChargingResult();

}
