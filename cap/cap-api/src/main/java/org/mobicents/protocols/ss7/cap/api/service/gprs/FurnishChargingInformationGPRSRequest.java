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

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;

/**
*

furnishChargingInformationGPRS {PARAMETERS-BOUND : bound} OPERATION ::= {
ARGUMENT FurnishChargingInformationGPRSArg {bound}
RETURN RESULT FALSE
ERRORS {missingParameter |
taskRefused |
unexpectedComponentSequence |
unexpectedDataValue |
unexpectedParameter |
unknownPDPID}
CODE opcode-furnishChargingInformationGPRS}
-- Direction: gsmSCF -> gprsSSF, Timer: Tfcig
-- This operation is used to request the gprsSSF to generate, register a logical record or to
-- include some information in the default logical GPRS record.
-- The registered logical record is intended for off line charging of the GPRS session
-- or PDP Context.

FurnishChargingInformationGPRSArg {PARAMETERS-BOUND : bound} ::=
FCIGPRSBillingChargingCharacteristics{bound}

FCIGPRSBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(1 .. 160))
(CONSTRAINED BY {-- shall be the result of the BER-encoded value of type -
CAMEL-FCIGPRSBillingChargingCharacteristics {bound}})
-- This parameter indicates the GPRS billing and/or charging characteristics.
-- The violation of the UserDefinedConstraint shall be handled as an ASN.1 syntax error.


* 
* @author sergey vetyutnev
* 
*/
public interface FurnishChargingInformationGPRSRequest extends GprsMessage {

	public CAMELFCIGPRSBillingChargingCharacteristics getFCIGPRSBillingChargingCharacteristics();

}
