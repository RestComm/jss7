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

import java.util.ArrayList;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;

/**
 * 
 
MAP V3-4:

resumeCallHandling  OPERATION ::= {				--Timer m
	ARGUMENT
		ResumeCallHandlingArg
	RESULT
		ResumeCallHandlingRes
		-- optional
	ERRORS {
		forwardingFailed |
		or-NotAllowed |
		unexpectedDataValue |
		dataMissing }
	CODE	local:6 }

ResumeCallHandlingArg ::= SEQUENCE {
	callReferenceNumber	[0] CallReferenceNumber	OPTIONAL,
	basicServiceGroup	[1] Ext-BasicServiceCode	OPTIONAL,
	forwardingData	[2] ForwardingData	OPTIONAL,
	imsi			[3] IMSI		OPTIONAL,
	cug-CheckInfo	[4] CUG-CheckInfo	OPTIONAL,
	o-CSI		[5] O-CSI		OPTIONAL,
	extensionContainer	[7] ExtensionContainer	OPTIONAL,
	ccbs-Possible	[8]	NULL		OPTIONAL,
	msisdn		[9]	ISDN-AddressString	OPTIONAL,
	uu-Data		[10] UU-Data	OPTIONAL,
	allInformationSent	[11] NULL		OPTIONAL,
	...,
	d-csi		[12]	D-CSI	OPTIONAL,
	o-BcsmCamelTDPCriteriaList 	[13]	O-BcsmCamelTDPCriteriaList 	OPTIONAL,
	basicServiceGroup2	[14] Ext-BasicServiceCode	OPTIONAL,
	mtRoamingRetry	[15] NULL		OPTIONAL
	 }

O-BcsmCamelTDPCriteriaList ::= SEQUENCE SIZE (1..10) OF O-BcsmCamelTDP-Criteria 

 * 
 * @author sergey vetyutnev
 * 
 */
public interface ResumeCallHandlingRequest extends CallHandlingMessage {

	public CallReferenceNumber getCallReferenceNumber();

	public ExtBasicServiceCode getBasicServiceGroup();

	public ForwardingData getForwardingData();

	public IMSI getImsi();

	public CUGCheckInfo getCugCheckInfo();

	public OCSI getOCsi();

	public MAPExtensionContainer getExtensionContainer();

	public boolean getCcbsPossible();

	public ISDNAddressString getMsisdn();

	public UUData getUuData();

	public boolean getAllInformationSent();

	public DCSI getDCsi();

	public ArrayList<OBcsmCamelTdpCriteria> getOBcsmCamelTDPCriteriaList();

	public ExtBasicServiceCode getBasicServiceGroup2();

	public boolean getMtRoamingRetry();

}
