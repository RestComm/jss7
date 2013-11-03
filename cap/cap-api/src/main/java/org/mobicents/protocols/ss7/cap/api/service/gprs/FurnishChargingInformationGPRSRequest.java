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

package org.mobicents.protocols.ss7.cap.api.service.gprs;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;

/**
 *
 furnishChargingInformationGPRS {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT FurnishChargingInformationGPRSArg {bound}
 * RETURN RESULT FALSE ERRORS {missingParameter | taskRefused | unexpectedComponentSequence | unexpectedDataValue |
 * unexpectedParameter | unknownPDPID} CODE opcode-furnishChargingInformationGPRS} -- Direction: gsmSCF -> gprsSSF, Timer: Tfcig
 * -- This operation is used to request the gprsSSF to generate, register a logical record or to -- include some information in
 * the default logical GPRS record. -- The registered logical record is intended for off line charging of the GPRS session -- or
 * PDP Context.
 *
 * FurnishChargingInformationGPRSArg {PARAMETERS-BOUND : bound} ::= FCIGPRSBillingChargingCharacteristics{bound}
 *
 * FCIGPRSBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(1 .. 160)) (CONSTRAINED BY {-- shall
 * be the result of the BER-encoded value of type - CAMEL-FCIGPRSBillingChargingCharacteristics {bound}}) -- This parameter
 * indicates the GPRS billing and/or charging characteristics. -- The violation of the UserDefinedConstraint shall be handled as
 * an ASN.1 syntax error.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface FurnishChargingInformationGPRSRequest extends GprsMessage {

    CAMELFCIGPRSBillingChargingCharacteristics getFCIGPRSBillingChargingCharacteristics();

}