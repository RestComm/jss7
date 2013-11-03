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

package org.mobicents.protocols.ss7.cap.api.service.sms;

import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELsequence1SMS;

/**
 *
furnishChargingInformationSMS {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT FurnishChargingInformationSMSArg {bound}
   RETURN RESULT FALSE ERRORS {
     missingParameter | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter}
   CODE opcode-furnishChargingInformationSMS}
   -- Direction: gsmSCF gsmSSF or gprsSSF, Timer: T fcisms
   -- This operation is used to request the smsSSF to generate, register a charging record
   -- or to include some information in the default SM record. The registered charging record is
   -- intended for off line charging of the Short Message.

FurnishChargingInformationSMSArg {PARAMETERS-BOUND : bound} ::= FCISMSBillingChargingCharacteristics {bound}

FCISMSBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(5 .. 255)) (CONSTRAINED BY {
-- shall be the result of the BER-encoded value of type - CAMEL-FCISMSBillingChargingCharacteristics {bound}})
-- This parameter indicates the SMS billing and/or charging characteristics.
-- The violation of the UserDefinedConstraint shall be handled as an ASN.1 syntax error.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface FurnishChargingInformationSMSRequest extends SmsMessage {

    FCIBCCCAMELsequence1SMS getFCIBCCCAMELsequence1();

//    CAMELFCISMSBillingChargingCharacteristics getCAMELFCISMSBillingChargingCharacteristics();

}
