package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IstSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;


/*
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
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public interface SendRoutingInformationRequest extends CallHandlingMessage {
	public ISDNAddressString getMsisdn(); //OCTET STRING
	public ISDNAddressString getGatewayMSCAddress(); //OCTET STRING
	public InterrogationType getInterogationType(); // ENUMERATED
	public MAPExtensionContainer getExtensionContainer(); //SEQUENCE
	public CallReferenceNumber getCallReferenceNumber(); //OCTET STRING
	public ForwardingReason getForwardingReason(); //ENUMERATED
	public ExtBasicServiceCode getBasicServiceGroup(); //CHOICE
	public ExtBasicServiceCode getBasicServiceGroup2(); //CHOICE
	public ExternalSignalInfo getNetworkSignalInfo(); //SEQUENCE
	public ExternalSignalInfo getNetworkSignalInfo2(); //SEQUENCE
	public ExtExternalSignalInfo getAdditionalSignalInfo(); //SEQUENCE
	public AlertingPattern getAlertingPattern(); // OCTET STRING
	public IstSupportIndicator getIstSupportIndicator(); // ENUMERATED
}