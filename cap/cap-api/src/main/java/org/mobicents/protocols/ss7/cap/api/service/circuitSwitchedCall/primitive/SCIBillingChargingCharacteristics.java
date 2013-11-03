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
 SCIBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE ( bound.&minSCIBillingChargingLength ..
 * bound.&maxSCIBillingChargingLength)) (CONSTRAINED BY {-- shall be the result of the BER-encoded value of type
 * CAMEL-SCIBillingChargingCharacteristics}) -- Indicates AOC information to be sent to a Mobile Station -- The violation of the
 * UserDefinedConstraint shall be handled as an ASN.1 syntax error.
 *
 * minSCIBillingChargingLength ::= 4 maxSCIBillingChargingLength ::= 255
 *
 * CAMEL-SCIBillingChargingCharacteristics ::= CHOICE { aOCBeforeAnswer [0] AOCBeforeAnswer, aOCAfterAnswer [1] AOCSubsequent,
 * aOC-extension [2] CAMEL-SCIBillingChargingCharacteristicsAlt }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SCIBillingChargingCharacteristics extends Serializable {

    AOCBeforeAnswer getAOCBeforeAnswer();

    AOCSubsequent getAOCSubsequent();

    CAMELSCIBillingChargingCharacteristicsAlt getAOCExtension();

}