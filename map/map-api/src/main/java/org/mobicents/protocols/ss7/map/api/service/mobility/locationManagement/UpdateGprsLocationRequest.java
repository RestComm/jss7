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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * 

MAP V3:
updateGprsLocation  OPERATION ::= {				--Timer m
	ARGUMENT
		UpdateGprsLocationArg
	RESULT
		UpdateGprsLocationRes
	ERRORS {
		systemFailure |
		unexpectedDataValue |
		unknownSubscriber |
		roamingNotAllowed}
	CODE	local:23 }


UpdateGprsLocationArg ::= SEQUENCE {
	imsi			IMSI,
	sgsn-Number	ISDN-AddressString,	
	sgsn-Address	GSN-Address,
	extensionContainer	ExtensionContainer	OPTIONAL,
	... ,
	sgsn-Capability	[0] SGSN-Capability	OPTIONAL,
	informPreviousNetworkEntity	[1]	NULL		OPTIONAL,
	ps-LCS-NotSupportedByUE	[2]	NULL		OPTIONAL,
	v-gmlc-Address	[3]	GSN-Address	OPTIONAL,
	add-info		[4]  ADD-Info	OPTIONAL,
	eps-info		[5]	EPS-Info	OPTIONAL,
	servingNodeTypeIndicator	[6]	NULL		OPTIONAL,
	skipSubscriberDataUpdate	[7] NULL		OPTIONAL,
	usedRAT-Type	[8] Used-RAT-Type	OPTIONAL,
	gprsSubscriptionDataNotNeeded	[9] NULL		OPTIONAL,
	nodeTypeIndicator	[10] NULL		OPTIONAL,
	areaRestricted	[11] NULL		OPTIONAL,
	ue-reachableIndicator	[12]	NULL		OPTIONAL, 
	epsSubscriptionDataNotNeeded	[13] NULL		OPTIONAL,
	ue-srvcc-Capability	[14] UE-SRVCC-Capability	OPTIONAL }

 * 
 * @author sergey vetyutnev
 * 
 */
public interface UpdateGprsLocationRequest {

	public IMSI getImsi();

	public ISDNAddressString getSgsnNumber();

	public GSNAddress getSgsnAddress();

	public MAPExtensionContainer getExtensionContainer();

	public SGSNCapability getSGSNCapability();

	public boolean getInformPreviousNetworkEntity();

	public boolean getPsLCSNotSupportedByUE();

	public GSNAddress getVGmlcAddress();

	public ADDInfo getADDInfo();

	public EPSInfo getEPSInfo();

	public boolean getServingNodeTypeIndicator();

	public boolean getSkipSubscriberDataUpdate();

	public UsedRATType getUsedRATType();

	public boolean getGprsSubscriptionDataNotNeeded();

	public boolean getNodeTypeIndicator();

	public boolean getAreaRestricted();

	public boolean getUeReachableIndicator();

	public boolean getEpsSubscriptionDataNotNeeded();

	public UESRVCCCapability getUESRVCCCapability();

}
