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

package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
*
* reportSM-DeliveryStatus  OPERATION ::= {			--Timer s
* 	ARGUMENT
* 		ReportSM-DeliveryStatusArg
* 	RESULT
* 		ReportSM-DeliveryStatusRes
* 		-- optional
* 	ERRORS {
* 		dataMissing |
* 		unexpectedDataValue |
* 		unknownSubscriber |
* 		messageWaitingListFull}
* 	CODE	local:47 }
*
* ReportSM-DeliveryStatusArg ::= SEQUENCE {
* 	msisdn		ISDN-AddressString,
* 	serviceCentreAddress	AddressString,
* 	sm-DeliveryOutcome	SM-DeliveryOutcome,
* 	absentSubscriberDiagnosticSM	[0] AbsentSubscriberDiagnosticSM
* 							OPTIONAL,
* 	extensionContainer	[1] ExtensionContainer	OPTIONAL,
* 	...,
* 	gprsSupportIndicator	[2]	NULL		OPTIONAL,
* 	-- gprsSupportIndicator is set only if the SMS-GMSC supports 
* 	-- handling of two delivery outcomes
* 	deliveryOutcomeIndicator	[3] 	NULL		OPTIONAL,
* 	-- DeliveryOutcomeIndicator is set when the SM-DeliveryOutcome
* 	-- is for GPRS
* 	additionalSM-DeliveryOutcome	[4] 	SM-DeliveryOutcome 	OPTIONAL,
* 	-- If received, additionalSM-DeliveryOutcome is for GPRS
* 	-- If DeliveryOutcomeIndicator is set, then AdditionalSM-DeliveryOutcome shall be absent
* 	additionalAbsentSubscriberDiagnosticSM	[5] 	AbsentSubscriberDiagnosticSM OPTIONAL
* 	-- If received additionalAbsentSubscriberDiagnosticSM is for GPRS
* 	-- If DeliveryOutcomeIndicator is set, then AdditionalAbsentSubscriberDiagnosticSM 
* 	-- shall be absent
* 	}
* 
* 
* @author sergey vetyutnev
* 
*/
public interface ReportSMDeliveryStatusRequestIndication extends SmsMessage {

	public ISDNAddressString getMsisdn();
	
	public AddressString getServiceCentreAddress();
	
	public SMDeliveryOutcome getSMDeliveryOutcome();
	
	public Integer getAbsentSubscriberDiagnosticSM();

	public MAPExtensionContainer getExtensionContainer();
	
	public boolean getGprsSupportIndicator();
	
	public boolean getDeliveryOutcomeIndicator();
	
	public SMDeliveryOutcome getAdditionalSMDeliveryOutcome();
	
	public Integer getAdditionalAbsentSubscriberDiagnosticSM();

}


