/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;

/**
 * 

forwardGroupCallSignalling  OPERATION ::= {				--Timer s
	ARGUMENT
		ForwardGroupCallSignallingArg
	CODE	local:42 }

ForwardGroupCallSignallingArg ::= SEQUENCE {
	imsi			IMSI			OPTIONAL,
	uplinkRequestAck	[0] NULL		OPTIONAL,
	uplinkReleaseIndication	[1] NULL		OPTIONAL,
	uplinkRejectCommand	[2] NULL		OPTIONAL,
	uplinkSeizedCommand	[3] NULL		OPTIONAL,
	uplinkReleaseCommand	[4] NULL		OPTIONAL,
	extensionContainer	ExtensionContainer	OPTIONAL,
	..., 
	stateAttributes	[5] StateAttributes	OPTIONAL,
	talkerPriority	[6] TalkerPriority	OPTIONAL,
	additionalInfo	[7] AdditionalInfo	OPTIONAL,
	emergencyModeResetCommandFlag	[8] NULL		OPTIONAL,
	sm-RP-UI		[9] SignalInfo	OPTIONAL,
	an-APDU	[10] AccessNetworkSignalInfo	OPTIONAL
 }

 * 
 * @author sergey vetyutnev
 * 
 */
public interface ForwardGroupCallSignallingRequest extends CallHandlingMessage {

	public IMSI getImsi();

	public boolean getUplinkRequestAck();

	public boolean getUplinkReleaseIndication();

	public boolean getUplinkRejectCommand();

	public boolean getUplinkSeizedCommand();

	public boolean getUplinkReleaseCommand();

	public MAPExtensionContainer getExtensionContainer();

	public StateAttributes getStateAttributes();

	public TalkerPriority getTalkerPriority();

	public AdditionalInfo getAdditionalInfo();

	public boolean getEmergencyModeResetCommandFlag();

	public SmsSignalInfo getSmRpUi();

	public AccessNetworkSignalInfo getAnApdu();

}
