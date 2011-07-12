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
 * sendRoutingInfoForSM  OPERATION ::= {				--Timer m
 *	ARGUMENT
 *		RoutingInfoForSM-Arg
 *	RESULT
 *		RoutingInfoForSM-Res
 *	ERRORS {
 *		systemFailure |
 *		dataMissing |
 *		unexpectedDataValue |
 *		facilityNotSupported |
 *		unknownSubscriber |
 *		teleserviceNotProvisioned |
 *		callBarred |
 *		absentSubscriberSM}
 *	CODE	local:45 }
 *
 *
 * RoutingInfoForSM-Arg ::= SEQUENCE {
 *	msisdn		[0] ISDN-AddressString,
 *	sm-RP-PRI		[1] BOOLEAN,
 *	serviceCentreAddress	[2] AddressString,
 *	extensionContainer	[6] ExtensionContainer	OPTIONAL,
 *	... ,
 *	gprsSupportIndicator	[7]	NULL		OPTIONAL,
 *	-- gprsSupportIndicator is set only if the SMS-GMSC supports
 *	-- receiving of two numbers from the HLR
 *	sm-RP-MTI		[8] SM-RP-MTI	OPTIONAL,
 *	sm-RP-SMEA	[9] SM-RP-SMEA	OPTIONAL }
 *
 *
 * 
 * @author sergey vetyutnev
 * 
 */
public interface SendRoutingInfoForSMRequestIndication extends SmsService {

	public ISDNAddressString getMsisdn();
	
	public Boolean getSm_RP_PRI();
	
	public AddressString getServiceCentreAddress();

	public MAPExtensionContainer getExtensionContainer();
	
	public Boolean getGprsSupportIndicator();
	
	public SM_RP_MTI getSM_RP_MTI();

	public byte[] getSM_RP_SMEA();
}


