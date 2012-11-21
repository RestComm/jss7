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

package org.mobicents.protocols.ss7.cap.api.service.gprs;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;

/**
*

sendChargingInformationGPRS {PARAMETERS-BOUND: bound} OPERATION ::= {
ARGUMENT SendChargingInformationGPRSArg { bound}
RETURN RESULT FALSE
ERRORS {missingParameter |
unexpectedComponentSequence |
unexpectedParameter |
parameterOutOfRange |
systemFailure |
taskRefused |
unexpectedDataValue |
unknownPDPID}
CODE opcode-sendChargingInformationGPRS}
-- Direction: gsmSCF -> gprsSSF, Timer: Tscig
-- This operation is used to instruct the gprsSSF on the charging information which the
-- gprsSSF shall send to the Mobile Station by means of GSM access signalling.

SendChargingInformationGPRSArg {PARAMETERS-BOUND: bound}::= SEQUENCE {
sCIGPRSBillingChargingCharacteristics [0] SCIGPRSBillingChargingCharacteristics {bound},
...
}

SCIGPRSBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE (4 .. 255))
(CONSTRAINED BY {-- shall be the result of the BER-encoded value of type 
CAMEL-SCIGPRSBillingChargingCharacteristics})
-- Indicates AOC information to be sent to a Mobile Station
-- The violation of the UserDefinedConstraint shall be handled as an ASN.1 syntax error.

CAMEL-SCIGPRSBillingChargingCharacteristics ::= SEQUENCE {
aOCGPRS [0] AOCGPRS,
pDPID [1] PDPID OPTIONAL,
...
}



* 
* @author sergey vetyutnev
* 
*/
public interface SendChargingInformationGPRSRequest extends GprsMessage {

	public CAMELSCIGPRSBillingChargingCharacteristics getSCIGPRSBillingChargingCharacteristics();

}
