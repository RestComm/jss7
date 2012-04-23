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

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;

/**
 * 

callInformationReport {PARAMETERS-BOUND : bound} OPERATION ::= { 
 ARGUMENT  CallInformationReportArg {bound} 
 RETURN RESULT FALSE 
 ALWAYS RESPONDS FALSE 
 CODE   opcode-callInformationReport} 
-- Direction: gsmSSF -> gsmSCF, Timer: T  
cirp
-- This operation is used to send specific call information for a single call party to the gsmSCF as 
-- requested by the gsmSCF in a previous CallInformationRequest. 
 
CallInformationReportArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { 
 requestedInformationList   [0] RequestedInformationList {bound}, 
 extensions       [2] Extensions {bound}      OPTIONAL, 
 legID        [3] ReceivingSideID DEFAULT receivingSideID:leg2, 
 ... 
 } 

 * 
 * @author sergey vetyutnev
 * 
 */
public interface CallInformationReportRequestIndication extends CircuitSwitchedCallMessage {

	public ArrayList<RequestedInformation> getRequestedInformationList();

	public CAPExtensions getExtensions();

	public ReceivingSideID getLegID();

}

