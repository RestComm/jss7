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

import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;

/* 
 * 
sendRoutingInfo  OPERATION ::= {				--Timer m
-- The timer is set to the upper limit of the range if the GMSC supports pre-paging.
	ARGUMENT
		SendRoutingInfoArg
	RESULT
		SendRoutingInfoRes
	ERRORS {
		systemFailure |
		dataMissing |
		unexpectedDataValue |
		facilityNotSupported |
		or-NotAllowed |
		unknownSubscriber |
		numberChanged |
		bearerServiceNotProvisioned |
		teleserviceNotProvisioned |
		absentSubscriber |
		busySubscriber |
		noSubscriberReply |
		callBarred |
		cug-Reject |
		forwardingViolation}
	CODE	local:22 }

 * MAP V2
 * SendRoutingInfoArg ::= SEQUENCE {
 * msisdn [0] ISDN-AddressString,
 * cug-CheckInfo [1] CUG-CheckInfo OPTIONAL,
-- cug-CheckInfo must be absent in version 1
 * numberOfForwarding [2] NumberOfForwarding OPTIONAL,
 * networkSignalInfo [10] ExternalSignalInfo OPTIONAL,
 * ...}
 */
/* MAP V3
 *SendRoutingInfoArg ::= SEQUENCE {
 *  msisdn [0] ISDN-AddressString,
 *  cug-CheckInfo [1] CUG-CheckInfo OPTIONAL,
 *  numberOfForwarding [2] NumberOfForwarding OPTIONAL,
 *  interrogationType [3] InterrogationType,
 *  or-Interrogation [4] NULL OPTIONAL*,
 *  or-Capability [5] OR-Phase OPTIONAL,
 *  gmsc-OrGsmSCF-Address [6] ISDN-AddressString,
 *  callReferenceNumber [7] CallReferenceNumber OPTIONAL,
 *  forwardingReason [8] ForwardingReason OPTIONAL,
 *  basicServiceGroup [9] Ext-BasicServiceCode OPTIONAL,
 *  networkSignalInfo [10] ExternalSignalInfo OPTIONAL,
 *  camelInfo [11] CamelInfo OPTIONAL,
 *  suppressionOfAnnouncement [12] SuppressionOfAnnouncement OPTIONAL,
 *  extensionContainer [13] ExtensionContainer OPTIONAL,
 *  ...,
 *  alertingPattern [14] AlertingPattern OPTIONAL,
 *  ccbs-Call [15] NULL OPTIONAL*,
 *  supportedCCBS-Phase [16] SupportedCCBS-Phase OPTIONAL,
 *  additionalSignalInfo [17] Ext-ExternalSignalInfo OPTIONAL,
 *  istSupportIndicator [18] IST-SupportIndicator OPTIONAL,
 *  pre-pagingSupported [19] NULL OPTIONAL*,
 *  callDiversionTreatmentIndicator [20] CallDiversionTreatmentIndicator OPTIONAL,
 *  longFTN-Supported [21] NULL OPTIONAL*,
 *  suppress-VT-CSI [22] NULL OPTIONAL*,
 *  suppressIncomingCallBarring [23] NULL OPTIONAL*,
 *  gsmSCF-InitiatedCall [24] NULL OPTIONAL*,
 *  basicServiceGroup2 [25] Ext-BasicServiceCode OPTIONAL,
 *  networkSignalInfo2 [26] ExternalSignalInfo OPTIONAL,
 *  suppressMTSS [27] SuppressMTSS OPTIONAL,
 *  mtRoamingRetrySupported [28] NULL OPTIONAL*,
 *  callPriority [29] EMLPP-Priority OPTIONAL }
 
 SuppressionOfAnnouncement ::= NULL
 
 NumberOfForwarding ::= INTEGER (1..5)
 
 OR-Phase ::= INTEGER (1..127)
 
 SupportedCCBS-Phase ::= INTEGER (1..127) 
-- exception handling:
-- Only value 1 is used.
-- Values in the ranges 2-127 are reserved for future use.
-- If received values 2-127 shall be mapped on to value 1.

 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public interface SendRoutingInformationRequest extends CallHandlingMessage {
	public ISDNAddressString getMsisdn(); //OCTET STRING
	public CUGCheckInfo getCUGCheckInfo(); //SEQUENCE
	public Integer getNumberOfForwarding(); //INTEGER
	public InterrogationType getInterogationType(); // ENUMERATED
	public boolean getORInterrogation(); //NULL
	public Integer getORCapability(); //NULL
	public ISDNAddressString getGmscOrGsmSCFAddress(); //OCTET STRING
	public CallReferenceNumber getCallReferenceNumber(); //OCTET STRING
	public ForwardingReason getForwardingReason(); //ENUMERATED
	public ExtBasicServiceCode getBasicServiceGroup(); //CHOICE
	public ExternalSignalInfo getNetworkSignalInfo(); //SEQUENCE
	public CamelInfo getCamelInfo(); //SEQUENCE
	public boolean getSuppressionOfAnnouncement(); //NULL
	public MAPExtensionContainer getExtensionContainer(); //SEQUENCE
	public AlertingPattern getAlertingPattern(); // OCTET STRING
	public boolean getCCBSCall(); //NULL
	public Integer getSupportedCCBSPhase(); //INTEGER
	public ExtExternalSignalInfo getAdditionalSignalInfo(); //SEQUENCE
	public ISTSupportIndicator getIstSupportIndicator(); // ENUMERATED
	public boolean getPrePagingSupported(); //NULL
	public CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator(); //OCTET STRING
	public boolean getLongFTNSupported(); //NULL
	public boolean getSuppressVtCSI(); //NULL
	public boolean getSuppressIncomingCallBarring(); //NULL
	public boolean getGsmSCFInitiatedCall(); //NULL
	public ExtBasicServiceCode getBasicServiceGroup2(); //CHOICE
	public ExternalSignalInfo getNetworkSignalInfo2(); //SEQUENCE
	public SuppressMTSS getSuppressMTSS(); //BIT STRING
	public boolean getMTRoamingRetrySupported(); //NULL
	public EMLPPPriority getCallPriority(); //INTEGER
	public long getMapProtocolVersion();
}
