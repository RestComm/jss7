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

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;


/**
 * 
 * mt-ForwardSM  OPERATION ::= {				--Timer ml
 *	ARGUMENT
 *		MT-ForwardSM-Arg
 *	RESULT
 *		MT-ForwardSM-Res
 *		-- optional
 *	ERRORS {
 *		systemFailure |
 *		dataMissing |
 *		unexpectedDataValue |
 *		facilityNotSupported |
 *		unidentifiedSubscriber |
 *		illegalSubscriber |
 *		illegalEquipment |
 *		subscriberBusyForMT-SMS |
 *		sm-DeliveryFailure |
 *		absentSubscriberSM}
 *	CODE	local:44 }
 *
 * 
 * 
 * MT-ForwardSM-Arg ::= SEQUENCE {
 *	sm-RP-DA		SM-RP-DA,
 *	sm-RP-OA		SM-RP-OA,
 *	sm-RP-UI		SignalInfo,
 *	moreMessagesToSend	NULL			OPTIONAL,
 *	extensionContainer	ExtensionContainer	OPTIONAL,
 *	...}
 *
 * 
 * @author sergey vetyutnev
 * 
 */
public interface MtForwardShortMessageRequestIndication extends SmsMessage {

	public SM_RP_DA getSM_RP_DA();

	public SM_RP_OA getSM_RP_OA();

	public byte[] getSM_RP_UI();

	public boolean getMoreMessagesToSend();

	public MAPExtensionContainer getExtensionContainer();

}
