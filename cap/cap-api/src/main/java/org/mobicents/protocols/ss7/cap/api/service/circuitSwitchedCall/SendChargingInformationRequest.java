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

import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;

/**
*

sendChargingInformation {PARAMETERS-BOUND : bound} OPERATION ::= { 
 ARGUMENT  SendChargingInformationArg {bound} 
 RETURN RESULT FALSE 
 ERRORS   {missingParameter | 
     unexpectedComponentSequence | 
     unexpectedParameter | 
     parameterOutOfRange | 
     systemFailure | 
     taskRefused | 
     unexpectedDataValue | 
     unknownLegID} 
 CODE   opcode-sendChargingInformation} 
-- Direction: gsmSCF -> gsmSSF, Timer: T  
sci 
-- This operation is used to instruct the gsmSSF on the charging information to send by the gsmSSF. 
-- The charging information can either be sent back by means of signalling or internal 
-- if the gsmSSF is located in the local exchange. In the local exchange 
-- this information may be used to update the charge meter or to create a standard call record. 
 
SendChargingInformationArg {PARAMETERS-BOUND : bound}::= SEQUENCE { 
 sCIBillingChargingCharacteristics [0] SCIBillingChargingCharacteristics {bound}, 
 partyToCharge      [1] SendingSideID, 
 extensions       [2] Extensions {bound}      OPTIONAL, 
 ... 
 } 

* 
* @author sergey vetyutnev
* 
*/
public interface SendChargingInformationRequest extends CircuitSwitchedCallMessage {

	public SCIBillingChargingCharacteristics getSCIBillingChargingCharacteristics();

	public SendingSideID getPartyToCharge();

	public CAPExtensions getExtensions();

}

