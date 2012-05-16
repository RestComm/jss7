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

import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;

/**
 * 
assistRequestInstructions {PARAMETERS-BOUND : bound} OPERATION ::= { 
 ARGUMENT  AssistRequestInstructionsArg {bound} 
 RETURN RESULT FALSE 
 ERRORS   {missingCustomerRecord | 
     missingParameter | 
     systemFailure | 
     taskRefused | 
     unexpectedComponentSequence | 
     unexpectedDataValue | 
     unexpectedParameter} 
 CODE   opcode-assistRequestInstructions} 
-- Direction: gsmSSF -> gsmSCF or gsmSRF -> gsmSCF, Timer: Tari
-- This operation is used when there is an assist procedure and may be 
-- sent by the gsmSSF or gsmSRF to the gsmSCF. This operation is sent by the 
-- assisting gsmSSF to gsmSCF, when the initiating gsmSSF has set up a connection to 
-- the gsmSRF or to the assisting gsmSSF as a result of receiving an 
-- EstablishTemporaryConnection from 
-- the gsmSCF. 
-- Refer to clause 11 for a description of the procedures associated with this operation. 
 
AssistRequestInstructionsArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { 
 correlationID      [0] CorrelationID {bound}, 
 iPSSPCapabilities     [2] IPSSPCapabilities {bound}, 
 extensions       [3] Extensions {bound}      OPTIONAL, 
 ... 
 } 
-- OPTIONAL denotes network operator specific use. The value of the correlationID may be the 
-- Called Party Number supplied by the initiating gsmSSF. 

 * 
 * @author sergey vetyutnev
 * 
 */
public interface AssistRequestInstructionsRequest extends CircuitSwitchedCallMessage {

	/**
	 * Use Digits.getGenericNumber() for CorrelationID
	 * 
	 * @return
	 */
	public Digits getCorrelationID();

	public IPSSPCapabilities getIPSSPCapabilities();

	public CAPExtensions getExtensions();
}
