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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;


/**
*

SCIBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE ( 
 bound.&minSCIBillingChargingLength .. bound.&maxSCIBillingChargingLength)) 
 (CONSTRAINED BY {-- shall be the result of the BER-encoded value of type 
 CAMEL-SCIBillingChargingCharacteristics}) 
-- Indicates AOC information to be sent to a Mobile Station 
-- The violation of the UserDefinedConstraint shall be handled as an ASN.1 syntax error.

minSCIBillingChargingLength ::= 4
maxSCIBillingChargingLength ::= 255

CAMEL-SCIBillingChargingCharacteristics ::= CHOICE { 
 aOCBeforeAnswer      [0] AOCBeforeAnswer, 
 aOCAfterAnswer      [1] AOCSubsequent, 
 aOC-extension      [2] CAMEL-SCIBillingChargingCharacteristicsAlt 
 } 

* 
* @author sergey vetyutnev
* 
*/
public interface SCIBillingChargingCharacteristics {

	public AOCBeforeAnswer getAOCBeforeAnswer();

	public AOCSubsequent getAOCSubsequent();

	public CAMELSCIBillingChargingCharacteristicsAlt getAOCExtension();

}

